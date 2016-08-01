package vop.groep7.vop7backend.Controllers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.AppConfig;
import vop.groep7.vop7backend.Exceptions.NotExistingResponseException;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.mongodb.EventDAO;
import vop.groep7.vop7backend.database.sql.POIDAO;
import vop.groep7.vop7backend.database.sql.RouteDAO;
import vop.groep7.vop7backend.database.sql.TravelDAO;
import vop.groep7.vop7backend.database.sql.UserDAO;

/**
 *
 * @author Backend Team
 */
public abstract class Controller {

    private UserDAO userDAO;
    private TravelDAO travelDAO;
    private POIDAO poiDAO;
    private RouteDAO routeDAO;
    private EventDAO eventDAO;

    /**
     * Initialize the UserDAO object to communicate with the database
     *
     * @throws DataAccessException This is thrown when the connection to the
     * database can't be made.
     */
    private void initUserDAO() throws DataAccessException {
        userDAO = AppConfig.getDataAccessProvider().getUserDataAccessContext().getUserDAO();
    }

    /**
     * Initialize the TravelDAO object to communicate with the database
     *
     * @throws DataAccessException This is thrown when the connection to the
     * database can't be made.
     */
    private void initTravelDAO() throws DataAccessException {
        travelDAO = AppConfig.getDataAccessProvider().getUserDataAccessContext().getTravelDAO();
    }

    /**
     * Initialize the POIDAO object to communicate with the database
     *
     * @throws DataAccessException This is thrown when the connection to the
     * database can't be made.
     */
    private void initPOIDAO() throws DataAccessException {
        poiDAO = AppConfig.getDataAccessProvider().getUserDataAccessContext().getPOIDAO();
    }

    /**
     * Initialize the RouteDAO object to communicate with the database
     *
     * @throws DataAccessException This is thrown when the connection to the
     * database can't be made.
     */
    private void initRouteDAO() throws DataAccessException {
        routeDAO = AppConfig.getDataAccessProvider().getUserDataAccessContext().getRouteDAO();
    }

    /**
     * Initialize the EventDAO object to communicate with the database
     *
     */
    private void initEventDAO() {
        eventDAO = AppConfig.getDataAccessProvider().getEventDataAccessContext().getEventDAO();
    }

    /**
     * Get the user data access object
     *
     * @see UserDAO
     * @return The initiated UserDAO
     * @throws DataAccessException This is thrown when the connection to the
     * database can't be made.
     */
    protected UserDAO getUserDAO() throws DataAccessException {
        if (userDAO == null) {
            initUserDAO();
        }
        return userDAO;
    }

    /**
     * Get the travel data access object
     *
     * @see TravelDAO
     * @return The initiated TravelDAO
     * @throws DataAccessException This is thrown when the connection to the
     * database can't be made.
     */
    protected TravelDAO getTravelDAO() throws DataAccessException {
        if (travelDAO == null) {
            initTravelDAO();
        }
        return travelDAO;
    }

    /**
     * Get the POI data access object
     *
     * @see POIDAO
     * @return The initiated POIDAO
     * @throws DataAccessException This is thrown when the connection to the
     * database can't be made.
     */
    protected POIDAO getPOIDAO() throws DataAccessException {
        if (poiDAO == null) {
            initPOIDAO();
        }
        return poiDAO;
    }

    /**
     * Get the route data access object
     *
     * @see RouteDAO
     * @return The initiated RouteDAO
     * @throws DataAccessException This is thrown when the connection to the
     * database can't be made.
     */
    protected RouteDAO getRouteDAO() throws DataAccessException {
        if (routeDAO == null) {
            initRouteDAO();
        }
        return routeDAO;
    }

    /**
     * Get the event data access object
     *
     * @see EventDAO
     * @return The initiated EventDAO
     */
    protected EventDAO getEventDAO() {
        if (eventDAO == null) {
            initEventDAO();
        }
        return eventDAO;
    }

    /**
     * The cache of the users
     */
    protected static Cache<Integer, User> userCache = CacheBuilder.newBuilder()
            .softValues()
            .recordStats()
            .build();

    /**
     * The cache of the events
     */
    protected static Cache<Integer, Event> eventCache = CacheBuilder.newBuilder()
            .softValues()
            .recordStats()
            .build();

    /**
     * Get a User object based on it's Id
     *
     * @param userId Id of a user
     * @see User
     * @return A user object
     * @throws DataAccessException This is thrown when no user can be found.
     */
    public User getUser(int userId) throws DataAccessException {
        User user = userCache.getIfPresent(userId);
        if (user == null) {
            // get user from dao
            user = getUserDAO().getUser(userId);
            if (user == null) {
                throw new NotExistingResponseException("The user does not exist!");
            }
            // load user into cache
            userCache.put(userId, user);
        }

        return user;
    }

    /**
     * Get all POI objects of a User
     *
     * @param userId Id of a user
     * @see POI
     * @return A list of all pois
     * @throws DataAccessException This is thrown when no pois can be found.
     */
    public List<POI> getPOIs(int userId) throws DataAccessException {
        User user = getUser(userId);

        List<POI> pois;
        if (!user.isPointsOfInterestLoaded()) {
            // load pois into user
            pois = getPOIDAO().getPOIs(userId);
            pois.stream().forEach((p) -> {
                user.addPOI(p);
            });
        } else {
            pois = user.getPointsOfInterest();
        }
        return pois;
    }

    /**
     * Get the POI object based on its id and the user id
     *
     * @param userId id of the user
     * @param poiId id of the poi
     * @return the POI object
     * @throws DataAccessException DataAccessException This is thrown when the
     * connection to the database can't be made.
     */
    public POI getPOI(int userId, int poiId) throws DataAccessException {
        // get pois of user
        for (POI p : getPOIs(userId)) {
            try {
                // return poi with poiId if it exists
                if (p.getPoiIdentifier() == poiId) {
                    return p;
                }
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, "Could not find POI without Id.", ex);
                // skip poi without id (poi should have id)
            }
        }
        return null;
    }

    /**
     * Get all Travel objects of a User
     *
     * @param userId Id of a user
     * @see Travel
     * @return A list of all travels
     * @throws DataAccessException This is thrown when no travels can be found.
     */
    public List<Travel> getTravels(int userId) throws DataAccessException {
        User user = getUser(userId);

        List<Travel> travels;
        if (!user.isTravelsLoaded()) {
            // load travels into user
            travels = getTravelDAO().getTravels(userId);
            travels.stream().forEach((t) -> {
                user.addTravel(t);
            });
        } else {
            travels = user.getTravels();
        }
        return travels;
    }

    /**
     * Get all Route objects of a Travel
     *
     * @param userId Id of a User
     * @param travelId Id of a Travel
     * @see Route id
     * @return A list of all routes of the Travel
     * @throws DataAccessException This is thrown when no travels can be found.
     */
    public List<Route> getRoutes(int userId, int travelId) throws DataAccessException {
        Travel travel = getTravel(userId, travelId);
        List<Route> routes;
        if (!travel.isRoutesLoaded()) {
            // load routes into travel
            routes = getRouteDAO().getRoutes(userId, travelId);
            routes.stream().forEach((r) -> {
                travel.addRoute(r);
            });
        } else {
            routes = travel.getRoutes();
        }
        return routes;
    }

    /**
     * Get a Route object based on it's id, the id of the travel and the id of
     * the user
     *
     * @param userId id the user
     * @param travelId id of the travel
     * @param routeId id of the route
     * @see Route
     * @return the Route object
     * @throws DataAccessException DataAccessException This is thrown when the
     * connection to the database can't be made.
     */
    public Route getRoute(int userId, int travelId, int routeId) throws DataAccessException {
        for (Route r : getRoutes(userId, travelId)) {
            try {
                if (r.getRouteIdentifier() == routeId) {
                    return r;
                }
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, "Route has no id.", ex);
                // skip route without id (should have id)
            }
        }
        return null;
    }

    /**
     * Get a Travel object based on it's Id and the Id of the User
     *
     * @param userId Id of a user
     * @param travelId Id of the travel
     * @see Travel
     * @return The travel object
     * @throws DataAccessException This is thrown when no travel can be found.
     */
    public Travel getTravel(int userId, int travelId) throws DataAccessException {
        // get travels of user
        for (Travel t : getTravels(userId)) {
            try {
                // return travel with travelId if it exists
                if (t.getTravelIdentifier() == travelId) {
                    return t;
                }
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, "Could not find travel without Id.", ex);
                // skip travel without id (travel should have id)
            }
        }
        // travel not found
        throw new NotExistingResponseException("Travel could not be found!");
    }

}
