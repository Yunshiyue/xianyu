package UI;

import chat.Chat;
import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

/**
 * @author Qingyue
 */
public class Home extends JFrame implements ActionListener{

    private static final int SCREEN_WIDTH = 1700;
    private static final int SCREEN_HEIGHT = 970;
    private Stack<String> history = new Stack<>();
    private String currentPage = "首页";
    private static String userId = null;
    private static Client client = null;
    public JPanel mainPanel;
    private JPanel list;
    private JPanel Need;
    private Cart Cart;
    private JPanel Information;
    private ChatUI ChatUI;
    public CardLayout cardLayout;
    public JPanel productDetail;

    public Home() {

    }

    public Home(String userId, Client client) {
        Home.userId = userId;
        Home.client = client;

        setTitle("闲鱼(用户:" + userId + ")");
        System.out.println("欢迎：" + userId);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        //设置图标
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon/logo.png"));
        setIconImage(imageIcon.getImage());

        //设置关闭弹窗
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int flag = JOptionPane.showConfirmDialog(null,"确定关闭吗？","注意",
                        JOptionPane.YES_NO_OPTION);
                if (flag == JOptionPane.YES_OPTION) {
                    Chat leave = new Chat(Home.userId, "下线", "下线");
                    leave.sendChat();
                    System.exit(0);
                }
            }
        });


        Container contentPane = getContentPane();

        //左侧菜单
        JPanel menu = menu();
        contentPane.add(menu);

        //右侧主面板
        cardLayout = new CardLayout();
        mainPanel = new JPanel();
        mainPanel.setBounds(100,0,1600,970);
        mainPanel.setLayout(cardLayout);
        this.add(mainPanel);

        list = new List();
        mainPanel.add(list,"首页");

        Cart = new Cart();
        mainPanel.add(Cart,"购物车");

        Information = new Information();
        mainPanel.add(Information,"个人信息");

        ChatUI = new ChatUI();
        mainPanel.add(ChatUI,"消息");

        productDetail = new ProductDetail();
        mainPanel.add(productDetail,"商品信息");

        OrderUI orderUI = new OrderUI();
        mainPanel.add(orderUI, "订单");

        OnSale onSale = new OnSale();
        mainPanel.add(onSale, "上架商品");

        MyProduct myProduct = new MyProduct();
        mainPanel.add(myProduct, "我的商品");

        Sell sell = new Sell();
        mainPanel.add(sell, "销售情况");
    }

    JPanel menu() {
        JPanel menu = new JPanel();
        menu.setLayout(null);
        menu.setBounds(0,0,100,970);
        menu.setBackground(new Color(67,67,67));

        //按钮
        //返回
        ImageIcon returnIcon = new ImageIcon(getClass().getResource("/icon/back.png"));
        JLabel returnButton = new JLabel(returnIcon);
        returnButton.setBounds(0,0,100,50);
        returnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!history.empty()) {
                    String lastPage = history.pop();
                    currentPage = lastPage;
                    cardLayout.show(mainPanel, lastPage);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                returnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        //主页
        ImageIcon homeIcon = new ImageIcon(getClass().getResource("/icon/首页.png"));
        JLabel homeButton = new JLabel(homeIcon);
        homeButton.setBounds(0,50,100,50);
        homeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = () -> {
                    switchPanel("首页");
                };
                Thread thread = new Thread(r);
                thread.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        //购物车
        ImageIcon cartIcon = new ImageIcon(getClass().getResource("/icon/购物车.png"));
        JLabel cartButton = new JLabel(cartIcon);
        cartButton.setBounds(0,100,100,50);
        cartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = () -> {
                    switchPanel("购物车");
                };
                Thread thread = new Thread(r);
                thread.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                cartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        //订单
        ImageIcon orderIcon = new ImageIcon(getClass().getResource("/icon/订单.png"));
        JLabel orderButton = new JLabel(orderIcon);
        orderButton.setBounds(0,150,100,50);
        orderButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = () -> {
                    switchPanel("订单");
                };
                Thread thread = new Thread(r);
                thread.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                orderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        //我的商品
        ImageIcon myProductIcon = new ImageIcon(getClass().getResource("/icon/我的商品.png"));
        JLabel myProductButton = new JLabel(myProductIcon);
        myProductButton.setBounds(0,200,100,50);
        myProductButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = () -> {
                    switchPanel("我的商品");
                };
                Thread thread = new Thread(r);
                thread.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                myProductButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        //上架
        ImageIcon onSaleIcon = new ImageIcon(getClass().getResource("/icon/上架.png"));
        JLabel onSaleButton = new JLabel(onSaleIcon);
        onSaleButton.setBounds(0,250,100,50);
        onSaleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = () -> {
                    switchPanel("上架商品");
                };
                Thread thread = new Thread(r);
                thread.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                onSaleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });


        //消息
        ImageIcon chatIcon = new ImageIcon(getClass().getResource("/icon/消息.png"));
        JLabel chatButton = new JLabel(chatIcon);
        chatButton.setBounds(0,300,100,50);
        chatButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = () -> {
                    switchPanel("消息");
                };
                Thread thread = new Thread(r);
                thread.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                chatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        //销售情况
        ImageIcon sellIcon = new ImageIcon(getClass().getResource("/icon/销售情况.png"));
        JLabel sellButton = new JLabel(sellIcon);
        sellButton.setBounds(0,350,100,50);
        sellButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = () -> {
                    switchPanel("销售情况");
                };
                Thread thread = new Thread(r);
                thread.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                sellButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        ImageIcon exitIcon = new ImageIcon(getClass().getResource("/icon/注销.png"));
        JLabel exitButton = new JLabel(exitIcon);
        exitButton.setBounds(0,860,100,50);
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int flag = JOptionPane.showConfirmDialog(null,"确定注销吗？","注意",
                        JOptionPane.YES_NO_OPTION);
                if(flag == JOptionPane.YES_OPTION)
                {
                    Chat leave = new Chat(Home.userId, "下线", "下线");
                    leave.sendChat();
                    dispose();
                    Welcome welcome = new Welcome();
                    welcome.setVisible(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        menu.add(returnButton);
        menu.add(homeButton);
        menu.add(cartButton);
        menu.add(orderButton);
        menu.add(myProductButton);
        menu.add(onSaleButton);
        menu.add(chatButton);
        menu.add(sellButton);
        menu.add(exitButton);
        return menu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("返回".equals(command)) {
            if (!history.empty()) {
                String lastPage = history.pop();
                currentPage = lastPage;
                cardLayout.show(mainPanel, lastPage);
            }
        }
        else if ("注销".equals(command)) {
            int flag = JOptionPane.showConfirmDialog(null,"确定注销吗？","注意",
                    JOptionPane.YES_NO_OPTION);
            if(flag == JOptionPane.YES_OPTION)
            {
                Chat leave = new Chat(Home.userId, "下线", "下线");
                leave.sendChat();
                dispose();
                Welcome welcome = new Welcome();
                welcome.setVisible(true);
            }
        }
        else {
            Runnable r = () -> {
                switchPanel(command);
            };
            Thread thread = new Thread(r);
            thread.start();
        }
    }

    public void switchPanel(String goalPage) {
        if (!goalPage.equals(currentPage)) {
            history.push(currentPage);
            currentPage = goalPage;
            cardLayout.show(mainPanel, goalPage);
        }
    }

    public static String getUserId() { return userId; }

    public static Client getClient() { return client; }

    public ChatUI getChatUI() { return ChatUI; }

    public static void main(String[] args) {

        EventQueue.invokeLater(()->
        {
            Home home = new Home("夕烧", null);
            home.setVisible(true);
        }
        );

    }
}


