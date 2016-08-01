package vop.groep7.vop7backend.Controllers;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APIJam;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.Jam;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.JamFactory;
import vop.groep7.vop7backend.factories.SourceFactory;
import vop.groep7.vop7backend.factories.TransportFactory;

/**
 *
 * @author Backend Team
 */
public class EventController extends Controller {

    @Autowired
    private Processor processor;

    /**
     * Get an APIEvent object based on it's Id
     *
     * @param eventId Id of an api event
     * @see APIEvent
     * @return An event object
     */
    public APIEvent getAPIEvent(int eventId) {
        Event event = eventCache.getIfPresent(eventId);
        if (event == null) {
            // get event from dao
            event = getEventDAO().getEvent(eventId);
            if (event == null) {
                return null;
            }
            // load event into cache
            eventCache.put(eventId, event);
        }
        return EventFactory.toAPIModel(event);
    }

    /**
     * Get all APIEvent objects
     *
     * @see APIEvent
     * @return A list of all api events
     */
    public List<APIEvent> getAPIEvents() {
        // get events from dao
        List<Event> events = getEventDAO().getEvents();

        return eventListToAPI(events);
    }

    /**
     * Get all APIEvent objects of a user
     *
     * @param userId The id of a user
     * @see APIEvent
     * @return A list of all api events of a user
     * @throws DataAccessException This is thrown when no connection to the
     * database can be made
     */
    public List<APIEvent> getUserAPIEvents(int userId) throws DataAccessException {
        // get the ids of the matched events for the user
        List<Integer> eventIds = getUserDAO().getMatchedEvents(userId);
        // get the events by their ids
        List<Event> events = getEventDAO().getEventsByIds(eventIds);

        return eventListToAPI(events);
    }

    /**
     * Insert an APIEvent object in the database
     *
     * @param event An api event object that has to be created
     * @see APIEvent
     * @return The created api event object
     */
    public APIEvent createAPIEvent(APIEvent event) {
        Address newAddress = AddressFactory.create(event.getCity(), 0, event.getStreet(), null, event.getCountry(), cutOffCoordinates(CoordinateFactory.toDomainModel(event.getCoordinates())));
        Source newSource = SourceFactory.toDomainModel(event.getSource());
        ArrayList<Transport> transports = new ArrayList<>();

        APITransport[] types = event.getRelevantForTransportationTypes();
        if (types != null) {
            for (APITransport t : types) {
                transports.add(TransportFactory.toDomain(t));
            }
        }
        ArrayList<Jam> jams = null;
        if (event.getJams() != null) {
            jams = new ArrayList<>();
            for (APIJam jam : event.getJams()) {
                jams.add(JamFactory.toDomainModel(jam));
            }
        }

        EventType type;
        if (event.getType() != null) {
            type = EventTypeFactory.toDomainModel(event.getType());
        } else {
            type = new EventType("Misc");
        }
        // get new event identifier from dao and create new event
        Event newEvent = EventFactory.create(getEventDAO().getNewId(), newAddress, event.isActive(), newSource, event.getDescription(), transports, jams, type);

        if (getEventDAO().getIdByEvent(newEvent, false) != -1) {
            return null;
        }

        // create event in db
        getEventDAO().createEvent(newEvent);

        // load event into cache
        eventCache.put(newEvent.getEventIdentifier(), newEvent);

        // process new event
        processor.process(newEvent);

        return EventFactory.toAPIModel(newEvent);
    }

    /**
     * Modify a APIEvent object in the database
     *
     * @param eventId Id of an event
     * @param event An api event object that has to be modified
     * @see APIEvent
     * @return The modified api event object
     * @throws DataAccessException This is thrown when no connection to the
     * database can be made
     */
    public APIEvent modifyAPIEvent(int eventId, APIEvent event) throws DataAccessException {
        Address address = new Address(event.getCity(), 0, event.getStreet(), null, event.getCountry(), cutOffCoordinates(CoordinateFactory.toDomainModel(event.getCoordinates())));
        Source source = SourceFactory.toDomainModel(event.getSource());
        EventType type = EventTypeFactory.toDomainModel(event.getType());
        ArrayList<Transport> transports = new ArrayList<>();
        for (APITransport t : event.getRelevantForTransportationTypes()) {
            transports.add(TransportFactory.toDomain(t));
        }
        ArrayList<Jam> jams = null;
        if (event.getJams() != null) {
            jams = new ArrayList<>();
            for (APIJam jam : event.getJams()) {
                jams.add(JamFactory.toDomainModel(jam));
            }
        }

        Event oldEvent = getEventDAO().getEvent(eventId);

        Timestamp publicationTimestamp = oldEvent.getPublicationTime();
        Timestamp editTimestamp = new Timestamp(System.currentTimeMillis());

        Event modifiedEvent = EventFactory.build(eventId, address, event.isActive(), source, event.getDescription(), publicationTimestamp, editTimestamp, type, transports, jams);
        
        // if event in cache: invalidate
        if (eventCache.getIfPresent(eventId) != null) {
            eventCache.invalidate(eventId);
        }
        // load modified event into cache
        eventCache.put(eventId, modifiedEvent);
        // modify event in db
        getEventDAO().modifyEvent(eventId, modifiedEvent);

        if (modifiedEvent.isActive()) {
            // process modified event
            processor.process(modifiedEvent);
        } else {
            getUserDAO().deleteMatchedEvent(eventId);
        }

        return EventFactory.toAPIModel(modifiedEvent);
    }

    /**
     * Delete an event in the database
     *
     * @param eventId The id of the api event object
     * @return true if the api event is successfully deleted
     */
    public boolean deleteAPIEvent(int eventId) {
        Event event = eventCache.getIfPresent(eventId);
        // if event in cache: delete
        if (event != null) {
            eventCache.invalidate(eventId);
        }

        try {
            // delete matches with users
            getUserDAO().deleteMatchedEvent(eventId);
        } catch (DataAccessException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, "Event could not be deleted: removal of matched users failed", ex);
            return false;
        }

        // delete event in db
        return getEventDAO().deleteEvent(eventId);
    }

    /**
     * Get all APIEvent types
     *
     * @see APIEventType
     * @return A list of all api event types
     */
    public List<APIEventType> getAPIEventTypes() {
        // get event types from dao
        List<EventType> eventTypes = getEventDAO().getEventTypes();

        // D2A all eventtypes
        List<APIEventType> res = new ArrayList<>();
        eventTypes.stream().forEach((e) -> {
            res.add(EventTypeFactory.toAPIModel(e));
        });

        return res;
    }

    /**
     * Get id of APIEvent if it exists
     *
     * @param event the API event for which the id has to be returned
     * @see APIEvent
     * @return id of the event, else -1
     */
    public int getIdByAPIEvent(APIEvent event) {
        return getEventDAO().getIdByEvent(EventFactory.toDomainModel(event), true);
    }

    private Coordinate cutOffCoordinates(Coordinate c) {
        double lat = new BigDecimal(c.getLat())
                .setScale(6, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        double lon = new BigDecimal(c.getLon())
                .setScale(6, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        return CoordinateFactory.build(lat, lon);
    }

    /**
     * Get all inactive events
     *
     * @return A list of inactive events
     */
    public List<APIEvent> getInactiveAPIEvents() {
        // get events from dao
        List<Event> events = getEventDAO().getEventsWithActiveParameter(false);

        return eventListToAPI(events);
    }

    /**
     * Get all active events
     *
     * @return A list of active events
     */
    public List<APIEvent> getActiveAPIEvents() {
        // get events from dao
        List<Event> events = getEventDAO().getEventsWithActiveParameter(true);

        return eventListToAPI(events);
    }

    private List<APIEvent> eventListToAPI(List<Event> events) {
        List<APIEvent> res = new ArrayList<>();
        events.stream().forEach((e) -> {
            res.add(EventFactory.toAPIModel(e));
        });

        return res;
    }
}
