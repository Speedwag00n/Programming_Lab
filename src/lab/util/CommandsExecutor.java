package lab.util;

import com.sun.deploy.util.ArrayUtil;
import lab.locations.Location;
import lab.server.start.ResponseBuilder;
import lab.util.commands.Commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * This class contains static methods to process commands and their arguments.
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.5.0
 */

public class CommandsExecutor {

    /**
     * Parses command to separate command name and its arguments.
     * @param command Entered string that contains command name and arguments
     * @return Array composed of two parts (first part is command name, second part is arguments in json format).
     */
    public static String[] parseCommand(String command) {
        command = command.trim();
        String[] commandParts = command.split(" ", 2);
        return commandParts;
    }

    public static boolean executeCommand(Commands.CommandsList command, byte[] argument, CollectionElementsManager collectionManager, ResponseBuilder response){
        Location location;
        switch (command){
            case ADD:
                location = Commands.unpackLocations(argument).get(0);
                collectionManager.addElementInCollection(location, response);
                break;
            case HELP:
                collectionManager.help(Commands.argumentToString(argument), response);
                break;
            case REMOVE_GREATER:
                location = Commands.unpackLocations(argument).get(0);
                collectionManager.removeGreater(location, response);
                break;
            case SHOW:
                collectionManager.show(response);
                break;
            case INFO:
                collectionManager.info(response);
                break;
            case REMOVE_LOWER:
                location = Commands.unpackLocations(argument).get(0);
                collectionManager.removeLower(location, response);
                break;
            case REMOVE:
                location = Commands.unpackLocations(argument).get(0);
                collectionManager.addElementInCollection(location, response);
                break;
            case ADD_IF_MAX:
                location = Commands.unpackLocations(argument).get(0);
                collectionManager.addIfMax(location, response);
                break;
/*            case EXIT:
                collectionManager.exit(response);
                break;*/
            case IMPORT:
                ArrayList<Location> locations = Commands.unpackLocations(argument);
                collectionManager.importObjects(locations, response);
                break;
                default:
                    response.append("Команда не найдена. Воспользуйтесь командой \"help help\" для получения подробного описания доступных команд.");
                    return false;
        }
        return true;
    }

    /**
     * Take command name and its arguments like an array, check their accuracy and choose method correspond with command name <br>
     * In current version method support command from list: <br>
     * - add {element} - adds new element to the collection. <br>
     * - help "command" - allows to recieve description of existing commands. <br>
     * - remove_greater {element} - removes from the collection all elements that are greater than argument. <br>
     * - show - shows in standard output stream all elements from the collection like a Strings. <br>
     * - info - shows in standard output stream information about the collection, its date of initialization and its size. <br>
     * - remove_lower {element} - removes from the collection all elements that are lower than argument. <br>
     * - remove  {element} - removes element from the collection. <br>
     * - add_if_max {element} - adds new element to the collection. <br>
     * - exit - saves collection in standard file of collection manager or in new file if standard file is inaccessible and close program.
     * @param commandParts Array that has 2 parts (first part is command name, second part is arguments in json format)
     * @param manager Object of collection manager class that allows to work with collection and change it
     * @return Return true if arguments are correct for current command and command is found. Return false if arguments are incorrect or command is unknown
     */


}
