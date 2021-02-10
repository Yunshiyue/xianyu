package shopping;

import UI.Home;
import client.Client;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Qingyue
 */
public class ShoppingCart implements Serializable {

    private String userId;

    public ShoppingCart() {

    }

    public String getUserId() {
        return userId;
    }

    public static ArrayList<ProductItem> viewCart() {
        Client client = new Client();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("查看购物车");
            dos.writeUTF(Home.getUserId());

            ObjectInputStream ois = new ObjectInputStream(client.getSocket().getInputStream());
            return (ArrayList<ProductItem>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //修改购物车商品数量
    public static boolean updateQuantity(int productId, int quantity) throws IOException {
        Client client = new Client();
        boolean success = false;
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("修改购物车");
            dos.writeUTF(Home.getUserId());
            dos.writeInt(productId);
            dos.writeInt(quantity);

            DataInputStream dis = new DataInputStream(client.getSocket().getInputStream());
            success = dis.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.getSocket().close();
        }
        return success;
    }

    //删除购物车某件商品
    public static void deleteProduct(int productId) throws IOException {
        Client client = new Client();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("删除购物车商品");
            dos.writeUTF(Home.getUserId());
            dos.writeInt(productId);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.getSocket().close();
        }
    }

    //清空购物车
    public static void clearCart() {
        Client client = new Client();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("清空购物车");
            dos.writeUTF(Home.getUserId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //修改选中
    public static void changeSelected(int productId, int selected) {
        Client client = new Client();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("修改状态");
            dos.writeUTF(Home.getUserId());
            dos.writeInt(productId);
            dos.writeInt(selected);

            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
