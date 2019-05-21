package lab.util.commands;

import lab.locations.Location;
import lab.server.response.Logger;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * This command allows to remove specified location from collection on server if user is an owner of specified object and specified object exists.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class Remove extends DBCommand {

    public Remove(String login, String password, byte[] argument) {
        super(login, password, argument);
    }

    public Remove(byte[] login, byte[] password, byte[] argument) {
        super(login, password, argument);
    }

    @Override
    public Commands.CommandExecutionStatus execute() {
        if (!authorization()) {
            return Commands.CommandExecutionStatus.NOT_LOGGED_NOT_SUCCESSFUL;
        }
        Logger logger = getLogger();
        List<Location> locations = Commands.unpackLocations(getPackedArgument());
        if (locations.size() == 0) {
            logger.append("Присланный объект невалидный.");
            return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
        }
        Location location = locations.get(0);
        location.setOwner(getLogin());
        Deque<Location> collection = getElementsManager().getCollection();
        Location deletedLocation = null;
        synchronized (collection) {
            for (Location loc : collection) {
                if (loc.equals(location)) {
                    deletedLocation = loc;
                    collection.remove(loc);
                    break;
                }
            }
            if (deletedLocation == null) {
                logger.append("У данного пользователя отсутствует введенный объект.");
                return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
            }
            locations = new ArrayList<>();
            locations.add(deletedLocation);
            if (deleteLocationFromDB(locations)) {
                logger.append("Объект успешно удален.");
                return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
            } else {
                collection.add(deletedLocation);
                logger.append("Не удалось произвести операцию, попробуйте ещё раз.");
                return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
            }
        }
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("remove");
    }

}
