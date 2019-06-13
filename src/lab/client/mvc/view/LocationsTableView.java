package lab.client.mvc.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import lab.client.settings.Settings;
import lab.locations.Location;

import java.util.List;
import java.util.ResourceBundle;

public class LocationsTableView extends View {

    @FXML
    private TableView locationsTableView;

    public void displayLocationsListTable(List<Location> locationsList, TableView locationsListTable, boolean clear) {
        if (clear)
            clear();
        if (locationsList != null && !locationsList.isEmpty()) {
            this.locationsTableView = locationsListTable;
            for (Location location : locationsList) {
                locationsListTable.getItems().add(location);
            }
        }
    }

    public void clear(){
        locationsTableView.getItems().clear();
    }

}
