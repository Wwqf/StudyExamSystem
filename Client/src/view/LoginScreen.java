package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class LoginScreen extends JFrame implements WindowListener, ActionListener {

    /* 屏幕控制 */
    JFrame screen;
    int scrWidth = 350;
    int scrHeight = 200;
    Point scrLocation = new Point(450, 200);

    // 用户名和密码
    JLabel usernameLabel, passwordLabel;
    JTextField usernameTextField;
    JPasswordField passwordTextField;
    JButton login;


    public LoginScreen() {
        screen = createJFrame();

        usernameLabel = new JLabel();
        passwordLabel = new JLabel();
        usernameTextField = new JTextField();
        passwordTextField = new JPasswordField();

        login = new JButton();

        setLabel();
        setTextField();
        setButton();
        login.addActionListener(this);
        screen.add(usernameLabel);
        screen.add(passwordLabel);
        screen.add(usernameTextField);
        screen.add(passwordTextField);
        screen.add(login);



        setJFrameParameter(screen);
        screen.addWindowListener(this);
    }

    /* 创建窗体 */
    private JFrame createJFrame() {
        return new JFrame();
    }

    /* 设置界面参数 */
    private void setJFrameParameter(JFrame screen) {
        screen.setSize(scrWidth, scrHeight);
        screen.setLocation(scrLocation.x, scrLocation.y);
        screen.setLayout(null);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setVisible(true);
    }


    private void setLabel() {
        Font font = new Font("楷体", Font.BOLD, 18);

        usernameLabel.setText("姓名: ");
        usernameLabel.setFont(font);
        usernameLabel.setSize(60, 25);
        usernameLabel.setLocation(50, 30);

        passwordLabel.setText("密码: ");
        passwordLabel.setFont(font);
        passwordLabel.setSize(60, 25);
        passwordLabel.setLocation(50, 65);
    }

    private void setTextField() {
        Font font = new Font("楷体", Font.BOLD, 16);
        usernameTextField.setSize(200, 25);
        usernameTextField.setLocation(110, 30);
        usernameTextField.setFont(font);

        passwordTextField.setSize(200, 25);
        passwordTextField.setLocation(110, 65);
        passwordTextField.setFont(font);
    }

    private void setButton() {
        Font font = new Font("楷体", Font.BOLD, 18);

        login.setText("登录");
        login.setFont(font);
        login.setSize(120, 30);
        login.setLocation((scrWidth - 100) / 2, 110);
    }


    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.setVisible(false);
        this.dispose();
        ExaminationScreen examinationScreen = new ExaminationScreen();
        examinationScreen.selfInformation.setName(usernameTextField.getText());
        examinationScreen.usernameLabel.setText("学生: " + usernameTextField.getText());
    }
}
