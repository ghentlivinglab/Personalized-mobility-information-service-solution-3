package vop.groep7.vop7backend.database.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Notifications;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.database.DataAccessException;
import static vop.groep7.vop7backend.database.sql.SQLDAO.TYPE;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.RouteFactory;
import vop.groep7.vop7backend.factories.TransportFactory;

/**
 *
 * @author Backend Team
 */
public class RouteDAO extends SQLDAO {

    private static final String TRANSPORT = "transport";
    private static final String ROUTE_TABLE = "eventtypeRoute";
    private static final String ROUTE_ID = "route_id";

    /**
     * Create a RouteDAO using a connection to a database
     *
     * @param connection A connection object to a database
     */
    public RouteDAO(Connection connection) {
        super(connection);
    }

    /**
     * Retrieve a route object from the database.
     *
     * @param userId The Id of the user
     * @param travelId The Id of a travel
     * @param routeId The Id of the route
     * @see Route
     * @return A route object that belongs to the user
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public Route getRoute(int userId, int travelId, int routeId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, transport, email_notify, cell_number_notify, active FROM route WHERE id = ? AND user_id = ? AND travel_id = ?")) {
            ps.setInt(1, routeId);
            ps.setInt(2, userId);
            ps.setInt(3, travelId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Transport trans = TransportFactory.build(rs.getString(TRANSPORT));
                    return RouteFactory.build(routeId, rs.getBoolean(ACTIVE), trans, rs.getBoolean(EMAIL_NOTIFY), rs.getBoolean(CELL_NUMBER_NOTIFY), getWaypoints(routeId), getNotifyForEventTypes(ROUTE_TABLE, ROUTE_ID, rs.getInt(ID)));
                }
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not get route.", ex);
            throw new DataAccessException("Could not find route.", ex);
        }
    }

    /**
     * Get all the routes from a user present in the database
     *
     * @param userId The Id of the user
     * @param travelId The Id of a travel
     * @see Route
     * @return A list of route objects
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public List<Route> getRoutes(int userId, int travelId) throws DataAccessException {
        List<Route> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, transport, email_notify, cell_number_notify, active FROM route WHERE user_id = ? AND travel_id = ?")) {
            ps.setInt(1, userId);
            ps.setInt(2, travelId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transport trans = TransportFactory.build(rs.getString(TRANSPORT));
                    Route r = RouteFactory.build(rs.getInt(ID), rs.getBoolean(ACTIVE), trans, rs.getBoolean(EMAIL_NOTIFY), rs.getBoolean(CELL_NUMBER_NOTIFY), getWaypoints(rs.getInt(ID)), getNotifyForEventTypes(ROUTE_TABLE, ROUTE_ID, rs.getInt(ID)));
                    result.add(r);
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not get all routes.", ex);
            throw new DataAccessException("Could not find routes.", ex);
        }
    }

    /**
     * Insert a route into the database
     *
     * @param userId The Id of the user
     * @param travelId The Id of a travel
     * @param transport The transportation type of the route
     * @param notify The notifications for the route
     * @param active Whether the route is active or not
     * @param waypoints The waypoints of the route
     * @param interpolatedWaypoints The waypoints used for route matching
     * @param transportationTypes The transportation types of this route
     * @param notifyForEventTypes The notification the user wants for this route
     * @return The Id of the route that has been inserted in the database
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private int createRoute(int userId, int travelId, Transport transport, Notifications notify, boolean active, ArrayList<Coordinate> waypoints, ArrayList<Coordinate> interpolatedWaypoints)
            throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO route(user_id, travel_id, transport, email_notify, cell_number_notify, active) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setInt(2, travelId);
            ps.setString(3, transport.getTransport());
            ps.setBoolean(4, notify.isNotifyEmail());
            ps.setBoolean(5, notify.isNotifyCellPhone());
            ps.setBoolean(6, active);
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    if (waypoints != null) {
                        for (Coordinate c : waypoints) {
                            createWaypoint(rs.getInt(1), c);
                        }
                    }
                    for (Coordinate c : interpolatedWaypoints) {
                        createGeneratedWaypoint(rs.getInt(1), c);
                    }
                    if (notify.getNotifyForEventTypes() != null) {
                        for (EventType e : notify.getNotifyForEventTypes()) {
                            createNotifyForEventType(ROUTE_TABLE, ROUTE_ID, rs.getInt(1), e);
                        }
                    }
                    return rs.getInt(ID);
                }
            }
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not create route.", ex);
            throw new DataAccessException("Could not create route", ex);
        }
    }

    /**
     * Insert a route into the database
     *
     * @param userId The Id of the user
     * @param travelId The Id of a travel
     * @param route A route object representing the new route
     * @return The Id of the route that has been inserted in the database
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public int createRoute(int userId, int travelId, Route route) throws DataAccessException {
        return createRoute(userId, travelId, route.getTransportationType(), route.getNotifications(), route.isActive(), route.getWaypoints(), route.getInterpolatedWaypoints());
    }

    /**
     * Delete a route from the database
     *
     * @param userId The Id of the user
     * @param travelId The Id of a travel
     * @param routeId The Id of the route
     * @return If the deletion has succeeded or not
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deleteRoute(int userId, int travelId, int routeId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM route WHERE id = ? AND user_id = ? AND travel_id = ?")) {
            ps.setInt(1, routeId);
            ps.setInt(2, userId);
            ps.setInt(3, travelId);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not delete route.", ex);
            throw new DataAccessException("Could not delete route", ex);
        }
    }

    /**
     * Modify a route from the database
     *
     * @param userId The Id of the user
     * @param travelId The Id of a travel
     * @param routeId The Id of the route
     * @param transport The transportation type of the route
     * @param notify The notifications for the route
     * @param active Whether the route is active or not
     * @param waypoints The waypoints of the route
     * @param interpolatedWaypoints The waypoints used for route matching
     * @param transportationTypes The transportation types of this route
     * @param notifyForEventTypes The notification the user wants for this route
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private void modifyRoute(int userId, int travelId, int routeId, Transport transport, Notifications notify, boolean active, ArrayList<Coordinate> waypoints, ArrayList<Coordinate> interpolatedWaypoints) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE route SET transport = ?, email_notify = ?, cell_number_notify = ?, active = ?"
                + " WHERE id = ? AND user_id = ? AND travel_id = ?")) {
            ps.setString(1, transport.getTransport());
            ps.setBoolean(2, notify.isNotifyEmail());
            ps.setBoolean(3, notify.isNotifyCellPhone());
            ps.setBoolean(4, active);
            ps.setInt(5, routeId);
            ps.setInt(6, userId);
            ps.setInt(7, travelId);
            ps.execute();
            deleteWaypoints(routeId);
            if (waypoints != null) {
                for (Coordinate c : waypoints) {
                    createWaypoint(routeId, c);
                }
            }

            deleteGeneratedWaypoints(routeId);
            if (interpolatedWaypoints != null) {
                for (Coordinate c : interpolatedWaypoints) {
                    createGeneratedWaypoint(routeId, c);
                }
            }

            deleteNotifyForEventType(ROUTE_TABLE, ROUTE_ID, routeId);
            if (notify.getNotifyForEventTypes() != null) {
                for (EventType e : notify.getNotifyForEventTypes()) {
                    createNotifyForEventType(ROUTE_TABLE, ROUTE_ID, routeId, e);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not modify route.", ex);
            throw new DataAccessException("Could not modify user", ex);
        }
    }

    /**
     * Modify a route from the database
     *
     * @param userId The Id of the user
     * @param travelId The Id of a travel
     * @param routeId The Id of the route
     * @param route A route object representing the route
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void modifyRoute(int userId, int travelId, int routeId, Route route) throws DataAccessException {
        modifyRoute(userId, travelId, routeId, route.getTransportationType(), route.getNotifications(), route.isActive(), route.getWaypoints(), route.getInterpolatedWaypoints());
    }

    /**
     * Check if a route already exists in the database. The id field will not be
     * filled in, so every field must be the same.
     *
     * @param userId The Id of a user
     * @param travelId The Id of a travel
     * @param route A route object
     * @return If the route already exists
     * @throws DataAccessException This is thrown when something went wrong with
     * the database
     */
    public boolean existsRoute(int userId, int travelId, Route route) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id FROM route WHERE user_id = ? AND travel_id = ? AND transport = ? AND email_notify = ? AND cell_number_notify = ? AND active = ?")) {
            ps.setInt(1, userId);
            ps.setInt(2, travelId);
            ps.setString(3, route.getTransportationType().getTransport());
            ps.setBoolean(4, route.getNotifications().isNotifyEmail());
            ps.setBoolean(5, route.getNotifications().isNotifyCellPhone());
            ps.setBoolean(6, route.isActive());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return hasEqualRouteEventTypes(new HashSet(route.getNotifications().getNotifyForEventTypes()), userId, travelId, rs.getInt(ID)) && hasEqualWaypoints(new HashSet(route.getWaypoints()), userId, travelId);
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not check route.", ex);
            throw new DataAccessException("Could not check if the route exists.", ex);
        }
    }

    /**
     * Check if the set of event types is equal to the event types of the route
     * in the db
     *
     * @param types Set of the types of events
     * @param userId The Id of the user
     * @param travelId The Id of the travel
     * @param routeId The Id of the route
     *
     * @return true if the set of event types is equal
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean hasEqualRouteEventTypes(Set<EventType> types, int userId, int travelId, int routeId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT event_type FROM route r INNER JOIN eventtypeRoute e ON (r.id = e.route_id) WHERE user_id = ? AND travel_id = ? AND r.id = ?")) {
            ps.setInt(1, userId);
            ps.setInt(2, travelId);
            ps.setInt(3, routeId);
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
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not check types.", ex);
            throw new DataAccessException("Could not check if the eventtypes match.", ex);
        }
    }

    /**
     * Check if the set of waypoints is equal to the waypoints of the route in
     * the db
     *
     * @param waypoints Set of the waypoints
     * @param userId The Id of the user
     * @param travelId The Id of the travel
     *
     * @return true if the set of waypoints is equal
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean hasEqualWaypoints(Set<Coordinate> waypoints, int userId, int travelId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT lat, lon FROM route r INNER JOIN waypoint w ON (r.id = w.route_id) WHERE user_id = ? AND travel_id = ?")) {
            ps.setInt(1, userId);
            ps.setInt(2, travelId);
            try (ResultSet rs = ps.executeQuery()) {
                int i = 0;
                while (rs.next()) {
                    i++;
                    // check if set contains waypoint in result
                    if (!waypoints.contains(new Coordinate(rs.getDouble(LAT), rs.getDouble(LON)))) {
                        return false;
                    }
                }
                // if there are as many waypoints in set as in result return true, else false
                return i == waypoints.size();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not check waypoints.", ex);
            throw new DataAccessException("Could not check if the waypoints match.", ex);
        }
    }

    /**
     * Create a way point
     *
     * @param routeId The Id of a route
     * @param waypoint The coordinates of the way point
     * @throws DataAccessException This is thrown when something went wrong with
     * the database
     */
    private void createWaypoint(int routeId, Coordinate waypoint) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO waypoint(lat, lon, route_id) VALUES (?,?,?)")) {
            ps.setDouble(1, waypoint.getLat());
            ps.setDouble(2, waypoint.getLon());
            ps.setInt(3, routeId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not create waypoint.", ex);
            throw new DataAccessException("Could not create waypoints.", ex);
        }
    }

    /**
     * Create a generated way point
     *
     * @param routeId The Id of a route
     * @param waypoint The coordinates of the way point
     * @throws DataAccessException This is thrown when something went wrong with
     * the database
     */
    private void createGeneratedWaypoint(int routeId, Coordinate waypoint) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO interpolated_waypoints(lat, lon, route_id) VALUES (?,?,?)")) {
            ps.setDouble(1, waypoint.getLat());
            ps.setDouble(2, waypoint.getLon());
            ps.setInt(3, routeId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not create generated waypoint.", ex);
            throw new DataAccessException("Could not create generate waypoints.", ex);
        }
    }

    /**
     * Get all waypoints of a route
     *
     * @param routeId The Id of a route
     * @see Coordinate
     * @return An array of coordinates representing the waypoints
     * @throws DataAccessException This is thrown when something went wrong with
     * the database
     */
    private ArrayList<Coordinate> getWaypoints(int routeId) throws DataAccessException {
        ArrayList<Coordinate> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT lat, lon FROM waypoint WHERE route_id = ?")) {
            ps.setInt(1, routeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Coordinate c = CoordinateFactory.build(rs.getDouble(LAT), rs.getDouble(LON));
                    result.add(c);
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not get all waypoints.", ex);
            throw new DataAccessException("Could not find waypoints.", ex);
        }
    }

    /**
     * Delete all waypoints of a route
     *
     * @param routeId The Id of a route
     * @throws DataAccessException This is thrown when something went wrong with
     * the database
     */
    private void deleteWaypoints(int routeId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM waypoint WHERE route_id = ?")) {
            ps.setInt(1, routeId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not delete waypoint.", ex);
            throw new DataAccessException("Could not delete waypoints", ex);
        }
    }

    /**
     * Delete all generated waypoints of a route
     *
     * @param routeId The Id of a route
     * @throws DataAccessException This is thrown when something went wrong with
     * the database
     */
    private void deleteGeneratedWaypoints(int routeId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM interpolated_waypoints WHERE route_id = ?")) {
            ps.setInt(1, routeId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(RouteDAO.class.getName()).log(Level.SEVERE, "Could not delete generated waypoints.", ex);
            throw new DataAccessException("Could not delete generated waypoints", ex);
        }
    }
}
