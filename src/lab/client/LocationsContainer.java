package lab.client;

import lab.locations.Location;
import lab.server.database.changes.DatabaseChange;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LocationsContainer {

    private static LocationsContainer singleLocationsContainer;
    private List<Location> locationsList = Collections.synchronizedList(new LinkedList<>());
    private List<DatabaseChange> changesList = Collections.synchronizedList(new LinkedList<>());

    private LocationsContainer() {

    }

    public static LocationsContainer getInstance() {
        if (singleLocationsContainer == null)
            singleLocationsContainer = new LocationsContainer();
        return singleLocationsContainer;
    }

    public List<Location> getLocations() {
        synchronized (changesList) {
            while (doChange() != null) {

            }
        }
        return locationsList;
    }

    public void setLocations(List<Location> locations) {
        synchronized (locationsList) {
            this.locationsList.clear();
            this.locationsList.addAll(locations);
        }
    }

    public void addChange(DatabaseChange change) {
        synchronized (locationsList) {
            this.changesList.add(change);
        }
    }

    public static DatabaseChange doChange() {
        synchronized (singleLocationsContainer.changesList) {
            if (singleLocationsContainer.changesList.size() != 0) {
                DatabaseChange change = singleLocationsContainer.changesList.remove(0);
                switch (change.getType()) {
                    case ADDED:
                        singleLocationsContainer.locationsList.add(change.getLocations().get(0));
                        break;
                    case REMOVED:
                        singleLocationsContainer.locationsList.remove(change.getLocations().get(0));
                        break;
                    case UPDATED:
                        for (Location location : singleLocationsContainer.locationsList) {
                            if (location.equals(change.getLocations().get(0))) {
                                location.setName(change.getLocations().get(1).getName());
                                location.setArea(change.getLocations().get(1).getArea());
                                location.setPosition(change.getLocations().get(1).getPosition());
                                location.clearItems();
                                location.addItems(change.getLocations().get(1).getItems());
                                break;
                            }
                        }
                        break;
                }
                return change;
            }
            else
                return null;
        }
    }

}
