package vop.groep7.vop7backend.Controllers;

import java.util.ArrayList;
import java.util.List;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APIRoute;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.RouteFactory;
import vop.groep7.vop7backend.factories.TransportFactory;

/**
 *
 * @author Backend Team
 */
public class RouteController extends Controller {

    /**
     * Get a APIRoute object based on it's Id and the Id of the User
     *
     * @param userId Id of a user
     * @param travelId The Id of a travel
     * @param routeId Id of the route
     * @see APIRoute
     * @return The API route object
     * @throws DataAccessException This is thrown when no route can be found.
     */
    public APIRoute getAPIRoute(int userId, int travelId, int routeId) throws DataAccessException {
        // get route
        Route r = getRoute(userId, travelId, routeId);
        if (r == null) {
            return null;
        } else {
            return RouteFactory.toAPIModel(r, userId, travelId);
        }
    }

    /**
     * Get all APIRoute objects of a User
     *
     * @param userId Id of a user
     * @param travelId The Id of a travel
     * @see APIRoute
     * @return A list of all API routes
     * @throws DataAccessException This is thrown when no routes can be found.
     */
    public List<APIRoute> getAPIRoutes(int userId, int travelId) throws DataAccessException {
        List<Route> routes = getRoutes(userId, travelId);
        // D2A routes
        List<APIRoute> res = new ArrayList<>();
        routes.stream().forEach((r) -> {
            res.add(RouteFactory.toAPIModel(r, userId, travelId));
        });
        return res;
    }

    /**
     * Insert a APIRoute object in the database
     *
     * @param userId Id of a user
     * @param travelId The Id of a travel
     * @param route A route object that has to be created
     * @see APIRoute
     * @return The created API route object
     * @throws DataAccessException This is thrown when no route can be created.
     */
    public APIRoute createAPIRoute(int userId, int travelId, APIRoute route) throws DataAccessException {
        Travel travel = getTravel(userId, travelId);

        Transport trans = TransportFactory.toDomain(route.getTransportationType());
        ArrayList<EventType> types = new ArrayList<>();
        for (APIEventType type : route.getNotifyForEventTypes()) {
            types.add(EventTypeFactory.toDomainModel(type));
        }
        ArrayList<Coordinate> waypoints = new ArrayList<>();
        if (route.getWaypoints() != null) {
            for (APICoordinate c : route.getWaypoints()) {
                waypoints.add(CoordinateFactory.toDomainModel(c));
            }
        }

        ArrayList<Coordinate> interpolatedWaypoints = new ArrayList<>();
        if (route.getInterpolatedWaypoints() != null) {
            for (APICoordinate c : route.getInterpolatedWaypoints()) {
                interpolatedWaypoints.add(CoordinateFactory.toDomainModel(c));
            }
        }

        Route newRoute = RouteFactory.create(route.isActive(), trans, route.getNotify().isEmailNotify(), route.getNotify().isCellNumberNotify(), waypoints, interpolatedWaypoints, types);

        // create route in db
        int generatedId = getRouteDAO().createRoute(userId, travelId, newRoute);
        // use generatedId as route identifier
        newRoute.setRouteIdentifier(generatedId);
        // load newRoute into travel
        travel.addRoute(newRoute);

        return RouteFactory.toAPIModel(newRoute, userId, travelId);
    }

    /**
     * Delete a APIRoute object from the database
     *
     * @param userId Id of a user
     * @param travelId The Id of a travel
     * @param routeId Id of the route
     * @return If the deleting has succeeded
     * @throws DataAccessException This is thrown when the route can't be
     * deleted.
     */
    public boolean deleteAPIRoute(int userId, int travelId, int routeId) throws DataAccessException {
        Travel travel = getTravel(userId, travelId);

        // delete route in cache
        travel.removeRoute(routeId);

        // clear already matched events of user
        getUserDAO().deleteMatchedEvents(userId);

        // delete route in db
        return getRouteDAO().deleteRoute(userId, travelId, routeId);
    }

    /**
     * Modify a APIRoute object in the database
     *
     * @param userId Id of a user
     * @param travelId The Id of a travel
     * @param routeId Id of the route
     * @param route An API route object that has to be modified
     * @see APIRoute
     * @return The modified API route object
     * @throws DataAccessException This is thrown when the route can't be
     * modified.
     */
    public APIRoute modifyAPIRoute(int userId, int travelId, int routeId, APIRoute route) throws DataAccessException {
        Travel travel = getTravel(userId, travelId);

        Route modifiedRoute = RouteFactory.toDomainModel(route);
        // load modified route into travel
        travel.removeRoute(routeId);
        travel.addRoute(modifiedRoute);

        // modify route in db
        getRouteDAO().modifyRoute(userId, travelId, routeId, modifiedRoute);
        
        // clear already matched events of user
        getUserDAO().deleteMatchedEvents(userId);

        return route;
    }

    /**
     * Check if the APIRoute already exists
     *
     * @param userId Id of a user
     * @param travelId The Id of a travel
     * @param route An API route object
     * @see APIRoute
     * @return If the API route already exists
     * @throws DataAccessException This is thrown when the route can't be
     * checked.
     */
    public boolean existsAPIRoute(int userId, int travelId, APIRoute route) throws DataAccessException {
        return getRouteDAO().existsRoute(userId, travelId, RouteFactory.toDomainModel(route));
    }
}
