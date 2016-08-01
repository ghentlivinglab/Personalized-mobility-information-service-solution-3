package vop.groep7.vop7backend.Spring;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.bull.javamelody.MonitoredWithSpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vop.groep7.vop7backend.Controllers.RouteController;
import vop.groep7.vop7backend.Exceptions.DatabaseResponseException;
import vop.groep7.vop7backend.Exceptions.ExistingResponseException;
import vop.groep7.vop7backend.Exceptions.InvalidInputResponseException;
import vop.groep7.vop7backend.Exceptions.NotExistingResponseException;
import vop.groep7.vop7backend.Models.APIModels.APIRoute;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Backend Team
 */
@MonitoredWithSpring
@CrossOrigin
@RestController
public class SpringRouteController extends SpringController {

    /**
     * Static URLs for user operations
     *
     */
    private static final String PARAM_ROUTE = "user/{user_id}/travel/{travel_id}/route/{route_id}";
    private static final String BASIC_ROUTES = "user/{user_id}/travel/{travel_id}/route";

    private static final String BASIC_ERROR = "The database is not available at the moment.";
    private static final String CREATE_EXISTS_ERROR = "The route you are trying to create already exists.";
    private static final String EXISTS_ERROR = "The route already exists.";
    private static final String NOT_EXISTS_ERROR = "The route doesn't exist.";

    @Autowired
    private RouteController routeController;

    /**
     * HTTP GET method for all routes of a user
     *
     * @param userId The Id of a user
     * @param travelId The Id of a travel
     * @see APIRoute
     * @return A list of route objects
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = BASIC_ROUTES, method = RequestMethod.GET)
    public @ResponseBody
    List<APIRoute> getAllRoutes(@PathVariable("user_id") int userId, @PathVariable("travel_id") int travelId) throws DatabaseResponseException {
        try {
            return routeController.getAPIRoutes(userId, travelId);
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringRouteController.class.getName()).log(Level.SEVERE, "Could not get all routes.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP POST method for routes
     *
     * @param userId the Id of a user
     * @param travelId The Id of a travel
     * @param route The route we want to create
     * @see APIRoute
     * @return The route object that has been created
     * @throws NotExistingResponseException This is thrown when no route or user
     * can be found.
     * @throws ExistingResponseException This is thrown when the route already
     * exists.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = BASIC_ROUTES, method = RequestMethod.POST)
    public @ResponseBody
    APIRoute createRoute(@PathVariable("user_id") int userId, @PathVariable("travel_id") int travelId, @RequestBody APIRoute route) throws NotExistingResponseException, ExistingResponseException, DatabaseResponseException {
        checkRouteInput(route);

        try {
            if (!routeController.existsAPIRoute(userId, travelId, route)) {
                APIRoute r = routeController.createAPIRoute(userId, travelId, route);
                if (r == null) {
                    throw new NotExistingResponseException(NOT_EXISTS_ERROR);
                } else {
                    return r;
                }
            } else {
                throw new ExistingResponseException(CREATE_EXISTS_ERROR);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringRouteController.class.getName()).log(Level.SEVERE, "Could not create route.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP GET method for a route of a user
     *
     * @param userId the Id of a user
     * @param travelId The Id of a travel
     * @param routeId the Id of a route
     * @see APIRoute
     * @return The route object that has the corresponding id
     * @throws NotExistingResponseException This is thrown when no route or user
     * can be found.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = PARAM_ROUTE, method = RequestMethod.GET)
    public @ResponseBody
    APIRoute getRoute(@PathVariable("user_id") int userId, @PathVariable("travel_id") int travelId, @PathVariable("route_id") int routeId) throws NotExistingResponseException, DatabaseResponseException {
        try {
            APIRoute route = routeController.getAPIRoute(userId, travelId, routeId);
            if (route == null) {
                throw new NotExistingResponseException(NOT_EXISTS_ERROR);
            }
            return route;
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringRouteController.class.getName()).log(Level.SEVERE, "Could not get route.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP PUT method for routes
     *
     * @param userId the Id of a user
     * @param travelId The Id of a travel
     * @param routeId the Id of a route
     * @param route The route we want to modify
     * @see APIRoute
     * @return The route object that has been modified
     * @throws ExistingResponseException Thrown if the route can't be created
     * because it exists already
     * @throws NotExistingResponseException This is thrown when no route or user
     * can be found.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = PARAM_ROUTE, method = RequestMethod.PUT)
    public @ResponseBody
    APIRoute modifyRoute(@PathVariable("user_id") int userId, @PathVariable("travel_id") int travelId, @PathVariable("route_id") int routeId, @RequestBody APIRoute route) throws ExistingResponseException, NotExistingResponseException, DatabaseResponseException {
        checkRouteInput(route);

        try {
            if (!routeController.existsAPIRoute(userId, travelId, route)) {
                return routeController.modifyAPIRoute(userId, travelId, routeId, route);
            } else {
                throw new ExistingResponseException(CREATE_EXISTS_ERROR);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringRouteController.class.getName()).log(Level.SEVERE, "Could not modify route", ex);
            try {
                APIRoute r = routeController.getAPIRoute(userId, travelId, routeId);
                if (r != null) {
                    throw new ExistingResponseException(EXISTS_ERROR);
                } else {
                    throw new NotExistingResponseException(NOT_EXISTS_ERROR);
                }
            } catch (DataAccessException ex1) {
                Logger.getLogger(SpringRouteController.class.getName()).log(Level.SEVERE, "Could not modify or get route", ex1);
                throw new DatabaseResponseException(BASIC_ERROR);
            }
        }
    }

    /**
     * HTTP DELETE method for routes
     *
     * @param userId the Id of a user
     * @param travelId The Id of a travel
     * @param routeId the Id of the route
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     * @throws NotExistingResponseException This is thrown when no route or user
     * can be found.
     */
    @RequestMapping(value = PARAM_ROUTE, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRoute(@PathVariable("user_id") int userId, @PathVariable("travel_id") int travelId, @PathVariable("route_id") int routeId) throws DatabaseResponseException, NotExistingResponseException {
        try {
            if (!routeController.deleteAPIRoute(userId, travelId, routeId)) {
                throw new NotExistingResponseException(NOT_EXISTS_ERROR);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringRouteController.class.getName()).log(Level.SEVERE, "Could not delete route", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    private void checkRouteInput(APIRoute r) {
        boolean valid = true;
        List<String> invalidFields = new ArrayList<>();

        if (r.getTransportationType() == null) {
            valid = false;
            invalidFields.add("transportation_type");
        }
        if (r.getWaypoints() == null) {
            valid = false;
            invalidFields.add("waypoints");
        }
        if (r.getNotifyForEventTypes() == null) {
            valid = false;
            invalidFields.add("notify_for_event_types");
        }
        if (r.getNotify() == null) {
            valid = false;
            invalidFields.add("notify");
        }

        if (!valid) {
            throw new InvalidInputResponseException(invalidFields);
        }
    }
}
