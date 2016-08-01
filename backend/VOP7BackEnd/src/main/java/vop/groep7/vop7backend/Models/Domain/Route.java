package vop.groep7.vop7backend.Models.Domain;

import java.util.ArrayList;
import vop.groep7.vop7backend.Models.DomainModel;

/**
 *
 * @author Backend Team
 */
public class Route extends DomainModel {

    private int routeIdentifier;

    private final boolean active;
    private final ArrayList<Coordinate> waypoints;
    private final ArrayList<Coordinate> interpolatedWaypoints;
    private final Transport transportationType;
    private final Notifications notifications;

    /**
     * The public constructor with all fields required to create a Route object
     * 
     * @param active boolean that indicates if the route is active
     * @param transportationType the type of transportation that will be used 
     * on this route
     * @param notifyEmail boolean that indicates if the user wants to be 
     * notified by email for events located on this route
     * @param notifyCellPhone boolean that indicates if the user wants to be 
     * notified by sms for events located on this route
     * @param waypoints list with waypoints on the route
     * @param interpolatedWaypoints The list of waypoints which we will use for event matching
     * @param types list of the event types for which the user wants to be notified
     */
    public Route(boolean active, Transport transportationType, boolean notifyEmail, boolean notifyCellPhone, ArrayList<Coordinate> waypoints, ArrayList<Coordinate> interpolatedWaypoints, ArrayList<EventType> types) {
        this.routeIdentifier = -1;
        this.active = active;
        if(waypoints!=null){
            this.waypoints = waypoints;
        }else{
            this.waypoints=new ArrayList<>();
        }
        this.interpolatedWaypoints = interpolatedWaypoints;
        this.transportationType = transportationType;
        this.notifications = new Notifications(notifyEmail, notifyCellPhone, types);
    }

    /**
     * Check if the route is active
     * 
     * @return true if the route is active, else false
     */
    public boolean isActive() {
        return active;
    }

    private boolean hasRouteIdentifier() {
        return routeIdentifier != -1;
    }

    /**
     * Set the unique identifier of this route.
     * This can only be done once. 
     * From then the method will throw an IllegalArgumentException
     * 
     * @param routeIdentifier the unique identfier for this route     
     */
    public void setRouteIdentifier(int routeIdentifier) {
        if (!hasRouteIdentifier()) {
            this.routeIdentifier = routeIdentifier;
        } else {
            throw new IllegalArgumentException("RouteIdentifier is already set and cannot be changed!");
        }
    }

    /**
     * Get the unique identifier of this route
     * 
     * @return the identifier
     * @throws IllegalAccessException when the identifier is not set
     */
    public int getRouteIdentifier() throws IllegalAccessException {
        if (!hasRouteIdentifier()) {
            throw new IllegalAccessException("RouteIdentifier is not set!");
        }
        return routeIdentifier;
    }

    /**
     * Get a list of the waypoints set on this route by the user
     * 
     * @return the list of waypoints
     * @see Coordinate
     */
    public ArrayList<Coordinate> getWaypoints() {
        return (ArrayList<Coordinate>) waypoints.clone();
    }
    
     /**
     * Get a list of the waypoints set on this route by the user
     * 
     * @return the list of waypoints
     * @see Coordinate
     */
    public ArrayList<Coordinate> getInterpolatedWaypoints() {
        return (ArrayList<Coordinate>) interpolatedWaypoints.clone();
    }

    /**
     * Add a waypoint to the list of waypoints on this route
     * 
     * @param coordinate the waypoints to be added
     * @see Coordinate
     */
    public void addWaypoint(Coordinate coordinate) {
        waypoints.add(coordinate);
    }

    /**
     * Remove a waypoint from the list of waypoints of this route
     * 
     * @param coordinate he waypoints to be removed
     * @see Coordinate
     */
    public void removeWaypoint(Coordinate coordinate) {
        waypoints.remove(coordinate);
    }

    /**
     * Get the transportation type that will bes used on this route
     * 
     * @return the transportation type
     * @see Transport
     */
    public Transport getTransportationType() {
        return transportationType;
    }

    /**
     * Get the notification settings of this route
     * 
     * @return the notifications object of this route
     * @see Notifications
     */
    public Notifications getNotifications() {
        return notifications;
    }
}
