package vop.groep7.vop7backend.database.mongodb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.bson.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import vop.groep7.vop7backend.AppConfig;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.EventFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;

/**
 *
 * @author Backend Team
 */
public class EventDAO extends MongoDBDAO {

    private static final String EVENT_DATABASE = "events";
    private static final String EVENT_COLLECTION = "event";
    private static final String EVENT_TYPE_COLLECTION = "event_type";
    private static final String EVENT_TYPE_XML = "eventtypes.xml";

    private final MongoDatabase eventDatabase;
    private final MongoCollection<BasicDBObject> events;
    private final MongoCollection<Document> eventTypes;

    private volatile long lastUsedId;

    /**
     * Create a EventDAO using a client to a database
     *
     * @param client A client object to a database
     */
    public EventDAO(MongoClient client) {
        super(client);
        eventDatabase = client.getDatabase(EVENT_DATABASE);
        events = eventDatabase.getCollection(EVENT_COLLECTION, BasicDBObject.class);
        eventTypes = eventDatabase.getCollection(EVENT_TYPE_COLLECTION);

        if (isEventTypeChanged()) {
            try {
                fillEventType();
            } catch (DataAccessException ex) {
                Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Can't initialize EventDAO.", ex);
                throw new RuntimeException("Can't create basic types.", ex);
            }
        }
        lastUsedId = getHighestId() + 1;
    }

    private long getHighestId() {
        BasicDBObject b = new BasicDBObject();
        b.put(ID, -1);
        MongoCursor<BasicDBObject> cursor = events.find().sort(b).limit(1).iterator();
        long id;
        if (cursor.hasNext()) {
            String lastId = cursor.next().getString(ID);
            id = Long.valueOf(lastId);
        } else {
            id = 0;
        }
        return id;
    }

    /**
     * Retrieve an event object from the database
     *
     * @param eventId the Id of the event
     * @see Event
     * @return an event object
     */
    public Event getEvent(int eventId) {
        BasicDBObject query = new BasicDBObject();
        query.put(ID, Integer.toString(eventId));
        try (MongoCursor<BasicDBObject> cursor = events.find(query).iterator()) {
            if (cursor.hasNext()) {
                return getEventFromJSON(cursor.next());
            }
        }
        return null;
    }

    /**
     * Get all the events present in the database
     *
     * @see Event
     * @return a collection of event objects
     */
    public List<Event> getEvents() {
        List<Event> result = new ArrayList<>();
        try (MongoCursor<BasicDBObject> cursor = events.find().iterator()) {
            while (cursor.hasNext()) {
                Event e = getEventFromJSON(cursor.next());
                if (e != null) {
                    result.add(e);
                }
            }
        }

        return result;
    }

    /**
     * Get all the events present in the database that are active or not
     * depending on the parameter
     *
     * @param active Whether the events we're looking for are active or not
     * @see Event
     * @return a collection of event objects
     */
    public List<Event> getEventsWithActiveParameter(boolean active) {
        List<Event> result = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.put(ACTIVE, active);
        try (MongoCursor<BasicDBObject> cursor = events.find(query).iterator()) {
            while (cursor.hasNext()) {
                Event e = getEventFromJSON(cursor.next());
                if (e != null) {
                    result.add(e);
                }
            }
        }

        return result;
    }

    private Event getEventFromJSON(BasicDBObject dbObject) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            APIEvent eventContainer = mapper.readValue(dbObject.toJson(), APIEvent.class);
            return EventFactory.toDomainModel(eventContainer);
        } catch (IOException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Json event could not be converted to event object!", ex);
            return null;
        }
    }

    /**
     * Get the json string of an event object
     *
     * @param event the event to be converted to json string
     * @return the json string
     */
    public String getJSONFromEvent(Event event) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(EventFactory.toAPIModel(event));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Event could not be converted to json event!", ex);
            return null;
        }
    }

    /**
     * Insert an event into the database
     *
     * @param event An event object representing the new event
     * @see Event
     * @return The new event object
     */
    public Event createEvent(Event event) {
        // insert event
        BasicDBObject dbObject = (BasicDBObject) JSON.parse(getJSONFromEvent(event));
        events.insertOne(dbObject);

        return event;
    }

    /**
     * Get an id for a new event
     *
     * @return the id for the event
     */
    public synchronized int getNewId() {
        lastUsedId++;
        return (int) lastUsedId;
    }

    /**
     * Get the type of event based on the id
     *
     * @param type The name of the type
     * @see EventType
     * @return The type of event
     */
    public EventType getEventType(String type) {
        if (type == null) {
            return null;
        }
        List<EventType> list = getEventTypes();
        for (EventType eventType : list) {
            if (eventType.getType().equalsIgnoreCase(type)) {
                return eventType;
            }
        }
        return null;
    }

    /**
     * Modify an event from the database
     *
     * @param eventId The Id of the event
     * @param event An event object representing the event
     * @see Event
     * @return The modified event
     */
    public Event modifyEvent(int eventId, Event event) {
        BasicDBObject query = new BasicDBObject();
        query.put(ID, Integer.toString(eventId));

        BasicDBObject dbObject = (BasicDBObject) JSON.parse(getJSONFromEvent(event));
        events.replaceOne(query, dbObject);

        return event;
    }

    /**
     * Get all the event types present in the database
     *
     * @see EventType
     * @return a collection of event types
     */
    public List<EventType> getEventTypes() {
        List<EventType> result = new ArrayList<>();

        try (MongoCursor<Document> cursor = eventTypes.find().iterator()) {
            while (cursor.hasNext()) {
                result.add(getEventTypeFromDocument(cursor.next()));
            }
        }

        return result;
    }

    private EventType getEventTypeFromDocument(Document doc) {
        return EventTypeFactory.build(doc.getString(TYPE));
    }

    private Document getDocumentFromEventType(EventType type) {
        Document doc = new Document();

        if (type.getType() != null && !type.getType().isEmpty()) {
            doc.put(TYPE, type.getType());
        }

        return doc;
    }

    private boolean isEventTypeChanged() {
        return eventTypes.count() == 0;
    }

    private void fillEventType() throws DataAccessException {
        eventTypes.deleteMany(new Document());

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(ClassLoader.getSystemResourceAsStream(EVENT_TYPE_XML));

            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    EventType type = null;
                    NodeList childNodes = node.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node cNode = childNodes.item(j);
                        if (cNode instanceof Element) {
                            String content = cNode.getTextContent().trim();
                            switch (cNode.getNodeName()) {
                                case TYPE:
                                    type = new EventType(content);
                                    break;
                            }
                        }
                    }
                    if (type != null) {
                        Document doc = getDocumentFromEventType(type);
                        eventTypes.insertOne(doc);
                    }
                }

            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Can't create basic event types.", ex);
            throw new DataAccessException("Can't create event types", ex);
        }

    }

    /**
     * Get the events are one of the provided types
     *
     * @param types An array of types
     * @see Event
     * @return A list of events
     */
    public List<Event> getUserEvents(List<EventType> types) {
        List<Event> result = new ArrayList<>();

        types.stream().map((type) -> {
            BasicDBObject query = new BasicDBObject();
            query.put(TYPE + "." + TYPE, type.getType());
            return query;
        }).forEach((query) -> {
            try (MongoCursor<BasicDBObject> cursor = events.find(query).iterator()) {
                while (cursor.hasNext()) {
                    Event e = getEventFromJSON(cursor.next());

                    if (e != null && !result.contains(e)) {
                        result.add(e);
                    }
                }
            }
        });

        return result;
    }

    /**
     * Delete an event based on an Id
     *
     * @param eventId the Id of the event
     * @return True if the deletion succeeded
     */
    public boolean deleteEvent(int eventId) {
        BasicDBObject query = new BasicDBObject();
        query.put(ID, Integer.toString(eventId));
        return events.deleteOne(query).wasAcknowledged();
    }

    /**
     * Get id of the event if it exists
     *
     * @param event the event for which the id has to be returned
     * @param onlyActive Whether or not we only look for active events
     * @return id of the event, else -1
     */
    public int getIdByEvent(Event event, boolean onlyActive) {
        BasicDBObject query = new BasicDBObject();

        double latOffsetSmall = Double.parseDouble(AppConfig.getApplicationProperty("offset.latitude.small"));
        double lonOffsetSmall = Double.parseDouble(AppConfig.getApplicationProperty("offset.longitude.small"));
        double latOffsetLarge = Double.parseDouble(AppConfig.getApplicationProperty("offset.latitude.large"));
        double lonOffsetLarge = Double.parseDouble(AppConfig.getApplicationProperty("offset.longitude.large"));

        QueryBuilder builder = new QueryBuilder();

        if (event.getType() != null) {
            builder.and(TYPE + "." + TYPE).is(event.getType().getType());
        } else {
            builder.and(TYPE + "." + TYPE).exists(false);
        }

        if (onlyActive) {
            builder.and(ACTIVE).is(true);
        }

        if (event.getSource() != null) {
            builder.and(SRC + "." + SRC_NAME).is(event.getSource().getName());
        } else {
            builder.and(SRC + "." + SRC_NAME).exists(false);
        }

        if (event.getLocation() != null && event.getLocation().getCoordinates() != null) {
            if (event.getLocation().getStreet() != null && event.getLocation().getCity().getCity() != null && event.getLocation().getCity().getCountry() != null) {
                builder.and(STREET).is(event.getLocation().getStreet());
                builder.and(CITY).is(event.getLocation().getCity().getCity());
                builder.and(COUNTRY).is(event.getLocation().getCity().getCountry());
                builder.and(LOCATION_COORD + "." + LATITUDE).lessThanEquals(event.getLocation().getCoordinates().getLat() + latOffsetLarge);
                builder.and(LOCATION_COORD + "." + LATITUDE).greaterThanEquals(event.getLocation().getCoordinates().getLat() - latOffsetLarge);
                builder.and(LOCATION_COORD + "." + LONGITUDE).lessThanEquals(event.getLocation().getCoordinates().getLon() + lonOffsetLarge);
                builder.and(LOCATION_COORD + "." + LONGITUDE).greaterThanEquals(event.getLocation().getCoordinates().getLon() - lonOffsetLarge);
            } else {
                builder.and(LOCATION_COORD + "." + LATITUDE).lessThanEquals(event.getLocation().getCoordinates().getLat() + latOffsetSmall);
                builder.and(LOCATION_COORD + "." + LATITUDE).greaterThanEquals(event.getLocation().getCoordinates().getLat() - latOffsetSmall);
                builder.and(LOCATION_COORD + "." + LONGITUDE).lessThanEquals(event.getLocation().getCoordinates().getLon() + lonOffsetSmall);
                builder.and(LOCATION_COORD + "." + LONGITUDE).greaterThanEquals(event.getLocation().getCoordinates().getLon() - lonOffsetSmall);
            }
        } else {
            builder.and(LOCATION_COORD + "." + LATITUDE).exists(false);
            builder.and(LOCATION_COORD + "." + LATITUDE).exists(false);
            builder.and(LOCATION_COORD + "." + LONGITUDE).exists(false);
            builder.and(LOCATION_COORD + "." + LONGITUDE).exists(false);
        }

        query.putAll(builder.get());

        // return id of existing event else -1
        MongoCursor<BasicDBObject> it = events.find(query).iterator();
        if (it.hasNext()) {
            return Integer.valueOf(it.next().getString(ID));
        } else {
            return -1;
        }
    }

    /**
     * Get the events by giving the event identifiers
     *
     * @param eventIds the list of event identifiers
     * @return the events for the event identifiers
     */
    public List<Event> getEventsByIds(List<Integer> eventIds) {
        // convert ids to string
        List<String> ids = new ArrayList<>();
        eventIds.stream().forEach((id) -> {
            ids.add(String.valueOf(id));
        });

        List<Event> res = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.putAll(new QueryBuilder()
                .and(ID).in(ids)
                .get());

        MongoCursor<BasicDBObject> cursor = events.find(query).iterator();
        while (cursor.hasNext()) {
            Event e = getEventFromJSON(cursor.next());
            res.add(e);
        }
        return res;
    }
}
