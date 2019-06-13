package lab.server.database;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lab.locations.Location;
import lab.objects.items.Item;
import lab.objects.items.miningInstruments.Hammer;
import lab.objects.items.miningInstruments.MiningInstrument;
import lab.objects.items.rock.Rock;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseElementBuilder is auxiliary class that provides static method for create request to database.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.7.0
 */
public class DatabaseElementsBuilder {

    /**
     * This method allows to receive PreparedStatement which can be used for adding location to database (without items).
     *
     * @param location   location that need to be added.
     * @param connection connection to database where this location will be placed.
     * @return PreparedStatement with insert request for add location to appropriate table in database.
     * @throws SQLException if something unexpected happened during PreparedStatement creation.
     */
    public static PreparedStatement getAddLocationStatement(Location location, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into location (name, area, position, date_seconds, time_zone, owner) values (?, ?, ?, ?, ?, ?) returning loc_id");
        statement.setString(1, location.getName());
        statement.setInt(2, location.getArea());
        statement.setArray(3, connection.createArrayOf("INTEGER", location.getPosition().asArray()));
        statement.setLong(4, location.getDateOfCreation().toInstant().getEpochSecond());
        statement.setString(5, location.getDateOfCreation().getZone().toString());
        statement.setString(6, location.getOwner());
        return statement;
    }

    /**
     * This method allows to receive PreparedStatement which can be used for adding item to database.
     *
     * @param item       item that need to be added.
     * @param connection connection to database where this item will be placed.
     * @param id         id of location where this item is.
     * @return PreparedStatement with insert request for add item to appropriate table in database.
     * @throws SQLException if something unexpected happened during PreparedStatement creation.
     */
    public static PreparedStatement getAddItemStatement(Item item, Connection connection, int id) throws SQLException {
        if (item instanceof Rock)
            return getAddStatementForRock((Rock) item, connection, id);
        else if (item instanceof Rock.Piece)
            return getAddStatementForPiece((Rock.Piece) item, connection, id);
        else if (item instanceof Hammer)
            return getAddStatementForHammer((Hammer) item, connection, id);
        else if (item instanceof MiningInstrument)
            return getAddStatementForMiningInstrument((MiningInstrument) item, connection, id);
        else
            return null;
    }

    /**
     * This method allows to receive PreparedStatement which can be used for updating location in database (without items).
     *
     * @param location location that need to be updated.
     * @param connection connection to database where this location is.
     * @param id id of location.
     * @return PreparedStatement with update request for update location in appropriate table in database.
     * @throws SQLException if something unexpected happened during PreparedStatement creation.
     */
    public static PreparedStatement getUpdateLocationStatement(Location location, Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("update location set name=?, area=?, position=? where loc_id=?");
        statement.setString(1, location.getName());
        statement.setInt(2, location.getArea());
        statement.setArray(3, connection.createArrayOf("INTEGER", location.getPosition().asArray()));
        statement.setInt(4, id);
        return statement;
    }

    /**
     * This method allows to delete all items of location by its id.
     *
     * @param connection connection to database where this location is.
     * @param id id of location which elements will be deleted.
     * @throws SQLException if something unexpected happened during items deleting.
     */
    public static void getClearItemsStatement(Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from rock where loc_id=?; delete from piece where loc_id=?;" +
                "delete from mininginstrument where loc_id=?; delete from hammer where loc_id=?");
        statement.setInt(1, id);
        statement.setInt(2, id);
        statement.setInt(3, id);
        statement.setInt(4, id);
        statement.executeUpdate();
        statement.close();
    }

    private static PreparedStatement getAddStatementForRock(Rock rock, Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into rock (loc_id, item_name, weight_of_ore, weight_of_stone, count_of_pieces, icon) values (?, ?, ?, ?, ?, ?)");
        statement.setInt(1, id);
        statement.setString(2, rock.getName());
        statement.setFloat(3, rock.getWeightOfOre());
        statement.setFloat(4, rock.getWeightOfStone());
        statement.setInt(5, 0);
        if (rock.getPackedIcon() != null) {
            statement.setBytes(6, rock.getPackedIcon());
        }
        else {
            statement.setBytes(6, new byte[0]);
        }
        return statement;
    }

    private static PreparedStatement getAddStatementForPiece(Rock.Piece piece, Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into piece (loc_id, item_name, weight, icon) values (?, ?, ?, ?)");
        statement.setInt(1, id);
        statement.setString(2, piece.getName());
        statement.setFloat(3, piece.getWeight());
        if (piece.getPackedIcon() != null) {
            statement.setBytes(4, piece.getPackedIcon());
        }
        else {
            statement.setBytes(4, new byte[0]);
        }
        return statement;
    }

    private static PreparedStatement getAddStatementForMiningInstrument(MiningInstrument miningInstrument, Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into mininginstrument (loc_id, item_name, power_coefficient, icon) values (?, ?, ?, ?)");
        statement.setInt(1, id);
        statement.setString(2, miningInstrument.getName());
        statement.setFloat(3, miningInstrument.getPowerCoefficient());
        if (miningInstrument.getPackedIcon() != null) {
            statement.setBytes(4, miningInstrument.getPackedIcon());
        }
        else {
            statement.setBytes(4, new byte[0]);
        }
        return statement;
    }

    private static PreparedStatement getAddStatementForHammer(Hammer hammer, Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into hammer (loc_id, item_name, head, handle, icon) values (?, ?, ?::instrument_material, ?::instrument_material, ?)");
        statement.setInt(1, id);
        statement.setString(2, hammer.getName());
        statement.setString(3, hammer.getHeadMaterial().name().toLowerCase());
        statement.setString(4, hammer.getHandleMaterial().name().toLowerCase());
        if (hammer.getPackedIcon() != null) {
            statement.setBytes(5, hammer.getPackedIcon());
        }
        else {
            statement.setBytes(5, new byte[0]);
        }
        return statement;
    }

    /**
     * This method allows to receive items from database for specified location.
     *
     * @param id         id of location where this item is.
     * @param connection connection to database where this item will be placed.
     * @return list of found items.
     * @throws SQLException if something unexpected happened during items list creation.
     */
    public static List<Item> selectItemsById(int id, Connection connection) throws SQLException {
        ArrayList<Item> result = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("select * from rock where loc_id = ?; select * from piece where loc_id = ?; " +
                "select * from mininginstrument where loc_id = ?; select * from hammer where loc_id = ?");
        statement.setInt(1, id);
        statement.setInt(2, id);
        statement.setInt(3, id);
        statement.setInt(4, id);
        statement.execute();
        ResultSet rs = statement.getResultSet();
        while (rs.next()) {
            Rock rock = new Rock(rs.getFloat(4), rs.getFloat(5), rs.getString(3));
            byte[] packedIcon = rs.getBytes(7);
            if (packedIcon.length != 0) {
                rock.setPackedIcon(packedIcon);
            }
            result.add(rock);
        }
        rs.close();
        statement.getMoreResults();
        rs = statement.getResultSet();
        while (rs.next()) {
            Rock.Piece piece = new Rock.Piece(rs.getFloat(4), rs.getString(3));
            byte[] packedIcon = rs.getBytes(5);
            if (packedIcon.length != 0) {
                piece.setPackedIcon(packedIcon);
            }
            result.add(piece);
        }
        rs.close();
        statement.getMoreResults();
        rs = statement.getResultSet();
        while (rs.next()) {
            MiningInstrument instrument = new MiningInstrument(rs.getFloat(4), rs.getString(3));
            byte[] packedIcon = rs.getBytes(5);
            if (packedIcon.length != 0) {
                instrument.setPackedIcon(packedIcon);
            }
            result.add(instrument);
        }
        rs.close();
        statement.getMoreResults();
        rs = statement.getResultSet();
        while (rs.next()) {
            Hammer hammer = new Hammer(rs.getString(3), MiningInstrument.Material.fromStringToMaterial(rs.getString(4)),
                    MiningInstrument.Material.fromStringToMaterial(rs.getString(5)));
            byte[] packedIcon = rs.getBytes(6);
            if (packedIcon.length != 0) {
                hammer.setPackedIcon(packedIcon);
            }
            result.add(hammer);
        }
        rs.close();
        statement.close();
        return result;
    }

}
