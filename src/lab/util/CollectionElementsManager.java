package lab.util;

import lab.locations.Location;
import lab.locations.position.CoordsPair;
import lab.locations.position.RectanglePosition;
import lab.server.database.DatabaseElementsBuilder;
import lab.util.commands.DBCommand;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * This class allows to manage collection.
 *
 * @author Nemankov Ilia
 * @version 1.3.0
 * @since 1.5.0
 */
public class CollectionElementsManager {

    private Deque<Location> collection = new ConcurrentLinkedDeque<>();
    private Date initDate;
    private Properties properties;

    /**
     * Constructor of CollectionElementsManager
     */
    public CollectionElementsManager() throws SQLException, ClassNotFoundException {
        initDate = new Date();
        Class.forName("org.postgresql.Driver");
        properties = new Properties();
        properties.setProperty("user", "user");
        properties.setProperty("password", "password");
        Connection connection;
        connection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", properties);
        regenerateDatabase(connection);
        initializeCollection(connection);
        connection.close();
    }

    private void regenerateDatabase(Connection connection) throws SQLException {
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("create table users (login varchar(30) primary key unique, email varchar unique, password varchar(32))");
            statement.close();
            System.out.println("Таблица пользователей не была обнаружена. Для продолжения работы была создана пустая таблица.");
        } catch (SQLException e) {
            if (!e.getSQLState().equals("42P07"))
                throw e;
        }
        try {
            statement = connection.createStatement();
            statement.executeUpdate("create table location(loc_id serial primary key unique, name varchar not null, area integer not null, " +
                    "position integer[4] not null, date_seconds bigint not null, time_zone varchar, owner varchar(30))");
            statement.close();
            System.out.println("Таблица локаций не была обнаружена. Для продолжения работы была создана пустая таблица.");
        } catch (SQLException e) {
            if (!e.getSQLState().equals("42P07"))
                throw e;
        }
        try {
            statement = connection.createStatement();
            statement.executeUpdate("create table rock(item_Id serial primary key unique, loc_id integer not null, item_name varchar not null, " +
                    "weight_Of_Ore real not null, weight_Of_Stone real not null, " +
                    "count_of_pieces integer not null, icon bytea, foreign key (loc_id) references location(loc_id) on delete cascade)");
            statement.close();
            System.out.println("Таблица предметов Rock не была обнаружена. Для продолжения работы была создана пустая таблица.");
        } catch (SQLException e) {
            if (!e.getSQLState().equals("42P07"))
                throw e;
        }
        try {
            statement = connection.createStatement();
            statement.executeUpdate("create table piece(item_Id serial primary key unique, loc_id integer not null, item_name varchar not null, " +
                    "weight real not null, icon bytea, foreign key (loc_id) references location(loc_id) on delete cascade)");
            statement.close();
            System.out.println("Таблица предметов Piece не была обнаружена. Для продолжения работы была создана пустая таблица.");
        } catch (SQLException e) {
            if (!e.getSQLState().equals("42P07"))
                throw e;
        }
        try {
            statement = connection.createStatement();
            statement.executeUpdate("create table mininginstrument(item_id serial primary key unique, loc_id integer not null, item_name varchar(50) not null, " +
                    "power_coefficient real not null, icon bytea, foreign key (loc_id) references location(loc_id) on delete cascade)");
            statement.close();
            System.out.println("Таблица предметов MiningInstrument не была обнаружена. Для продолжения работы была создана пустая таблица.");
        } catch (SQLException e) {
            if (!e.getSQLState().equals("42P07"))
                throw e;
        }
        try {
            statement = connection.createStatement();
            statement.executeUpdate("create type instrument_material as enum ('steel', 'iron', 'stone', 'wood', 'plastic')");
            statement.close();
        } catch (SQLException e) {
            if (!e.getSQLState().equals("42710"))
                throw e;
        }
        try {
            statement = connection.createStatement();
            statement.executeUpdate("create table hammer(item_id serial primary key unique, loc_id integer not null, item_name varchar(50) not null, " +
                    "head instrument_material not null, handle instrument_material not null, icon bytea, foreign key (loc_id) references location(loc_id) on delete cascade);");
            statement.close();
            System.out.println("Таблица предметов Hammer не была обнаружена. Для продолжения работы была создана пустая таблица.");
        } catch (SQLException e) {
            if (!e.getSQLState().equals("42P07"))
                throw e;
        }
    }

    private void initializeCollection(Connection connection) throws SQLException {
        ResultSet locationRS = connection.createStatement().executeQuery("select loc_id, name, area, position, date_seconds, time_zone, owner from location");
        while (locationRS.next()) {
            Array array = locationRS.getArray(4);
            Integer[] coords = (Integer[]) array.getArray();
            RectanglePosition position = new RectanglePosition(new CoordsPair(coords[0], coords[1]), new CoordsPair(coords[2], coords[3]));
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(locationRS.getLong(5)), ZoneId.of(locationRS.getString(6)));
            Location location = new Location(locationRS.getString(2), locationRS.getInt(3), position, zonedDateTime);
            location.setOwner(locationRS.getString(7));
            location.setId(locationRS.getInt(1));
            collection.add(location);
        }
        locationRS.close();
        for (Location location : collection) {
            location.addItems(DatabaseElementsBuilder.selectItemsById(location.getId(), connection));
        }
        locationRS.close();
    }

    /**
     * Sets for specified DBCommand Connection object and CollectionElementsManager current object.
     *
     * @param command specified DBCommand object.
     * @return false if creation of Connection object to database wasn't successful else true.
     */
    public boolean connect(DBCommand command) {
        Connection connection = null;
        command.setElementsManager(this);
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", properties);
            connection.setAutoCommit(false);
        } catch (SQLException e) {

        }
        if (connection == null) {
            return false;
        } else {
            command.setConnection(connection);
            return true;
        }
    }

    /**
     * Gets collection of locations of current CollectionElementsManager object.
     *
     * @return collection of locations.
     */
    public Deque<Location> getCollection() {
        return collection;
    }

    /**
     * Sets collection of locations for current CollectionElementsManager object.
     *
     * @param collection collection of locations
     */
    public void setCollection(Deque<Location> collection) {
        this.collection = collection;
    }

    /**
     * Getter of initialization date object.
     *
     * @return date and time of collection initialization.
     */
    public Date getInitDate() {
        return initDate;
    }

    /**
     * Gets maximum element from collection.
     *
     * @return maximum element from collection.
     */
    public Location getMaxElement() {
        synchronized (collection) {
            return collection.stream().max(Comparator.naturalOrder()).orElse(null);
        }
    }

    /**
     * Gets minimum element from collection.
     *
     * @return minimum element from collection.
     */
    public Location getMinElement() {
        synchronized (collection) {
            return collection.stream().min(Comparator.naturalOrder()).orElse(null);
        }
    }

}