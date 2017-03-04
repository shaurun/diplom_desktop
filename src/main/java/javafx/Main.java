package javafx;

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

        Parent panel = FXMLLoader.load(this.getClass().getResource("/login.fxml"));
        Scene scene = new Scene(panel, 600, 763);

        primaryStage.setTitle("JavaFX title");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String ... args) {
        launch(args);
    }
}
