package fx.javafx;

import fx.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Created by User on 02.03.2017.
 */
public class Main extends Application {
    public void start(Stage primaryStage) throws Exception {
        /*Button button = new Button("test");
        Text text = new Text(10, 20, "text java");
        text.setFont(new Font(40));
        BorderPane pane = new BorderPane();
        pane.setCenter(button);
        pane.setTop(text);

        Scene scene = new Scene(pane, 400, 400);*/

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent panel = loader.load();
        LoginController controller = (LoginController) loader.getController();
        controller.setStage(primaryStage);
        Scene scene = new Scene(panel, 600, 763);

        primaryStage.setTitle("Полиглот");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String ... args) {
        launch(args);
    }
}
