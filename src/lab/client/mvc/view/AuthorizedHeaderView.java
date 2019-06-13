package lab.client.mvc.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AuthorizedHeaderView extends View {

    @FXML
    private Label greetingsLabel;

        public void displayTextInGreetingsLabel(String text, Label greetingsLabel) {
        this.greetingsLabel = greetingsLabel;
        greetingsLabel.setText(text);
    }

}
