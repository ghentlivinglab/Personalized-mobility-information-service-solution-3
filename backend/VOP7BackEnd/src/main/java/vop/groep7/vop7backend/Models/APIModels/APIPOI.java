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
public class APIPOI extends APIModel {

    private String id;
    private APILinks[] links;
    private APIAddress address;
    private String name;
    private int radius;
    private boolean active;
    @JsonProperty("notify_for_event_types")
    private APIEventType[] notifyForEventTypes;
    private APINotify notify;

    /**
     * Compare this APIPOI object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIPOI)) {
            return false;
        }
        APIPOI other = (APIPOI) o;

        return Objects.equals(getAddress(), other.getAddress())
                && Objects.equals(getNotify(), other.getNotify())
                && Objects.equals(getName(), other.getName())
                && (getRadius() == other.getRadius())
                && (isActive() == other.isActive())
                && Arrays.deepEquals(getLinks(), other.getLinks())
                && Arrays.deepEquals(getNotifyForEventTypes(), other.getNotifyForEventTypes());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Arrays.deepHashCode(this.links);
        hash = 43 * hash + Objects.hashCode(this.address);
        hash = 43 * hash + Objects.hashCode(this.name);
        hash = 43 * hash + this.radius;
        hash = 43 * hash + (this.active ? 1 : 0);
        hash = 43 * hash + Arrays.deepHashCode(this.notifyForEventTypes);
        hash = 43 * hash + Objects.hashCode(this.notify);
        return hash;
    }

    /**
     * Getter for id
     *
     * @return The Id of the APIPOI
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id The Id of the APIPOI
     */
    public void setId(String id) {
        this.id = id;
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
     * Get the address of this APIPOI
     *
     * @return The address of this APIPOI
     */
    public APIAddress getAddress() {
        return address;
    }

    /**
     * Set the address of this APIPOI
     *
     * @param address The address of this APIPOI
     */
    public void setAddress(APIAddress address) {
        this.address = address;
    }

    /**
     * Get the name of this APIPOI
     *
     * @return The name of this APIPOI
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this APIPOI
     *
     * @param name The name of this APIPOI
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the radius of this APIPOI
     *
     * @return The radius of this APIPOI
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Set the radius of this APIPOI
     *
     * @param radius The radius of this APIPOI
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Get if this APIPOI is active (does the user want notifications)
     *
     * @return If this APIPOI is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set if this APIPOI is active (does the user want notifications)
     *
     * @param active If this APIPOI is active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Getter for the type of notifications the user wants with this poi
     *
     * @return The type of notifications this poi will generate
     */
    @JsonSerialize
    @JsonProperty("notify_for_event_types")
    public APIEventType[] getNotifyForEventTypes() {
        return notifyForEventTypes;
    }

    /**
     * Setter for the type of notifications the user wants with this poi
     *
     * @param notifyForEventTypes The type of notifications this poi will
     * generate
     */
    @JsonSerialize
    @JsonProperty("notify_for_event_types")
    public void setNotifyForEventTypes(APIEventType[] notifyForEventTypes) {
        this.notifyForEventTypes = notifyForEventTypes;
    }
}
