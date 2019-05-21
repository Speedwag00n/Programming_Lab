package lab.server.response;

/**
 * ResponseBuilder class is a container for messages about command executing.
 * * @author Nemankov Ilia
 * * @version 1.1.0
 * * @since 1.6.0
 */
public class ResponseBuilder implements Logger {

    private StringBuilder stringBuilder = new StringBuilder();

    /**
     * ResponseBuilder class constructor.
     */
    public ResponseBuilder() {

    }

    /**
     * Appends data to buffer.
     *
     * @param string appended data.
     */
    public void append(String string) {
        stringBuilder.append(string);
        stringBuilder.append('\n');
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

}
