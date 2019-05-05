package lab.util.commands;

import lab.locations.Location;
import lab.util.CollectionElementsBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class Commands {

    private static HashMap<String, CommandsList> commandsCodes = new HashMap<>();
    static {
        commandsCodes.put("add", CommandsList.ADD);
        commandsCodes.put("help", CommandsList.HELP);
        commandsCodes.put("remove_greater", CommandsList.REMOVE_GREATER);
        commandsCodes.put("show", CommandsList.SHOW);
        commandsCodes.put("info", CommandsList.INFO);
        commandsCodes.put("remove_lower", CommandsList.REMOVE_LOWER);
        commandsCodes.put("remove", CommandsList.REMOVE);
        commandsCodes.put("add_if_max", CommandsList.ADD_IF_MAX);
        commandsCodes.put("exit", CommandsList.EXIT);
        commandsCodes.put("import", CommandsList.IMPORT);
        commandsCodes.put("register", CommandsList.REGISTER);
        commandsCodes.put("login", CommandsList.LOGIN);
    }

    public enum CommandsList{
        ADD (0, true, true, true),
        HELP (1, true, false, false),
        REMOVE_GREATER (2, true, true, true),
        SHOW (3, false, false, true),
        INFO (4, false, false, true),
        REMOVE_LOWER (5, true, true, true),
        REMOVE (6, true, true, true),
        ADD_IF_MAX (7, true, true, true),
        EXIT (8, false, false, false),
        IMPORT (9, true, true, true),
        REGISTER (10, true, false, false),
        LOGIN (11, true, false, false);

        private int number;
        private boolean hasArgument;
        private boolean isArgumentObject;
        private boolean needBeLogged;

        private CommandsList(int number, boolean hasArgument, boolean isArgumentObject, boolean needBeLogged){
            this.number = number;
            this.hasArgument = hasArgument;
            this.isArgumentObject = isArgumentObject;
            this.needBeLogged = needBeLogged;
        }

        public int getNumber(){
            return number;
        }

        public static CommandsList getCommand(int number){
            if (number >= 0 && number < values().length)
                return values()[number];
            else
                return null;
        }

        public boolean hasArgument(){
            return hasArgument;
        }

        public boolean isArgumentObject(){
            return isArgumentObject;
        }

        public boolean needBeLogged() { return needBeLogged; }

    }

    public enum CommandExecutionStatus{

        NOT_LOGGED_SUCCESSFUL(0),
        NOT_LOGGED_NOT_SUCCESSFUL(1),
        LOGGED_SUCCESSFUL(2),
        LOGGED_NOT_SUCCESSFUL(3),
        NO_LONGER_LOGGED(4),
        MADE_LOGGED(5);

        private int code;

        private CommandExecutionStatus(int code){
            this.code = code;
        }

        public int getCode(){
            return code;
        }

        public static CommandExecutionStatus getStatus(int code){
            if (code >= 0 && code < values().length)
                return values()[code];
            else
                return null;
        }

    }

    public static CommandsList getCommand(String command){
        return commandsCodes.getOrDefault(command, null);
    }

    public static boolean isCommandValid(CommandsList command, boolean hasArgument){
        if (command.hasArgument && !hasArgument)
            return false;
        return true;
    }

    public static byte[] packArgument(CommandsList command, String argument){
        if (!command.hasArgument())
            return new byte[0];
        else if (command.isArgumentObject()){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (command == CommandsList.IMPORT) {
                try {
                    ArrayList<Location> collection = new ArrayList<>();
                    CollectionElementsBuilder.fromFile(argument, collection);
                    collection.sort(Comparator.naturalOrder());
                    if (collection.isEmpty())
                        return null;
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    for (Location location : collection)
                        oos.writeObject(location);
                } catch (IOException e) {

                }
            }
            else{
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    Location location = CollectionElementsBuilder.parseJsonElement(argument);
                    if (location == null)
                        return null;
                    oos.writeObject(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return baos.toByteArray();
        }
        else {
            if (command == CommandsList.REGISTER){
                String[] args = argument.trim().split(" ", 2);
                if ((args.length != 2) || !isLoginValid(args[0].trim()) || !isEmailValid(args[1].trim()))
                    return null;
                argument = args[0].trim() + " " + args[1].trim();
            }
            if (command == CommandsList.LOGIN){
                String[] args = argument.trim().split(" ", 2);
                if ((args.length != 2) || !isLoginValid(args[0].trim()) || !isPasswordValid(args[1].trim()))
                    return null;
                argument = args[0].trim() + " " + args[1].trim();
            }
            return argument.getBytes();
        }
    }

    public static ArrayList<Location> unpackLocations(byte[] argument){
        ByteArrayInputStream bais = new ByteArrayInputStream(argument);
        ArrayList<Location> locations = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            while (true) {
                try {
                    locations.add((Location) ois.readObject());
                }
                catch (ClassNotFoundException e) {
                    break;
                }
            }

        }
        catch (IOException e) {

        }
        return locations;
    }

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

    public static String argumentToString(byte[] argument){
        return new String(argument);
    }

    public static boolean isLoginValid(String login){
        for (char ch : login.toCharArray()){
            if (!Character.isLetter(ch) && !Character.isDigit(ch))
                return false;
        }
        return true;
    }

    public static boolean isEmailValid(String email){
        if (email.indexOf("@") - email.indexOf(".") >= -1)
            return false;
        boolean findAtSymbol = false;
        boolean findPointSymbol = false;
        for (char ch : email.toCharArray()){
            if (ch == '@'){
                if (findAtSymbol)
                    return false;
                else {
                    findAtSymbol = true;
                    continue;
                }
            }
            if (ch == '.'){
                if (findPointSymbol)
                    return false;
                else {
                    findPointSymbol = true;
                    continue;
                }
            }
            if (!Character.isLetter(ch) && !Character.isDigit(ch))
                return false;
        }
        if (!findAtSymbol || !findPointSymbol)
            return false;
        return true;
    }

    public static boolean isPasswordValid(String password){
        return isLoginValid(password);
    }

}
