package lab.util.commands;

import lab.locations.Location;
import lab.server.database.changes.DatabaseChange;
import lab.server.response.Logger;
import lab.server.start.RunServer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This command allows to remove all locations from collection on server which has less items than specified location.
 * Command deletes locations which are owned by specified user.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class RemoveLower extends DBCommand {

    public RemoveLower(byte[] argument) {
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
        location.setOwner(getLogin());
        Deque<Location> collection = getElementsManager().getCollection();
        synchronized (collection) {
            int count = (int) collection.parallelStream().filter((element) -> location.compareTo(element) > 0).filter((element) -> element.getOwner().equals(getLogin())).count();
            ArrayList<Location> deletedLocations = collection.parallelStream().filter((element) -> location.compareTo(element) > 0)
                    .filter((element) -> element.getOwner().equals(getLogin())).collect(Collectors.toCollection(ArrayList::new));
            if (deleteLocationFromDB(deletedLocations)) {
                getElementsManager().getCollection().removeAll(deletedLocations);
                logger.append("Было удалено " + count + " объектов.");

                DatabaseChange change = new DatabaseChange(DatabaseChange.ChangeType.REMOVED, locations, getLogin());
                RunServer.notifyPublisher(change);

                return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
            } else {
                logger.append("Не удалось произвести операцию, попробуйте ещё раз.");
                return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
            }
        }
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("remove_lower");
    }

    @Override
    public boolean needBeAuthorized(){
        return true;
    }

}
