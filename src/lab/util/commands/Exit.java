package lab.util.commands;

import lab.server.response.Logger;

/**
 * This command allows to exit from client program.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class Exit extends ClientCommand {

    public Exit() {
        super("");
    }

    @Override
    public Commands.CommandExecutionStatus execute() {
        Logger logger = getLogger();
        logger.append("Завершение работы приложения.");
        System.out.println(logger);
        System.exit(0);
        return Commands.CommandExecutionStatus.CLIENT_SUCCESSFUL;
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("exit");
    }

}