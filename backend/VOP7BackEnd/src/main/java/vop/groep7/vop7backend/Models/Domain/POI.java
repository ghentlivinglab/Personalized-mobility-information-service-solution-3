package vop.groep7.vop7backend.Models.Domain;

import java.util.ArrayList;
import vop.groep7.vop7backend.Models.DomainModel;

/**
 *
 * @author Backend Team
 */
public class POI extends DomainModel {

    private int poiIdentifier;

    private final Address address;
    private final String name;
    private final int radius;
    private final boolean active;
    private final Notifications notifications;

    /**
     * The public constructor with all fields required to create a POI object
     * 
     * @param address the address object that represents the location of the 
     * point of interest
     * @param name the name given by the user to identify his poi
     * @param radius the radius in meters around the location
     * @param active boolean that indicates if the poi is active
     * @param notifyEmail boolean that indicates if the user wants to be 
     * notified by email for events located within the radius
     * @param notifyCellPhone boolean that indicates if the user wants to be 
     * notified by sms for events located within the radius
     * @param types list of the event types for which the user wants to be notified
     */
    public POI(Address address, String name, int radius, boolean active, boolean notifyEmail, boolean notifyCellPhone, ArrayList<EventType> types) {
        this.poiIdentifier = -1;
        this.address = address;
        this.name = name;
        this.radius = radius;
        this.active = active;
        this.notifications = new Notifications(notifyEmail, notifyCellPhone, types);
    }

    /**
     * Get the unique identifier of this point of interest
     * 
     * @return the identifier
     * @throws IllegalAccessException when the identifier is not set
     */
    public int getPoiIdentifier() throws IllegalAccessException {
        if (!hasPoiIdentifier()) {
            throw new IllegalAccessException("POIIdentifier is not set!");
        }
        return poiIdentifier;
    }

    private boolean hasPoiIdentifier() {
        return poiIdentifier != -1;
    }

    /**
     * Set the unique identifier of this poi.
     * This can only be done once. 
     * From then the method will throw an IllegalArgumentException
     * 
     * @param poiIdentifier the unique identifier for this poi     
     */
    public void setPoiIdentifier(int poiIdentifier) {
        if (!hasPoiIdentifier()) {
            this.poiIdentifier = poiIdentifier;
        } else {
            throw new IllegalArgumentException("POIIdentifier is already set and cannot be changed!");
        }
    }

    /**
     * Get the location of the poi
     * 
     * @return the location as an address object
     * @see Address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Get the name of this poi, given by the user to recognize it
     * 
     * @return the name of the poi
     */
    public String getName() {
        return name;
    }

    /**
     * Get the radius of the poi
     * 
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Check if the poi is active
     * 
     * @return true if the poi is active, else false
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Get the notification settings of this poi
     * 
     * @return the notifications object of this poi
     * @see Notifications
     */
    public Notifications getNotifications() {
        return notifications;
    }
}
