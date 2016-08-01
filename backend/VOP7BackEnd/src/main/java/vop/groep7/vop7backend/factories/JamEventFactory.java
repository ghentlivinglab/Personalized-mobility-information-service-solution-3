package vop.groep7.vop7backend.factories;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIJam;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.JamEvent;
import vop.groep7.vop7backend.Models.Domain.Jam;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.Domain.Transport;
import static vop.groep7.vop7backend.factories.Factory.getLinks;

/**
 *
 * @author Backend Team
 */
public class JamEventFactory extends Factory {

    /**
     * Create a new JamEvent object
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
     * @return A new JamEvent object
     */
    public static JamEvent create(int eventIdentifier, Address location, boolean active, Source source, String description, ArrayList<Transport> transportTypes, ArrayList<Jam> jams, EventType type) {
        JamEvent event = new JamEvent(eventIdentifier, location, active, source, description, null, null, type, transportTypes, jams);
        return event;
    }

    /**
     * Build a valid JamEvent object
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
     * @return A valid JamEvent object
     */
    public static JamEvent build(Integer eventIdentifier, Address location, boolean active, Source source, String description, Timestamp publicationTime, Timestamp lastEditTime, EventType type, ArrayList<Transport> transportTypes, ArrayList<Jam> jams) {
        JamEvent event = new JamEvent(eventIdentifier, location, active, source, description, publicationTime, lastEditTime, type, transportTypes, jams);
        return event;
    }

    /**
     * Create an APIEvent object starting from a JamEvent
     *
     * @param event A JamEvent object
     * @return An APIEvent object
     */
    public static APIEvent toAPIModel(JamEvent event) {
        APIEvent result = new APIEvent();
        result.setActive(event.isActive());
        result.setCoordinates(CoordinateFactory.toAPIModel(event.getLocation().getCoordinates()));
        if(event.getLocation().getCity() != null) {
            result.setCity(event.getLocation().getCity().getCity());
            result.setCountry(event.getLocation().getCity().getCountry());
        }
        result.setStreet(event.getLocation().getStreet());
        result.setDescription(event.getDescription());
        APIJam[] jamList = new APIJam[event.getJams().size()];
        if (event.getJams() != null) {
            for (int i = 0; i < jamList.length; i++) {
                jamList[i] = JamFactory.toAPIModel(event.getJams().get(i));
            }
        }
        result.setJams(jamList);
        if (event.getLastEditTime() != null) {
            result.setLastEditTime(event.getLastEditTime().toString());
        }
        if (event.getPublicationTime() != null) {
            result.setPublicationTime(event.getPublicationTime().toString());
        }
        APITransport[] transportList = new APITransport[event.getRelevantTransportationTypes().size()];
        if (event.getRelevantTransportationTypes() != null) {
            for (int i = 0; i < transportList.length; i++) {
                transportList[i] = TransportFactory.toAPIModel(event.getRelevantTransportationTypes().get(i));
            }
        }
        result.setRelevantForTransportationTypes(transportList);
        if (event.getSource() != null) {
            result.setSource(SourceFactory.toAPIModel(event.getSource()));
        }
        if (event.getType() != null) {
            result.setType(EventTypeFactory.toAPIModel(event.getType()));
        }
        result.setId(String.valueOf(event.getEventIdentifier()));
        result.setLinks(getLinks(EVENT + event.getEventIdentifier()));
        return result;
    }

    /**
     * Create a JamEvent object starting from an APIEvent
     *
     * @param event An APIEvent object
     * @return A JamEvent object
     */
    public static JamEvent toDomainModel(APIEvent event) {
        Timestamp publicationTimestamp = null;
        Timestamp editTimestamp = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date publicationDate, editDate;
            if (event.getPublicationTime() != null) {
                publicationDate = sdf.parse(event.getPublicationTime());
                publicationTimestamp = new java.sql.Timestamp(publicationDate.getTime());
            }
            if (event.getLastEditTime() != null) {
                editDate = sdf.parse(event.getLastEditTime());
                editTimestamp = new java.sql.Timestamp(editDate.getTime());
            }
        } catch (Exception ex) {
            Logger.getLogger(JamEventFactory.class.getName()).log(Level.SEVERE, "Could not format publication time or edit time.", ex);
            publicationTimestamp = null;
            editTimestamp = null;
        }

        Address address = new Address(event.getCity(), 0, event.getStreet(), null, event.getCountry(), CoordinateFactory.toDomainModel(event.getCoordinates()));
        ArrayList<Transport> transports = new ArrayList<>();
        if (event.getRelevantForTransportationTypes() != null) {
            for (APITransport t : event.getRelevantForTransportationTypes()) {
                transports.add(TransportFactory.toDomain(t));
            }
        }
        ArrayList<Jam> jams = new ArrayList<>();
        if (event.getJams() != null) {
            for (APIJam jam : event.getJams()) {
                jams.add(JamFactory.toDomainModel(jam));
            }
        }
        Source source = null;
        if (event.getSource() != null) {
            source = SourceFactory.toDomainModel(event.getSource());
        }
        EventType type = null;
        if (event.getType() != null) {
            type = EventTypeFactory.toDomainModel(event.getType());
        }
        Integer id = null;
        if (event.getId() != null) {
            id = Integer.valueOf(event.getId());
        }
        JamEvent result = new JamEvent(id, address, event.isActive(), source, event.getDescription(), publicationTimestamp, editTimestamp, type, transports, jams);
        return result;
    }
}
