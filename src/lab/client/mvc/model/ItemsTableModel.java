package lab.client.mvc.model;

import lab.client.settings.Settings;
import lab.objects.items.Item;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ItemsTableModel extends Model {

    private List<Item> itemsList;

    public List<Item> getItemsList() {
        return itemsList;
    }

    public List<Item> filter(String field, String filteringValue) {
        List<Item> items = itemsList;
        int code = Integer.parseInt(field.trim());
        switch (code) {
            case 0:
                return items.parallelStream().filter((item -> {
                    ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
                    return resources.getString("items_table_type_cell_" + item.getClass().getSimpleName().toLowerCase()).contains(filteringValue);
                })).collect(Collectors.toList());
            case 1:
                return items.parallelStream().filter((item -> item.getName().contains(filteringValue))).collect(Collectors.toList());
            default:
                return null;
        }
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

}
