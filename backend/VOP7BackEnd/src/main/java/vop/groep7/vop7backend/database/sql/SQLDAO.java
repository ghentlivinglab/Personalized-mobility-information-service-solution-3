package vop.groep7.vop7backend.database.sql;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Notifications;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.POIFactory;

/**
 *
 * @author Backend Team
 */
public abstract class SQLDAO {

    /**
     * A connection to the SQL database
     */
    protected Connection connection;

    /**
     * The id field
     */
    protected static final String ID = "id";

    /**
     * The city field
     */
    protected static final String CITY = "city";

    /**
     * The postal code field
     */
    protected static final String POSTAL_CODE = "postal_code";

    /**
     * The street field
     */
    protected static final String STREET = "street";

    /**
     * The house number field
     */
    protected static final String HOUSENUMBER = "housenumber";

    /**
     * The country field
     */
    protected static final String COUNTRY = "country";

    /**
     * The latitude field
     */
    protected static final String LAT = "lat";

    /**
     * The longitude field
     */
    protected static final String LON = "lon";

    /**
     * The address id field
     */
    protected static final String ADDRESS_ID = "address_id";

    /**
     * The name field
     */
    protected static final String NAME = "name";

    /**
     * The radius field
     */
    protected static final String RADIUS = "radius";

    /**
     * The active field
     */
    protected static final String ACTIVE = "active";

    /**
     * The poi table field
     */
    protected static final String POI_TABLE = "eventtypePOI";

    /**
     * The poi id field
     */
    protected static final String POI_ID = "poi_id";

    /**
     * The event type field
     */
    protected static final String TYPE = "event_type";

    /**
     * The email notification field
     */
    protected static final String EMAIL_NOTIFY = "email_notify";

    /**
     * The cell number notification field
     */
    protected static final String CELL_NUMBER_NOTIFY = "cell_number_notify";
    
    /**
     * The where field
     */
    protected static final String WHERE = " WHERE ";
    
    /**
     * The user Id field
     */
    protected static final String USER_ID = "user_id";
    
    /**
     * The travel Id field
     */
    protected static final String TRAVEL_ID = "travel_id";

    /**
     * The constructor every SQLDAO should have. Every SQLDAO needs a connection
     * to a database.
     *
     * @param connection A connection object to a database
     */
    public SQLDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Get an address based on an Id
     *
     * @param addressId The Id of the address
     * @see Address
     * @return An address object
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public Address getAddress(int addressId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT city, postal_code, street, housenumber, country, lat, lon FROM address WHERE id = ?")) {
            ps.setInt(1, addressId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coordinate coordinate = CoordinateFactory.build(rs.getDouble(LAT), rs.getDouble(LON));
                    return AddressFactory.build(rs.getString(CITY), rs.getInt(POSTAL_CODE), rs.getString(STREET), rs.getString(HOUSENUMBER), rs.getString(COUNTRY), coordinate);
                }
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(SQLDAO.class.getName()).log(Level.SEVERE, "Could not get address.", ex);
            throw new DataAccessException("Could not find address.", ex);
        }
    }

    /**
     * Create a new address
     *
     * @param city The city of the address
     * @param postalCode The postalCode of the address
     * @param street The street of the address
     * @param housenumber The house number of the address
     * @param country The country of the address
     * @param coordinate The coordinates of the address
     * @return The Id of the new address
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private int createAddress(String city, int postalCode, String street, String housenumber, String country, Coordinate coordinate)
            throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO address(city, postal_code, street, housenumber, country, lat, lon) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, city);
            ps.setInt(2, postalCode);
            ps.setString(3, street);
            ps.setString(4, housenumber);
            ps.setString(5, country);
            ps.setDouble(6, coordinate.getLat());
            ps.setDouble(7, coordinate.getLon());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        } catch (SQLException ex) {
            Logger.getLogger(SQLDAO.class.getName()).log(Level.SEVERE, "Could not create address.", ex);
            throw new DataAccessException("Could not create address", ex);
        }
    }

    /**
     * Create a new address
     *
     * @param address The new address
     * @return The Id of the address
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public int createAddress(Address address) throws DataAccessException {
        return createAddress(address.getCity().getCity(), address.getCity().getPostalCode(), address.getStreet(), address.getHousenumber(), address.getCity().getCountry(), address.getCoordinates());
    }

    /**
     * Modify an address
     *
     * @param id The Id of the address
     * @param city The city of the address
     * @param postalCode The postalCode of the address
     * @param street The street of the address
     * @param housenumber The house number of the address
     * @param country The country of the address
     * @param coordinate The coordinates of the address
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private void modifyAddress(int id, String city, int postalCode, String street, String housenumber, String country, Coordinate coordinate)
            throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE address SET city = ?, postal_code = ?, street = ?, housenumber = ?, country = ?, lat = ?, lon = ? WHERE id = ?")) {
            ps.setString(1, city);
            ps.setInt(2, postalCode);
            ps.setString(3, street);
            ps.setString(4, housenumber);
            ps.setString(5, country);
            ps.setDouble(6, coordinate.getLat());
            ps.setDouble(7, coordinate.getLon());
            ps.setInt(8, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(SQLDAO.class.getName()).log(Level.SEVERE, "Could not modify address.", ex);
            throw new DataAccessException("Could not modify address", ex);
        }
    }

    /**
     * Modify an address
     *
     * @param id The Id of the address
     * @param address The address object
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void modifyAddress(int id, Address address) throws DataAccessException {
        modifyAddress(id, address.getCity().getCity(), address.getCity().getPostalCode(), address.getStreet(), address.getHousenumber(), address.getCity().getCountry(), address.getCoordinates());
    }

    /**
     * Check if an address already exists
     *
     * @param address The address object
     * @return If the address already exists
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean existsAddress(Address address) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id FROM address WHERE city = ? AND postal_code = ? AND street = ? AND housenumber = ? AND country = ?")) {
            ps.setString(1, address.getCity().getCity());
            ps.setInt(2, address.getCity().getPostalCode());
            ps.setString(3, address.getStreet());
            ps.setString(4, address.getHousenumber());
            ps.setString(5, address.getCity().getCountry());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLDAO.class.getName()).log(Level.SEVERE, "Could not check address.", ex);
            throw new DataAccessException("Could not check if the address exists.", ex);
        }
    }

    /**
     * Get the POI's of a user
     *
     * @param userId The Id of the user
     * @see POI
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     * @return Array of POI objects
     */
    public List<POI> getPOIs(int userId) throws DataAccessException {
        List<POI> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, address_id, name, radius, active, email_notify, cell_number_notify FROM pointsOfInterest WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    POI p = POIFactory.build(rs.getInt(ID), getAddress(rs.getInt(ADDRESS_ID)), rs.getString(NAME), rs.getInt(RADIUS), rs.getBoolean(ACTIVE), rs.getBoolean(EMAIL_NOTIFY), rs.getBoolean(CELL_NUMBER_NOTIFY), getNotifyForEventTypes(POI_TABLE, POI_ID, rs.getInt(ID)));
                    result.add(p);
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLDAO.class.getName()).log(Level.SEVERE, "Could not get all POIs.", ex);
            throw new DataAccessException("Could not find points of interest", ex);
        }
    }

    /**
     * Create a POI of a user
     *
     * @param userId The Id of the user
     * @param addressId The Id of the address of the POI
     * @param name The name of the POI
     * @param radius The radius around this POI
     * @param active If this POI is active for the user
     * @param notifyForEventTypes The notification the user wants for this poi
     * @return The Id of a newly created POI object
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private int createPOI(int userId, int addressId, String name, int radius, boolean active, Notifications notify) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO pointsOfInterest(user_id, address_id, name, radius, active, email_notify, cell_number_notify) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setInt(2, addressId);
            ps.setString(3, name);
            ps.setInt(4, radius);
            ps.setBoolean(5, active);
            ps.setBoolean(6, notify.isNotifyEmail());
            ps.setBoolean(7, notify.isNotifyCellPhone());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    if (notify.getNotifyForEventTypes() != null) {
                        for (EventType e : notify.getNotifyForEventTypes()) {
                            createNotifyForEventType(POI_TABLE, POI_ID, rs.getInt(1), e);
                        }
                    }
                    return rs.getInt(ID);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLDAO.class.getName()).log(Level.SEVERE, "Could not create POI.", ex);
            throw new DataAccessException("Could not create points of interest", ex);
        }
        return -1;
    }

    /**
     * Create a link from an object to an event type
     *
     * @param table The table in which we want to insert
     * @param idField The first field of the new record
     * @param id The Id of an object
     * @param notifyForEventType An event type which is important for this
     * object
     * @throws DataAccessException This is thrown when something went wrong with
     * the database
     */
    protected void createNotifyForEventType(String table, String idField, int id, EventType notifyForEventType) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO " + table + "(" + idField + ", event_type) VALUES (?,?)")) {
            ps.setInt(1, id);
            ps.setString(2, notifyForEventType.getType());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(SQLDAO.class.getName()).log(Level.SEVERE, "Could not create notification.", ex);
            throw new DataAccessException("Could not create notify for event type array.", ex);
        }
    }

    /**
     * Create a POI of a user
     *
     * @param userId The Id of the user
     * @param poi The POI object
     * @return The Id of the newly created POI
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public int createPOI(int userId, POI poi) throws DataAccessException {
        int id = createAddress(poi.getAddress());
        return createPOI(userId, id, poi.getName(), poi.getRadius(), poi.isActive(), poi.getNotifications());
    }

    /**
     * Modify a POI of a user
     *
     * @param userId The Id of the user
     * @param poiId The Id of this POI
     * @param poi The POI object
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void modifyPOI(int userId, int poiId, POI poi) throws DataAccessException {
        modifyPOI(userId, poiId, poi.getName(), poi.getRadius(), poi.isActive(), poi.getNotifications(), poi.getAddress());
    }

    /**
     * Modify a POI of a user
     *
     * @param userId The Id of the user
     * @param poiId The Id of this POI
     * @param name The name of the POI
     * @param radius The radius around this POI
     * @param active If this POI is active for the user
     * @param notifyForEventTypes The notification the user wants for this poi
     * @param address The address of the POI
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private void modifyPOI(int userId, int poiId, String name, int radius, boolean active, Notifications notify, Address address) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE pointsOfInterest SET name = ?, radius = ?, active = ?, email_notify = ?, cell_number_notify = ? WHERE user_id = ? AND id = ?")) {
            ps.setString(1, name);
            ps.setInt(2, radius);
            ps.setBoolean(3, active);
            ps.setBoolean(4, notify.isNotifyEmail());
            ps.setBoolean(5, notify.isNotifyCellPhone());
            ps.setInt(6, userId);
            ps.setInt(7, poiId);
            ps.execute();
            modifyAddress(getPOIAddressId(poiId, userId), address);
            deleteNotifyForEventType(POI_TABLE, POI_ID, poiId);
            if (notify.getNotifyForEventTypes() != null) {
                for (EventType e : notify.getNotifyForEventTypes()) {
                    createNotifyForEventType(POI_TABLE, POI_ID, poiId, e);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLDAO.class.getName()).log(Level.SEVERE, "Could not modify POI.", ex);
            throw new DataAccessException("Could not modify points of interest", ex);
        }
    }
    
    /**
     * Get the Id of an address of a POI
     *
     * @param poiId An Id of a travel
     * @param userId An Id of a user
     * @return The Id of the address
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private int getPOIAddressId(int poiId, int userId) throws DataAccessException {
        int result = 0;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT address_id FROM pointsOfInterest WHERE id = ? AND user_id = ?")) {
            ps.setInt(1, poiId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(ADDRESS_ID);
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLDAO.class.getName()).log(Level.SEVERE, "Could not get address id.", ex);
            throw new DataAccessException("Could not find address id", ex);
        }
    }

    /**
     * Delete all event types of an object
     *
     * @param table The table in which we want to insert
     * @param idField The first field of the new record
     * @param id The Id of an object
     * @throws DataAccessException This is thrown when something went wrong with
     * the database
     */
    protected void deleteNotifyForEventType(String table, String idField, int id) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM " + table + WHERE + idField + " = ?")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(SQLDAO.class.getName()).log(Level.SEVERE, "Could not delete notification.", ex);
            throw new DataAccessException("Could not delete event types", ex);
        }
    }

    /**
     * Get all event types of an object
     *
     * @param table The table in which we want to insert
     * @param idField The first field of the new record
     * @param id The Id of an object
     * @see EventType
     * @return An array of event types
     * @throws DataAccessException This is thrown when something went wrong with
     * the database
     */
    protected ArrayList<EventType> getNotifyForEventTypes(String table, String idField, int id) throws DataAccessException {
        ArrayList<EventType> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT event_type FROM " + table + WHERE + idField + " = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(EventTypeFactory.build(rs.getString(TYPE)));
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLDAO.class.getName()).log(Level.SEVERE, "Could not get notifications.", ex);
            throw new DataAccessException("Could not find event types.", ex);
        }
    }
}
