package UI;

import shopping.Order;
import shopping.ProductItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author Qingyue
 */
public class Sell extends JPanel {
    private ArrayList<ProductItem> sellList = new ArrayList<>();

    JPanel[] proItem = new JPanel[5];

    //页码标签
    private JLabel page;

    //设置框
    private JTextField setPage;

    //总页码
    private JLabel totalPage;

    Sell() {
        setLayout(null);
        setBounds(100, 0, 1600, 970);

        //上面的bar
        JPanel bar = new JPanel();
        bar.setBounds(0, 0, 1600, 50);
        bar.setLayout(null);
        bar.setBackground(new Color(250, 80, 0));
        add(bar);

        //bar中的信息
        JLabel nameOfBar = new JLabel("销售情况");
        nameOfBar.setFont(new Font("黑体", Font.BOLD, 20));
        nameOfBar.setBounds(700, 10, 200, 30);
        nameOfBar.setForeground(Color.WHITE);
        nameOfBar.setHorizontalAlignment(JTextField.CENTER);
        bar.add(nameOfBar);

        //中间
        JPanel list = new JPanel();
        list.setLayout(null);
        list.setBounds(0, 50, 1600, 920);
        add(list);

        //产品项
        for (int i = 0; i < 5; i++) {
            proItem[i] = new JPanel();
            proItem[i].setLayout(null);
            proItem[i].setBounds(100, i * 160, 1400, 160);
            //proItem[i].setBackground(Color.pink);
            list.add(proItem[i]);
        }

        //页码
        JPanel pageBar = new JPanel();
        pageBar.setBounds(0, 820, 1600, 80);
        pageBar.setLayout(null);
        pageBar.setBackground(new Color(250, 80, 0));
        list.add(pageBar);

        //刷新按钮
        ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/icon/刷新.png"));
        JLabel refreshButton = new JLabel(refreshIcon);
        refreshButton.setBounds(10, 10, 100, 40);
        refreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = () -> {
                    refreshSell();
                };
                Thread thread = new Thread(r);
                thread.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        pageBar.add(refreshButton);

        //上一页按钮
        ImageIcon lastPageIcon = new ImageIcon(getClass().getResource("/icon/page_turning_left.png"));
        JLabel lastPage = new JLabel(lastPageIcon);
        lastPage.setBounds(770, 15, 20, 20);
        lastPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int currentPage = Integer.parseInt(page.getText());
                if (currentPage != 1) {
                    page.setText(Integer.toString(currentPage - 1));
                    switchPage(currentPage - 1);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                lastPage.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        pageBar.add(lastPage);

        //下一页按钮
        ImageIcon nextPageIcon = new ImageIcon(getClass().getResource("/icon/page_turning_right.png"));
        JLabel nextPage = new JLabel(nextPageIcon);
        nextPage.setBounds(820, 15, 20, 20);
        nextPage.setOpaque(false);
        nextPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int currentPage = Integer.parseInt(page.getText());
                if (currentPage != Integer.parseInt(totalPage.getText())) {
                    page.setText(Integer.toString(currentPage + 1));
                    switchPage(currentPage + 1);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                nextPage.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        pageBar.add(nextPage);

        //页码
        page = new JLabel("1");
        page.setBounds(800, 15, 30, 20);
        page.setForeground(Color.white);
        pageBar.add(page);

        setPage = new JTextField();
        setPage.setBounds(900, 15, 30, 30);
        setPage.setDocument(new List.IntegerDocument());
        pageBar.add(setPage);

        ImageIcon surePageIcon = new ImageIcon(getClass().getResource("/icon/sure.png"));
        JLabel surePage = new JLabel(surePageIcon);
        surePage.setBounds(930, 15, 60, 30);
        surePage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if ("".equals(setPage.getText()) || "0".equals(setPage.getText())) {
                    return;
                }
                int goalPage = Integer.parseInt(setPage.getText());
                if (goalPage <= Integer.parseInt(totalPage.getText())) {
                    page.setText(setPage.getText());
                    switchPage(goalPage);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                surePage.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        pageBar.add(surePage);

        JLabel text1 = new JLabel("共");
        text1.setBounds(1030, 15, 20, 30);
        text1.setForeground(Color.white);
        pageBar.add(text1);

        JLabel text2 = new JLabel("页");
        text2.setBounds(1070, 15, 20, 30);
        text2.setForeground(Color.white);
        pageBar.add(text2);

        totalPage = new JLabel("1");
        totalPage.setBounds(1050, 15, 20, 30);
        totalPage.setForeground(Color.white);
        pageBar.add(totalPage);
    }

    public void switchPage(int newPage) {
        if (sellList.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                proItem[i].removeAll();
                proItem[i].repaint();
            }
            JOptionPane.showMessageDialog(null, "抱歉，没有您没有订单哦~", "注意", JOptionPane.ERROR_MESSAGE);
        } else {
            for (int i = 0; i < 5; i++) {
                if ((newPage - 1) * 5 + i > sellList.size() - 1) {
                    proItem[i].removeAll();
                    proItem[i].repaint();
                } else {
                    proItem[i].removeAll();
                    proItem[i].add(new SellItem(sellList.get((newPage - 1) * 5 + i)));
                    proItem[i].repaint();
                }
            }
        }
    }

    public void refreshSell() {
        sellList = Order.viewSell();
        page.setText("1");
        int sellListSize = sellList.size();
        if (sellListSize == 0) {
            totalPage.setText("1");
        }
        else if (sellListSize % 5 == 0) {
            totalPage.setText(Integer.toString(sellListSize / 5));
        }
        else {
            totalPage.setText(Integer.toString(sellListSize / 5 + 1));
        }
        switchPage(1);
    }

    public class SellItem extends JPanel {
        private final JLabel itemImage;   //图片
        private final JTextArea itemName;    //商品名
        private final JTextArea itemDetail;
        private final JTextArea itemPrice;
        private final JTextArea itemQuantity;
        private final JTextArea itemUser;
        private final JTextArea itemType;

        SellItem(ProductItem a) {
            setLayout(null);
            setBounds(0, 0, 1400, 160);

            itemImage = new JLabel();
            itemImage.setBounds(10, 10, 140, 140);
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/clientImage/" + a.getProduct().getProductId() + ".png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(140, 140,  Image.SCALE_DEFAULT));
            itemImage.setIcon(imageIcon);
            itemImage.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Home home = (Home) getRootPane().getParent();
                    home.switchPanel("商品信息");
                    ((ProductDetail) home.productDetail).watch(a.getProduct());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    itemImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            add(itemImage);

            itemName = new JTextArea();
            itemName.setBounds(180, 20, 140, 30);
            itemName.setEditable(false);
            itemName.setOpaque(false);
            itemName.setText(a.getProduct().getProductName());
            itemName.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Home home = (Home) getRootPane().getParent();
                    home.switchPanel("商品信息");
                    ((ProductDetail) home.productDetail).watch(a.getProduct());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    itemName.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            add(itemName);

            itemDetail = new JTextArea();
            itemDetail.setBounds(180, 70, 300, 70);
            itemDetail.setLineWrap(true);
            itemDetail.setEditable(false);
            itemDetail.setOpaque(false);
            itemDetail.setText(a.getProduct().getDetail());
            add(itemDetail);

            itemPrice = new JTextArea();
            itemPrice.setBounds(500, 60, 80, 40);
            itemPrice.setEditable(false);
            itemPrice.setOpaque(false);
            itemPrice.setText("单价：" + a.getProduct().getPrice() + "￥");
            add(itemPrice);

            itemQuantity = new JTextArea();
            itemQuantity.setBounds(600, 60, 80, 40);
            itemQuantity.setEditable(false);
            itemQuantity.setOpaque(false);
            itemQuantity.setText(Integer.toString(a.getQuantity()));
            add(itemQuantity);

            JTextArea itemTotal = new JTextArea();
            itemTotal.setBounds(700, 60, 80, 40);
            itemTotal.setEditable(false);
            itemTotal.setOpaque(false);
            itemTotal.setText("总价：" + (a.getProduct().getPrice() * a.getQuantity()) + "￥");
            add(itemTotal);

            itemType = new JTextArea();
            itemType.setBounds(800, 60, 80, 40);
            itemType.setEditable(false);
            itemType.setOpaque(false);
            itemType.setText("类型：" + a.getProduct().getType());
            add(itemType);

            itemUser = new JTextArea();
            itemUser.setBounds(900, 60, 200, 40);
            itemUser.setEditable(false);
            itemUser.setOpaque(false);
            itemUser.setText("买家：" + a.getBuyer());
            itemUser.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (itemUser.equals(Home.getUserId()))
                    {
                        return;
                    }
                    Runnable r = ()->{
                        Home home = (Home) getRootPane().getParent();
                        home.switchPanel("消息");
                        ChatUI chatUI = home.getChatUI();
                        chatUI.gotoChat(a.getBuyer());
                    };
                    Thread thread = new Thread(r);
                    thread.start();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    itemUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            add(itemUser);

            JTextArea itemDate = new JTextArea();
            itemDate.setBounds(1120, 60, 100, 40);
            itemDate.setEditable(false);
            itemDate.setOpaque(false);
            itemDate.setText(a.getDate().toString());
            add(itemDate);

            if (a.getSelected() == 1) {
                JLabel finished = new JLabel("正在交易");
                finished.setBounds(1300,50,100,40);
                finished.setFont(new Font("宋体", Font.PLAIN, 20));
                add(finished);
            }
            else {
                JLabel finished = new JLabel("已售出");
                finished.setBounds(1300,50,100,40);
                finished.setFont(new Font("宋体", Font.PLAIN, 20));
                add(finished);
            }
        }
    }
}
