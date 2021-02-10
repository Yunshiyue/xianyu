package shopping;

import UI.Home;
import client.Client;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Qingyue
 */
public class Order {
    private int orderId;
    private String customerId;
    private int productId;
    private Date dateCreated;
    private double totalPrice;
    private String orderStatus;

    public Order() {

    }

    public static void submit() {
        Client client = new Client();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("提交订单");
            dos.writeUTF(Home.getUserId());
            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cancelOrder(int orderId, int productId, int quantity) {
        Client client = new Client();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("取消订单");
            dos.writeInt(orderId);
            dos.writeInt(productId);
            dos.writeInt(quantity);
            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void finishOrder(int orderId) {
        Client client = new Client();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("完成订单");
            dos.writeInt(orderId);
            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ProductItem> viewOrder() {
        Client client = new Client();
        ArrayList<ProductItem> orderItems = null;
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("查看订单");
            dos.writeUTF(Home.getUserId());

            ObjectInputStream ois = new ObjectInputStream(client.getSocket().getInputStream());
            orderItems = (ArrayList<ProductItem>) ois.readObject();
            client.getSocket().close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    public static ArrayList<ProductItem> viewSell() {
        Client client = new Client();
        ArrayList<ProductItem> sellList = null;
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("查看销售情况");
            dos.writeUTF(Home.getUserId());

            ObjectInputStream ois = new ObjectInputStream(client.getSocket().getInputStream());
            sellList = (ArrayList<ProductItem>) ois.readObject();

            client.getSocket().close();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return sellList;
    }
}
