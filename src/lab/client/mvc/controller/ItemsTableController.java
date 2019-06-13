package lab.client.mvc.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lab.client.mvc.graphic.ComboBoxElement;
import lab.client.mvc.model.ItemsTableModel;
import lab.client.mvc.view.ItemsTableView;
import lab.client.settings.Settings;
import lab.locations.Location;
import lab.locations.position.RectanglePosition;
import lab.objects.items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ItemsTableController extends Controller {

    private ItemsTableModel model = new ItemsTableModel();
    private ItemsTableView view = new ItemsTableView();

    @FXML
    private TableView locations_table_view;
    @FXML
    private TextField items_filter_pane_field;
    @FXML
    private ComboBox<ComboBoxElement> items_filter_pane_field_list;

    public ItemsTableController() {
        model.setItemsList(new ArrayList<>());
    }

    public ItemsTableController(List<Item> itemsList) {
        model.setItemsList(itemsList);
    }

    @FXML
    public void initialize() {
        view.showTablePlaceHolder(locations_table_view);

        items_filter_pane_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                startFiltering();
            }
        });

        ((TableColumn<Item, Void>)locations_table_view.getColumns().get(0)).setCellFactory((param) -> {
            TableCell<Item, Void> cell = new TableCell<Item, Void>() {
                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        Image image = getTableView().getItems().get(getIndex()).getIcon();
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(getTableColumn().getWidth() - 2);
                        imageView.setFitHeight(getTableColumn().getWidth() - 1);
                        imageView.getStyleClass().add("items-table-icon");
                        setGraphic(imageView);
                    }
                }
            };

            return cell;
        });
        ((TableColumn<Item, String>)locations_table_view.getColumns().get(1)).setCellValueFactory((item) -> {
            ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
            return new SimpleStringProperty(resources.getString("items_table_type_cell_" + item.getValue().getClass().getSimpleName().toLowerCase()));
        });
        ((TableColumn<Item, String>)locations_table_view.getColumns().get(3)).setCellValueFactory((item) -> {return new SimpleStringProperty(item.getValue().getAttributesDescription());});

        view.displayLocationsListTable(model.getItemsList(), locations_table_view, false);
    }

    @FXML
    public void onTableFilterSelected() {
        startFiltering();
    }

    private void startFiltering() {
        List<Item> items = null;
        if (items_filter_pane_field_list.getValue() instanceof ComboBoxElement)
            items = model.filter(items_filter_pane_field_list.getValue().getValue(), items_filter_pane_field.getText());
        if (items != null)
            view.displayLocationsListTable(items, locations_table_view, true);

    }

}
