package UI;

import chat.Chat;
import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Qingyue
 */
public class ChatUI extends JPanel {
    private final JPanel personList;
    private JTextArea chatArea;
    private ArrayList<Chat> chats = null;
    private String currentUser = "";
    private ArrayList<String> histories = new ArrayList<>();
    private JLabel nameOfBar;

    ChatUI() {
        setLayout(null);
        setBounds(0,0,1600,970);

        //上面的bar
        JPanel bar = new JPanel();
        bar.setBounds(0,0,1600,50);
        bar.setLayout(null);
        bar.setBackground(new Color(250,80,0));
        add(bar);

        //bar中的信息
        nameOfBar = new JLabel("消息");
        nameOfBar.setFont(new Font("黑体",Font.BOLD,20));
        nameOfBar.setBounds(700,10,200,30);
        nameOfBar.setForeground(Color.WHITE);
        nameOfBar.setHorizontalAlignment(JTextField.CENTER);
        bar.add(nameOfBar);

        //联系人列表
        personList = new JPanel();
        personList.setLayout(new GridLayout(0,1,4,4));
        personList.setBounds(0,50,150,900);
        add(personList);

        //聊天区
        chatArea = new JTextArea();
        chatArea.setLineWrap(true);
        chatArea.setSize(1300,600);
        chatArea.setEditable(false);
        add(chatArea);

        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBounds(200,100,1300,600);
        chatScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(chatScroll);

        //输入框
        JTextArea chatSend = new JTextArea();
        chatSend.setBounds(200,750,1000,150);
        chatSend.setFont(new Font("宋体",0,15));
        chatSend.setLineWrap(true);
        add(chatSend);

        //按钮
        ImageIcon sendIcon = new ImageIcon(getClass().getResource("/icon/发送.png"));
        JLabel send = new JLabel(sendIcon);
        send.setBounds(1300,800,100,80);
        send.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String content = chatSend.getText();
                if ("".equals(content) || currentUser == null) {
                    return;
                }
                else {
                    Chat chat = new Chat(Home.getUserId(), currentUser, content);
                    chat.sendChat();
                    chatArea.append(Home.getUserId() + "\t" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + "\n");
                    chatArea.append(content + "\n\n");
                }
                chatSend.setText("");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                send.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        add(send);

        Runnable r = ()->{
            histories = Chat.viewHistory();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (String history : histories) {
                personList.add(new Item(history));
            }
            personList.repaint();
            receiveChat();
        };
        Thread thread = new Thread(r);
        thread.start();
    }

    public void receiveChat() {
        Client client = Home.getClient();
        try {
            DataInputStream dis = new DataInputStream(client.getSocket().getInputStream());
            //无限循环等待接收消息
            while (true) {
                String from = dis.readUTF();
                String content = dis.readUTF();
                if ("下线".equals(from) && "下线".equals(content)) {
                    break;
                }

                //判断是否在与目标联系人聊天
                if (currentUser.equals(from)) {
                    chatArea.append(from + "\t" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + "\n");
                    chatArea.append(content + "\n\n");
                }
                else if (!histories.contains(from)) {
                    personList.removeAll();
                    histories = Chat.viewHistory();
                    for (String history : histories) {
                        personList.add(new Item(history));
                        System.out.println(history);
                    }
                    personList.repaint();
                }
            }
            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotoChat(String totalChat) {
        Client client = new Client();
        try {
            DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
            dos.writeUTF("查看目标联系人");
            dos.writeUTF(Home.getUserId());
            dos.writeUTF(totalChat);

            DataInputStream dis = new DataInputStream(client.getSocket().getInputStream());
            int exist = dis.readInt();
            if (exist == 1) {
                currentUser = totalChat;
                nameOfBar.setText("消息 to " + totalChat);
                chats = Chat.viewChat(totalChat);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                chatArea.setText("");
                for (int i = 0; i < chats.size(); i++) {
                    chatArea.append(chats.get(i).getUserId1() + "\t" + chats.get(i).getChatDate() + "\n");
                    chatArea.append(chats.get(i).getChatContent() + "\n\n");
                }
            }
            else {
                personList.add(new Item(totalChat));
                nameOfBar.setText("消息 to " + totalChat);
                currentUser = totalChat;
            }
            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Item extends JPanel {

        Item(String userId) {
            setSize(150,50);
            setLayout(null);

            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon/客户.png"));
            JLabel icon = new JLabel(imageIcon);
            icon.setBounds(10,10,30,30);
            add(icon);

            JLabel name = new JLabel(userId);
            name.setBounds(50,10,90,30);
            name.setFont(new Font("宋体",0,20));
            name.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Runnable r = ()->{
                        currentUser = userId;
                        nameOfBar.setText("消息 to " + userId);
                        chats = Chat.viewChat(userId);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        chatArea.setText("");
                        for (int i = 0; i < chats.size(); i++) {
                            chatArea.append(chats.get(i).getUserId1() + "\t" + chats.get(i).getChatDate() + "\n");
                            chatArea.append(chats.get(i).getChatContent() + "\n\n");
                        }
                    };
                    Thread thread = new Thread(r);
                    thread.start();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    name.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            add(name);
        }
    }
}