package lab.util.commands;

import lab.server.database.PasswordsGenerator;
import lab.server.response.Logger;
import lab.server.start.RunServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This command allows to receive port of server publisher thread that sends changes in collection to its subscribers.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.8.0
 */
public class Subscribe extends ServerCommand {

    public Subscribe(){
        super();
    }

    public Subscribe(byte[] argument) {
        super(argument);
    }

    @Override
    public Commands.CommandExecutionStatus execute() {
        try {
            RunServer.subscribe(getInetAddress(), Integer.parseInt(new String(getPackedArgument()).trim()));
        }
        catch (NumberFormatException e) {
            return Commands.CommandExecutionStatus.LOGGED_NOT_SUCCESSFUL;
        }
        return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("subscribe");
    }

    @Override
    public boolean needBeAuthorized(){
        return true;
    }

}
