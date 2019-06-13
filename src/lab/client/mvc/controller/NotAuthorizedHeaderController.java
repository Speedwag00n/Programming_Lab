package lab.client.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lab.client.settings.Settings;

import java.io.IOException;
import java.util.ResourceBundle;

public class NotAuthorizedHeaderController extends HeaderController {

    @FXML
    private Button auth_button;
    @FXML
    private Button registration_button;
    @FXML
    private Button settings_button;

    @FXML
    public void onLoginButtonClick() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Stage stage = (Stage) auth_button.getScene().getWindow();
        Controllers.changeSceneBody("/resources/login_view.fxml", stage, resources.getString("window_title_login_page"), new LoginController());
    }

    @FXML
    public void onRegistrationButtonClick() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Stage stage = (Stage) registration_button.getScene().getWindow();
        Controllers.changeSceneBody("/resources/registration_view.fxml", stage, resources.getString("window_title_registration_page"));
    }

    @FXML
    public void onSettingsButtonClick() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Stage stage = (Stage) settings_button.getScene().getWindow();
        Controllers.changeSceneBody("/resources/settings_view.fxml", stage, resources.getString("window_title_settings_page"), new SettingsController());
    }

    @FXML
    public void onExitButtonClick() {
        System.exit(0);
    }

}
