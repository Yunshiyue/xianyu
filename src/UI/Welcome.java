package UI;

import character.User;
import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * @author Qingyue
 */
public class Welcome extends JFrame {
    private static final int SCREEN_WIDTH = 1700;
    private static final int SCREEN_HEIGHT = 970;
    private final CardLayout cardLayout;
    private final Container contentPane;

    Welcome() {
        //设置图标
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon/logo.png"));
        setIconImage(imageIcon.getImage());

        setTitle("闲鱼");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        cardLayout = new CardLayout();

        contentPane = getContentPane();
        contentPane.setLayout(cardLayout);

        contentPane.add(new Login(),"login");
        contentPane.add(new Register(),"register");

        cardLayout.show(contentPane,"login");

    }

    public class Login extends JPanel {
        private final JTextField username;
        private final JPasswordField password;

        Login() {
            setLayout(null);
            setBounds(0,0,1700,960);
            setBackground(new Color(255,80,0));

            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/icon/logo2.png"));
            JLabel logo = new JLabel(logoIcon);
            logo.setBounds(600,100,400,400);
            add(logo);

            JLabel usernameLabel = new JLabel("用户名");
            usernameLabel.setBounds(600,550,200,40);
            usernameLabel.setFont(new Font("宋体",Font.PLAIN,16));
            add(usernameLabel);

            JLabel passwordLabel = new JLabel("密码");
            passwordLabel.setBounds(600,650,200,40);
            passwordLabel.setFont(new Font("宋体",Font.PLAIN,16));
            add(passwordLabel);

            username = new JTextField(30);
            username.setBounds(700,550,200,40);
            username.setFont(new Font("宋体",Font.PLAIN,16));
            add(username);

            password = new JPasswordField(30);
            password.setBounds(700,650,200,40);
            password.setFont(new Font("宋体",Font.PLAIN,16));
            add(password);

            ImageIcon loginIcon = new ImageIcon(getClass().getResource("/icon/登录.png"));
            JLabel login = new JLabel(loginIcon);
            login.setBounds(700,750,80,40);
            login.setFont(new Font("宋体",Font.PLAIN,16));
            login.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Runnable r = () -> {
                        try {
                            if("".equals(username.getText()) || "".equals(password.getText()))
                            {
                                JOptionPane.showMessageDialog(null, "账号或密码不可为空", "错误", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            Client client = new Client();
                            boolean succeed = client.sendLogin(username.getText(),password.getText());
                            if (succeed){
                                Home home = new Home(username.getText(), client);
                                dispose();
                                home.setVisible(true);
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "账号或密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException exception) {
                            JOptionPane.showMessageDialog(null, "请检查网络连接", "登录失败", JOptionPane.ERROR_MESSAGE);
                        }
                    };
                    Thread thread = new Thread(r);
                    thread.start();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    login.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            add(login);

            ImageIcon registerIcon = new ImageIcon(getClass().getResource("/icon/注册.png"));
            JLabel register = new JLabel(registerIcon);
            register.setBounds(800,750,80,40);
            register.setFont(new Font("宋体",Font.PLAIN,16));
            register.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                        cardLayout.show(contentPane,"register");
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    register.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            add(register);
        }

    }

    public class Register extends JPanel {
        private JTextField userNameArea;
        private JPasswordField passwordArea1;
        private JPasswordField passwordArea2;
        private JTextField phoneNumberArea;
        private JTextField emailArea;

        Register() {
            setLayout(null);
            setBackground(new Color(255,80,0));

            //用户名
            JLabel userName = new JLabel("用户名");
            userName.setFont(new Font("宋体",Font.PLAIN, 20));
            userName.setBounds(650,200,80,40);
            add(userName);

            userNameArea = new JTextField();
            userNameArea.setBounds(750,200,160,40);
            userNameArea.setFont(new Font("宋体",Font.PLAIN, 20));
            add(userNameArea);

            //密码1
            JLabel password1 = new JLabel("密码");
            password1.setFont(new Font("宋体",Font.PLAIN, 20));
            password1.setBounds(650,300,80,40);
            add(password1);

            passwordArea1 = new JPasswordField();
            passwordArea1.setBounds(750,300,160,40);
            passwordArea1.setFont(new Font("宋体",Font.PLAIN, 20));
            add(passwordArea1);

            //密码2
            JLabel password2 = new JLabel("确认密码");
            password2.setFont(new Font("宋体",Font.PLAIN, 20));
            password2.setBounds(650,400,90,40);
            add(password2);

            passwordArea2 = new JPasswordField();
            passwordArea2.setBounds(750,400,160,40);
            passwordArea2.setFont(new Font("宋体",Font.PLAIN, 20));
            add(passwordArea2);

            //手机
            JLabel phoneNumber = new JLabel("手机");
            phoneNumber.setFont(new Font("宋体",Font.PLAIN, 20));
            phoneNumber.setBounds(650,500,80,40);
            add(phoneNumber);

            phoneNumberArea = new JTextField();
            phoneNumberArea.setBounds(750,500,160,40);
            phoneNumberArea.setFont(new Font("宋体",Font.PLAIN, 20));
            add(phoneNumberArea);

            //邮箱
            JLabel email = new JLabel("邮箱");
            email.setFont(new Font("宋体",Font.PLAIN, 20));
            email.setBounds(650,600,80,40);
            add(email);

            emailArea = new JTextField();
            emailArea.setBounds(750,600,160,40);
            emailArea.setFont(new Font("宋体",Font.PLAIN, 20));
            add(emailArea);

            //注册按钮
            ImageIcon registerIcon = new ImageIcon(getClass().getResource("/icon/注册注册.png"));
            JLabel registerButton = new JLabel(registerIcon);
            registerButton.setBounds(700,700,160,40);
            registerButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    String userIdContent = userNameArea.getText();
                    String passwordContent1 = passwordArea1.getText();
                    String passwordContent2 = passwordArea2.getText();
                    String phoneNumberContent = phoneNumberArea.getText();
                    String emailContent = emailArea.getText();
                    if (("".equals(userIdContent) || "".equals(passwordContent1) || "".equals(passwordContent2) || "".equals(phoneNumberContent) || "".equals(emailContent))) {
                        JOptionPane.showMessageDialog(null, "请输入完整信息！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (!passwordContent1.equals(passwordContent2)) {
                        JOptionPane.showMessageDialog(null, "两次输入的密码不一致！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        User user = new User(userIdContent, passwordContent1, phoneNumberContent, emailContent);
                        if (user.register()) {
                            JOptionPane.showMessageDialog(null, "注册成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                            cardLayout.show(contentPane, "login");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "用户名已存在！", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            add(registerButton);

            //返回按钮
            ImageIcon backIcon = new ImageIcon(getClass().getResource("/icon/返回.png"));
            JLabel backButton = new JLabel(backIcon);
            backButton.setBounds(700,750,160,40);
            backButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    cardLayout.show(contentPane, "login");
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            add(backButton);
        }
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(()->
                {
                    Welcome welcome = new Welcome();
                    welcome.setVisible(true);
                }
        );

    }
}