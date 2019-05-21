package lab.util.commands;

/**
 * This interface should be implemented by any class which is a command.
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public interface Executable {

    /**
     * Executes this command.
     *
     * @return status of command execution.
     */
    Commands.CommandExecutionStatus execute();

}
