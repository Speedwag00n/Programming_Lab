package lab.util.commands;

import lab.locations.Location;
import lab.server.response.Logger;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * This command allows to add new locations in collection from specified file that contains serializable locations in xml format.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class Import extends DBCommand {

    public Import(String login, String password, byte[] argument) {
        super(login, password, argument);
    }

    public Import(byte[] login, byte[] password, byte[] argument) {
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
        Deque<Location> collection = getElementsManager().getCollection();
        locations.parallelStream().forEach((element) -> element.setOwner(getLogin()));
        synchronized (collection) {
            if (!addLocationToDB(locations)) {
                logger.append("Не удалось произвести операцию, попробуйте ещё раз.");
                return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
            }
        }
        int count = (int) locations.parallelStream().filter((element) -> element != null).count();
        locations.parallelStream().filter((element) -> element != null).forEach((element) -> collection.add(element));
        logger.append(count + " объектов из предоставленного файла успешно добавлены.");
        return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("import");
    }

}
