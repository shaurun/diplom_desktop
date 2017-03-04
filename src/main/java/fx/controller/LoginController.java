package fx.controller;

import fx.javafx.Main;
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

import java.io.IOException;

/**
 * Created by User on 04.03.2017.
 */
public class LoginController {
    @FXML private Label appTitle;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button login;

    private Stage stage;

    public void login(ActionEvent event) throws IOException {
        if (username.getText().equals("admin")
                && password.getText().equals("admin")) {

            Parent panel = FXMLLoader.load(this.getClass().getResource("/subjects.fxml"));
            Scene scene = new Scene(panel, 600, 763);
            stage.setScene(scene);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
