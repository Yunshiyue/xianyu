package chat;

import UI.Home;
import client.Client;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Qingyue
 */
public class Chat implements Serializable {

    private String userId1;
    private String userId2;
    private String chatContent;
    private String chatDate;

    public Chat(String customerId1,  String customerId2, String chatContent, String chatDate) {
        this.userId1 = customerId1;
        this.userId2 = customerId2;
        this.chatContent = chatContent;
        this.chatDate = chatDate;
    }

    public Chat(String customerId1,  String customerId2, String chatContent) {
        this.userId1 = customerId1;
        this.userId2 = customerId2;
        this.chatContent = chatContent;
    }

    public String getUserId1() { return userId1; }

    public String getUserId2() { return userId2; }

    public String getChatContent() { return chatContent; }

    public String getChatDate() { return chatDate; }

    public void sendChat() {
        OutputStream out = null;
        try {
            out = Home.getClient().getSocket().getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeUTF(getUserId1());
            dos.writeUTF(getUserId2());
            dos.writeUTF(getChatContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Chat> viewChat(String userId) {
        Client client = new Client();
        ArrayList<Chat> chats = null;
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("查看聊天");
            dos.writeUTF(Home.getUserId());
            dos.writeUTF(userId);

            ObjectInputStream ois = new ObjectInputStream(client.getSocket().getInputStream());
            chats = (ArrayList<Chat>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return chats;
    }

    public static ArrayList<String> viewHistory() {
        Client client = new Client();
        ArrayList<String> users = new ArrayList<>();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("查看历史");
            dos.writeUTF(Home.getUserId());

            ObjectInputStream ois = new ObjectInputStream(client.getSocket().getInputStream());
            users = (ArrayList<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }
}
