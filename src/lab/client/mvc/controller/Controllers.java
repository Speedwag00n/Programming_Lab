package lab.client.mvc.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lab.client.ServerAnswer;
import lab.client.ServerChangeListener;
import lab.client.settings.Settings;
import lab.util.commands.Commands;

import java.io.IOException;
import java.util.ResourceBundle;

public class Controllers {

    private static FXMLLoader previousLoader;

    public static void changeSceneHeader(String newSceneHeader, Stage stage) throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Scene scene = stage.getScene();
        VBox root = (VBox)scene.getRoot();
        Node body = root.getChildren().remove(1);
        root.getChildren().remove(0);
        if (previousLoader != null)
            ((Controller)previousLoader.getController()).stop();
        FXMLLoader loader = new FXMLLoader(Controllers.class.getResource(newSceneHeader), resources);
        previousLoader = loader;
        root.getChildren().add(loader.load());
        root.getChildren().add(body);
    }

    public static void changeSceneBody(String newSceneBody, Stage stage, String windowTitle) throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Scene scene = stage.getScene();
        VBox root = (VBox)scene.getRoot();
        root.getChildren().remove(1);
        if (previousLoader != null)
            ((Controller)previousLoader.getController()).stop();
        FXMLLoader loader = new FXMLLoader(Controllers.class.getResource(newSceneBody), resources);
        previousLoader = loader;
        root.getChildren().add(loader.load());
        stage.setTitle(windowTitle);
    }

    public static void changeSceneBody(String newSceneBody, Stage stage, String windowTitle, Controller controller) throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Scene scene = stage.getScene();
        VBox root = (VBox)scene.getRoot();
        root.getChildren().remove(1);
        if (previousLoader != null)
            ((Controller)previousLoader.getController()).stop();
        FXMLLoader loader = new FXMLLoader(Controllers.class.getResource(newSceneBody), resources);
        previousLoader = loader;
        loader.setController(controller);
        root.getChildren().add(loader.load());
        stage.setTitle(windowTitle);
    }

    public static void setIntegerInputType(TextField field, int maxNums) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > maxNums) {
                    field.setText(oldValue);
                }
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public static void setFloatInputType(TextField field) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 10) {
                    field.setText(oldValue);
                }
            }
        });
    }

    public static String checkServerAnswer(ServerAnswer serverAnswer) {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        String answer = "";
        Commands.CommandExecutionStatus status = serverAnswer.getStatus();
        if (status == Commands.CommandExecutionStatus.NOT_LOGGED_NOT_SUCCESSFUL) {
            answer = resources.getString("logout_not_authorized");
        }
        else if (status == Commands.CommandExecutionStatus.NO_RESPONSE) {
            answer = resources.getString("logout_no_response");
        }
        else if (status == Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL) {
            answer = resources.getString("location_creation_successful_added");
        }
        return answer;
    }

    public static boolean checkServerAnswer(Stage stage, ServerAnswer serverAnswer) {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Commands.CommandExecutionStatus status = serverAnswer.getStatus();
        boolean stopListener = (Settings.getToken() != null);
        try {
            if (status == Commands.CommandExecutionStatus.NOT_LOGGED_NOT_SUCCESSFUL) {
                logout(stage, resources.getString("logout_not_authorized"), stopListener);
                return false;
            }
            else if (status == Commands.CommandExecutionStatus.NO_RESPONSE) {
                logout(stage, resources.getString("logout_no_response"), stopListener);
                return false;
            }
        }
        catch (IOException e) {

        }
        return true;
    }

    public static void logout(Stage stage, String message, boolean stopListener) throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Settings.setToken(null);
        if (stopListener)
            ServerChangeListener.stop();
        Controllers.changeSceneHeader("/resources/not_authorized_header_view.fxml", stage);
        Controllers.changeSceneBody("/resources/login_view.fxml", stage, resources.getString("window_title_login_page"), new LoginController(message));
    }

}
