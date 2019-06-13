package lab.util.commands;

import lab.locations.Location;
import lab.util.CollectionElementsBuilder;

import java.io.*;
import java.util.*;

/**
 * Commands class contains static methods to receive commands classes and supported methods for them.
 *
 * @author Nemankov Ilia
 * @version 1.2.0
 * @since 1.6.0
 */
public class Commands {

    private static HashMap<String, Integer> commandsCodes = new HashMap<>();

    static {
        commandsCodes.put("add", 0);
        commandsCodes.put("help", 1);
        commandsCodes.put("remove_greater", 2);
        commandsCodes.put("show", 3);
        commandsCodes.put("info", 4);
        commandsCodes.put("remove_lower", 5);
        commandsCodes.put("remove", 6);
        commandsCodes.put("add_if_max", 7);
        commandsCodes.put("exit", 8);
        commandsCodes.put("import", 9);
        commandsCodes.put("register", 10);
        commandsCodes.put("login", 11);
        commandsCodes.put("subscribe", 12);
        commandsCodes.put("get", 13);
        commandsCodes.put("update", 14);
    }

    /**
     * Enum of commands execution statuses.
     */
    public enum CommandExecutionStatus {

        NOT_LOGGED_SUCCESSFUL(0),
        NOT_LOGGED_NOT_SUCCESSFUL(1),
        LOGGED_SUCCESSFUL(2),
        LOGGED_NOT_SUCCESSFUL(3),
        CLIENT_SUCCESSFUL(4),
        CLIENT_NOT_SUCCESSFUL(5),
        CLIENT_CANT_SEND(6),
        SERVER_CANT_SEND(7),
        NO_RESPONSE(8),
        AUTHORIZED(9),
        SERVER_SEND_DATA(10);

        private int code;

        private CommandExecutionStatus(int code) {
            this.code = code;
        }

        /**
         * Gets code of status.
         *
         * @return status code.
         */
        public int getCode() {
            return code;
        }

        /**
         * Gets CommandExecutionStatus object by specified code.
         *
         * @param code code of status.
         * @return CommandExecutionStatus object.
         */
        public static CommandExecutionStatus getStatus(int code) {
            if (code >= 0 && code < values().length)
                return values()[code];
            else
                return null;
        }

    }

    /**
     * Factory method that returns object of command.
     *
     * @param command String from command line that contains command name and its arguments.
     * @return command object.
     * @throws InvalidArgumentsException if arguments of command is incorrect.
     */
    public static Command getCommand(String command) throws InvalidArgumentsException {
        String[] commandParts = command.trim().split(" ", 2);
        return getCommand(commandsCodes.getOrDefault(commandParts[0], -1), (commandParts.length == 1) ? "" : commandParts[1]);
    }

    /**
     * Factory method that returns object of command.
     *
     * @param commandCode code of command.
     * @param data        String from command line that contains command arguments.
     * @return command object.
     * @throws InvalidArgumentsException if arguments of command is incorrect.
     */
    public static Command getCommand(int commandCode, String data) throws InvalidArgumentsException {
        try {
            String[] arguments = data.split(" ", 3);
            switch (commandCode) {
                case 0:
                    checkLoginAndPw(arguments[0], arguments[1]);
                    return new Add(Commands.packLocation(arguments[2]));
                case 1:
                    return new Help(data);
                case 2:
                    checkLoginAndPw(arguments[0], arguments[1]);
                    return new RemoveGreater(Commands.packLocation(arguments[2]));
                case 3:
                    checkLoginAndPw(arguments[0], arguments[1]);
                    return new Show(new byte[0]);
                case 4:
                    checkLoginAndPw(arguments[0], arguments[1]);
                    return new Info();
                case 5:
                    checkLoginAndPw(arguments[0], arguments[1]);
                    return new RemoveLower(Commands.packLocation(arguments[2]));
                case 6:
                    checkLoginAndPw(arguments[0], arguments[1]);
                    return new Remove(Commands.packLocation(arguments[2]));
                case 7:
                    checkLoginAndPw(arguments[0], arguments[1]);
                    return new AddIfMax(Commands.packLocation(arguments[2]));
                case 8:
                    return new Exit();
                case 9:
                    checkLoginAndPw(arguments[0], arguments[1]);
                    return new Import(Commands.packLocations(arguments[2]));
                case 10:
                    if (!Commands.isLoginValid(arguments[0])) {
                        throw new InvalidArgumentsException("Введен невалидный логин");
                    } else if (!Commands.isEmailValid(arguments[1])) {
                        throw new InvalidArgumentsException("Введен невалидная почта");
                    } else
                        return new Register((arguments[0].trim() + " " + arguments[1].trim()).getBytes());
                default:
                    return null;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidArgumentsException("Неверное количество аргументов");
        }
    }

    private static void checkLoginAndPw(String login, String password) throws InvalidArgumentsException {
        if (!Commands.isLoginValid(login))
            throw new InvalidArgumentsException("Невалидный логин");
        if (!Commands.isPasswordValid(password))
            throw new InvalidArgumentsException("Невалидный пароль");
    }

    /**
     * Factory method that returns object of server command.
     *
     * @param commandCode code of command.
     * @param data        packed data where contains command object fields.
     * @return ServerCommand object.
     */
    public static ServerCommand getServerCommand(int commandCode, byte[] data) {
        switch (commandCode) {
            case 0:
                return new Add(data);
            case 2:
                return new RemoveGreater(data);
            case 3:
                return new Show(new byte[0]);
            case 4:
                return new Info();
            case 5:
                return new RemoveLower(data);
            case 6:
                return new Remove(data);
            case 7:
                return new AddIfMax(data);
            case 9:
                return new Import(data);
            case 10:
                return new Register(data);
            case 11:
                return new Login(data);
            case 12:
                return new Subscribe(data);
            case 13:
                return new Get();
            case 14:
                return new Update(data);
            default:
                return null;
        }
    }

    /**
     * This method allows to receive code of command.
     *
     * @param commandName name of command.
     * @return command code.
     */
    public static int getCommandCode(String commandName) {
        return commandsCodes.getOrDefault(commandName, -1);
    }

    /**
     * Packs location that is serialized in json format.
     *
     * @param argument location that is serialized in json format.
     * @return packed location
     * @throws InvalidArgumentsException if json format of location isn't valid or unexpected situation during parsing happened.
     */
    public static byte[] packLocation(String argument) throws InvalidArgumentsException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            Location location = CollectionElementsBuilder.parseJsonElement(argument);
            if (location == null)
                throw new InvalidArgumentsException("Объект невалиден");
            oos.writeObject(location);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new InvalidArgumentsException("Не удалось сформировать запрос");
        }
    }

    /**
     * Packs locations that are serialized in json format.
     *
     * @param fileName file name where saved locations in xml format.
     * @return packed locations
     * @throws InvalidArgumentsException if json format of all locations isn't valid or unexpected situation during parsing happened.
     */
    public static byte[] packLocations(String fileName) throws InvalidArgumentsException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ArrayList<Location> collection = new ArrayList<>();
            CollectionElementsBuilder.fromFile(fileName, collection);
            collection.sort(Comparator.naturalOrder());
            if (collection.isEmpty())
                throw new InvalidArgumentsException("Объекты невалидны");
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            for (Location location : collection)
                oos.writeObject(location);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new InvalidArgumentsException("Не удалось сформировать запрос");
        }
    }

    /**
     * Unpack locations.
     *
     * @param argument serialized locations.
     * @return list of locations.
     */
    public static List<Location> unpackLocations(byte[] argument) {
        ByteArrayInputStream bais = new ByteArrayInputStream(argument);
        ArrayList<Location> locations = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            while (true) {
                try {
                    locations.add((Location) ois.readObject());
                } catch (ClassNotFoundException e) {
                    break;
                }
            }

        } catch (IOException e) {

        }
        return locations;
    }

    /**
     * Checks is login valid or not.
     *
     * @param login login.
     * @return true if login is valid else false.
     */
    public static boolean isLoginValid(String login) {
        login = login.trim();
        if (login.length() > 30 || login.length() == 0)
            return false;
        for (char ch : login.toCharArray()) {
            if (!Character.isLetter(ch) && !Character.isDigit(ch))
                return false;
        }
        return true;
    }

    /**
     * Checks is email valid or not.
     *
     * @param email email.
     * @return true if email is valid else false.
     */
    public static boolean isEmailValid(String email) {
        email = email.trim();
        if (email.length() > 50 || email.length() == 0)
            return false;
        if (email.indexOf("@") - email.lastIndexOf(".") >= -1 || email.indexOf("@") == 0)
            return false;
        boolean findAtSymbol = false;
        boolean findPointSymbol = false;
        for (char ch : email.toCharArray()) {
            if (ch == '@') {
                if (findAtSymbol)
                    return false;
                else {
                    findAtSymbol = true;
                    continue;
                }
            }
            if (ch == '.') {
                if (findPointSymbol)
                    return false;
                else if (findAtSymbol) {
                    findPointSymbol = true;
                    continue;
                }
            }
            if (!Character.isLetter(ch) && !Character.isDigit(ch) && (ch != 45) && (ch != 46) && (ch != 95))
                return false;
        }
        if (!findAtSymbol || !findPointSymbol)
            return false;
        return true;
    }

    /**
     * Checks is password valid or not.
     *
     * @param password password.
     * @return true if password is valid else false.
     */
    public static boolean isPasswordValid(String password) {
        password = password.trim();
        if (password.length() != 16 || password.length() == 0)
            return false;
        for (char ch : password.toCharArray()) {
            if (!Character.isLetter(ch) && !Character.isDigit(ch))
                return false;
        }
        return true;
    }

}
