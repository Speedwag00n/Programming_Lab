package lab.client.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lab.client.mvc.view.AuthorizedHeaderView;
import lab.client.settings.Settings;

import java.io.IOException;
import java.util.ResourceBundle;

public class AuthorizedHeaderController extends HeaderController{

    private AuthorizedHeaderView view = new AuthorizedHeaderView();

    @FXML
    private Button logout_button;
    @FXML
    private Button settings_button;
    @FXML
    private Label greetings_label;

    @FXML
    public void onCreationButtonClick() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Stage stage = (Stage) settings_button.getScene().getWindow();
        Controllers.changeSceneBody("/resources/location_creation_view.fxml", stage, resources.getString("window_title_location_creation_page"), new LocationCreationController());
    }

    @FXML
    public void onTableButtonClick() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Stage stage = (Stage) settings_button.getScene().getWindow();
        Controllers.changeSceneBody("/resources/locations_table_view.fxml", stage, resources.getString("window_title_locations_table_page"));
    }

    @FXML
    public void onVisualisationButtonClick() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Stage stage = (Stage) settings_button.getScene().getWindow();
        Controllers.changeSceneBody("/resources/visualisation_view.fxml", stage, resources.getString("window_title_visualisation_page"), VisualisationController.getInstance());
    }

    @FXML
    public void onSettingsButtonClick() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Stage stage = (Stage) settings_button.getScene().getWindow();
        Controllers.changeSceneBody("/resources/settings_view.fxml", stage, resources.getString("window_title_settings_page"), new SettingsController());
    }

    @FXML
    public void onLogoutButtonClick() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Controllers.logout((Stage) logout_button.getScene().getWindow(), resources.getString("logout_message"), true);
    }

    @FXML
    public void initialize() {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        view.displayTextInGreetingsLabel(resources.getString("header_greetings_label") + " " + Settings.getUsername(), greetings_label);
    }

}