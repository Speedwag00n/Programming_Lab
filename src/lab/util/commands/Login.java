package lab.util.commands;

import lab.server.database.PasswordsGenerator;
import lab.server.processing.Session;
import lab.server.response.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This command allows to login into objects system.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.8.0
 */
public class Login extends DBCommand {

    public Login(byte[] argument) {
        super(argument);
    }

    @Override
    public Commands.CommandExecutionStatus execute() {
        Logger logger = getLogger();
        String[] loginAndPassword = (new String(getPackedArgument())).trim().split(" ", 2);
        if (loginAndPassword.length == 2 && Commands.isLoginValid(loginAndPassword[0].trim()) && Commands.isPasswordValid(loginAndPassword[1].trim())) {
            PasswordsGenerator passwordsGenerator = new PasswordsGenerator();
            try (PreparedStatement statement = getConnection().prepareStatement("SELECT Login FROM Users WHERE Login LIKE ? AND Password LIKE ?")) {
                statement.setString(1, loginAndPassword[0].trim());
                statement.setString(2, passwordsGenerator.encode(loginAndPassword[1].trim()));
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    return Commands.CommandExecutionStatus.AUTHORIZED;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.append("Авторизация не удалась. Проверьте правильность введенного логина и пароля.");
        return Commands.CommandExecutionStatus.NOT_LOGGED_NOT_SUCCESSFUL;
    }

    /**
     * Gets login of authorizing user.
     *
     * @return login of authorizing user.
     */
    public String getLogin(){
        String[] loginAndPassword = (new String(getPackedArgument())).trim().split(" ", 2);
        return loginAndPassword[0];
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("login");
    }

    @Override
    public boolean needBeAuthorized(){
        return false;
    }

}
