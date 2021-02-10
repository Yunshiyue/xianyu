package shopping;

import UI.Home;
import client.Client;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Qingyue
 */
public class Comment implements Serializable {
    private int productId;
    private String userId;
    private String content;
    private String time;

    public Comment(int productId, String userId, String content, String time) {
        this.productId = productId;
        this.userId = userId;
        this.content = content;
        this.time = time;
    }


    public String getUserId() { return userId; }

    public String getContent() { return content; }

    public String getTime() { return time; }

    public static void makeComment(int productId, String content) {
        Client client = new Client();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("留言");
            dos.writeUTF(Home.getUserId());
            dos.writeInt(productId);
            dos.writeUTF(content);

            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Comment> viewComment(int productId) {
        Client client = new Client();
        ArrayList<Comment> comments = null;
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("查看留言");
            dos.writeInt(productId);

            ObjectInputStream ois = new ObjectInputStream(client.getSocket().getInputStream());
            comments = (ArrayList<Comment>) ois.readObject();
            client.getSocket().close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return comments;
    }
}
