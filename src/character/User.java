package character;

import client.Client;

import java.io.*;
import java.util.Date;

/**
 * @author Qingyue
 */
public class User implements Serializable {
    private String userId;
    private String password;
    private String phoneNumber;
    private String email;

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public User(String userId, String password, String phoneNumber, String email) {
        this.userId = userId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public boolean register() {
        Client client = new Client();
        boolean success = false;
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("注册");
            ObjectOutputStream oos = new ObjectOutputStream(client.getSocket().getOutputStream());
            oos.writeObject(this);

            DataInputStream dis = new DataInputStream(client.getSocket().getInputStream());
            success = dis.readBoolean();
            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public String getUserId() { return userId; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public String getPhoneNumber() { return  phoneNumber; }

}
