package lab.util;


import lab.locations.Location;
import lab.server.start.ClientID;
import lab.server.start.ResponseBuilder;
import lab.util.commands.Commands;

import java.util.ArrayList;

/**
 * This class contains static methods to process commands and their arguments.
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.5.0
 */

public class CommandsExecutor {

    public static Commands.CommandExecutionStatus executeCommand(Commands.CommandsList command, byte[] argument, CollectionElementsManager collectionManager, ClientID clientID, ResponseBuilder response){
        if (command.needBeLogged() && !clientID.isLogged()){
            response.append("Похоже, что соединение с сервером было разорвано по техническим причинам, необходима повторная авторизация.");
            response.append("Пожалуйста, оспользуйтесь командой \"login\".");
            return Commands.CommandExecutionStatus.NO_LONGER_LOGGED;
        }
        Location location;
        boolean result;
        switch (command){
            case ADD:
                location = Commands.unpackLocations(argument).get(0);
                result = collectionManager.addElementInCollection(location, response);
                break;
            case HELP:
                result = collectionManager.help(Commands.argumentToString(argument), response);
                break;
            case REMOVE_GREATER:
                location = Commands.unpackLocations(argument).get(0);
                if (collectionManager.removeGreater(location, response) > 0)
                    result = true;
                else
                    result = false;
                break;
            case SHOW:
                result = collectionManager.show(response);
                break;
            case INFO:
                result = collectionManager.info(response);
                break;
            case REMOVE_LOWER:
                location = Commands.unpackLocations(argument).get(0);
                if (collectionManager.removeLower(location, response) > 0)
                    result = true;
                else
                    result = false;
                break;
            case REMOVE:
                location = Commands.unpackLocations(argument).get(0);
                result = collectionManager.addElementInCollection(location, response);
                break;
            case ADD_IF_MAX:
                location = Commands.unpackLocations(argument).get(0);
                result = collectionManager.addIfMax(location, response);
                break;
/*            case EXIT:
                collectionManager.exit(response);
                break;*/
            case IMPORT:
                ArrayList<Location> locations = Commands.unpackLocations(argument);
                if (collectionManager.importObjects(locations, response) > 0)
                    result = true;
                else
                    result = false;
                break;
            case REGISTER:
                if (clientID.isLogged()){
                    response.append("Вы уже авторизованы, нет необходимости создавать новый аккаунт.");
                    result = false;
                    break;
                }
                result = collectionManager.register(Commands.argumentToString(argument), response);
                break;
            case LOGIN:
                if (clientID.isLogged()){
                    response.append("Вы уже авторизованы.");
                    result = false;
                    break;
                }
                result = collectionManager.login(Commands.argumentToString(argument), response);
                if (result == true)
                    return Commands.CommandExecutionStatus.MADE_LOGGED;
                break;
                default:
                    response.append("Команда не найдена. Воспользуйтесь командой \"help help\" для получения подробного описания доступных команд.");
                    result = false;
                    break;
        }
        if (result && !clientID.isLogged())
            return Commands.CommandExecutionStatus.NOT_LOGGED_SUCCESSFUL;
        else if (!result && !clientID.isLogged())
            return Commands.CommandExecutionStatus.NOT_LOGGED_NOT_SUCCESSFUL;
        else if (result && clientID.isLogged())
            return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
        else
            return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
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
