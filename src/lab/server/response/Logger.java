package lab.server.response;

/**
 * The Logger interface should be implemented by any class which can append symbolic data in buffer and transform it to String.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.7.0
 */
public interface Logger {

    /**
     * Appends data to buffer.
     *
     * @param string appended data.
     */
    void append(String string);

}
