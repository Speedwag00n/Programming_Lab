package lab.util.commands;

import lab.locations.Location;
import lab.server.database.changes.DatabaseChange;
import lab.server.response.Logger;
import lab.server.start.RunServer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * This command allows to add new location in collection.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class Add extends DBCommand {

    public Add(byte[] argument) {
        super(argument);
    }

    @Override
    public Commands.CommandExecutionStatus execute() {
        Logger logger = getLogger();
        List<Location> locations = Commands.unpackLocations(getPackedArgument());
        if (locations.size() == 0) {
            logger.append("Присланный объект невалидный.");
            return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
        }
        Location location = locations.get(0);
        Deque<Location> collection = getElementsManager().getCollection();
        location.setOwner(getLogin());
        locations = new ArrayList<>();
        locations.add(location);
        synchronized (collection) {
            if (addLocationToDB(locations)) {
                collection.add(location);

                List<Location> addedLocations = new ArrayList<>();
                addedLocations.add(location);
                DatabaseChange change = new DatabaseChange(DatabaseChange.ChangeType.ADDED, addedLocations, getLogin());
                RunServer.notifyPublisher(change);

                logger.append("Объект успешно добавлен.");
                return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
            }
            logger.append("Не удалось произвести операцию, попробуйте ещё раз.");
            return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
        }
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("add");
    }

    @Override
    public boolean needBeAuthorized(){
        return true;
    }

}
