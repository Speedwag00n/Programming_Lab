package lab.util.commands;

import lab.locations.Location;
import lab.objects.items.Item;
import lab.server.database.DatabaseElementsBuilder;
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

    private CollectionElementsManager elementsManager;
    private Connection connection;

    /**
     * Constructor of DBCommand without argument.
     */
    public DBCommand(){
        super();
    }

    /**
     * Constructor of DBCommand with argument.
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
            e.printStackTrace();
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

    /**
     * Update location in database founded by id.
     *
     * @param id id of location in database that needs to be changed
     * @param newLocation location from that will get new attributes.
     * @return true if updating of location was successful else false.
     */
    public boolean updateLocationInDB(int id, Location newLocation) {
        try {
            PreparedStatement statement = DatabaseElementsBuilder.getUpdateLocationStatement(newLocation, getConnection(), id);
            statement.executeUpdate();
            DatabaseElementsBuilder.getClearItemsStatement(connection, id);
            for (Item item : newLocation.getItems()) {
                statement = DatabaseElementsBuilder.getAddItemStatement(item, connection, id);
                statement.executeUpdate();
            }
            statement.close();
            getConnection().commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
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
