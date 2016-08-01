package vop.groep7.vop7backend.database.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.POIFactory;

/**
 *
 * @author Backend Team
 */
public class POIDAO extends SQLDAO {

    /**
     * Create a POIDAO using a connection to a database
     *
     * @param connection a connection object to a database
     */
    public POIDAO(Connection connection) {
        super(connection);
    }

    /**
     * Get a specific point of interest from a user.
     *
     * @param userId The Id of the user
     * @param poiId The Id of the point of interest
     * @see POI
     * @return A point of interest object
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public POI getPOI(int userId, int poiId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, address_id, name, radius, active, email_notify, cell_number_notify FROM pointsOfInterest "
                + "WHERE id = ? AND user_id = ?")) {
            ps.setInt(1, poiId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return POIFactory.build(poiId, getAddress(rs.getInt(ADDRESS_ID)), rs.getString(NAME), rs.getInt(RADIUS), rs.getBoolean(ACTIVE), rs.getBoolean(EMAIL_NOTIFY), rs.getBoolean(CELL_NUMBER_NOTIFY), getNotifyForEventTypes(POI_TABLE, POI_ID, rs.getInt(ID)));
                }
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(POIDAO.class.getName()).log(Level.SEVERE, "Could not get POI.", ex);
            throw new DataAccessException("Could not find point of interest", ex);
        }
    }

    /**
     * Delete a point of interest from a user
     *
     * @param userId The Id of the user
     * @param poiId The Id of the point of interest
     * @return True if the deletion was successful, false otherwise
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deletePOI(int userId, int poiId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM pointsOfInterest WHERE id = ? AND user_id = ?")) {
            ps.setInt(1, poiId);
            ps.setInt(2, userId);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(POIDAO.class.getName()).log(Level.SEVERE, "Could not delete POI.", ex);
            throw new DataAccessException("Could not delete point of interest", ex);
        }
    }

    /**
     * Check if a point of interest already exists in the database. The Id field
     * will not be filled in, so every field must be the same.
     *
     * @param userId Id of a user
     * @param poi A point of interest object
     * @return If the point of interest already exists
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean existsPOI(int userId, POI poi) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT poi.id FROM pointsOfInterest poi INNER JOIN address a ON (poi.address_id=a.id)"
                + " WHERE poi.user_id = ?"
                + " AND poi.name = ? AND poi.radius = ? AND poi.active = ? AND poi.email_notify = ? AND poi.cell_number_notify = ?"
                + " AND a.city = ? AND a.street = ? AND a.housenumber = ? AND a.country = ? AND a.postal_code = ? ")) {
            // set values of poi
            ps.setInt(1, userId);
            ps.setString(2, poi.getName());
            ps.setInt(3, poi.getRadius());
            ps.setBoolean(4, poi.isActive());
            ps.setBoolean(5, poi.getNotifications().isNotifyEmail());
            ps.setBoolean(6, poi.getNotifications().isNotifyCellPhone());
            // set values of address
            ps.setString(7, poi.getAddress().getCity().getCity());
            ps.setString(8, poi.getAddress().getStreet());
            ps.setString(9, poi.getAddress().getHousenumber());
            ps.setString(10, poi.getAddress().getCity().getCountry());
            ps.setInt(11, poi.getAddress().getCity().getPostalCode());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return hasEqualPOIEventTypes(new HashSet(poi.getNotifications().getNotifyForEventTypes()), userId, rs.getInt(ID));
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(POIDAO.class.getName()).log(Level.SEVERE, "Could not check POI.", ex);
            throw new DataAccessException("Could not check if the point of interest exists.", ex);
        }
    }

    /**
     * Check if the set of event types is equal to the event types of the poi in
     * the db
     *
     * @param types Set of the event types
     * @param userId The Id of the user
     * @param poiId The Id of the point of interest
     * @return true if the set of event types is equal
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean hasEqualPOIEventTypes(Set<EventType> types, int userId, int poiId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT event_type FROM pointsOfInterest poi INNER JOIN eventtypePoi e ON (poi.id = e.poi_id)"
                + "WHERE user_id = ? AND poi.id = ?")) {
            ps.setInt(1, userId);
            ps.setInt(2, poiId);
            try (ResultSet rs = ps.executeQuery()) {
                int i = 0;
                while (rs.next()) {
                    i++;
                    // check if set contains type in result
                    if (!types.contains(new EventType(rs.getString(TYPE)))) {
                        return false;
                    }
                }
                // if there are as many types in set as in result return true, else false
                return i == types.size();
            }
        } catch (SQLException ex) {
            Logger.getLogger(POIDAO.class.getName()).log(Level.SEVERE, "Could not check types.", ex);
            throw new DataAccessException("Could not check if the point of interest has the same event types.", ex);
        }
    }
}
