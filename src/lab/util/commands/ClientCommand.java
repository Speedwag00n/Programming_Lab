package lab.util.commands;

/**
 * This class is a base of all client commands classes.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public abstract class ClientCommand extends Command {

    private String argument;

    /**
     * Constructor of ClientCommand class.
     *
     * @param argument argument of command.
     */
    public ClientCommand(String argument) {
        this.argument = argument;
    }

    /**
     * Gets argument of command.
     *
     * @return argument of command.
     */
    public String getArgument() {
        return argument;
    }

}
