package vop.groep7.vop7backend.database.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.TravelFactory;

/**
 *
 * @author Backend Team
 */
public class TravelDAO extends SQLDAO {

    private DateFormat timeFormatter;

    private static final String STARTPOINT = "startpoint";
    private static final String ENDPOINT = "endpoint";
    private static final String TIME_INTERVAL_START = "notify_start";
    private static final String TIME_INTERVAL_STOP = "notify_stop";
    private static final String RECURRING_ID = "recurring_id";
    private static final String MONDAY = "monday";
    private static final String TUESDAY = "tuesday";
    private static final String WEDNESDAY = "wednesday";
    private static final String THURSDAY = "thursday";
    private static final String FRIDAY = "friday";
    private static final String SATURDAY = "saturday";
    private static final String SUNDAY = "sunday";

    /**
     * Create a TravelDAO using a connection to a database
     *
     * @param connection A connection object to a database
     */
    public TravelDAO(Connection connection) {
        super(connection);
        timeFormatter = new SimpleDateFormat("HH:mm");
    }

    /**
     * Get a specific travel from a user.
     *
     * @param userId The Id of the user
     * @param travelId The Id of the travel
     * @see Travel
     * @return A travel object
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public Travel getTravel(int userId, int travelId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, name, notify_start, notify_stop, recurring_id, startpoint, endpoint FROM travel WHERE id = ? AND user_id = ?")) {
            ps.setInt(1, travelId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String[] timeInterval = new String[2];
                    timeInterval[0] = timeFormatter.format(rs.getTime(TIME_INTERVAL_START));
                    timeInterval[1] = timeFormatter.format(rs.getTime(TIME_INTERVAL_STOP));
                    return TravelFactory.build(travelId, rs.getString(NAME), getAddress(rs.getInt(STARTPOINT)), getAddress(rs.getInt(ENDPOINT)), timeInterval, getRecurring(rs.getInt(RECURRING_ID)));
                }
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TravelDAO.class.getName()).log(Level.SEVERE, "Could not get travel.", ex);
            throw new DataAccessException("Could not find travel", ex);
        }
    }

    /**
     * Get all travels of a user
     *
     * @param userId The Id of the user
     * @see Travel
     * @return A collection of travel objects
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public List<Travel> getTravels(int userId) throws DataAccessException {
        List<Travel> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, name, notify_start, notify_stop, recurring_id, startpoint, endpoint FROM travel WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] timeInterval = new String[2];
                    timeInterval[0] = timeFormatter.format(rs.getTime(TIME_INTERVAL_START));
                    timeInterval[1] = timeFormatter.format(rs.getTime(TIME_INTERVAL_STOP));
                    Travel t = TravelFactory.build(rs.getInt(ID), rs.getString(NAME), getAddress(rs.getInt(STARTPOINT)), getAddress(rs.getInt(ENDPOINT)), timeInterval, getRecurring(rs.getInt(RECURRING_ID)));
                    result.add(t);
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TravelDAO.class.getName()).log(Level.SEVERE, "Could not get all travels.", ex);
            throw new DataAccessException("Could not find travels", ex);
        }
    }

    /**
     * Create a travel for a user
     *
     * @param userId The Id of the user
     * @param name The name of the travel
     * @param date The date of the travel
     * @param timeInterval The time interval of this travel
     * @param recurring An array of booleans that represent the days of the week
     * on which this travel is repeated
     * @param startpoint An Address object that represents the starting point of
     * the travel
     * @param endpoint An Address object that represents the end point of the
     * travel
     *
     * @return Id of the newly created travel
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private int createTravel(int userId, String name, String[] timeInterval, Boolean[] recurring, Address startpoint, Address endpoint) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO travel(user_id, name, notify_start, notify_stop, recurring_id, startpoint, endpoint) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setTime(3, new Time(timeFormatter.parse(timeInterval[0]).getTime()));
            ps.setTime(4, new Time(timeFormatter.parse(timeInterval[1]).getTime()));
            int recurringId = createRecurring(recurring);
            ps.setInt(5, recurringId);
            int startId = createAddress(startpoint);
            ps.setInt(6, startId);
            int endId = createAddress(endpoint);
            ps.setInt(7, endId);
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(ID);
                }
            }
            return -1;
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(TravelDAO.class.getName()).log(Level.SEVERE, "Could not create travel.", ex);
            throw new DataAccessException("Could not create travel", ex);
        }
    }

    /**
     * Create a travel for a user
     *
     * @param userId The Id of the user
     * @param travel A travel object representing the new travel
     * @return Id of the newly created travel
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public int createTravel(int userId, Travel travel) throws DataAccessException {
        return createTravel(userId, travel.getName(), travel.getInterval().toArray(), travel.getRecurring().toArray(), travel.getStart(), travel.getEnd());
    }

    /**
     * Delete a travel from a user
     *
     * @param userId The Id of the user
     * @param travelId The Id of the travel
     * @return True if the deletion was successful, false otherwise
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deleteTravel(int userId, int travelId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM travel WHERE id = ? AND user_id = ?")) {
            ps.setInt(1, travelId);
            ps.setInt(2, userId);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(TravelDAO.class.getName()).log(Level.SEVERE, "Could not delete travel.", ex);
            throw new DataAccessException("Could not delete travel", ex);
        }
    }

    /**
     * Modify a travel from a user
     *
     * @param travelId The Id of the travel
     * @param userId The Id of the user
     * @param name The name of the travel
     * @param timeInterval The time interval of this travel
     * @param recurring An array of booleans that represent the days of the week
     * on which this travel is repeated
     * @param start An Address object that represents the starting point of the
     * travel
     * @param end An Address object that represents the end point of the travel
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private void modifyTravel(int travelId, int userId, String name, String[] timeInterval, Boolean[] recurring, Address start, Address end) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE travel SET name = ?, notify_start = ?,"
                + " notify_stop = ?"
                + " WHERE id = ? AND user_id = ?")) {
            ps.setString(1, name);
            ps.setTime(2, new Time(timeFormatter.parse(timeInterval[0]).getTime()));
            ps.setTime(3, new Time(timeFormatter.parse(timeInterval[1]).getTime()));
            ps.setInt(4, travelId);
            ps.setInt(5, userId);
            ps.execute();
            modifyRecurring(getRecurringId(travelId, userId), recurring);
            modifyAddress(getTravelAddressId(travelId, userId, STARTPOINT), start);
            modifyAddress(getTravelAddressId(travelId, userId, ENDPOINT), end);
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(TravelDAO.class.getName()).log(Level.SEVERE, "Could not modify travel.", ex);
            throw new DataAccessException("Could not modify travel", ex);
        }
    }

    /**
     * Modify a travel from a user
     *
     * @param travelId The Id of the travel
     * @param userId The Id of the user
     * @param travel A travel object representing the travel
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void modifyTravel(int travelId, int userId, Travel travel) throws DataAccessException {
        modifyTravel(travelId, userId, travel.getName(), travel.getInterval().toArray(), travel.getRecurring().toArray(), travel.getStart(), travel.getEnd());
    }

    /**
     * Check if a travel already exists in the database. The Id field will not
     * be filled in, so every field must be the same.
     *
     * @param userId Id of a user
     * @param travel A travel object
     * @return If the travel already exists
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean existsTravel(int userId, Travel travel) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT t.id FROM travel t "
                + "INNER JOIN address a1 ON (t.startpoint = a1.id) "
                + "INNER JOIN address a2 on (t.endpoint = a2.id) "
                + "INNER JOIN recurring r on (t.recurring_id = r.id) "
                + "WHERE user_id = ? "
                + "AND t.name = ? AND t.notify_start = ? AND t.notify_stop = ? "
                + "AND a1.city = ? AND a1.street = ? AND a1.housenumber = ? AND a1.country = ? AND a1.postal_code = ? "
                + "AND a2.city = ? AND a2.street = ? AND a2.housenumber = ? AND a2.country = ? AND a2.postal_code = ? "
                + "AND r.monday = ? AND r.tuesday = ? AND r.wednesday = ? AND r.thursday = ? AND r.friday = ? AND r.saturday = ? AND r.sunday = ?")) {
            // set travel values
            ps.setInt(1, userId);
            ps.setString(2, travel.getName());
            ps.setTime(3, new Time(timeFormatter.parse(travel.getInterval().toArray()[0]).getTime()));
            ps.setTime(4, new Time(timeFormatter.parse(travel.getInterval().toArray()[1]).getTime()));
            // set startpoint address values
            Address start = travel.getStart();
            ps.setString(5, start.getCity().getCity());
            ps.setString(6, start.getStreet());
            ps.setString(7, start.getHousenumber());
            ps.setString(8, start.getCity().getCountry());
            ps.setInt(9, start.getCity().getPostalCode());
            // set endpoint address values
            Address end = travel.getEnd();
            ps.setString(10, end.getCity().getCity());
            ps.setString(11, end.getStreet());
            ps.setString(12, end.getHousenumber());
            ps.setString(13, end.getCity().getCountry());
            ps.setInt(14, end.getCity().getPostalCode());
            // set recurring values
            Boolean[] recurring = travel.getRecurring().toArray();
            ps.setBoolean(15, recurring[0]);
            ps.setBoolean(16, recurring[1]);
            ps.setBoolean(17, recurring[2]);
            ps.setBoolean(18, recurring[3]);
            ps.setBoolean(19, recurring[4]);
            ps.setBoolean(20, recurring[5]);
            ps.setBoolean(21, recurring[6]);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(TravelDAO.class.getName()).log(Level.SEVERE, "Could not check travel.", ex);
            throw new DataAccessException("Could not check if the route exists.", ex);
        }
    }

    /**
     * Get the Id of the Recurring array
     *
     * @param travelId An Id of a travel
     * @param userId An Id of a user
     * @return The Id of the recurring array
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private int getRecurringId(int travelId, int userId) throws DataAccessException {
        int result = 0;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT recurring_id FROM travel WHERE id = ? AND user_id = ?")) {
            ps.setInt(1, travelId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(RECURRING_ID);
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TravelDAO.class.getName()).log(Level.SEVERE, "Could not get recurring id.", ex);
            throw new DataAccessException("Could not find recurring id", ex);
        }
    }

    /**
     * Get the Id of an address of a travel
     *
     * @param travelId An Id of a travel
     * @param userId An Id of a user
     * @param address The field we want to get (start or end)
     * @return The Id of the address
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private int getTravelAddressId(int travelId, int userId, String address) throws DataAccessException {
        int result = 0;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT " + address + " FROM travel WHERE id = ? AND user_id = ?")) {
            ps.setInt(1, travelId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(address);
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TravelDAO.class.getName()).log(Level.SEVERE, "Could not get travel address id.", ex);
            throw new DataAccessException("Could not find address id", ex);
        }
    }

    /**
     * Get the recurring array based on it's Id
     *
     * @param recurringId An Id of an array of boolean values
     * @return The recurring array
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private Boolean[] getRecurring(int recurringId) throws DataAccessException {
        List<Boolean> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT monday, tuesday, wednesday, thursday, friday, saturday, sunday FROM recurring WHERE id = ?")) {
            ps.setInt(1, recurringId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.add(rs.getBoolean(MONDAY));
                    result.add(rs.getBoolean(TUESDAY));
                    result.add(rs.getBoolean(WEDNESDAY));
                    result.add(rs.getBoolean(THURSDAY));
                    result.add(rs.getBoolean(FRIDAY));
                    result.add(rs.getBoolean(SATURDAY));
                    result.add(rs.getBoolean(SUNDAY));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TravelDAO.class.getName()).log(Level.SEVERE, "Could not get recurring.", ex);
            throw new DataAccessException("Could not get the recurring array", ex);
        }
        return result.toArray(new Boolean[result.size()]);
    }

    /**
     * Create a recurring array
     *
     * @param recurring An array of boolean values
     * @return The Id of this array
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private int createRecurring(Boolean[] recurring) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO recurring (monday, tuesday, wednesday, thursday, friday, saturday, sunday) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, recurring[0]);
            ps.setBoolean(2, recurring[1]);
            ps.setBoolean(3, recurring[2]);
            ps.setBoolean(4, recurring[3]);
            ps.setBoolean(5, recurring[4]);
            ps.setBoolean(6, recurring[5]);
            ps.setBoolean(7, recurring[6]);
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        } catch (SQLException ex) {
            Logger.getLogger(TravelDAO.class.getName()).log(Level.SEVERE, "Could not create recurring.", ex);
            throw new DataAccessException("Could not create the recurring array", ex);
        }
    }

    /**
     * Modify a recurring array
     *
     * @param recurringId An Id of an array of boolean values
     * @param recurring An array of boolean values
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private void modifyRecurring(int recurringId, Boolean[] recurring) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE recurring SET monday = ?, tuesday = ?, wednesday = ?, thursday = ?,"
                + " friday = ?, saturday = ?, sunday = ? WHERE id = ?")) {
            ps.setBoolean(1, recurring[0]);
            ps.setBoolean(2, recurring[1]);
            ps.setBoolean(3, recurring[2]);
            ps.setBoolean(4, recurring[3]);
            ps.setBoolean(5, recurring[4]);
            ps.setBoolean(6, recurring[5]);
            ps.setBoolean(7, recurring[6]);
            ps.setInt(8, recurringId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(TravelDAO.class.getName()).log(Level.SEVERE, "Could not modify recurring.", ex);
            throw new DataAccessException("Could not modify the recurring array", ex);
        }
    }

}
