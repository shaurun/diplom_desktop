package app;

import forms.LoginForm;

import javax.swing.*;

import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Application {
    public static void main(String ... args) {
        JFrame frame = new JFrame("Полиглот");



        frame.setContentPane(new LoginForm().getPanel());
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(new Dimension(400, 600));
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);

    }
}
