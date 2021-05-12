package server;
import character.User;
import chat.Chat;
import shopping.Comment;
import shopping.Product;
import shopping.ProductItem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Qingyue
 */
public class ServerOneJabber extends Thread {
    private final Socket socket;
    private static final String password = "******";
    private static HashMap<String, Socket> userLogin = new HashMap<>();

    public ServerOneJabber(Socket s) throws IOException {
        socket = s;
        start();
    }

    @Override
    public void run()  {
        try {
            //while (true) {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String str = dis.readUTF();
                System.out.println(str);
                switch (str){
                    case "登录":
                        receiveLogin();
                        break;
                    case "注册":
                        receiveRegister();
                        break;
                    case "商品":
                        receiveProduct();
                        break;
                    case "加入购物车":
                        receiveAddCart();
                        break;
                    case "查看购物车":
                        receiveViewCart();
                        break;
                    case "修改购物车":
                        receiveUpdateCart();
                        break;
                    case "删除购物车商品":
                        receiveDeleteProductOfCart();
                        break;
                    case "清空购物车":
                        receiveClearCart();
                        break;
                    case "提交订单":
                        receiveSubmitOrder();
                        break;
                    case  "取消订单":
                        receiveCancelOrder();
                        break;
                    case "修改状态":
                        receiveChangeSelected();
                        break;
                    case "查看订单":
                        receiveViewOrder();
                        break;
                    case "完成订单":
                        receiveFinishOrder();
                        break;
                    case "上架":
                        receiveOnSale();
                        break;
                    case "我的商品":
                        receiveViewMyProduct();
                        break;
                    case "删除商品":
                        receiveDeleteMyProduct();
                        break;
                    case "留言":
                        receiveMakeComment();
                        break;
                    case "查看留言":
                        receiveViewComment();
                        break;
                    case "查看聊天":
                        receiveViewChat();
                        break;
                    case "查看历史":
                        receiveViewHistory();
                        break;
                    case "查看目标联系人":
                        receiveGoalPerson();
                        break;
                    case "查看销售情况":
                        receiveViewSell();
                        break;
                    default:
                        break;
               // }
            }
        }catch (IOException | ClassNotFoundException e){
            System.err.println("IO Exception");
            e.printStackTrace();
        }
    }

    private void receiveRegister() {
        boolean success = false;
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            User user = (User) ois.readObject();

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "select userId from user where userId = '" + user.getUserId() + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                dos.writeBoolean(success);
            }
            else {
                sql = "insert into user (userId, password, registerDate, email, phoneNumber, type) VALUES " +
                        "('" + user.getUserId() + "', '" + user.getPassword() + "', now(), '" + user.getEmail() + "', '" + user.getPhoneNumber() + "', 1)";
                stmt.executeUpdate(sql);
                success = true;
                dos.writeBoolean(success);
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //登录验证
    private void receiveLogin() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        User user = (User)ois.readObject();
        String userId = user.getUserId();
        String password = user.getPassword();
        System.out.println(userId + "\t" + password);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/xianyu?serverTimezone=UTC",
                    "root",ServerOneJabber.password);
            Statement stmt = conn.createStatement();
            String sql = "select * from user where userId = '"+userId+"' and password = '"+ password + "'";
            ResultSet rs = stmt.executeQuery(sql);
            boolean succeed;
            if (rs.next()) {
                succeed = true;
            } else {
                succeed = false;
            }

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeBoolean(succeed);

            userLogin.put(userId, socket);

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            while (true) {
                String userId1 = dis.readUTF();
                String userId2 = dis.readUTF();
                String content = dis.readUTF();
                if ("下线".equals(userId2) && "下线".equals(content)) {
                    userLogin.remove(userId1);
                    dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF("下线");
                    dos.writeUTF("下线");
                    break;
                }
                //用户在线状态
                boolean onLine = false;
                //存储目标对象socket
                Socket user2 = null;
                //判断用户是否在线
                for (String key : userLogin.keySet()) {
                    if (key.equals(userId2)) {
                        onLine = true;
                        user2 = userLogin.get(userId2);
                    }
                }
                //存储聊天记录
                sql = "insert into chat (userId1, userId2, chatContent, chatDate) " +
                        "values ('" + userId1 +"', '" + userId2 +"', '" + content+"', now())";
                stmt.executeUpdate(sql);
                //如果用户在线，则转发消息
                if (onLine) {
                    dos = new DataOutputStream(user2.getOutputStream());
                    dos.writeUTF(userId1);
                    dos.writeUTF(content);
                }
            }

            socket.close();


        } catch (SQLException e) {
            System.out.println("异常");
            e.printStackTrace();
        }
    }

    //展示商品
    private void receiveProduct() throws IOException {
        DataInputStream ois = new DataInputStream(socket.getInputStream());
        String selectedType = ois.readUTF();
        String content = ois.readUTF();
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql;
            if ("全部".equals(selectedType))
            {
                sql = "select * from product where quantity > 0 and (productName like '%" + content + "%' or detail like '%"+content+"%')";
            }
            else {
                sql = "select * from product where quantity > 0 " +
                        "and (productName like '%" + content + "%' " +
                        "or detail like '%"+content+"%') and type = '" + selectedType + "'";
            }
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String userId = rs.getString("userId");
                int productId = rs.getInt("productId");
                String productName = rs.getString("productName");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String type = rs.getString("type");
                String detail = rs.getString("detail");
                products.add(new Product(userId,productId,productName,price,"onSale",quantity,type,detail));
            }
            stmt.close();
            conn.close();

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(products);

            //存储字节数组
            ArrayList<byte[]> imageArray = new ArrayList<>(products.size());

            for (int i = 0; i < products.size(); i++) {
                File file = new File("D:\\JAVA\\workspace\\xianyu\\src\\serverImage\\" + products.get(i).getProductId() + ".png");
                BufferedImage bi;
                bi = ImageIO.read(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bi,"png",baos);

                imageArray.add(baos.toByteArray());
                System.out.println(i);
            }

            oos.writeObject(imageArray);

            ois.close();
            oos.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //加入购物车
    private void receiveAddCart() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int productId = dis.readInt();
            int addQuantity = dis.readInt();
            String userId = dis.readUTF();

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql;
            sql = "select addedQuantity from shopping_cart where productId = " + productId + " and userId = '" + userId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            int cartQuantity = 0;
            int leftQuantity = 0;
            boolean success = false;
            //查购物车的量
            if (rs.next()) {
                cartQuantity = rs.getInt(1);
            }

            //查库存
            sql = "select quantity from product where productId = " + productId;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                leftQuantity = rs.getInt(1);
                int totalQuantity = addQuantity + cartQuantity;
                if (totalQuantity <= leftQuantity && cartQuantity != 0) {
                    success = true;
                    String sql2 = "update shopping_cart set addedQuantity = " + totalQuantity + " where productId =" + productId;
                    Statement stmt2 = conn.createStatement();
                    stmt2.executeUpdate(sql2);
                }
                else if (totalQuantity <= leftQuantity) {
                    success = true;
                    String sql2 = "insert into shopping_cart values('" + userId + "'," + productId + "," + totalQuantity + ", 1)";
                    Statement stmt2 = conn.createStatement();
                    stmt2.executeUpdate(sql2);
                }
            }
            stmt.close();
            conn.close();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeBoolean(success);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //产看购物车
    private void receiveViewCart() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String userId = dis.readUTF();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            System.out.println("连接");
            Statement stmt = conn.createStatement();
            String sql = "select * from shopping_cart as a join product as b on a.productId = b.productId " +
                    "where a.userId = '" + userId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<ProductItem> cartItems = new ArrayList<>();
            while (rs.next()) {
                System.out.println(1);
                int productId = rs.getInt("a.productId");
                String productName = rs.getString("productName");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String type = rs.getString("type");
                String detail = rs.getString("detail");
                String seller = rs.getString("b.userId");
                int addedQuantity = rs.getInt("addedQuantity");
                int selected = rs.getInt("selected");
                ProductItem productItem = new ProductItem(new Product(seller, productId, productName,
                        price, "onSale", quantity, type, detail), addedQuantity, selected);
                cartItems.add(productItem);
            }
            System.out.println(cartItems.size());
            stmt.close();
            conn.close();

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(cartItems);

            socket.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //修改购物车
    private void receiveUpdateCart() {
        try {
            boolean success = false;
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String userId = dis.readUTF();
            int productId = dis.readInt();
            int updateQuantity = dis.readInt();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "select quantity from product where productId = " + productId;
            ResultSet rs = stmt.executeQuery(sql);
            int totalQuantity = 0;
            while (rs.next())
            {
                totalQuantity = rs.getInt("quantity");
            }
            if (totalQuantity >= updateQuantity)
            {
                sql = "update shopping_cart set addedQuantity = " + updateQuantity + " where productId = " + productId
                        + " and userId = '" + userId + "'";
                stmt.executeUpdate(sql);
                success = true;
            }
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeBoolean(success);
            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //删除购物车商品
    private void receiveDeleteProductOfCart() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String userId = dis.readUTF();
            int productId = dis.readInt();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "delete from shopping_cart where userId = '" + userId + "' and productId = " + productId;
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //清空购物车
    private void receiveClearCart() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String userId = dis.readUTF();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "delete from shopping_cart where userId = '" + userId + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //提交订单
    private void receiveSubmitOrder() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String userId = dis.readUTF();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "select a.productId,price, quantity, addedQuantity from shopping_cart as a join product as b on a.productId = b.productId " +
                    "where selected = 1 and a.userId = '" + userId + "'";
            ResultSet rs1 = stmt.executeQuery(sql);
            while (rs1.next()) {
                int productId = rs1.getInt("a.productId");
                int quantity = rs1.getInt("quantity");
                int addedQuantity = rs1.getInt("addedQuantity");
                if (quantity < addedQuantity) {
                    continue;
                }
                double price = rs1.getDouble("price");
                //插入
                String sql2 = "insert into `order` (productId, userId, dateCreated, price, status, quantity) VALUES " +
                        "(" + productId + ", '" + userId + "', now(), " + price + ", " + " 1, " + addedQuantity + ")";
                stmt.executeUpdate(sql2);
                int leftQuantity = quantity - addedQuantity;
                String sql3 = "update product set quantity = " + leftQuantity + " where productId = " + productId;
                stmt.executeUpdate(sql3);
                String sql4 = "delete from shopping_cart where userId = '" + userId + "' and selected = 1 and productId = " + productId;
                stmt.executeUpdate(sql4);
            }
            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //取消订单
    private void receiveCancelOrder() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int orderId = dis.readInt();
            int productId = dis.readInt();
            int quantity = dis.readInt();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "update product set quantity = quantity +" + quantity + " where productId = " + productId;
            stmt.executeUpdate(sql);
            sql = "delete from `order` where orderId = " + orderId;
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //修改状态
    private void receiveChangeSelected() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String userId = dis.readUTF();
            int productId = dis.readInt();
            int selected = dis.readInt();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "update shopping_cart set selected = " + selected + " where userId = '" + userId + "' and productId = " + productId;
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //查看订单
    private void receiveViewOrder() {

        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String userId = dis.readUTF();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "select * from `order` as a join product as b on a.productId = b.productId " +
                    "where a.userId = '" + userId + "' order by a.status desc , dateCreated desc ";
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<ProductItem> orderItems = new ArrayList<>();
            while (rs.next()) {
                System.out.println(1);
                int orderId = rs.getInt("orderId");
                int productId = rs.getInt("a.productId");
                String productName = rs.getString("productName");
                double price = rs.getDouble("b.price");
                int quantity = rs.getInt("b.quantity");
                String type = rs.getString("type");
                String detail = rs.getString("detail");
                String seller = rs.getString("b.userId");
                int boughtQuantity = rs.getInt("a.quantity");
                int status = rs.getInt("a.status");
                Date date = rs.getDate("dateCreated");
                ProductItem productItem = new ProductItem(new Product(seller, productId, productName,
                        price, "onSale", quantity, type, detail), boughtQuantity, status, date, orderId);
                orderItems.add(productItem);
            }
            System.out.println(orderItems.size());
            stmt.close();
            conn.close();

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(orderItems);

            System.out.println(orderItems.size());
            socket.close();

        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    //完成订单
    private void receiveFinishOrder() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int orderId = dis.readInt();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "update `order` set status = 0 where orderId = " + orderId;
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //上架
    private void receiveOnSale() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String userId = dis.readUTF();
            String productName = dis.readUTF();
            double price = dis.readDouble();
            int quantity = dis.readInt();
            String type = dis.readUTF();
            String detail = dis.readUTF();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "insert into product (userId, productName, price, status, quantity, type, detail) values " +
                    "('" +userId+ "','" + productName + "'," + price + ", 'onSale'," + quantity + ",'" + type + "', '" + detail + "')";
            stmt.executeUpdate(sql);
            sql = "select productId from product where userId = '" + userId + "' " +
                    "and productName = '" + productName + "' and detail = + '" + detail + "'";
            int productId = 0;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                productId = rs.getInt("productId");
            }

            //图片
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            ArrayList<byte[]> image = (ArrayList<byte[]>) ois.readObject();
            ByteArrayInputStream bais = new ByteArrayInputStream(image.get(0));
            BufferedImage bi1 = ImageIO.read(bais);
            File w2 = new File("D:\\JAVA\\workspace\\xianyu\\src\\serverImage\\" + productId + ".png");
            ImageIO.write(bi1, "png", w2);



        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //查看我的商品
    private void receiveViewMyProduct() {
        try {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String userId = dis.readUTF();
                ArrayList<Product> products = new ArrayList<Product>();

                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                            "root",password);
                Statement stmt = conn.createStatement();
                String sql = "select * from product where userId = '" + userId + "' order by quantity desc ";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    System.out.println("找到");
                    int productId = rs.getInt("productId");
                    String productName = rs.getString("productName");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    String type = rs.getString("type");
                    String detail = rs.getString("detail");
                    products.add(new Product(userId, productId, productName, price, "onSale", quantity, type, detail));
                }
                stmt.close();
                conn.close();

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(products);

        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //删除我的商品
    private void receiveDeleteMyProduct() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int productId = dis.readInt();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "update product set quantity = -1 where productId = " + productId;
            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    //留言
    private void receiveMakeComment() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String userId = dis.readUTF();
            int productId = dis.readInt();
            String content = dis.readUTF();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "insert into comment (productId, userId, content, date) values (" + productId + ", '" + userId + "', '" + content + "', now())";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //查看留言
    private void receiveViewComment() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int productId = dis.readInt();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "select * from comment where productId = " + productId + " order by date ";
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                String userId = rs.getString("userId");
                String content = rs.getString("content");
                String date = rs.getString("date");
                comments.add(new Comment(productId, userId, content, date));
            }

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(comments);

            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //查看聊天
    private void receiveViewChat() {
        ArrayList<Chat> chats = new ArrayList<>();
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String from = dis.readUTF();
            String to = dis.readUTF();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "select * from chat where " +
                    "(userId1 = '" + from + "' and userId2 = '" + to + "') or (userId2 = '" + from + "' and userId1 = '" + to + "') order by chatDate ";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String userId1 = rs.getString("userId1");
                String userId2 = rs.getString("userId2");
                String content = rs.getString("chatContent");
                String date = rs.getString("chatDate");
                Chat chat = new Chat(userId1, userId2, content,date);
                chats.add(chat);
            }

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(chats);

            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //查看历史
    private void receiveViewHistory() {
        try {
            ArrayList<String> users = new ArrayList<>();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String userId = dis.readUTF();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "select distinct userId2 as userName from chat where userId1 = '" + userId +"' union " +
                    "select distinct userId1 as userName from chat where userId2 = '" + userId +"'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                users.add(rs.getString("userName"));
            }

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(users);

            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //查看目标联系人
    private void receiveGoalPerson() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String user1 = dis.readUTF();
            String user2 = dis.readUTF();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "select * from chat where (userId1 = '" + user1 +"' and userId2 = '" + user2 +"') " +
                    "or (userId1 = '" + user2 +"' and userId2 = '" + user1 + "')";
            ResultSet rs = stmt.executeQuery(sql);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            if (rs.next()) {
                dos.writeInt(1);
            }else {
                dos.writeInt(0);
            }

            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //查看销售情况
    private void receiveViewSell() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String userId = dis.readUTF();

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xianyu?serverTimezone=UTC",
                    "root",password);
            Statement stmt = conn.createStatement();
            String sql = "select * from `order` as a join product as b on a.productId = b.productId " +
                    "where b.userId = '" + userId + "' order by a.status desc , a.dateCreated desc ";
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<ProductItem> sellList = new ArrayList<>();
            while (rs.next()) {
                int productId = rs.getInt("b.productId");
                String productName = rs.getString("b.productName");
                double price = rs.getDouble("b.price");
                int quantity = rs.getInt("b.quantity");
                String type = rs.getString("b.type");
                String detail = rs.getString("b.detail");

                int orderId = rs.getInt("a.orderId");
                int sellQuantity = rs.getInt("a.quantity");
                Date date = rs.getDate("dateCreated");
                String buyer = rs.getString("a.userId");
                int status = rs.getInt("a.status");

                sellList.add(new ProductItem(new Product(userId,productId,productName,price,"onSale",quantity,type,detail),sellQuantity, status, date, orderId, buyer));
            }

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(sellList);

            stmt.close();
            conn.close();
        } catch (IOException | ClassNotFoundException | SQLException exception) {
            exception.printStackTrace();
        }
    }

}