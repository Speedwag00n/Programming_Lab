package lab.util.commands;

import lab.server.response.Logger;

import java.text.SimpleDateFormat;

/**
 * This command allows to receive about collection on server.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class Info extends DBCommand {

    public Info() {
        super(new byte[0]);
    }

    @Override
    public Commands.CommandExecutionStatus execute() {
        Logger logger = getLogger();
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy, в hh:mm:ss");
        logger.append("Программа хранит объекты класса Location, содержащие классы Item, в коллекции типа ConcurrentLinkedDeque");
        logger.append("Коллекция была загружена " + formater.format(getElementsManager().getInitDate()) + ". Сейчас она содержит " + getElementsManager().getCollection().size() + " элементов.");
        return Commands.CommandExecutionStatus.LOGGED_SUCCESSFUL;
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("info");
    }

    @Override
    public boolean needBeAuthorized(){
        return true;
    }

}
