package lab.client.mvc.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SettingsView extends View {

    @FXML
    private Label errorLabel;
    @FXML
    private TextField portField;

    public void displayTextInErrorLabel(String text, Label errorLabel) {
        this.errorLabel = errorLabel;
        errorLabel.setText(text);
    }

    public void displayPort(int port, TextField portField) {
        this.portField = portField;
        portField.setText(port + "");
    }

}
