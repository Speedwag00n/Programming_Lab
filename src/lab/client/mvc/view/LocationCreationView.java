package lab.client.mvc.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lab.client.settings.Settings;
import lab.objects.items.Item;

import java.util.List;
import java.util.ResourceBundle;

public class LocationCreationView extends View{

    @FXML
    private Label errorLabel;
    @FXML
    private TextField locationName;
    @FXML
    private TextField bottomLeftX;
    @FXML
    private TextField bottomLeftY;
    @FXML
    private TextField topRightX;
    @FXML
    private TextField topRightY;
    @FXML
    private Menu itemsMenuTitle;
    @FXML
    private TableView itemsListTable;
    @FXML
    private Label title;
    @FXML
    private Button creationButton;
    @FXML
    private Label pathLabel;

    public void displayTextInErrorLabel(String text, Label errorLabel) {
        this.errorLabel = errorLabel;
        errorLabel.setText(text);
    }

    public void displayLocationName(String text, TextField locationName) {
        this.locationName= locationName;
        locationName.setText(text);
    }

    public void displayBottomLeftX(String text, TextField bottomLeftX) {
        this.bottomLeftX= bottomLeftX;
        bottomLeftX.setText(text);
    }

    public void displayBottomLeftY(String text, TextField bottomLeftY) {
        this.bottomLeftY= bottomLeftY;
        bottomLeftY.setText(text);
    }

    public void displayTopRightX(String text, TextField topRightX) {
        this.topRightX= topRightX;
        topRightX.setText(text);
    }

    public void displayTopRightY(String text, TextField topRightY) {
        this.topRightY= topRightY;
        topRightY.setText(text);
    }

    public void displayItemsMenuTitle(String text, Menu itemsMenuTitle) {
        this.itemsMenuTitle = itemsMenuTitle;
        itemsMenuTitle.setText(text);
    }

    public void displayItemsListTable(List<Item> itemsList, TableView itemsListTable) {
        this.itemsListTable = itemsListTable;
        for (Item item : itemsList){
            itemsListTable.getItems().add(item);
        }
    }

    public void displayTitle(String text, Label title) {
        this.title = title;
        title.setText(text);
    }

    public void displayCreationButtonText(String text, Button creationButton){
        this.creationButton = creationButton;
        creationButton.setText(text);
    }

    public void showTablePlaceHolder(TableView itemsListTable) {
        this.itemsListTable = itemsListTable;
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        itemsListTable.setPlaceholder(new Text(resources.getString("items_list_empty_placeholder")));
    }

    public void displayImagePath(String path, Label pathLabel) {
        this.pathLabel = pathLabel;
        pathLabel.setText(path);
    }

}
