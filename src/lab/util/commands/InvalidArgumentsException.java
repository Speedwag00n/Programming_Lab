package lab.util.commands;

/**
 * Exception that throws when command and its arguments can't be parsed.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.7.0
 */
public class InvalidArgumentsException extends Exception {

    /**
     * Constructor of InvalidArgumentsException class.
     *
     * @param message message that describes cause of exception.
     */
    public InvalidArgumentsException(String message) {
        super(message);
    }

}