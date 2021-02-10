package UI;

import shopping.Order;
import shopping.ProductItem;
import shopping.ShoppingCart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Qingyue
 */
public class Cart extends JPanel {
    private ArrayList<ProductItem> proList = new ArrayList<>();
    JPanel[] proItem = new JPanel[5];
    //页码标签
    private JLabel page;
    //设置框
    private JTextField setPage;
    //总页码
    private JLabel totalPage;

    Cart() {
        setLayout(null);
        setBounds(100,0,1600,970);
        setBackground(Color.RED);

        //上面的bar
        JPanel bar = new JPanel();
        bar.setBounds(0,0,1600,50);
        bar.setLayout(null);
        bar.setBackground(new Color(250,80,0));
        add(bar);

        //bar中的信息
        JLabel nameOfBar = new JLabel("购物车");
        nameOfBar.setFont(new Font("黑体",Font.BOLD,20));
        nameOfBar.setBounds(700,10,200,30);
        nameOfBar.setForeground(Color.WHITE);
        nameOfBar.setHorizontalAlignment(JTextField.CENTER);
        bar.add(nameOfBar);

        //中间
        JPanel list = new JPanel();
        list.setLayout(null);
        list.setBounds(0,50,1600,920);
        add(list);

        //产品项
        for (int i = 0; i < 5; i++) {
            proItem[i] = new JPanel();
            proItem[i].setLayout(null);
            proItem[i].setBounds(100,i*160,1400,160);
            list.add(proItem[i]);
        }

        //页码
        JPanel pageBar = new JPanel();
        pageBar.setBounds(0,820,1600,80);
        pageBar.setLayout(null);
        pageBar.setBackground(new Color(250,80,0));
        list.add(pageBar);

        //刷新按钮
        ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/icon/刷新.png"));
        JLabel refreshButton = new JLabel(refreshIcon);
        refreshButton.setBounds(10,10,100,40);
        refreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = ()->{
                    refreshCart();
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

        //提交订单按钮
        ImageIcon submitIcon = new ImageIcon(getClass().getResource("/icon/提交订单.png"));
        JLabel submit = new JLabel(submitIcon);
        submit.setBounds(1400,10,100,40);
        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = () -> {
                    Order.submit();
                    JOptionPane.showMessageDialog(null, "提交成功，请在订单中查看！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                    refreshCart();
                };
                Thread thread = new Thread(r);
                thread.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        pageBar.add(submit);

        //上一页按钮
        ImageIcon lastPageIcon = new ImageIcon(getClass().getResource("/icon/page_turning_left.png"));
        JLabel lastPage = new JLabel(lastPageIcon);
        lastPage.setBounds(770,15,20,20);
        lastPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int currentPage = Integer.parseInt(page.getText());
                if (currentPage != 1)
                {
                    page.setText(Integer.toString(currentPage-1));
                    switchPage (currentPage-1);
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
        nextPage.setBounds(820,15,20,20);
        nextPage.setOpaque(false);
        nextPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int currentPage = Integer.parseInt(page.getText());
                if (currentPage != Integer.parseInt(totalPage.getText()))
                {
                    page.setText(Integer.toString(currentPage+1));
                    switchPage (currentPage+1);
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
        page.setBounds(800,15,30,20);
        page.setForeground(Color.white);
        pageBar.add(page);

        setPage = new JTextField();
        setPage.setBounds(900,15,30,30);
        setPage.setDocument(new List.IntegerDocument());
        pageBar.add(setPage);

        ImageIcon surePageIcon = new ImageIcon(getClass().getResource("/icon/sure.png"));
        JLabel surePage = new JLabel(surePageIcon);
        surePage.setBounds(930,15,60,30);
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
        text1.setBounds(1030,15,20,30);
        text1.setForeground(Color.white);
        pageBar.add(text1);

        JLabel text2 = new JLabel("页");
        text2.setBounds(1070,15,20,30);
        text2.setForeground(Color.white);
        pageBar.add(text2);

        totalPage = new JLabel("1");
        totalPage.setBounds(1050,15,20,30);
        totalPage.setForeground(Color.white);
        pageBar.add(totalPage);

    }

    public void switchPage(int newPage) {
        if (proList.isEmpty()){
            for (int i = 0; i < 5; i++) {
                proItem[i].removeAll();
                proItem[i].repaint();
            }
            JOptionPane.showMessageDialog(null, "抱歉，您的购物车是空的哦~", "注意", JOptionPane.ERROR_MESSAGE);
        }
        else {
            for (int i = 0; i < 5; i++) {
                if ((newPage-1) * 5 + i > proList.size()-1) {
                    proItem[i].removeAll();
                    proItem[i].repaint();
                }
                else {
                    proItem[i].removeAll();
                    proItem[i].add(new CartItem(proList.get((newPage-1) * 5 + i)));
                    proItem[i].repaint();
                }
            }
        }
    }

    public void refreshCart() {
        proList = ShoppingCart.viewCart();
        page.setText("1");
        int orderListSize = proList.size();
        if (orderListSize == 0) {
            totalPage.setText("1");
        }
        else if (orderListSize % 5 == 0) {
            totalPage.setText(Integer.toString(orderListSize / 5));
        }
        else {
            totalPage.setText(Integer.toString(orderListSize / 5 + 1));
        }
        switchPage(1);
    }



    public class CartItem extends JPanel{
        private JCheckBox checkBox;
        private final JTextArea changeQuantity;

        CartItem(ProductItem a) {

            setSize(1600,160);
            setLayout(null);

            //选择框
            checkBox = new JCheckBox();
            checkBox.setBounds(50,60,40,40);
            if (a.getSelected() == 1)
            {
                checkBox.setSelected(true);
            }
            else {
                checkBox.setSelected(false);
            }
            checkBox.addActionListener((e)->{
                Runnable r = () ->{
                    if (checkBox.isSelected()){
                        ShoppingCart.changeSelected(a.getProduct().getProductId(), 1);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                    else {
                        ShoppingCart.changeSelected(a.getProduct().getProductId(), 0);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                    proList = ShoppingCart.viewCart();
                };
                Thread thread = new Thread(r);
                thread.start();
            });
            add(checkBox);

            //图片
            JLabel itemImage = new JLabel();
            itemImage.setBounds(110, 10, 140, 140);
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

            //名字
            JTextArea itemName = new JTextArea();
            itemName.setBounds(280, 60, 140, 30);
            itemName.setEditable(false);
            itemName.setOpaque(false);
            //itemName.setBackground(Color.PINK);
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

            //类型
            JTextArea itemType = new JTextArea();
            itemType.setBounds(500, 60, 80, 40);
            itemType.setEditable(false);
            itemType.setOpaque(false);
            //itemType.setBackground(Color.PINK);
            itemType.setText("类型：" + a.getProduct().getType());
            add(itemType);

            //单价
            JTextArea itemPrice = new JTextArea();
            itemPrice.setBounds(700, 60, 80, 40);
            itemPrice.setEditable(false);
            itemPrice.setOpaque(false);
            //itemPrice.setBackground(Color.PINK);
            itemPrice.setText(a.getProduct().getPrice() + "￥");
            add(itemPrice);

            //卖家
            JTextArea itemUser = new JTextArea();
            itemUser.setBounds(800, 60, 100, 40);
            itemUser.setEditable(false);
            itemUser.setOpaque(false);
            //itemUser.setBackground(Color.PINK);
            itemUser.setText("卖家：" + a.getProduct().getUserId());
            add(itemUser);

            //要买的数量
            JTextArea itemQuantity = new JTextArea();
            itemQuantity.setBounds(1000, 60, 80, 40);
            itemQuantity.setEditable(false);
            itemQuantity.setOpaque(false);
            //itemQuantity.setBackground(Color.PINK);
            itemQuantity.setText(Integer.toString(a.getQuantity()));
            add(itemQuantity);

            //修改框
            changeQuantity = new JTextArea();
            changeQuantity.setBounds(1100, 60, 80, 40);
            changeQuantity.setDocument(new List.IntegerDocument());
            add(changeQuantity);

            //修改按钮
            ImageIcon changeIcon = new ImageIcon(getClass().getResource("/icon/购物车修改.png"));
            JLabel changeButton = new JLabel(changeIcon);
            changeButton.setBounds(1200,60,80,40);
            changeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Runnable r = ()->{
                        if (!"".equals(changeQuantity.getText()))
                        {
                            int goalQuantity = Integer.parseInt(changeQuantity.getText());
                            try {
                                if (ShoppingCart.updateQuantity(a.getProduct().getProductId(),goalQuantity))
                                {
                                    try {
                                        Thread.sleep(1500);
                                    } catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                    itemQuantity.setText(changeQuantity.getText());
                                    //refreshTotal();
                                    JOptionPane.showMessageDialog(null, "修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "修改失败，库存可能不足！", "失败", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    };
                    Thread thread = new Thread(r);
                    thread.start();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    changeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            add(changeButton);

            //删除按钮
            ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/icon/购物车删除.png"));
            JLabel deleteButton = new JLabel(deleteIcon);
            deleteButton.setBounds(1300,60,80,40);
            deleteButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Runnable r = ()->{
                        try {
                            ShoppingCart.deleteProduct(a.getProduct().getProductId());
                            JOptionPane.showMessageDialog(null, "删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            refreshCart();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    };
                    Thread thread = new Thread(r);
                    thread.start();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            add(deleteButton);
        }
    }
}