package fx.controller;

import fx.dao.UserDao;
import fx.dao.UserDaoImpl;
import fx.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import fx.model.User;

import java.io.IOException;

public class LoginController {
    @FXML private Label appTitle;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button login;

    private Stage stage;

    public void login(ActionEvent event) throws IOException {
        String usernameText = username.getText();
        String passText = password.getText();

        if (usernameText.trim().isEmpty()) {
            username.requestFocus();
            return;
        }

        if (passText.trim().isEmpty()) {
            password.requestFocus();
            return;
        }

        UserDao userDao = new UserDaoImpl();
        User user = userDao.findByUsername(usernameText);

        if (user != null) {
            Parent panel = FXMLLoader.load(this.getClass().getResource("/subjects.fxml"));
            Scene scene = new Scene(panel, 600, 763);
            stage.setScene(scene);
            UserSession.setUser(user);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
