package shopping;

import UI.Home;
import client.Client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Product implements Serializable {

    private String userId;
    private int productId;
    private String productName;
    private double price;
    private String status;
    private int quantity;
    private String type;
    private String detail;

    public Product() {

    }

    public Product(String userId, int productId, String productName,
                   double price, String status, int quantity, String type, String detail) {
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.status = status;
        this.quantity = quantity;
        this.type = type;
        this.detail = detail;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUserId() {
        return userId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public static void onSale(String name, double price, int quantity, String type, String detail, String image) {
        Client client = new Client();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("上架");
            dos.writeUTF(Home.getUserId());
            dos.writeUTF(name);
            dos.writeDouble(price);
            dos.writeInt(quantity);
            dos.writeUTF(type);
            dos.writeUTF(detail);

            //图片
            ArrayList<byte[]> length = new ArrayList<>();

            File file = new File(image);
            BufferedImage bi;
            bi = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi,"png",baos);
            length.add(baos.toByteArray());

            ObjectOutputStream oos = new ObjectOutputStream(client.getSocket().getOutputStream());
            oos.writeObject(length);

            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Product> viewMyProduct() {
        Client client = new Client();
        ArrayList<Product> products = null;
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("我的商品");
            dos.writeUTF(Home.getUserId());

            ObjectInputStream ois = new ObjectInputStream(client.getSocket().getInputStream());
            products = (ArrayList<Product>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static void deleteProduct(int productId) {
        Client client = new Client();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("删除商品");
            dos.writeInt(productId);

            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addCart(int amount) {
        boolean success = false;
        try {
            Client client = new Client();

            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("加入购物车");
            dos.writeInt(getProductId());
            dos.writeInt(amount);
            dos.writeUTF(Home.getUserId());

            InputStream in;
            DataInputStream dis = new DataInputStream(client.getSocket().getInputStream());
            success = dis.readBoolean();

            dos.close();
            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}
