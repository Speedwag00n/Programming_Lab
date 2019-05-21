package lab.util.commands;

import lab.locations.Location;
import lab.objects.items.Item;
import lab.server.database.DatabaseElementsBuilder;
import lab.server.database.PasswordsGenerator;
import lab.server.response.Logger;
import lab.util.CollectionElementsManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This class is a base of all server commands classes which can work with database.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public abstract class DBCommand extends ServerCommand {

    private String login = "";
    private String password = "";

    private CollectionElementsManager elementsManager;
    private Connection connection;

    /**
     * Constructor of DBCommand that receives login and password from command line.
     *
     * @param login    login to auth in database.
     * @param password password to auth in database.
     * @param argument argument of command.
     */
    public DBCommand(String login, String password, byte[] argument) {
        super(argument);
        this.login = login;
        this.password = password;
    }

    /**
     * Constructor of DBCommand that receives login and password from UDP package.
     *
     * @param login    login to auth in database.
     * @param password password to auth in database.
     * @param argument argument of command.
     */
    public DBCommand(byte[] login, byte[] password, byte[] argument) {
        super(argument);
        this.login = new String(login).trim();
        this.password = new String(password).trim();
    }

    /**
     * Constructor of DBCommand that can work with database without auth.
     *
     * @param argument argument of command.
     */
    public DBCommand(byte[] argument) {
        super(argument);
    }

    /**
     * Setter of CollectionElementsManager.
     *
     * @param elementsManager manager of collection.
     */
    public void setElementsManager(CollectionElementsManager elementsManager) {
        this.elementsManager = elementsManager;
    }

    /**
     * Getter of CollectionElementsManager.
     *
     * @return manager of collection.
     */
    public CollectionElementsManager getElementsManager() {
        return elementsManager;
    }

    /**
     * Setter of Connection.
     *
     * @param connection connection to database.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Getter of Connection.
     *
     * @return connection to database.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes connection of current command object.
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {

        }
    }

    /**
     * Getter of login.
     *
     * @return login.
     */
    public String getLogin() {
        return login;
    }

    @Override
    public byte[] getPackedData() {
        byte[] previousData = super.getPackedData();
        byte[] data = new byte[60 + 40 + previousData.length];
        int offset = 0;
        byte space = " ".getBytes()[0];
        byte[] currentData = login.getBytes();
        for (int i = 0; i < currentData.length; i++) {
            data[offset + i] = currentData[i];
        }
        for (int i = currentData.length; i < 60; i++) {
            data[offset + i] = space;
        }
        offset += 60;
        currentData = password.getBytes();
        for (int i = 0; i < currentData.length; i++) {
            data[offset + i] = currentData[i];
        }
        for (int i = currentData.length; i < 40; i++) {
            data[offset + i] = space;
        }
        offset += 40;
        currentData = getPackedArgument();
        for (int i = 0; i < currentData.length; i++) {
            data[offset + i] = currentData[i];
        }
        return data;
    }

    /**
     * Try to auth in database using specified login and password of current command object.
     *
     * @return true if auth was successful else false.
     */
    public boolean authorization() {
        Logger logger = getLogger();
        if (Commands.isLoginValid(login) && Commands.isPasswordValid(password)) {
            PasswordsGenerator passwordsGenerator = new PasswordsGenerator();
            try (PreparedStatement statement = getConnection().prepareStatement("SELECT Login FROM Users WHERE Login LIKE ? AND Password LIKE ?")) {
                statement.setString(1, login);
                statement.setString(2, passwordsGenerator.encode(password));
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    logger.append("Авторизация прошла успешно.");
                    return true;
                }
            } catch (SQLException e) {

            }
        }
        logger.append("Авторизация не удалась. Проверьте правильность введенного логина и пароля.");
        return false;
    }

    /**
     * Adds all location in list to collection.
     *
     * @param locations list of location that need to be added.
     * @return true if adding of locations was successful else false.
     */
    public boolean addLocationToDB(List<Location> locations) {
        ResultSet rs;
        try {
            for (Location location : locations) {
                PreparedStatement statement = DatabaseElementsBuilder.getAddLocationStatement(location, getConnection());
                rs = statement.executeQuery();
                rs.next();
                int id = rs.getInt(1);
                location.setId(id);
                rs.close();
                for (Item item : location.getItems()) {
                    statement = DatabaseElementsBuilder.getAddItemStatement(item, connection, id);
                    statement.executeUpdate();
                }
                statement.close();
            }
            getConnection().commit();
            return true;
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {

            }
            return false;
        }
    }

    /**
     * Removes all location in list from collection.
     *
     * @param locations list of location that need to be removed.
     * @return true if removing of locations was successful else false.
     */
    public boolean deleteLocationFromDB(List<Location> locations) {
        try {
            for (Location location : locations) {
                PreparedStatement statement = connection.prepareStatement("delete from location where loc_id = ?");
                statement.setInt(1, location.getId());
                statement.executeUpdate();
                statement.close();
            }
            getConnection().commit();
            return true;
        } catch (SQLException e) {

            try {
                getConnection().rollback();
            } catch (SQLException e1) {

            }
            return false;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        connection.close();
    }
}
