package lab.client.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lab.client.mvc.graphic.ComboBoxElement;
import lab.client.mvc.model.SettingsModel;
import lab.client.mvc.view.SettingsView;
import lab.client.settings.Settings;
import lab.util.commands.InvalidArgumentsException;

import java.io.IOException;
import java.util.ResourceBundle;

public class SettingsController extends Controller {

    @FXML
    private Button settings_pane_apply_button;
    @FXML
    private Label settings_pane_errors_label;
    @FXML
    private TextField port_field;
    @FXML
    private ComboBox<ComboBoxElement> items_type_combo_box;

    private SettingsModel model = new SettingsModel();
    private SettingsView view = new SettingsView();

    private String defaultErrorMessage;

    public SettingsController() {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        defaultErrorMessage = resources.getString("location_creation_pane_creation_hint");
    }

    public SettingsController(String errorMessage) {
        defaultErrorMessage = errorMessage;
    }

    @FXML
    public void onApplyButtonClick() throws IOException {
        ResourceBundle resources;
        try {
            if (items_type_combo_box.getValue() instanceof ComboBoxElement)
                model.applyLanguage(items_type_combo_box.getValue().getValue());
            model.applyPort(port_field.getText());

            Stage stage = (Stage) settings_pane_apply_button.getScene().getWindow();
            if (Settings.getToken() == null)
                Controllers.changeSceneHeader("/resources/not_authorized_header_view.fxml", stage);
            else
                Controllers.changeSceneHeader("/resources/authorized_header_view.fxml", stage);
            resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
            Controllers.changeSceneBody("/resources/settings_view.fxml", stage, resources.getString("window_title_settings_page"), new SettingsController(resources.getString("settings_applied")));
        }
        catch (InvalidArgumentsException e) {
            String errorMessage = e.getMessage();
            view.displayTextInErrorLabel(errorMessage, settings_pane_errors_label);
        }
    }

    @FXML
    public void initialize() {
        if (Settings.getToken() != null) {
            port_field.setDisable(true);
        }

        items_type_combo_box.setConverter(new StringConverter<ComboBoxElement>() {

            @Override
            public String toString(ComboBoxElement cbe) {
                return cbe.toString();
            }

            @Override
            public ComboBoxElement fromString(String s) {
                String[] factors = s.split("-");
                if (factors.length != 2) {
                    return null;
                }
                try {
                    return new ComboBoxElement(factors[0], factors[1]);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        });

        view.displayTextInErrorLabel(defaultErrorMessage, settings_pane_errors_label);
        Controllers.setIntegerInputType(port_field, 5);
        view.displayPort(model.getServerPort(), port_field);
    }

}
