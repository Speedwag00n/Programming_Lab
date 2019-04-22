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
    }

    public enum CommandsList{
        ADD (1, true, true),
        HELP (2, true, false),
        REMOVE_GREATER (3, true, true),
        SHOW (4, false, false),
        INFO (5, false, false),
        REMOVE_LOWER (6, true, true),
        REMOVE (7, true, true),
        ADD_IF_MAX (8, true, true),
        EXIT (9, false, false),
        IMPORT (10, true, true);

        private int number;
        private boolean hasArgument;
        private boolean isArgumentObject;

        private CommandsList(int number, boolean hasArgument, boolean isArgumentObject){
            this.number = number;
            this.hasArgument = hasArgument;
            this.isArgumentObject = isArgumentObject;
        }

        public int getNumber(){
            return number;
        }

        public boolean hasArgument(){
            return hasArgument;
        }

        public boolean isArgumentObject(){
            return isArgumentObject;
        }

    }

    public static CommandsList getCommand(String command){
        return commandsCodes.getOrDefault(command, null);
    }

    public static boolean isCommandValid(CommandsList command, boolean hasArgument){
        if ((command.hasArgument == true) && !hasArgument)
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

    public static String argumentToString(byte[] argument){
        return new String(argument);
    }

}
