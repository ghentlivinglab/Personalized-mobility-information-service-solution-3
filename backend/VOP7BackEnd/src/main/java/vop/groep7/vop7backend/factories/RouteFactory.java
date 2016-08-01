package vop.groep7.vop7backend.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APINotify;
import vop.groep7.vop7backend.Models.APIModels.APIRoute;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Transport;

/**
 *
 * @author Backend Team
 */
public class RouteFactory extends Factory {

    private static final double maxDistance = 10;

    /**
     * Create a new Route object
     *
     * @param active Whether or not the route is active
     * @param transportationType The transportation type of the route
     * @param notifyEmail Whether or not email notifications are enabled for
     * this route
     * @param notifyCellPhone Whether or not cell phone notifications are
     * enabled for this route
     * @param waypoints The waypoints of this route
     * @param interpolatedWaypoints Waypoints used for event matching
     * @param types The event types for which notifications are enabled for this
     * route
     * @return A new Route object
     */
    public static Route create(boolean active, Transport transportationType, boolean notifyEmail, boolean notifyCellPhone, ArrayList<Coordinate> waypoints, ArrayList<Coordinate> interpolatedWaypoints, ArrayList<EventType> types) {
        ArrayList<Coordinate> newInterpolatedWaypoints = (ArrayList<Coordinate>) interpolateWaypoints(interpolatedWaypoints);

        Route route = new Route(active, transportationType, notifyEmail, notifyCellPhone, waypoints, newInterpolatedWaypoints, types);
        return route;
    }

    private static List<Coordinate> interpolateWaypoints(List<Coordinate> waypoints) {
        List<Coordinate> interpolatedWaypoints = new ArrayList<>();
        if (waypoints.size() > 0) {
            double distance, distanceLat, distanceLon;
            double ratio;

            Coordinate previous = waypoints.get(0);
            interpolatedWaypoints.add(previous);
            waypoints.remove(0);

            for (Coordinate co : waypoints) {

                // Bereken afstand tot vorige punt
                distanceLat = co.getLat() - previous.getLat();
                distanceLon = co.getLon() - previous.getLon();
                distance = Math.sqrt(Math.pow(distanceLat, 2) + Math.pow(distanceLon, 2));

                ratio = distance / maxDistance;

                for (int i = 1; i < ratio; i++) {
                    Coordinate newCo = new Coordinate(
                            previous.getLat() + i * distanceLat / ratio,
                            previous.getLon() + i * distanceLon / ratio
                    );
                    interpolatedWaypoints.add(newCo);
                }

                previous = co;
                interpolatedWaypoints.add(co);
            }
        }
        return interpolatedWaypoints;
    }

    /**
     * Build a valid Route object
     *
     * @param routeIdentifier The identifier of the route
     * @param active Whether or not the route is active
     * @param transportationType The transportation type of the route
     * @param notifyEmail Whether or not email notifications are enabled for
     * this route
     * @param notifyCellPhone Whether or not cell phone notifications are
     * enabled for this route
     * @param waypoints The waypoints of this route
     * @param types The event types for which notifications are enabled for this
     * route
     * @return A valid Route object
     */
    public static Route build(int routeIdentifier, boolean active, Transport transportationType, boolean notifyEmail, boolean notifyCellPhone, ArrayList<Coordinate> waypoints, ArrayList<EventType> types) {
        Route route = new Route(active, transportationType, notifyEmail, notifyCellPhone, waypoints, null, types);
        route.setRouteIdentifier(routeIdentifier);
        return route;
    }

    /**
     * Create a Route object starting from an APIRoute
     *
     * @param route An APIRoute object
     * @return A Route object
     */
    public static Route toDomainModel(APIRoute route) {
        Transport transport = TransportFactory.toDomain(route.getTransportationType());
        ArrayList<EventType> types = new ArrayList<>();
        if (route.getNotifyForEventTypes() != null) {
            for (APIEventType type : route.getNotifyForEventTypes()) {
                if (type.getType() != null) {
                    types.add(EventTypeFactory.toDomainModel(type));
                }
            }
        }

        ArrayList<Coordinate> interpolatedWaypoints = new ArrayList<>();
        if (route.getInterpolatedWaypoints() != null) {
            for (APICoordinate coordinate : route.getInterpolatedWaypoints()) {
                interpolatedWaypoints.add(CoordinateFactory.toDomainModel(coordinate));
            }
        }
        
        ArrayList<Coordinate> waypoints = new ArrayList<>();
        if (route.getWaypoints() != null) {
            for (APICoordinate coordinate : route.getWaypoints()) {
                waypoints.add(CoordinateFactory.toDomainModel(coordinate));
            }
        }

        ArrayList<Coordinate> newInterpolatedWaypoints = (ArrayList<Coordinate>) interpolateWaypoints(interpolatedWaypoints);

        Route result = new Route(route.isActive(), transport, route.getNotify().isEmailNotify(), route.getNotify().isCellNumberNotify(), waypoints, newInterpolatedWaypoints, types);
        String id = route.getId();
        if (id != null) {
            result.setRouteIdentifier(Integer.valueOf(id));
        }
        return result;
    }

    /**
     * Create an APIRoute object starting from a Route
     *
     * @param route A Route object
     * @param userId The identifier of the owner of the route
     * @param travelId The identifier of the travel this route belongs to
     * @return A APIRoute object
     */
    public static APIRoute toAPIModel(Route route, int userId, int travelId) {
        APIRoute result = new APIRoute();
        result.setActive(route.isActive());
        APICoordinate[] coordinates = null;
        if (route.getWaypoints() != null) {
            coordinates = new APICoordinate[route.getWaypoints().size()];
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[i] = CoordinateFactory.toAPIModel(route.getWaypoints().get(i));
            }
        }
        result.setWaypoints(coordinates);
        APINotify notify = new APINotify();
        notify.setEmailNotify(route.getNotifications().isNotifyEmail());
        notify.setCellNumberNotify(route.getNotifications().isNotifyCellPhone());
        result.setNotify(notify);
        ArrayList<EventType> list = route.getNotifications().getNotifyForEventTypes();
        APIEventType[] types = null;
        if (list != null) {
            types = new APIEventType[list.size()];
            for (int i = 0; i < types.length; i++) {
                if (list.get(i) != null) {
                    types[i] = EventTypeFactory.toAPIModel(list.get(i));
                }
            }
        }
        result.setNotifyForEventTypes(types);
        APITransport trans = TransportFactory.toAPIModel(route.getTransportationType());
        result.setTransportationType(trans);

        try {
            result.setId(String.valueOf(route.getRouteIdentifier()));
            result.setLinks(getLinks(USER + userId + TRAVEL + travelId + ROUTE + route.getRouteIdentifier()));
        } catch (IllegalAccessException ex) {
            Logger.getLogger(RouteFactory.class.getName()).log(Level.SEVERE, "Route id cannot be null!", ex);
            return null;
        }
        return result;
    }
}
