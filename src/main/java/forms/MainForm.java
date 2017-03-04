package forms;

import components.BackgroundPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainForm extends JFrame {
    private JLabel usernameLabel, passwordLabel;
    private JTextField username;
    private JPasswordField password;
    private JButton login;


    public MainForm() throws IOException {
        setTitle("Полиглот");
        setSize(600,763);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        setContentPane(new BackgroundPanel(ImageIO.read(new File(this.getClass().getClassLoader().getResource("note.jpg").getFile()))));


        setLayout(new FlowLayout());

        ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("user.png").getFile());
        icon.setImage(getScaledImage(icon.getImage(), 50, 50));

        usernameLabel = new JLabel(
                //"Имя пользователя",
                icon/*,
                100*/);
        usernameLabel.setLabelFor(username);
        add(usernameLabel);

        username = new JTextField(35);
        username.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        add(username);


        ImageIcon pass = new ImageIcon(this.getClass().getClassLoader().getResource("pass.png").getFile());
        pass.setImage(getScaledImage(pass.getImage(), 50, 50));
        passwordLabel = new JLabel(pass);
        passwordLabel.setLabelFor(password);
        add(passwordLabel);

        password = new JPasswordField(35);
        password.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        add(password);

        login = new JButton("Войти");
        login.setBounds(50, 400, 200, 50);
        add(login);


       // add(b1);
        //setSize(400,400);*/
        //pack();

        setVisible(true);
    }



    public static void main(String ... args) throws IOException {
        new MainForm();
    }

    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
}
