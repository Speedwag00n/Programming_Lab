package lab.client.mvc.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RegistrationView extends View{

    @FXML
    private Label errorLabel;

    public void displayTextInErrorLabel(String text, Label errorLabel) {
        this.errorLabel = errorLabel;
        errorLabel.setText(text);
    }

}
