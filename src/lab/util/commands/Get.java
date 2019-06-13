package lab.util.commands;

import lab.locations.Location;
import lab.server.response.Logger;
import lab.util.CollectionElementsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;

/**
 * This command allows to get all locations from database on server
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class Get extends DBCommand {

    public Get() {
        super();
    }

    public Get(byte[] argument) {
        super(argument);
    }

    @Override
    public Commands.CommandExecutionStatus execute() {
        Logger logger = getLogger();
        Deque<Location> collection = getElementsManager().getCollection();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            for (Location location : collection)
                oos.writeObject(location);
            setPackedArgument(baos.toByteArray());
        } catch (IOException e) {
            return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
        }

        return Commands.CommandExecutionStatus.SERVER_SEND_DATA;
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("get");
    }

    @Override
    public boolean needBeAuthorized(){
        return true;
    }

}
