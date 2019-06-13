package lab.client.mvc.controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import lab.client.ServerAnswer;
import lab.client.mvc.graphic.ComboBoxElement;
import lab.client.mvc.model.LocationCreationModel;
import lab.client.mvc.view.LocationCreationView;
import lab.client.settings.Settings;
import lab.locations.Location;
import lab.locations.position.RectanglePosition;
import lab.objects.items.Item;
import lab.objects.items.miningInstruments.Hammer;
import lab.objects.items.miningInstruments.MiningInstrument;
import lab.objects.items.rock.Rock;
import lab.util.commands.InvalidArgumentsException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class LocationCreationController extends Controller {

    public enum Mode {
        ADD, UPDATE;
    }

    private LocationCreationModel model = new LocationCreationModel();
    private LocationCreationView view = new LocationCreationView();

    private Mode mode;

    private String defaultErrorMessage;

    @FXML
    private TextField location_creation_name_field;
    @FXML
    private TextField location_creation_blp_x_field;
    @FXML
    private TextField location_creation_blp_y_field;
    @FXML
    private TextField location_creation_trp_x_field;
    @FXML
    private TextField location_creation_trp_y_field;
    @FXML
    private AnchorPane body_pane;

    @FXML
    private Button location_creation_pane_next_button;
    @FXML
    private Label location_creation_pane_errors_label;

    @FXML
    private MenuBar included_items_menu_view;
    @FXML
    private Menu items_menu_title;

    @FXML
    private TableView items_table;

    @FXML
    private Label location_creation_pane_title;

    @FXML
    private Button item_creation_pane_create_button;

    @FXML
    private Button creation_pane_image_chooser;
    @FXML
    private Label creation_pane_image_chooser_path_label;

    public LocationCreationController() {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        this.mode = Mode.ADD;
        defaultErrorMessage = resources.getString("location_creation_pane_creation_hint");
    }

    public LocationCreationController(String errorMessage) {
        this.mode = Mode.ADD;
        defaultErrorMessage = errorMessage;
    }

    public LocationCreationController(Location location) {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        this.mode = Mode.UPDATE;
        model.setLocation(location);
        defaultErrorMessage = resources.getString("update_location_pane_creation_hint");
    }

    @FXML
    public void onNextButtonClick() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        model.setName(location_creation_name_field.getText());
        try {
            model.validateName(location_creation_name_field.getText());
            model.validatePosition(location_creation_blp_x_field.getText(), location_creation_blp_y_field.getText(),
                    location_creation_trp_x_field.getText(), location_creation_trp_y_field.getText());
            model.setPosition(location_creation_blp_x_field.getText(), location_creation_blp_y_field.getText(),
                    location_creation_trp_x_field.getText(), location_creation_trp_y_field.getText());
            Stage stage = (Stage) body_pane.getScene().getWindow();
            Controllers.changeSceneBody("/resources/piece_creation_view.fxml", stage, resources.getString("window_title_items_adding_page"), this);
            view.displayItemsMenuTitle(resources.getString("items_menu_piece"), items_menu_title);

            showCreationButton();
            Controllers.setFloatInputType(piece_creation_pane_weight_field);

            itemClass = Rock.Piece.class;
            initializeImageChooser();

            setItemsMenuRowFactory(items_table);
            List<Item> items = model.getItems();
            view.displayItemsListTable(items, items_table);
        }
        catch (InvalidArgumentsException e) {
            String errorMessage = e.getMessage();
            view.displayTextInErrorLabel(errorMessage, location_creation_pane_errors_label);
        }
    }

    @FXML
    public void onBackButtonClick() throws IOException {
        Stage stage = (Stage) body_pane.getScene().getWindow();
        Controllers.changeSceneBody("/resources/location_creation_view.fxml", stage, getWindowTitle(), this);
        view.displayLocationName(model.getName(), location_creation_name_field);
        RectanglePosition position = model.getPosition();
        view.displayBottomLeftX(position.getLeftBottomPoint().getX() + "", location_creation_blp_x_field);
        view.displayBottomLeftY(position.getLeftBottomPoint().getY() + "", location_creation_blp_y_field);
        view.displayTopRightX(position.getRightTopPoint().getX() + "", location_creation_trp_x_field);
        view.displayTopRightY(position.getRightTopPoint().getY() + "", location_creation_trp_y_field);
    }

    private Class itemClass;

    @FXML
    public void onPieceMenuSelected() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Stage stage = (Stage) body_pane.getScene().getWindow();
        Controllers.changeSceneBody("/resources/piece_creation_view.fxml", stage, resources.getString("window_title_items_adding_page"), this);
        view.displayItemsMenuTitle(resources.getString("items_menu_piece"), items_menu_title);
        showCreationButton();
        itemClass = Rock.Piece.class;
        initializeImageChooser();

        Controllers.setFloatInputType(piece_creation_pane_weight_field);

        setItemsMenuRowFactory(items_table);
        List<Item> items = model.getItems();
        view.displayItemsListTable(items, items_table);
    }

    @FXML
    public void onRockMenuSelected() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Stage stage = (Stage) body_pane.getScene().getWindow();
        Controllers.changeSceneBody("/resources/rock_creation_view.fxml", stage, resources.getString("window_title_items_adding_page"), this);
        view.displayItemsMenuTitle(resources.getString("items_menu_rock"), items_menu_title);
        showCreationButton();
        itemClass = Rock.class;
        initializeImageChooser();

        Controllers.setFloatInputType(rock_creation_pane_stone_weight_field);
        Controllers.setFloatInputType(rock_creation_pane_ore_weight_field);

        setItemsMenuRowFactory(items_table);
        List<Item> items = model.getItems();
        view.displayItemsListTable(items, items_table);
    }

    @FXML
    public void onMiningInstrumentMenuSelected() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Stage stage = (Stage) body_pane.getScene().getWindow();
        Controllers.changeSceneBody("/resources/mining_instrument_creation_view.fxml", stage, resources.getString("window_title_items_adding_page"), this);
        view.displayItemsMenuTitle(resources.getString("items_menu_mining_instrument"), items_menu_title);
        showCreationButton();
        itemClass = MiningInstrument.class;
        initializeImageChooser();

        Controllers.setFloatInputType(mining_instrument_creation_pane_coefficient_field);

        setItemsMenuRowFactory(items_table);
        List<Item> items = model.getItems();
        view.displayItemsListTable(items, items_table);
    }

    @FXML
    public void onHammerMenuSelected() throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        Stage stage = (Stage) body_pane.getScene().getWindow();
        Controllers.changeSceneBody("/resources/hammer_creation_view.fxml", stage, resources.getString("window_title_items_adding_page"), this);
        view.displayItemsMenuTitle(resources.getString("items_menu_hammer"), items_menu_title);
        showCreationButton();
        itemClass = Hammer.class;
        initializeImageChooser();

        setItemsMenuRowFactory(items_table);
        List<Item> items = model.getItems();
        view.displayItemsListTable(items, items_table);
    }

    @FXML
    private TextField piece_creation_pane_name_field;
    @FXML
    private TextField piece_creation_pane_weight_field;


    @FXML
    private TextField rock_creation_pane_name_field;
    @FXML
    private TextField rock_creation_pane_stone_weight_field;
    @FXML
    private TextField rock_creation_pane_ore_weight_field;


    @FXML
    private TextField mining_instrument_creation_pane_name_field;
    @FXML
    private TextField mining_instrument_creation_pane_coefficient_field;


    @FXML
    private TextField hammer_creation_pane_name_field;
    @FXML
    private ComboBox<ComboBoxElement> head_material_combo_box;
    @FXML
    private ComboBox<ComboBoxElement> handle_material_combo_box;

    @FXML
    public void onAddButtonClick() throws IOException {
        HashMap<String, String> itemAttributes = new HashMap<>();
        try {
            if (itemClass == Rock.Piece.class) {
                model.validatePiece(piece_creation_pane_name_field.getText(), piece_creation_pane_weight_field.getText());
                itemAttributes.put("itemName", piece_creation_pane_name_field.getText());
                itemAttributes.put("weight", piece_creation_pane_weight_field.getText());

                piece_creation_pane_name_field.setText("");
                piece_creation_pane_weight_field.setText("");
            } else if (itemClass == Rock.class) {
                model.validateRock(rock_creation_pane_name_field.getText(), rock_creation_pane_stone_weight_field.getText(), rock_creation_pane_ore_weight_field.getText());
                itemAttributes.put("itemName", rock_creation_pane_name_field.getText());
                itemAttributes.put("weightOfStone", rock_creation_pane_stone_weight_field.getText());
                itemAttributes.put("weightOfOre", rock_creation_pane_ore_weight_field.getText());

                rock_creation_pane_name_field.setText("");
                rock_creation_pane_stone_weight_field.setText("");
                rock_creation_pane_ore_weight_field.setText("");
            } else if (itemClass == MiningInstrument.class) {
                model.validateMiningInstrument(mining_instrument_creation_pane_name_field.getText(), mining_instrument_creation_pane_coefficient_field.getText());
                itemAttributes.put("itemName", mining_instrument_creation_pane_name_field.getText());
                itemAttributes.put("powerCoefficient", mining_instrument_creation_pane_coefficient_field.getText());

                mining_instrument_creation_pane_name_field.setText("");
                mining_instrument_creation_pane_coefficient_field.setText("");
            } else if (itemClass == Hammer.class) {
                model.validateHammer(hammer_creation_pane_name_field.getText(), head_material_combo_box.getValue(), handle_material_combo_box.getValue());
                itemAttributes.put("itemName", hammer_creation_pane_name_field.getText());
                itemAttributes.put("head", head_material_combo_box.getValue().getValue());
                itemAttributes.put("handle", handle_material_combo_box.getValue().getValue());

                hammer_creation_pane_name_field.setText("");
            }
            Item item = model.addItem(itemClass.getSimpleName(), itemAttributes);
            model.assignIcon(item);
            List<Item> items = new ArrayList<>();
            items.add(item);

            view.displayItemsListTable(items, items_table);
        }
        catch (InvalidArgumentsException e) {
            String errorMessage = e.getMessage();
            view.displayTextInErrorLabel(errorMessage, location_creation_pane_errors_label);
        }
    }

    @FXML
    public void onCreateButtonClick() throws IOException {
        Stage stage = (Stage) body_pane.getScene().getWindow();
        if (mode == Mode.ADD) {
            Location location = model.getLocation();
            ServerAnswer answer = model.saveLocation(location);
            if (Controllers.checkServerAnswer(stage, answer))
                Controllers.changeSceneBody("/resources/location_creation_view.fxml", stage, getWindowTitle(), new LocationCreationController(Controllers.checkServerAnswer(answer)));
        }
        else {
            ServerAnswer answer = model.updateLocationOnServer();
            if (Controllers.checkServerAnswer(stage, answer))
                Controllers.changeSceneBody("/resources/locations_table_view.fxml", stage, getWindowTitle());
        }
    }

    public void setItemsMenuRowFactory(TableView itemsListTable){
        view.showTablePlaceHolder(items_table);
        itemsListTable.setRowFactory(
                new Callback<TableView<Item>, TableRow<Item>>() {
                    @Override
                    public TableRow<Item> call(TableView<Item> tableView) {
                        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
                        TableRow<Item> row = new TableRow<>();
                        ContextMenu itemsListMenu = new ContextMenu();
                        itemsListMenu.getStyleClass().add("items-list-context-menu");
                        MenuItem removeItem = new MenuItem(resources.getString("context_menu_delete"));
                        removeItem.getStyleClass().add("items-list-context-menu-item");
                        removeItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                itemsListTable.getItems().remove(row.getItem());
                                model.deleteItem(row.getItem());
                            }
                        });
                        itemsListMenu.getItems().add(removeItem);

                        row.contextMenuProperty().bind(
                                Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                        .then(itemsListMenu)
                                        .otherwise((ContextMenu)null));

                        return row;
                    }
                });
    }

    @FXML
    public void initialize() {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        view.displayTextInErrorLabel(defaultErrorMessage, location_creation_pane_errors_label);

        Controllers.setIntegerInputType(location_creation_blp_x_field, 4);
        Controllers.setIntegerInputType(location_creation_blp_y_field, 4);
        Controllers.setIntegerInputType(location_creation_trp_x_field, 4);
        Controllers.setIntegerInputType(location_creation_trp_y_field, 4);

        if (mode == Mode.ADD) {
            view.displayTitle(resources.getString("location_creation_pane_title"), location_creation_pane_title);
        }
        else if (mode == Mode.UPDATE) {
            view.displayLocationName(model.getName(), location_creation_name_field);
            RectanglePosition position = model.getPosition();
            view.displayBottomLeftX(position.getLeftBottomPoint().getX() + "", location_creation_blp_x_field);
            view.displayBottomLeftY(position.getLeftBottomPoint().getY() + "", location_creation_blp_y_field);
            view.displayTopRightX(position.getRightTopPoint().getX() + "", location_creation_trp_x_field);
            view.displayTopRightY(position.getRightTopPoint().getY() + "", location_creation_trp_y_field);

            view.displayTitle(resources.getString("update_location_pane_title"), location_creation_pane_title);
        }
    }

    private String getWindowTitle() {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        if (mode == Mode.ADD)
            return resources.getString("window_title_location_creation_page");
        else
            return resources.getString("window_title_location_update_page");
    }

    private void showCreationButton() {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        String text;
        if (mode == Mode.ADD)
            text = resources.getString("item_creation_pane_create_button");
        else
            text = resources.getString("item_creation_pane_update_button");
        view.displayCreationButtonText(text, item_creation_pane_create_button);
    }

    private void initializeImageChooser() {
        FileChooser imageChooser = new FileChooser();
        FileChooser.ExtensionFilter anyFilter = new FileChooser.ExtensionFilter("Any files (*)", "*.bmp", "*.jpeg", ".jpg", "*.png");
        FileChooser.ExtensionFilter bmpFilter = new FileChooser.ExtensionFilter("BMP files (*.bmp)", "*.bmp");
        FileChooser.ExtensionFilter jpegFilter = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.jpeg", ".jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        imageChooser.getExtensionFilters().add(anyFilter);
        imageChooser.getExtensionFilters().add(bmpFilter);
        imageChooser.getExtensionFilters().add(jpegFilter);
        imageChooser.getExtensionFilters().add(pngFilter);
        creation_pane_image_chooser.setOnMouseClicked(event ->
        {
            File file = imageChooser.showOpenDialog(creation_pane_image_chooser.getScene().getWindow());
            model.saveItemIcon(file.getPath());
            view.displayImagePath(file.getPath(), creation_pane_image_chooser_path_label);
        });
    }

}
