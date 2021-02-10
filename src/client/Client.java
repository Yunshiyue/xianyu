package client;

import character.User;
import shopping.Product;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Qingyue
 */
public class Client {
    static final int PORT = 9999;
    static final String IP = "127.0.0.1";
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public Client() {
        try {
            socket = new Socket(IP,PORT);


        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "请检查网络连接", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Socket getSocket() { return socket; }

    //登录验证
    public boolean sendLogin(String username, String password) throws IOException {
        User user = new User(username,password);

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF("登录");

        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(user);
        oos.flush();

        DataInputStream dis = new DataInputStream(socket.getInputStream());

        return dis.readBoolean();
    }

    public ArrayList<Product> sendProduct(String type, String content) throws IOException, ClassNotFoundException {
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF("商品");
        dos.writeUTF(type);
        dos.writeUTF(content);

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ArrayList<Product> products = (ArrayList<Product>) ois.readObject();

        ArrayList<byte[]> image = (ArrayList<byte[]>) ois.readObject();

        for (int i = 0; i < products.size(); i++) {

            ByteArrayInputStream bais = new ByteArrayInputStream(image.get(i));
            BufferedImage bi1 = ImageIO.read(bais);
            File w2 = new File("D:\\JAVA\\workspace\\xianyu\\src\\clientImage\\" + products.get(i).getProductId() + ".png");
            ImageIO.write(bi1, "png", w2);
        }

        dos.close();
        ois.close();
        socket.close();

        return products;
    }
}
