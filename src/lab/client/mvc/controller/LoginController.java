package lab.client.mvc.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lab.client.ServerAnswer;
import lab.client.LocationsContainer;
import lab.client.mvc.model.LoginModel;
import lab.client.mvc.view.LoginView;
import lab.client.settings.Settings;
import lab.locations.Location;
import lab.util.commands.Commands;
import lab.util.commands.InvalidArgumentsException;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController extends Controller {


    private LoginModel model = new LoginModel();
    private LoginView view = new LoginView();

    private String defaultErrorMessage;

    @FXML
    private Label login_pane_errors_label;
    @FXML
    private TextField login_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Button login_pane_login_button;

    public LoginController() {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        defaultErrorMessage = resources.getString("login_pane_login_hint");
    }

    public LoginController(String errorMessage) {
        defaultErrorMessage = errorMessage;
    }

    @FXML
    public void onLoginButtonClick() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        model.setLogin(login_field.getText());
        model.setPassword(password_field.getText());
        try {
            Stage stage = (Stage) login_pane_login_button.getScene().getWindow();
            model.validate();
            ServerAnswer authServerAnswer = model.authorize();
            if (Controllers.checkServerAnswer(stage, authServerAnswer)) {
                Settings.setToken(authServerAnswer.getAnswer());
                Settings.setUsername(login_field.getText().trim());

                ServerAnswer getServerAnswer = model.getLocations();
                if (Controllers.checkServerAnswer(stage, getServerAnswer)) {
                    List<Location> locations = Commands.unpackLocations(getServerAnswer.getPackedData());
                    LocationsContainer.getInstance().setLocations(locations);

                    Controllers.changeSceneHeader("/resources/authorized_header_view.fxml", stage);
                    Controllers.changeSceneBody("/resources/locations_table_view.fxml", stage, resources.getString("window_title_locations_table_page"));
                }
            }
            else {
                view.displayTextInErrorLabel(Controllers.checkServerAnswer(authServerAnswer), login_pane_errors_label);
            }
        }
        catch (InvalidArgumentsException e) {
            String errorMessage = e.getMessage();
            view.displayTextInErrorLabel(errorMessage, login_pane_errors_label);
        }
    }

    @FXML
    public void initialize() {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        view.displayTextInErrorLabel(defaultErrorMessage, login_pane_errors_label);

        login_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (newValue.length() > 30 ) {
                    login_field.setText(oldValue);
                }
            }
        });

        password_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (newValue.length() > 16) {
                    password_field.setText(oldValue);
                }
            }
        });
    }

}