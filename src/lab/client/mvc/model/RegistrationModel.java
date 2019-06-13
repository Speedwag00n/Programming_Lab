package lab.client.mvc.model;

import lab.client.CommandsSender;
import lab.client.ServerAnswer;
import lab.client.settings.Settings;
import lab.util.commands.*;

import java.util.ResourceBundle;

public class RegistrationModel extends Model {

    private String login;
    private String email;

    public void setLogin (String login) {
        this.login = login;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public void validate() throws InvalidArgumentsException {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        if (!Commands.isLoginValid(login))
            throw new InvalidArgumentsException(resources.getString("registration_form_model_invalid_login"));
        if (!Commands.isEmailValid(email))
            throw new InvalidArgumentsException(resources.getString("registration_form_model_invalid_email"));
    }

    public ServerAnswer register(){
        CommandsSender commandsSender = CommandsSender.getInstance();
        ServerCommand registerCommand = new Register((login + " " + email).getBytes());
        return commandsSender.trySendRequest(registerCommand);
    }

}
