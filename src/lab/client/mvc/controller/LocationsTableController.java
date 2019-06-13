package lab.client.mvc.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import lab.client.LocationsContainer;
import lab.client.ServerAnswer;
import lab.client.mvc.graphic.ComboBoxElement;
import lab.client.mvc.model.LocationsTableModel;
import lab.client.mvc.view.LocationsTableView;
import lab.client.settings.Settings;
import lab.locations.Location;
import lab.locations.position.RectanglePosition;
import lab.objects.items.Item;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LocationsTableController extends Controller {

    private LocationsTableModel model = new LocationsTableModel();
    private LocationsTableView view = new LocationsTableView();

    @FXML
    private TableView locations_table_view;
    @FXML
    private TextField locations_filter_pane_field;
    @FXML
    private ComboBox<ComboBoxElement> locations_filter_pane_field_list;

    @FXML
    public void initialize() {
        ((TableColumn<Location, String>)locations_table_view.getColumns().get(2)).setCellValueFactory((location) -> {RectanglePosition position = location.getValue().getPosition(); return new SimpleStringProperty("(" + position.getLeftBottomPoint().getX() + ";" + position.getLeftBottomPoint().getY() + "), (" +
                +position.getRightTopPoint().getX() + ";" + position.getRightTopPoint().getY() + ")");});
        ((TableColumn<Location, String>)locations_table_view.getColumns().get(3)).setCellValueFactory((location) -> {DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return new SimpleStringProperty(formatter.format(location.getValue().getDateOfCreation()));});
        ((TableColumn<Location, String>)locations_table_view.getColumns().get(4)).setCellValueFactory((location) -> {return new SimpleStringProperty(location.getValue().getItems().size() + "");});

        locations_filter_pane_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                startFiltering();
            }
        });

        locations_table_view.setRowFactory(
                new Callback<TableView<Location>, TableRow<Location>>() {
                    @Override
                    public TableRow<Location> call(TableView<Location> tableView) {
                        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
                        TableRow<Location> row = new TableRow<>();
                        ContextMenu locationsListMenu = new ContextMenu();
                        locationsListMenu.getStyleClass().add("items-list-context-menu");
                        MenuItem showItem = new MenuItem(resources.getString("context_menu_show"));
                        showItem.getStyleClass().add("items-list-context-menu-item");
                        showItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
                                Stage stage = (Stage) locations_table_view.getScene().getWindow();
                                try {
                                    Controllers.changeSceneBody("/resources/items_table_view.fxml", stage, resources.getString("window_title_items_table_page"), new ItemsTableController(row.getItem().getItems()));
                                }
                                catch (IOException e) {

                                }
                            }
                        });
                        MenuItem editItem = new MenuItem(resources.getString("context_menu_edit"));
                        editItem.getStyleClass().add("items-list-context-menu-item");
                        editItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
                                Stage stage = (Stage) locations_table_view.getScene().getWindow();
                                try {
                                    Controllers.changeSceneBody("/resources/location_creation_view.fxml", stage, resources.getString("window_title_location_update_page"), new LocationCreationController(row.getItem()));
                                }
                                catch (IOException e) {

                                }
                            }
                        });
                        MenuItem removeItem = new MenuItem(resources.getString("context_menu_delete"));
                        removeItem.getStyleClass().add("items-list-context-menu-item");
                        removeItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                ServerAnswer answer = model.removeLocation(row.getItem());
                                if (Controllers.checkServerAnswer((Stage)locations_table_view.getScene().getWindow(), answer))
                                    tableView.getItems().remove(row.getItem());
                            }
                        });

                        locationsListMenu.getItems().add(showItem);
                        locationsListMenu.setOnShowing(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                if (row.getItem().getOwner().equals(Settings.getUsername()) && locationsListMenu.getItems().size() != 3) {
                                    locationsListMenu.getItems().add(editItem);
                                    locationsListMenu.getItems().add(removeItem);
                                }
                            }
                        });

                        row.contextMenuProperty().bind(
                                Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                        .then(locationsListMenu)
                                        .otherwise((ContextMenu)null));
                        return row;
                    }
                });

        view.displayLocationsListTable(LocationsContainer.getInstance().getLocations(), locations_table_view, false);

    }

    @FXML
    public void onTableFilterSelected() {
        startFiltering();
    }

    private void startFiltering() {
        List<Location> locations = null;
        if (locations_filter_pane_field_list.getValue() instanceof ComboBoxElement)
            locations = model.filter(locations_filter_pane_field_list.getValue().getValue(), locations_filter_pane_field.getText());
        if (locations != null)
            view.displayLocationsListTable(locations, locations_table_view, true);

    }

}
