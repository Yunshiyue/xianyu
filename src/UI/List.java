package UI;

import client.Client;
import shopping.Product;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Qingyue
 */
public class List extends JPanel {
    private static final String[] TYPE = {"全部","电子产品","图书","文具","服装","化妆品","日用品","其他"};
    private ArrayList<Product> proList = new ArrayList<Product>();

    //下面的
    private final JPanel list;

    //上面的搜索栏
    private final JTextField searchArea;

    //分类选项卡
    private JComboBox<String> menu;

    //排序
    private JComboBox<String> order;

    //产品显示列表
    private JPanel[] proItem = new JPanel[5];

    //页码标签
    private JLabel page;

    //设置框
    private final JTextField setPage;
    private JLabel totalPage;

    List(){
        setLayout(null);
        setBounds(100,0,1600,970);

        //搜索JPanel
        JPanel search = new JPanel();
        search.setLayout(null);
        search.setBounds(0,0,1600,50);
        search.setBackground(new Color(255,80,0));
        add(search);

        //搜索框
        searchArea = new JTextField(130);
        searchArea.setBounds(100,10,1360,30);
        search.add(searchArea);

        //搜索按钮
        ImageIcon searchIcon = new ImageIcon(getClass().getResource("/icon/search.png"));
        JLabel click = new JLabel(searchIcon);
        click.setBounds(1480,10,32,32);
        click.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = ()->{
                    Client client = null;
                    try {
                        client = new Client();
                        proList = client.sendProduct(TYPE[menu.getSelectedIndex()], searchArea.getText());
                    } catch (IOException | ClassNotFoundException exception) {
                        exception.printStackTrace();
                    }
                    page.setText("1");
                    int proListSize = proList.size();
                    if (proListSize == 0) {
                        totalPage.setText("1");
                    }
                    else if (proListSize % 5 == 0) {
                        totalPage.setText(Integer.toString(proListSize / 5));
                    }
                    else {
                        totalPage.setText(Integer.toString(proListSize/ 5 + 1));
                    }
                    switchPage(1);
                };
                Thread t = new Thread(r);
                t.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                click.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        search.add(click);

        //分类选项卡
        menu = new JComboBox<String>(TYPE);
        menu.setBounds(10,10,80,30);
        search.add(menu);


        //下面的商品区域
        list = new JPanel();
        list.setLayout(null);
        list.setBounds(0,50,1600,920);
        add(list);

        //产品项
        for (int i = 0; i < 5; i++) {
            proItem[i] = new JPanel();
            proItem[i].setLayout(null);
            proItem[i].setBounds(200,i*160,1200,160);
            list.add(proItem[i]);
        }

        //页码
        JPanel pageBar = new JPanel();
        pageBar.setBounds(0,820,1600,80);
        pageBar.setLayout(null);
        pageBar.setBackground(new Color(250,80,0));
        list.add(pageBar);

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
        setPage.setDocument(new IntegerDocument());
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
            JOptionPane.showMessageDialog(null, "抱歉，没有您需要的商品哦~", "注意", JOptionPane.ERROR_MESSAGE);
        }
        else {
            for (int i = 0; i < 5; i++) {
                if ((newPage-1) * 5 + i > proList.size()-1) {
                    proItem[i].removeAll();
                    proItem[i].repaint();
                }
                else {
                    proItem[i].removeAll();
                    proItem[i].add(new productItem(proList.get((newPage-1) * 5 + i)));
                    proItem[i].repaint();
                }
            }
        }
    }

    public static class productItem extends JPanel {
        private final JLabel itemImage;   //图片
        private final JTextArea itemName;    //商品名
        private final JTextArea itemDetail;
        private final JTextArea itemPrice;
        private final JTextArea itemQuantity;
        private final JTextArea itemUser;
        private final JTextArea itemType;

        productItem(Product a) {
            setLayout(null);
            setBounds(0, 0, 1400, 160);

            itemImage = new JLabel();
            itemImage.setBounds(10, 10, 140, 140);
            ImageIcon imageIcon = new ImageIcon("D:\\JAVA\\workspace\\xianyu\\src\\clientImage\\" + a.getProductId() + ".png");
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(140, 140,  Image.SCALE_DEFAULT));
            itemImage.setIcon(imageIcon);
            itemImage.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Home home = (Home) getRootPane().getParent();
                    home.switchPanel("商品信息");
                    ((ProductDetail) home.productDetail).watch(a);
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
            itemName.setText(a.getProductName());
            itemName.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Home home = (Home) getRootPane().getParent();
                    home.switchPanel("商品信息");
                    ((ProductDetail) home.productDetail).watch(a);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    itemName.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            add(itemName);

            itemDetail = new JTextArea();
            itemDetail.setBounds(180, 70, 600, 70);
            itemDetail.setOpaque(false);
            itemDetail.setLineWrap(true);
            itemDetail.setEditable(false);
            itemDetail.setText(a.getDetail());
            add(itemDetail);

            itemPrice = new JTextArea();
            itemPrice.setBounds(800, 60, 80, 40);
            itemPrice.setEditable(false);
            itemPrice.setOpaque(false);
            itemPrice.setText(a.getPrice() + "￥");
            add(itemPrice);

            itemQuantity = new JTextArea();
            itemQuantity.setBounds(900, 60, 80, 40);
            itemQuantity.setEditable(false);
            itemQuantity.setOpaque(false);
            itemQuantity.setText(Integer.toString(a.getQuantity()));
            add(itemQuantity);

            itemType = new JTextArea();
            itemType.setBounds(1000, 60, 80, 40);
            itemType.setEditable(false);
            itemType.setOpaque(false);
            itemType.setText("类型：" + a.getType());
            add(itemType);

            itemUser = new JTextArea();
            itemUser.setBounds(1100, 60, 250, 40);
            itemUser.setEditable(false);
            itemUser.setOpaque(false);
            itemUser.setText("卖家：" + a.getUserId());
            add(itemUser);
        }
    }

    public static class IntegerDocument extends PlainDocument {
        private static final long serialVersionUID = 1L;

        @Override
        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {
            // 若字符串为空，直接返回。
            if (str == null) {
                return;
            }
            int len = getLength();
            String s = getText(0, len);// 文本框已有的字符
            try {
                s = s.substring(0, offs) + str + s.substring(offs, len);// 在已有的字符后添加字符
                int i = Integer.parseInt(s); // 只能为正整数
                if (i < 1 || i > 255) { // 限制范围
                    throw new Exception();
                }
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();// 发出提示声音
                return;
            }
            super.insertString(offs, str, a);// 把字符添加到文本框
        }
    }
}