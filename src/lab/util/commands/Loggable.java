package lab.util.commands;

import lab.server.response.Logger;

/**
 * This interface should be implemented by any class which needs to log data about execution of its method.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public interface Loggable {

    /**
     * Sets object of logger.
     *
     * @param logger logger object.
     */
    void setLogger(Logger logger);

    /**
     * Gets objetc of logger.
     *
     * @return logger object.
     */
    Logger getLogger();

}
