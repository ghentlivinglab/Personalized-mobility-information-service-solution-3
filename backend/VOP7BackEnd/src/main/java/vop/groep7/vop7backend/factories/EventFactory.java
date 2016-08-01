package vop.groep7.vop7backend.factories;

import java.sql.Timestamp;
import java.util.ArrayList;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.AlertEvent;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.Events.JamEvent;
import vop.groep7.vop7backend.Models.Domain.Jam;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.Domain.Transport;

/**
 *
 * @author Backend Team
 */
public class EventFactory extends Factory {

    /**
     * Create a new Event object
     * 
     * @param eventIdentifier A unique identifier of the event
     * @param location Address object of the location of the event
     * @param active Whether the event is active or not
     * @param source A Source object that contains the URL of the icon
     * @param description A description of the event
     * @param transportTypes A list of Transport objects that are relevant for
     * this type of event
     * @param jams A list of Jam objects
     * @param type An EventType object that contains the type of this event
     * @return A new JamEvent or AlertEvent object
     */
    public static Event create(int eventIdentifier, Address location, boolean active, Source source, String description, ArrayList<Transport> transportTypes, ArrayList<Jam> jams, EventType type) {
        if (jams != null) {
            // create JamEvent
            return JamEventFactory.create(eventIdentifier, location, active, source, description, transportTypes, jams, type);
        } else {
            // create AlertEvent
            return AlertEventFactory.create(eventIdentifier, location, active, source, description, transportTypes, type);
        }

    }

    /**
     * Build a valid Event object
     * 
     * @param eventIdentifier A unique identifier of the event
     * @param location Address object of the location of the event
     * @param active Whether the event is active or not
     * @param source A Source object that contains the URL of the icon
     * @param description A description of the event
     * @param publicationTime A Timestamp object of the time the Event was
     * published
     * @param lastEditTime A Timestamp object of the time the Event was last
     * edited
     * @param transportTypes A list of Transport objects that are relevant for
     * this type of event
     * @param jams A list of Jam objects
     * @param type An EventType object that contains the type of this event
     * @return A valid JamEvent or AlertEvent object
     */
    public static Event build(int eventIdentifier, Address location, boolean active, Source source, String description, Timestamp publicationTime, Timestamp lastEditTime, EventType type, ArrayList<Transport> transportTypes, ArrayList<Jam> jams) {
        if (jams != null) {
            // build JamEvent
            return JamEventFactory.build(eventIdentifier, location, active, source, description, publicationTime, lastEditTime, type, transportTypes, jams);
        } else {
            // build AlertEvent
            return AlertEventFactory.build(eventIdentifier, location, active, source, description, publicationTime, lastEditTime, type, transportTypes);
        }
    }

    /**
     * Create an APIEvent object starting from an Event
     * 
     * @param event An Event object
     * @return An APIEvent object
     */
    public static APIEvent toAPIModel(Event event) {
        if (event instanceof JamEvent) {
            JamEvent result = (JamEvent) event;
            return JamEventFactory.toAPIModel(result);
        } else if (event instanceof AlertEvent) {
            AlertEvent result = (AlertEvent) event;
            return AlertEventFactory.toAPIModel(result);
        }
        return null;
    }

    /**
     * Create an Event object starting from an APIEvent
     *
     * @param event An APIEvent object
     * @return An Event object
     */
    public static Event toDomainModel(APIEvent event) {
        if (event.getJams() != null) {
            return JamEventFactory.toDomainModel(event);
        } else {
            return AlertEventFactory.toDomainModel(event);
        }
    }

}
