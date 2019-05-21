package lab.util.packet;

/**
 * Exception that throws when UDP package can't be represent cause by too many data.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.6.0
 */
public class PacketOverflowException extends Exception {

    /**
     * Constructor of PacketOverflowException class.
     *
     * @param message message that describes cause of exception.
     */
    public PacketOverflowException(String message) {
        super(message);
    }

}
