package UI;

import shopping.Product;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Objects;

public class OnSale extends JPanel {
    private JTextField nameArea;
    private JTextField priceArea;
    private JTextField quantityArea;
    private JTextArea detailArea;
    private JLabel image;
    private String path = null;

    OnSale() {
        setLayout(null);
        setBounds(0,0,1600,970);
        setBackground(Color.PINK);

        //上面的bar
        JPanel bar = new JPanel();
        bar.setBounds(0,0,1600,50);
        bar.setLayout(null);
        bar.setBackground(new Color(250,80,0));
        add(bar);

        //bar中的信息
        JLabel nameOfBar = new JLabel("上架商品");
        nameOfBar.setFont(new Font("黑体",Font.BOLD,20));
        nameOfBar.setBounds(700,10,200,30);
        nameOfBar.setForeground(Color.WHITE);
        nameOfBar.setHorizontalAlignment(JTextField.CENTER);
        bar.add(nameOfBar);

        //商品名
        JLabel name = new JLabel("商品名：");
        name.setFont(new Font("宋体", Font.PLAIN, 20));
        name.setBounds(200,200,80,40);
        add(name);

        nameArea = new JTextField();
        nameArea.setFont(new Font("宋体", Font.PLAIN, 20));
        nameArea.setBounds(300,200,200,40);
        add(nameArea);

        //单价
        JLabel price = new JLabel("单价：");
        price.setFont(new Font("宋体", Font.PLAIN, 20));
        price.setBounds(200,300,80,40);
        add(price);

        priceArea = new JTextField();
        priceArea.setFont(new Font("宋体", Font.PLAIN, 20));
        priceArea.setBounds(300,300,80,40);
        priceArea.setDocument(new DoubleDocument());
        add(priceArea);

        //数量
        JLabel quantity = new JLabel("数量：");
        quantity.setFont(new Font("宋体", Font.PLAIN, 20));
        quantity.setBounds(200,400,80,40);
        add(quantity);

        quantityArea = new JTextField();
        quantityArea.setFont(new Font("宋体", Font.PLAIN, 20));
        quantityArea.setBounds(300,400,80,40);
        quantityArea.setDocument(new IntegerDocument());
        add(quantityArea);

        //类型
        JLabel type = new JLabel("类型：");
        type.setFont(new Font("宋体", Font.PLAIN, 20));
        type.setBounds(200,500,80,40);
        add(type);

        final String[] TYPE = {"全部","电子产品","图书","文具","服装","化妆品","日用品","其他"};
        JComboBox<String> typeArea = new JComboBox<>(TYPE);
        typeArea.setBounds(300,500,80,40);
        add(typeArea);

        //详细
        JLabel detail = new JLabel("描述：");
        detail.setFont(new Font("宋体", Font.PLAIN, 20));
        detail.setBounds(200,600,80,40);
        add(detail);

        detailArea = new JTextArea();
        detailArea.setFont(new Font("宋体", Font.PLAIN, 20));
        detailArea.setBounds(300,600,600,200);
        detailArea.setLineWrap(true);
        add(detailArea);

        //图片
        image = new JLabel();
        image.setBounds(1000,300,400,400);
        add(image);

        //文件选择
        JLabel imageLabel = new JLabel("图片：");
        imageLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        imageLabel.setBounds(1000,200,60,40);
        add(imageLabel);

        ImageIcon selectIcon = new ImageIcon(getClass().getResource("/icon/image.png"));
        JLabel selectImage = new JLabel(selectIcon);
        selectImage.setBounds(1050,200,40,40);
        selectImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser jfc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("文本文件(*png)", "png");
                jfc.setFileFilter(filter);
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
                jfc.showDialog(new JLabel(), "选择");
                File file = jfc.getSelectedFile();
                if (file != null) {
                    path = file.getAbsolutePath();
                    ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
                    imageIcon.setImage(imageIcon.getImage().getScaledInstance(400, 400,  Image.SCALE_DEFAULT));
                    image.setIcon(imageIcon);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                selectImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        add(selectImage);

        //提交
        ImageIcon submitIcon = new ImageIcon(getClass().getResource("/icon/上架商品.png"));
        JLabel submit = new JLabel(submitIcon);
        submit.setBounds(700,850,200,40);
        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Runnable r = ()->{
                    String name2 = nameArea.getText();
                    String detail2 = detailArea.getText();
                    String type2 = Objects.requireNonNull(typeArea.getSelectedItem()).toString();
                    if (!("".equals(name2) || "".equals(priceArea.getText()) || "".equals(quantityArea.getText())
                            || "".equals(detail2) || path == null))
                    {
                        double price2 = Double.parseDouble(priceArea.getText());
                        int quantity2 = Integer.parseInt(quantityArea.getText());
                        Product.onSale(name2, price2, quantity2, type2, detail2, path);
                        JOptionPane.showMessageDialog(null, "上架成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        refresh();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "请完善商品信息！", "失败", JOptionPane.ERROR_MESSAGE);
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
        add(submit);
    }

    private void refresh() {
        nameArea.setText("");
        priceArea.setText("");
        quantityArea.setText("");
        detailArea.setText("");
        image.setIcon(null);
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
                if (i <= 0 || i > 255) { // 限制范围
                    throw new Exception();
                }
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();// 发出提示声音
                return;
            }
            super.insertString(offs, str, a);// 把字符添加到文本框
        }
    }

    public class DoubleDocument extends PlainDocument {
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
                double i = Double.parseDouble(s); // 只能为正整数
                if (i <= 0 || i > 10000000) { // 限制范围
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
