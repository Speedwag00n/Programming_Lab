package lab.client.mvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import lab.client.settings.Settings;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController extends Controller {

    @FXML
    private VBox root;

    @FXML
    public void initialize() throws IOException {
        FXMLLoader loader;
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());

        loader = new FXMLLoader(getClass().getResource("/resources/not_authorized_header_view.fxml"), resources);
        root.getChildren().add(loader.load());
        loader = new FXMLLoader(getClass().getResource("/resources/login_view.fxml"), resources);
        loader.setController(new LoginController());
        root.getChildren().add(loader.load());
    }

}