package lab.client.mvc.model;

import lab.client.CommandsSender;
import lab.client.ServerAnswer;
import lab.client.ServerChangeListener;
import lab.client.settings.Settings;
import lab.util.commands.*;

import java.util.ResourceBundle;

public class LoginModel extends Model {

    private String login;
    private String password;

    public void setLogin (String login) {
        this.login = login;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public void validate() throws InvalidArgumentsException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        if (!Commands.isLoginValid(login))
            throw new InvalidArgumentsException(resources.getString("login_form_model_invalid_login"));
        if (!Commands.isPasswordValid(password))
            throw new InvalidArgumentsException(resources.getString("login_form_model_invalid_password"));
    }

    public ServerAnswer authorize(){
        CommandsSender commandsSender = CommandsSender.getInstance();
        ServerCommand loginCommand = new Login((login + " " + password).getBytes());
        return commandsSender.trySendRequest(loginCommand);
    }

    public ServerAnswer getLocations(){
        CommandsSender commandsSender = CommandsSender.getInstance();
        ServerChangeListener.initialize();
        ServerChangeListener listener = ServerChangeListener.getInstance();
        Thread listenerThread = ServerChangeListener.getThreadInstance();
        listener.connect();
        listenerThread.start();
        ServerCommand subscribeCommand = new Subscribe((listener.getPort() + "").getBytes());
        ServerAnswer answer = commandsSender.trySendRequest(subscribeCommand);
        ServerCommand getCommand = new Get();
        return commandsSender.trySendRequest(getCommand);
    }

}
