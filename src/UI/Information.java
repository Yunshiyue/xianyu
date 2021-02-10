package UI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Qingyue
 */
public class Information extends JPanel {
    Information() {
        setLayout(null);
        setBounds(0,0,1600,970);

        //上面的bar
        JPanel bar = new JPanel();
        bar.setBounds(0,0,1600,50);
        bar.setLayout(null);
        bar.setBackground(new Color(250,80,0));
        add(bar);

        //bar中的信息
        JLabel nameOfBar = new JLabel("个人信息");
        nameOfBar.setFont(new Font("黑体",Font.BOLD,20));
        nameOfBar.setBounds(700,10,200,30);
        nameOfBar.setForeground(Color.WHITE);
        nameOfBar.setHorizontalAlignment(JTextField.CENTER);
        bar.add(nameOfBar);

        //用户名
        JLabel username = new JLabel("您好," + Home.getUserId());
        username.setBounds(100,100,200,40);
        username.setFont(new Font("黑体",Font.PLAIN,20));
        add(username);

        //订单
        JButton orderButton = new JButton("查看订单");
        orderButton.setBounds(100,500,150,40);
        orderButton.addActionListener(e->{
            Runnable r = ()->{
                Home home = (Home) getRootPane().getParent();
                home.switchPanel("订单");
            };
            Thread thread = new Thread(r);
            thread.start();
        });
        add(orderButton);

        //上架商品
        JButton onSaleButton = new JButton("上架商品");
        onSaleButton.setBounds(100,600,150,40);
        onSaleButton.addActionListener(e->{
            Runnable r = ()->{
                Home home = (Home) getRootPane().getParent();
                home.switchPanel("上架商品");
            };
            Thread thread = new Thread(r);
            thread.start();
        });
        add(onSaleButton);

        //我的
        JButton myProductButton = new JButton("我的商品");
        myProductButton.setBounds(400,600,150,40);
        myProductButton.addActionListener(e->{
            Runnable r = ()->{
                Home home = (Home) getRootPane().getParent();
                home.switchPanel("我的商品");
            };
            Thread thread = new Thread(r);
            thread.start();
        });
        add(myProductButton);
    }
}
