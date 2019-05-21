package lab.util.commands;

/**
 * This class is a base of all server commands classes.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public abstract class ServerCommand extends Command implements Packable {

    private byte[] packedArgument;

    /**
     * Constructor of ServerCommand class.
     *
     * @param argument argument of command.
     */
    public ServerCommand(byte[] argument) {
        this.packedArgument = argument;
    }

    /**
     * Gets packed argument of command.
     *
     * @return packed argument of command.
     */
    public byte[] getPackedArgument() {
        return packedArgument;
    }

    @Override
    public byte[] getPackedData() {
        return packedArgument;
    }

}
