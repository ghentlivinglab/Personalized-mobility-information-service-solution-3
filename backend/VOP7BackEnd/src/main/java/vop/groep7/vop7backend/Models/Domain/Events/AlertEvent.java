package vop.groep7.vop7backend.Models.Domain.Events;

import java.sql.Timestamp;
import java.util.ArrayList;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.Domain.Transport;

/**
 *
 * @author Backend Team
 */
public class AlertEvent extends Event {

     /**
     * The public constructor with all fields required to create an AlertEvent object
     * 
     * @param eventIdentifier A unique identifier of the event
     * @param location Address object of the location of the event
     * @param active Whether the event is active or not
     * @param source A Source object that contains the URL of the icon
     * @param description A description of the event
     * @param publicationTime A Timestamp object of the time the Event was published
     * @param lastEditTime A Timestamp object of the time the Event was last edited
     * @param transportTypes A list of Transport objects that are relevant for this type of event
     * @param type An EventType object that contains the type of this event
     */
    public AlertEvent(int eventIdentifier, Address location, boolean active, Source source, String description, Timestamp publicationTime, Timestamp lastEditTime, EventType type, ArrayList<Transport> transportTypes) {
        super(eventIdentifier, location, active, source, description, publicationTime, lastEditTime, type, transportTypes);
    }
}
