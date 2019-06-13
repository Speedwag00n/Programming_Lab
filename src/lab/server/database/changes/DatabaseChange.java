package lab.server.database.changes;

import lab.locations.Location;

import java.io.Serializable;
import java.util.List;

public class DatabaseChange implements Serializable {

    public enum ChangeType{
        ADDED, REMOVED, UPDATED;
    }

    private ChangeType type;
    private List<Location> locations;
    private String initiator;

    public DatabaseChange(ChangeType type, List<Location> locations, String initiator) {
        this.type = type;
        this.locations = locations;
        this.initiator = initiator;
    }

    public ChangeType getType(){
        return type;
    }

    public List<Location> getLocations(){
        return locations;

    }

    public String getInitiator() {
        return initiator;
    }

}
