package lab.server.database;

import lab.locations.Location;
import lab.objects.items.Item;
import lab.objects.items.miningInstruments.Hammer;
import lab.objects.items.miningInstruments.MiningInstrument;
import lab.objects.items.rock.Rock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * @param item       item that need to be added
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
        else if (item instanceof MiningInstrument)
            return getAddStatementForMiningInstrument((MiningInstrument) item, connection, id);
        else if (item instanceof Hammer)
            return getAddStatementForHammer((Hammer) item, connection, id);
        else
            return null;
    }

    private static PreparedStatement getAddStatementForRock(Rock rock, Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into rock (loc_id, item_name, weight_of_ore, weight_of_stone) values (?, ?, ?, ?)");
        statement.setInt(1, id);
        statement.setString(2, rock.getName());
        statement.setFloat(3, rock.getWeightOfOre());
        statement.setFloat(4, rock.getWeightOfStone());
        return statement;
    }

    private static PreparedStatement getAddStatementForPiece(Rock.Piece piece, Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into piece (loc_id, item_name, weight) values (?, ?, ?)");
        statement.setInt(1, id);
        statement.setString(2, piece.getName());
        statement.setFloat(3, piece.getWeight());
        return statement;
    }

    private static PreparedStatement getAddStatementForMiningInstrument(MiningInstrument miningInstrument, Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into mininginstrument (loc_id, item_name, power_coefficient) values (?, ?, ?)");
        statement.setInt(1, id);
        statement.setString(2, miningInstrument.getName());
        statement.setFloat(3, miningInstrument.getPowerCoefficient());
        return statement;
    }

    private static PreparedStatement getAddStatementForHammer(Hammer hammer, Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into rock (loc_id, item_name, head, handle) values (?, ?, ?, ?)");
        statement.setInt(1, id);
        statement.setString(2, hammer.getName());
        statement.setString(3, hammer.getHeadMaterialName());
        statement.setString(4, hammer.getHandleMaterialName());
        return statement;
    }

    /**
     * This method allows to receive items from database for specified location.
     *
     * @param id         id of location where this item is.
     * @param connection connection to database where this item will be placed.
     * @return list of found items.
     * @throws SQLException if something unexpected happened during PreparedStatement creation.
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
            result.add(rock);
        }
        rs.close();
        statement.getMoreResults();
        rs = statement.getResultSet();
        while (rs.next()) {
            Rock.Piece piece = new Rock.Piece(rs.getFloat(4), rs.getString(3));
            result.add(piece);
        }
        rs.close();
        statement.getMoreResults();
        rs = statement.getResultSet();
        while (rs.next()) {
            MiningInstrument instrument = new MiningInstrument(rs.getFloat(4), rs.getString(3));
            result.add(instrument);
        }
        rs.close();
        statement.getMoreResults();
        rs = statement.getResultSet();
        while (rs.next()) {
            Hammer hammer = new Hammer(rs.getString(3), MiningInstrument.Material.fromStringToMaterial(rs.getString(4)),
                    MiningInstrument.Material.fromStringToMaterial(rs.getString(5)));
            result.add(hammer);
        }
        rs.close();
        statement.close();
        return result;
    }

}
