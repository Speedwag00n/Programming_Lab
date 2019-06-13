package lab.client.mvc.model;

import lab.client.CommandsSender;
import lab.client.LocationsContainer;
import lab.client.ServerAnswer;
import lab.locations.Location;
import lab.locations.position.RectanglePosition;
import lab.util.commands.Remove;
import lab.util.commands.ServerCommand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class LocationsTableModel extends Model {

    public List<Location> filter(String field, String filteringValue) {
        List<Location> locations = LocationsContainer.getInstance().getLocations();
        int code = Integer.parseInt(field.trim());
        switch (code) {
            case 0:
                return locations.parallelStream().filter((location -> location.getName().contains(filteringValue))).collect(Collectors.toList());
            case 1:
                return locations.parallelStream().filter((location -> (location.getArea() + "").startsWith(filteringValue))).collect(Collectors.toList());
            case 2:
                return locations.parallelStream().filter((location -> {
                    RectanglePosition position = location.getPosition();
                return ("(" + position.getLeftBottomPoint().getX() + ";" + position.getLeftBottomPoint().getY() + "), (" +
                        +position.getRightTopPoint().getX() + ";" + position.getRightTopPoint().getY() + ")").contains(filteringValue);})).collect(Collectors.toList());
            case 3:
                return locations.parallelStream().filter((location -> (location.getItems().size() + "").contains(filteringValue))).collect(Collectors.toList());
            case 4:
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                return locations.parallelStream().filter((location -> formatter.format(location.getDateOfCreation()).contains(filteringValue))).collect(Collectors.toList());
            case 5:
                return locations.parallelStream().filter((location -> location.getOwner().contains(filteringValue))).collect(Collectors.toList());
                default:
                    return null;
        }
    }

    public ServerAnswer removeLocation(Location location) {
        CommandsSender commandsSender = CommandsSender.getInstance();
        ServerCommand removeCommand = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(location);
            baos.toByteArray();
            removeCommand = new Remove(baos.toByteArray());
        } catch (IOException e) {

        }
        LocationsContainer.getInstance().getLocations().remove(location);
        return commandsSender.trySendRequest(removeCommand);
    }

}
