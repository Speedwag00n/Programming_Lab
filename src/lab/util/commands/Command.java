package lab.util.commands;

import lab.server.response.Logger;
import lab.server.response.ResponseBuilder;

/**
 * Command class is a main class of all commands classes.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public abstract class Command implements Executable, Loggable {

    private Logger logger = new ResponseBuilder();

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    /**
     * Gets code of command.
     *
     * @return code of command.
     */
    public abstract int getCode();

}
