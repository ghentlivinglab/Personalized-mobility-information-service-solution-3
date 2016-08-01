package vop.groep7.vop7backend.Models.APIModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Arrays;
import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APIRoute extends APIModel {

    private String id;
    private APILinks[] links;
    private APINotify notify;
    private boolean active;
    private APICoordinate[] waypoints;
    @JsonProperty("interpolated_waypoints")
    private APICoordinate[] interpolatedWaypoints;
    @JsonProperty("transportation_type")
    private APITransport transportationType;
    @JsonProperty("notify_for_event_types")
    private APIEventType[] notifyForEventTypes;

    /**
     * Compare this APIRoute object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIRoute)) {
            return false;
        }
        APIRoute other = (APIRoute) o;

        return Arrays.deepEquals(getLinks(), other.getLinks())
                && Objects.equals(getNotify(), other.getNotify())
                && Objects.equals(isActive(), other.isActive())
                && Arrays.deepEquals(getWaypoints(), other.getWaypoints())
                && Objects.equals(getTransportationType(), other.getTransportationType())
                && Arrays.deepEquals(getNotifyForEventTypes(), other.getNotifyForEventTypes());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Arrays.deepHashCode(this.links);
        hash = 67 * hash + Objects.hashCode(this.notify);
        hash = 67 * hash + (this.active ? 1 : 0);
        hash = 67 * hash + Arrays.deepHashCode(this.waypoints);
        hash = 67 * hash + Objects.hashCode(this.transportationType);
        hash = 67 * hash + Arrays.deepHashCode(this.notifyForEventTypes);
        return hash;
    }

    /**
     * Getter for id
     *
     * @return The Id of the route
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id The Id of the route
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for the links
     *
     * @return The links of this object
     */
    public APILinks[] getLinks() {
        return links;
    }

    /**
     * Setter for the links
     *
     * @param links The links of this object
     */
    public void setLinks(APILinks[] links) {
        this.links = links;
    }

    /**
     * Getter for the notifications
     *
     * @return The notifications of this object
     */
    public APINotify getNotify() {
        return notify;
    }

    /**
     * Setter for the notifications
     *
     * @param notify The notifications of this object
     */
    public void setNotify(APINotify notify) {
        this.notify = notify;
    }

    /**
     * Getter for waypoints
     *
     * @return The waypoints of the route
     */
    public APICoordinate[] getWaypoints() {
        return waypoints;
    }

    /**
     * Setter for waypoints
     *
     * @param waypoints The waypoints of the route
     */
    public void setWaypoints(APICoordinate[] waypoints) {
        this.waypoints = waypoints;
    }

      /**
     * Getter for waypoints
     *
     * @return The waypoints of the route
     */
    @JsonSerialize
    @JsonProperty("interpolated_waypoints")
    public APICoordinate[] getInterpolatedWaypoints() {
        return interpolatedWaypoints;
    }

    /**
     * Setter for waypoints
     *
     * @param interpolatedWaypoints The waypoints of the route
     */
    @JsonSerialize
    @JsonProperty("interpolated_waypoints")
    public void setInterpolatedWaypoints(APICoordinate[] interpolatedWaypoints) {
        this.interpolatedWaypoints = interpolatedWaypoints;
    }
    
    /**
     * Getter for transportationType
     *
     * @return The transport type this route will use
     */
    @JsonSerialize
    @JsonProperty("transportation_type")
    public APITransport getTransportationType() {
        return transportationType;
    }

    /**
     * Setter for transportationType
     *
     * @param transportationType The transport type this route will use
     */
    @JsonSerialize
    @JsonProperty("transportation_type")
    public void setTransportationType(APITransport transportationType) {
        this.transportationType = transportationType;
    }

    /**
     * Getter for the type of notifications the user wants with this route
     *
     * @return The type of notifications this route will generate
     */
    @JsonSerialize
    @JsonProperty("notify_for_event_types")
    public APIEventType[] getNotifyForEventTypes() {
        return notifyForEventTypes;
    }

    /**
     * Setter for the type of notifications the user wants with this route
     *
     * @param notifyForEventTypes The type of notifications this route will
     * generate
     */
    @JsonSerialize
    @JsonProperty("notify_for_event_types")
    public void setNotifyForEventTypes(APIEventType[] notifyForEventTypes) {
        this.notifyForEventTypes = notifyForEventTypes;
    }

    /**
     * Getter for active
     *
     * @return The active of the route
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Setter for active
     *
     * @param active The active of the route
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
