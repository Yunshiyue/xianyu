package UI;

import shopping.Comment;
import shopping.Product;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author Qingyue
 */
public class ProductDetail extends JPanel {
    private Product product = null;
    private JLabel nameOfBar;
    private JLabel image;
    private JTextField name;
    private JLabel price;
    private JLabel quantity;
    private JLabel user;
    private JTextArea detail;
    private JScrollPane messageArea;
    private JTextField messageWrite;
    private ArrayList<Comment> comments = null;
    private JTextArea commentPanel;
    private String seller;

    public ProductDetail() {
        setLayout(null);
        setBounds(0,0,1600,970);

        //上面的bar
        JPanel bar = new JPanel();
        bar.setBounds(0,0,1600,50);
        bar.setLayout(null);
        bar.setBackground(new Color(250,80,0));
        add(bar);

        //bar中的信息
        nameOfBar = new JLabel();
        nameOfBar.setFont(new Font("黑体",Font.BOLD,20));
        nameOfBar.setBounds(700,10,200,30);
        nameOfBar.setForeground(Color.WHITE);
        nameOfBar.setHorizontalAlignment(JTextField.CENTER);
        bar.add(nameOfBar);

        //下面的面板
        JPanel content = new JPanel();
        content.setLayout(null);
        content.setBounds(0,0,1600,920);
        add(content);

        //图片
        image = new JLabel();
        image.setBounds(100,100,400,400);
        content.add(image);

        //商品名
        name = new JTextField();
        name.setFont(new Font("宋体",Font.PLAIN,30));
        name.setEditable(false);
        name.setBounds(100,550,400,40);
        name.setBorder(null);
        content.add(name);

        //价格
        price = new JLabel();
        price.setBounds(100,600,400,40);
        price.setForeground(new Color(250,80,0));
        price.setFont(new Font("宋体",Font.PLAIN,18));
        content.add(price);

        //库存
        quantity = new JLabel();
        quantity.setBounds(100,650,400,40);
        quantity.setFont(new Font("宋体",Font.PLAIN,18));
        content.add(quantity);

        //卖家
        user = new JLabel();
        user.setBounds(100,700,400,40);
        user.setFont(new Font("宋体",Font.PLAIN,18));
        content.add(user);
        user.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (seller.equals(Home.getUserId()))
                {
                    return;
                }
                Runnable r = ()->{
                    Home home = (Home) getRootPane().getParent();
                    home.switchPanel("消息");
                    ChatUI chatUI = home.getChatUI();
                    chatUI.gotoChat(seller);
                };
                Thread thread = new Thread(r);
                thread.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                user.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        //添加数量
        JTextField addQuantity = new JTextField();
        addQuantity.setText("1");
        addQuantity.setBounds(100,800,50,40);
        addQuantity.setDocument(new IntegerDocument());
        content.add(addQuantity);

        //加入购物车
        ImageIcon addCartIcon = new ImageIcon(getClass().getResource("/icon/加入购物车.png"));
        JLabel addCart = new JLabel(addCartIcon);
        addCart.setBounds(200,800,200,40);
        addCart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if ("".equals(addQuantity.getText()))
                {
                    Runnable r = () ->{
                    if (product.getUserId().equals(Home.getUserId()))
                        {
                            JOptionPane.showMessageDialog(null, "这件商品是您自己的哦！", "失败", JOptionPane.ERROR_MESSAGE);
                        }
                        else if(product.addCart(1))
                        {
                            JOptionPane.showMessageDialog(null, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "添加失败，库存可能不足！", "失败", JOptionPane.ERROR_MESSAGE);
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                }
                else {
                    int theQuantity = Integer.parseInt(addQuantity.getText());
                    Runnable r = () ->{
                        if (product.addCart(theQuantity))
                        {
                            JOptionPane.showMessageDialog(null, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "添加失败，库存可能不足！", "失败", JOptionPane.ERROR_MESSAGE);
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                addCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        content.add(addCart);

        //详细介绍
        ImageIcon detailIcon = new ImageIcon(getClass().getResource("/icon/描述.png"));
        JLabel detailLabel = new JLabel(detailIcon);
        detailLabel.setBounds(650,100,100,40);
        content.add(detailLabel);

        detail = new JTextArea();
        detail.setEditable(false);
        detail.setOpaque(false);
        detail.setLineWrap(true);
        detail.setFont(new Font("宋体",Font.PLAIN,18));
        detail.setBounds(800,100,700,300);
        content.add(detail);

        //评论
        ImageIcon commentIcon = new ImageIcon(getClass().getResource("/icon/评论.png"));
        JLabel commentLabel = new JLabel(commentIcon);
        commentLabel.setBounds(650,500,100,40);
        content.add(commentLabel);

        commentPanel = new JTextArea();
        commentPanel.setLayout(null);
        commentPanel.setLineWrap(true);
        commentPanel.setEditable(false);
        commentPanel.setOpaque(false);
        commentPanel.setSize(700,300);

        messageArea = new JScrollPane(commentPanel);
        messageArea.setBounds(800,500,700,300);
        messageArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageArea.setOpaque(false);
        messageArea.getViewport().setOpaque(false);
        messageArea.setBorder(null);
        content.add(messageArea);

        messageWrite = new JTextField();
        messageWrite.setBounds(800,830,600,50);
        content.add(messageWrite);

        ImageIcon submitIcon = new ImageIcon(getClass().getResource("/icon/留言.png"));
        JLabel submit = new JLabel(submitIcon);
        submit.setBounds(1420,830,80,50);
        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = ()->{
                    String commentContent = messageWrite.getText();
                    if (!"".equals(commentContent)) {
                        Comment.makeComment(product.getProductId(), commentContent);
                        JOptionPane.showMessageDialog(null, "留言成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        messageWrite.setText("");
                        viewComment();
                    }
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
        content.add(submit);
    }

    public void watch(Product a) {
        Runnable r = ()->{
            product = a;

            seller = a.getUserId();
            ImageIcon imageIcon = new ImageIcon("D:\\JAVA\\workspace\\xianyu\\src\\clientImage\\" + a.getProductId() + ".png");
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(400, 400,  Image.SCALE_DEFAULT));
            image.setIcon(imageIcon);
            nameOfBar.setText(a.getProductName());
            name.setText(a.getProductName());
            price.setText("单价：" + a.getPrice() + " ￥");
            quantity.setText("剩余：" + a.getQuantity());
            user.setText("<html><u>卖家： " + a.getUserId() + "</u></html>");
            detail.setText(a.getDetail());

            viewComment();
        };
        Thread thread = new Thread(r);
        thread.start();
    }

    public void viewComment() {
        commentPanel.setText("");
        comments = Comment.viewComment(product.getProductId());
        for (int i = 0; i < comments.size(); i++) {
            commentPanel.append(comments.get(i).getUserId() + "\t" + comments.get(i).getTime() + "\n");
            commentPanel.append(comments.get(i).getContent() + "\n\n");

        }
    }

    public class CommentItem extends JPanel{
        private Comment comment;

        CommentItem(Comment comment) {
            this.comment = comment;
            setLayout(null);

            setSize(600,50);

            JTextField userName = new JTextField(comment.getUserId());
            userName.setFont(new Font("宋体",0,10));
            userName.setEditable(false);
            userName.setBounds(10,10,100, 10);
            add(userName);

            JTextField time = new JTextField(String.valueOf(comment.getTime()));
            time.setFont(new Font("宋体",0,10));
            time.setEditable(false);
            time.setBounds(200,10,100,10);
            add(time);

            JTextArea content = new JTextArea(comment.getContent());
            content.setFont(new Font("宋体",0,15));
            content.setEditable(false);
            content.setLineWrap(true);
            content.setBounds(10,15,500,30);
            add(content);
        }
    }

    public class IntegerDocument extends PlainDocument {
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
                if (i <= 0 || i > product.getQuantity()) { // 限制范围
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
