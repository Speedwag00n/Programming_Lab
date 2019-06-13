package lab.util.commands;

import lab.locations.Location;
import lab.server.database.changes.DatabaseChange;
import lab.server.response.Logger;
import lab.server.start.RunServer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * This command allows to update location on server if user is an owner of this location.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class Update extends DBCommand {

    public Update(byte[] argument) {
        super(argument);
    }

    @Override
    public Commands.CommandExecutionStatus execute() {
        Logger logger = getLogger();
        List<Location> locations = Commands.unpackLocations(getPackedArgument());
        if (locations.size() != 2) {
            logger.append("Присланный объект невалидный.");
            return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
        }
        Location oldLocation = locations.get(0);
        Location newLocation = locations.get(1);
        oldLocation.setOwner(getLogin());
        newLocation.setOwner(getLogin());
        Deque<Location> collection = getElementsManager().getCollection();
        Location updatedLocation = null;
        synchronized (collection) {
            for (Location loc : collection) {
                if (loc.equals(oldLocation)) {
                    updatedLocation = loc;
                    break;
                }
            }
            if (updatedLocation == null) {
                logger.append("У данного пользователя отсутствует введенный объект.");
                return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
            }
            if (updateLocationInDB(updatedLocation.getId(), newLocation)) {
                updatedLocation.setName(newLocation.getName());
                updatedLocation.setArea(newLocation.getArea());
                updatedLocation.setPosition(newLocation.getPosition());
                updatedLocation.clearItems();
                updatedLocation.addItems(newLocation.getItems());

                List<Location> updatedLocationChange = new ArrayList<>();
                updatedLocationChange.add(oldLocation);
                updatedLocationChange.add(newLocation);
                DatabaseChange change = new DatabaseChange(DatabaseChange.ChangeType.UPDATED, updatedLocationChange, getLogin());
                RunServer.notifyPublisher(change);

                logger.append("Объект успешно отредактирован.");
                return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
            }
            logger.append("Не удалось произвести операцию, попробуйте ещё раз.");
            return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
        }
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("update");
    }

    @Override
    public boolean needBeAuthorized(){
        return true;
    }

}
