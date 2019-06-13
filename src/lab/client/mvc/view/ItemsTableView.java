package lab.client.mvc.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import lab.client.settings.Settings;
import lab.objects.items.Item;

import java.util.List;
import java.util.ResourceBundle;

public class ItemsTableView extends View {

    @FXML
    private TableView itemsTableView;

    public void displayLocationsListTable(List<Item> itemsList, TableView itemsListTable, boolean clear) {
        if (clear)
            clear();
        if (itemsList != null && !itemsList.isEmpty()) {
            this.itemsTableView = itemsListTable;
            for (Item item : itemsList) {
                itemsListTable.getItems().add(item);
            }
        }
    }

    public void showTablePlaceHolder(TableView itemsTableView) {
        this.itemsTableView = itemsTableView;
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        itemsTableView.setPlaceholder(new Text(resources.getString("items_table_empty_placeholder")));
    }

    public void clear(){
        itemsTableView.getItems().clear();
    }

}
