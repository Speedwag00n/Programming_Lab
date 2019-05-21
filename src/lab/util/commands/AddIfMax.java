package lab.util.commands;

import lab.locations.Location;
import lab.server.response.Logger;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * This command allows to add new location in collection if it is more items than each other location in collection.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class AddIfMax extends DBCommand {

    public AddIfMax(String login, String password, byte[] argument) {
        super(login, password, argument);
    }

    public AddIfMax(byte[] login, byte[] password, byte[] argument) {
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
        Deque<Location> collection = getElementsManager().getCollection();
        location.setOwner(getLogin());
        locations = new ArrayList<>();
        locations.add(location);
        synchronized (collection) {
            if ((getElementsManager().getMaxElement() == null) || (location.compareTo(getElementsManager().getMaxElement()) > 0)) {
                if (addLocationToDB(locations)) {
                    collection.add(location);
                    logger.append("Элемент оказался наибольшим и был добавлен в коллекцию.");
                    return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
                }
            } else {
                logger.append("В коллекции присутствуют элементы большие или равные введенному.");
                return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
            }
            logger.append("Не удалось произвести операцию, попробуйте ещё раз.");
            return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
        }
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("add_if_max");
    }

}
