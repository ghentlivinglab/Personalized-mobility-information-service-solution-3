package vop.groep7.vop7backend.database.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Backend Team
 */
public class UserDataAccessContext {

    private final Connection connection;
    private UserDAO userDAO;
    private RouteDAO routeDAO;
    private TravelDAO travelDAO;
    private POIDAO poiDAO;
    private EventProcessingDAO eventProcessingDAO;

    /**
     * The constructor of a UserDataAccessContext needs a connection to a
     * database.
     *
     * @param connection A connection object to a database
     */
    public UserDataAccessContext(Connection connection) {
        this.connection = connection;
    }

    /**
     * Get the UserDAO object
     *
     * @return The UserDAO object to execute sql queries on the database
     */
    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAO(connection);
        }
        return userDAO;
    }

    /**
     * Get a RouteDAO object
     *
     * @return The RouteDAO object to execute sql queries on the database
     */
    public RouteDAO getRouteDAO() {
        if (routeDAO == null) {
            routeDAO = new RouteDAO(connection);
        }
        return routeDAO;
    }

    /**
     * Get a TravelDAO object
     *
     * @return The TravelDAO object to execute sql queries on the database
     */
    public TravelDAO getTravelDAO() {
        if (travelDAO == null) {
            travelDAO = new TravelDAO(connection);
        }
        return travelDAO;
    }

    /**
     * Get a POIDAO object
     *
     * @return The POIDAO object to execute sql queries on the database
     */
    public POIDAO getPOIDAO() {
        if (poiDAO == null) {
            poiDAO = new POIDAO(connection);
        }
        return poiDAO;
    }

    /**
     * Get the EventProcessingDAO object
     *
     * @return The EventProcessingDAO object to execute the specific queries to
     * match and process events
     */
    public EventProcessingDAO getEventProcessingDAO() {
        if (eventProcessingDAO == null) {
            eventProcessingDAO = new EventProcessingSquareDAO(connection);
        }
        return eventProcessingDAO;
    }

    /**
     * Called when Spring wants to close the server. Will make sure all
     * connections are closed.
     *
     * @throws DataAccessException This is thrown when the connection to the
     * database can't be closed.
     */
    public void destroy() throws DataAccessException {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDataAccessContext.class.getName()).log(Level.SEVERE, "Could not close connection.", ex);
            throw new DataAccessException("Could not close database connection.", ex);
        }
    }
}
