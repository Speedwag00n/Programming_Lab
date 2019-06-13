package lab.client.start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lab.client.settings.Settings;

import java.util.*;

/**
 * Main class of client side of application that reads commands from command line, parses them and sends server command to server side.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.6.0
 */
public class Main extends Application{


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Parent root = FXMLLoader.load(getClass().getResource("/resources/main_view.fxml"), resources);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Objects service application (login page)");
        primaryStage.show();
        if (primaryStage.getHeight() > 1000){
            scene.getStylesheets().add("/resources/css/style-1280-1024.css");
        }
        else {
            scene.getStylesheets().add("/resources/css/style-small.css");
        }
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (primaryStage.getHeight() > 1000){
                scene.getStylesheets().clear();
                scene.getStylesheets().add("/resources/css/style-1280-1024.css");
            }
            else {
                scene.getStylesheets().clear();
                scene.getStylesheets().add("/resources/css/style-small.css");
            }
        });
    }

}
