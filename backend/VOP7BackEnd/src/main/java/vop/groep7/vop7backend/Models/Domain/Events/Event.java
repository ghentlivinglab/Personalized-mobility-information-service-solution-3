package vop.groep7.vop7backend.Models.Domain.Events;

import java.sql.Timestamp;
import java.util.ArrayList;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.DomainModel;
import vop.groep7.vop7backend.Models.Domain.Transport;

/**
 *
 * @author Backend Team
 */
public abstract class Event extends DomainModel {

    private final int eventIdentifier;

    private final Address location;
    private boolean active;
    private final Timestamp publicationTime;
    private final Timestamp lastEditTime;
    private final String description;
    private final EventType type;
    private final Source source;

    /**
     *
     */
    protected final ArrayList<Transport> transportTypes;

    /**
     * The public constructor with all fields required to create an Event object
     * 
     * @param eventIdentifier the unique identifier of the event
     * @param location the address of where the event is located
     * @param active boolean that indicates if the event is active
     * @param source the source of the event
     * @param description the description of the event
     * @param transportTypes a list of transport types for which this event is
     * relevant
     * @param type the type of this event
     * @param publicationTime the publication time and date of this event
     * @param lastEditTime the time and date of when the event is last edited
     */
    public Event(int eventIdentifier, Address location, boolean active, Source source, String description, Timestamp publicationTime, Timestamp lastEditTime, EventType type, ArrayList<Transport> transportTypes) {
        this.eventIdentifier = eventIdentifier;
        this.location = location;
        this.source = source;
        this.description = description;
        if(publicationTime != null) {
            this.publicationTime = publicationTime;
        } else {
            this.publicationTime = new Timestamp(System.currentTimeMillis());
        }
        if(lastEditTime != null) {
            this.lastEditTime = lastEditTime;
        } else {
            this.lastEditTime = new Timestamp(System.currentTimeMillis());
        }
        this.active = active;
        this.type = type;
        this.transportTypes = transportTypes;
    }

    /**
     * Get the unique identifier of the event
     *
     * @return identifier
     */
    public int getEventIdentifier() {
        return eventIdentifier;
    }

    /**
     * Check if the event is active
     *
     * @return true if active, else false
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Mark the event as finished
     *
     */
    public void finish() {
        active = false;
        update();
    }

    /**
     * Get the location of the event
     *
     * @return the location
     * @see Address
     */
    public Address getLocation() {
        return location;
    }

    /**
     * Get the description of this event
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the source of the event
     *
     * @return the source
     * @see Source
     */
    public Source getSource() {
        return source;
    }

    /**
     * Get the publication timestamp of the event
     *
     * @return the publication time
     * @see Timestamp
     */
    public Timestamp getPublicationTime() {
        return publicationTime;
    }

    /**
     * Get the last edit timestamp of the event
     *
     * @return the publication time
     * @see Timestamp
     */
    public Timestamp getLastEditTime() {
        return lastEditTime;
    }

    protected void update() {
        lastEditTime.setTime(System.currentTimeMillis());
    }

    /**
     * Get the type of event
     *
     * @return the event type
     * @see EventType
     */
    public EventType getType() {
        return type;
    }

    /**
     * Get the list of transportation types for which this event is relevant
     *
     * @return the list of transportation types
     * @see Transport
     */
    public ArrayList<Transport> getRelevantTransportationTypes() {
        return (ArrayList<Transport>) transportTypes.clone();
    }

    /**
     * Add a transportation type to the list of transportation types
     *
     * @param transport the transportation type to be added
     * @see Transport
     */
    public void addTransportationType(Transport transport) {
        transportTypes.add(transport);
        update();
    }

    /**
     * Remove a transportation type from the list of transportation types
     *
     * @param transport the transportation type to be removed
     * @see Transport
     */
    public void removeTransportationType(Transport transport) {
        transportTypes.remove(transport);
        update();
    }
    
    
}
