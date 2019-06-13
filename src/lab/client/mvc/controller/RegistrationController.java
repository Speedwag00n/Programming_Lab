package lab.client.mvc.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lab.client.ServerAnswer;
import lab.client.mvc.model.RegistrationModel;
import lab.client.mvc.view.RegistrationView;
import lab.client.settings.Settings;
import lab.util.commands.InvalidArgumentsException;

import java.util.ResourceBundle;

public class RegistrationController extends Controller {

    private RegistrationModel model = new RegistrationModel();
    private RegistrationView view = new RegistrationView();

    @FXML
    private Label registration_pane_errors_label;
    @FXML
    private TextField login_field;
    @FXML
    private TextField email_field;
    @FXML
    private Button registration_pane_login_button;

    @FXML
    public void onRegisterButtonClick() {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        model.setLogin(login_field.getText());
        model.setEmail(email_field.getText());
        try {
            model.validate();
            ServerAnswer serverAnswer = model.register();
            view.displayTextInErrorLabel(Controllers.checkServerAnswer(serverAnswer), registration_pane_errors_label);
        } catch (InvalidArgumentsException e) {
            String errorMessage = e.getMessage();
            view.displayTextInErrorLabel(errorMessage, registration_pane_errors_label);
        }
    }

    @FXML
    public void initialize() {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        registration_pane_errors_label.setText(resources.getString("registration_pane_register_hint"));

        login_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (newValue.length() > 30 ) {
                    login_field.setText(oldValue);
                }
            }
        });
    }

}
