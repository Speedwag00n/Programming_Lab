package lab.util.commands;

import lab.server.database.PasswordsGenerator;
import lab.server.mail.MessageSender;
import lab.server.response.Logger;

import javax.mail.MessagingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This command allows to register new user.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class Register extends DBCommand {

    private PasswordsGenerator passwordsGenerator;
    private MessageSender messageSender;

    public Register(byte[] argument) {
        super(argument);
        this.passwordsGenerator = new PasswordsGenerator();
        this.messageSender = new MessageSender();
    }

    @Override
    public Commands.CommandExecutionStatus execute() {
        Logger logger = getLogger();
        String[] loginAndEmail = (new String(getPackedArgument())).trim().split(" ", 2);
        if (loginAndEmail.length == 2 && Commands.isLoginValid(loginAndEmail[0].trim()) && Commands.isEmailValid(loginAndEmail[1].trim())) {
            try {
                PreparedStatement statement = getConnection().prepareStatement("INSERT INTO Users (Login, Email, Password) VALUES (?, ?, ?)");
                String password = passwordsGenerator.getPassword();
                statement.setString(1, loginAndEmail[0]);
                statement.setString(2, loginAndEmail[1]);
                statement.setString(3, passwordsGenerator.encode(password));
                statement.executeUpdate();
                try {
                    messageSender.sendMessage(loginAndEmail[1], "Спасибо за регистрацию!\nВаш логин: " + loginAndEmail[0] + "\nВаш пароль для работы с приложением: " + password);
                } catch (MessagingException e) {
                    logger.append("Не удалось отправить сообщение на указанную почту.");
                    getConnection().rollback();
                    return Commands.CommandExecutionStatus.NOT_LOGGED_NOT_SUCCESSFUL;
                }
                getConnection().commit();
                logger.append("Новый пользователь зарегистрирован, сгенерированный пароль выслан на указанную почту.");
                return Commands.CommandExecutionStatus.NOT_LOGGED_SUCCESSFUL;
            } catch (SQLException e) {
                if (e.getSQLState().equals("23505"))
                    logger.append("Пользователь с таким логином или почтой уже существует.");
                else
                    logger.append("Не удалось зарегистрировать нового пользователя.");
                return Commands.CommandExecutionStatus.NOT_LOGGED_NOT_SUCCESSFUL;
            }
        }
        logger.append("Не удалось зарегистрировать нового пользователя.");
        return Commands.CommandExecutionStatus.NOT_LOGGED_NOT_SUCCESSFUL;
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("register");
    }

    @Override
    public boolean needBeAuthorized(){
        return false;
    }

}
