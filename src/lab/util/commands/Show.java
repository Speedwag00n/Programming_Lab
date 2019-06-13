package lab.util.commands;

import lab.locations.Location;
import lab.server.response.Logger;

import java.util.Deque;

/**
 * This command allows to show all objects that contains collection on server.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class Show extends DBCommand {

    public Show(byte[] argument) {
        super(argument);
    }

    @Override
    public Commands.CommandExecutionStatus execute() {
        Logger logger = getLogger();
        Deque<Location> collection = getElementsManager().getCollection();
        synchronized (collection) {
            if (!collection.isEmpty()) {
                collection.stream().forEach((Location location) -> {
                    logger.append(location.toString() + " Владелец: " + location.getOwner() + ".");
                    if (location.getItems().size() != 0)
                        logger.append("Предметы в локации:");
                    location.getItems().stream().forEach((item) -> logger.append(item.toString()));
                });
            } else {
                logger.append("Коллекция пуста.");
            }
            return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
        }
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("show");
    }

    @Override
    public boolean needBeAuthorized(){
        return true;
    }

}
