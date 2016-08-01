package vop.groep7.vop7backend.database.sql;

import static java.lang.Math.cos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Backend Team
 */
public abstract class EventProcessingDAO extends SQLDAO {

    protected final Coordinate ref;
    protected final double meterDegreeLatitude;
    protected final double meterDegreeLongitude;
    
    // events in this radius of the waypoint will be matched
    protected final static int DELTA = 20;
    
    protected final Map<Integer,String> days = new HashMap<Integer, String>(){{
        put(1, "sunday");
        put(2, "monday");
        put(3, "tuesday");
        put(4, "wednesday");
        put(5, "thursday");
        put(6, "friday");
        put(7, "saturday");
    }};
    
    protected final String begin = "SELECT r.user_id, r.travel_id, r.id "
            + "FROM route r INNER JOIN interpolated_waypoints w ON (r.id=w.route_id) INNER JOIN eventtyperoute er ON (r.id=er.route_id) "
            + "WHERE r.active=true AND er.event_type=?";
            //+ "AND ((((w.lat-?)*?)*((w.lat-?)*?))+(((w.lon-?)*?)*((w.lon-?)*?)) < ?)"
    protected final String middle = "AND r.travel_id IN "
            + "(SELECT t.id "
            + "FROM TRAVEL t INNER JOIN recurring r ON (t.recurring_id=r.id)"
            + " WHERE "; // + days.get(day) +;
    protected final String end = " =true AND notify_start < ? AND notify_stop > ?)";
    
    /**
     * Create a EventProcessingDAO using a connection to a database
     *
     * @param connection a connection object to a database
     */
    public EventProcessingDAO(Connection connection) {
        super(connection);
        // set reference point (center of Ghent)
        ref = new Coordinate(51.0 + 3.0 / 60, 3.0 + 42.0 / 60);
        meterDegreeLatitude = 111132.92 - 559.82 * cos(Math.toRadians(2 * ref.getLat())) + 1.175 * cos(Math.toRadians(4 * ref.getLat())) - 0.0023 * cos(Math.toRadians(6 * ref.getLat()));
        meterDegreeLongitude = 111412.84 * cos(Math.toRadians(ref.getLat())) - 93.5 * cos(Math.toRadians(3 * ref.getLat())) - 0.118 * cos(Math.toRadians(5 * ref.getLat()));
    }

    /**
     * Get the travel and routes of the users that correspond with the provided parameters
     * 
     * @param day The day of the week
     * @param time The current time
     * @param eventType The type of event
     * @param c A location
     * @return A map of users paired with a map of their travels and routes
     * @throws DataAccessException This is thrown when the connection to the database fails.
     */
    public Map<Integer, Map<Integer, List<Integer>>> getTimeTravels(int day, Time time, String eventType, Coordinate c) throws DataAccessException{
        try {
            Map<Integer, Map<Integer, List<Integer>>> res = new HashMap<>();
            PreparedStatement ps = getPreparedStatement(day, time, eventType, c);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt(USER_ID);
                    // get map with travel ids
                    Map<Integer, List<Integer>> travelIds = res.getOrDefault(userId, new HashMap<>());
                    int travelId = rs.getInt(TRAVEL_ID);
                    // get map with route ids
                    List<Integer> routeIds = travelIds.getOrDefault(travelId, new ArrayList<>());
                    routeIds.add(rs.getInt(ID));
                    travelIds.put(travelId, routeIds);
                    res.put(userId, travelIds);
                }
                return res;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventProcessingDAO.class.getName()).log(Level.SEVERE, "Could not find travels that match.", ex);
            throw new DataAccessException("Could not get matching travels", ex);
        }
    }    

    /**
     * Get the prepared statement to look for the travel and routes of the users that correspond with the provided parameters
     * @param day The day of the week
     * @param time The current time
     * @param eventType The type of event
     * @param c A location
     * @return A prepared SQL statement
     * @throws SQLException This is thrown when the query is not valid
     */
    protected abstract PreparedStatement getPreparedStatement(int day, Time time, String eventType, Coordinate c) throws SQLException;
    
    /**
     * Get the matching POI's of all users depending on the parameters
     * 
     * @param eventType The type of event
     * @param c The location
     * @return A map of users and their POI's
     * @throws DataAccessException This is thrown when the connection to the database fails
     */
    public Map<Integer, List<Integer>> getMatchingPOIs(String eventType, Coordinate c) throws DataAccessException{     
        Map<Integer, List<Integer>> res = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(
            "SELECT p.user_id, p.id FROM pointsofinterest p"
            + " INNER JOIN eventtypepoi ep ON (p.id=ep.poi_id) INNER JOIN address a ON (p.address_id=a.id)"
            + " WHERE p.active=true AND ep.event_type=? "
            + "AND ((((a.lat-?)*?)*((a.lat-?)*?))+(((a.lon-?)*?)*((a.lon-?)*?)) < p.radius*p.radius)")) {

            ps.setString(1, eventType);
            ps.setDouble(2, c.getLat());
            ps.setDouble(3, meterDegreeLatitude);
            ps.setDouble(4, c.getLat());
            ps.setDouble(5, meterDegreeLatitude);

            ps.setDouble(6, c.getLon());
            ps.setDouble(7, meterDegreeLongitude);
            ps.setDouble(8, c.getLon());
            ps.setDouble(9, meterDegreeLongitude);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt(USER_ID);
                    // get map with poi ids
                    List<Integer> poiIds = res.getOrDefault(userId, new ArrayList<>());
                    poiIds.add(rs.getInt(ID));
                    res.put(userId, poiIds);
                }
                return res;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventProcessingDAO.class.getName()).log(Level.SEVERE, "Could not find POIs that match.", ex);
            throw new DataAccessException("Could not get matching pois", ex);
        }
    }

}
