package Database.mongodb;

import static Other.TestUtils.compareEventTypes;
import static Other.TestUtils.compareEvents;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.Jam;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.mongodb.EventDAO;
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
public class EventDAOIT {

    private EventDAO eventDAO;
    private Fongo fongo;
    private DB db;
    private DBCollection eventCollection;
    private DBCollection eventTypeCollection;

    private Event expectedEvent;
    private Event otherEvent;
    private final List<Event> expectedEvents;

    private EventType expectedEventType;
    private final List<EventType> expectedEventTypes;

    public EventDAOIT() {
        expectedEvents = new ArrayList<>();
        expectedEventTypes = new ArrayList<>();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        fongo = new Fongo("MongoDBTest");
        db = fongo.getDB("events");
        eventCollection = db.getCollection("event");
        eventTypeCollection = db.getCollection("event_type");

        eventDAO = new EventDAO(fongo.getMongo());
        createExpectedEvent();
        createExpectedEvents();
        createEvent();

        createExpectedEventType();
        createExpectedEventTypes();
    }

    @After
    public void tearDown() {
    }

    private void createExpectedEvent() {
        Coordinate coordinates = CoordinateFactory.build(51.043368, 3.779090);
        Address location = AddressFactory.build(null, 0, null, null, null, coordinates);
        Source source = SourceFactory.build("TestEvent", "http://www.example.org/random_icon_url.png");
        Timestamp publicationTime = Timestamp.valueOf("2016-04-06 19:15:44.926");
        Timestamp lastEditTime = Timestamp.valueOf("2016-04-06 19:15:44.926");
        EventType type = EventTypeFactory.build("RANDOM_TEST_EVENT");
        ArrayList<Transport> transportTypes = new ArrayList<>();
        Transport transport1 = TransportFactory.build("car");
        Transport transport2 = TransportFactory.build("bus");
        transportTypes.add(transport1);
        transportTypes.add(transport2);
        Coordinate start = CoordinateFactory.build(51.075762, 3.721739);
        Coordinate end = CoordinateFactory.build(51.035747, 3.765745);
        ArrayList<Jam> jams = new ArrayList<>();
        Jam jam = JamFactory.build(start, end, 10, 2987);
        jams.add(jam);
        expectedEvent = EventFactory.build(0, location, true, source, "Dit is een test event.", publicationTime, lastEditTime, type, transportTypes, jams);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json1 = mapper.writeValueAsString(EventFactory.toAPIModel(expectedEvent));
            DBObject dbObject1 = (DBObject) JSON.parse(json1);
            eventCollection.insert(dbObject1);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Event could not be converted to json event!", ex);
        }
    }

    private void createExpectedEvents() {
        expectedEvents.add(expectedEvent);
    }

    private void createEvent() {
        Coordinate coordinates = CoordinateFactory.build(53.043368, 2.779090);
        Address location = AddressFactory.build(null, 0, null, null, null, coordinates);
        Source source = SourceFactory.build("TestEvent2", "http://www.example.org/random2_icon_url.png");
        Timestamp publicationTime = Timestamp.valueOf("2016-04-06 19:15:45.926");
        Timestamp lastEditTime = Timestamp.valueOf("2016-04-06 19:15:45.926");
        EventType type = EventTypeFactory.build("RANDOM_TEST_EVENT2");
        ArrayList<Transport> transportTypes = new ArrayList<>();
        Transport transport1 = TransportFactory.build("car");
        Transport transport2 = TransportFactory.build("bike");
        transportTypes.add(transport1);
        transportTypes.add(transport2);
        Coordinate start = CoordinateFactory.build(53.075762, 4.721739);
        Coordinate end = CoordinateFactory.build(53.035747, 4.765745);
        ArrayList<Jam> jams = new ArrayList<>();
        Jam jam = JamFactory.build(start, end, 21, 3000);
        jams.add(jam);
        otherEvent = EventFactory.build(1, location, false, source, "Dit is een test 2 event.", publicationTime, lastEditTime, type, transportTypes, jams);
    }

    private void createExpectedEventType() {
        expectedEventType = EventTypeFactory.build("Accident");
    }

    private void createExpectedEventTypes() {
        expectedEventTypes.add(expectedEventType);
        expectedEventTypes.add(EventTypeFactory.build("Jam"));
        expectedEventTypes.add(EventTypeFactory.build("Weatherhazard"));
        expectedEventTypes.add(EventTypeFactory.build("Hazard"));
        expectedEventTypes.add(EventTypeFactory.build("Misc"));
        expectedEventTypes.add(EventTypeFactory.build("Construction"));
        expectedEventTypes.add(EventTypeFactory.build("Road_closed"));
    }

    @Test
    public void getEvent_shouldReturnEvent() {
        Event e = eventDAO.getEvent(0);
        assertNotNull(e);
        System.out.println("\n\n\n" + expectedEvent.getDescription() + "\n\n\n");
        assertTrue("The id of the returned event is incorrect!", expectedEvent.getEventIdentifier() == (e.getEventIdentifier()));
        assertTrue("The expected event was not returned!", compareEvents(e, expectedEvent));
    }

    @Test
    public void getEvent_shouldReturnNull() {
        Event e = eventDAO.getEvent(666);
        assertNull(e);
    }

    @Test
    public void getEvents_shouldReturnEvents() {
        List<Event> events = eventDAO.getEvents();
        assertEquals(expectedEvents.size(), events.size());
        for (int i = 0; i < expectedEvents.size(); i++) {
            assertNotNull(events.get(i));
            assertTrue("The id of this event is incorrect!", expectedEvents.get(i).getEventIdentifier() == (events.get(i).getEventIdentifier()));
            assertTrue("The expected event was not returned!", compareEvents(events.get(i), expectedEvents.get(i)));
        }
    }

    @Test
    public void getEventsWithActiveParameterTrue_shouldReturnActiveEvents() {
        //Make sure there are 2 events in the collection
        eventDAO.createEvent(otherEvent);

        List<Event> events = eventDAO.getEvents();
        assertEquals(expectedEvents.size() + 1, events.size());

        List<Event> activeEvents = eventDAO.getEventsWithActiveParameter(true);
        assertEquals(expectedEvents.size(), activeEvents.size());
        for (int i = 0; i < expectedEvents.size(); i++) {
            assertNotNull(activeEvents.get(i));
            assertTrue("The id of this event is incorrect!", expectedEvents.get(i).getEventIdentifier() == (activeEvents.get(i).getEventIdentifier()));
            assertTrue("The expected event was not returned!", compareEvents(activeEvents.get(i), expectedEvents.get(i)));
        }
    }

    @Test
    public void getEventsWithActiveParameterFalse_shouldReturnActiveEvents() {
        //Make sure there are 2 events in the collection
        eventDAO.createEvent(otherEvent);
        expectedEvents.add(otherEvent);
        expectedEvents.remove(expectedEvent);

        List<Event> events = eventDAO.getEvents();
        assertEquals(expectedEvents.size() + 1, events.size());

        List<Event> inactiveEvents = eventDAO.getEventsWithActiveParameter(false);
        assertEquals(expectedEvents.size(), inactiveEvents.size());
        for (int i = 0; i < expectedEvents.size(); i++) {
            assertNotNull(inactiveEvents.get(i));
            assertTrue("The id of this event is incorrect!", expectedEvents.get(i).getEventIdentifier() == (inactiveEvents.get(i).getEventIdentifier()));
            assertTrue("The expected event was not returned!", compareEvents(inactiveEvents.get(i), expectedEvents.get(i)));
        }
    }

    @Test
    public void createEvent_shouldCreateCorrect() throws DataAccessException {
        Event e = eventDAO.createEvent(otherEvent);
        assertNotNull(e);
        assertTrue("The id of the returned event is incorrect!", otherEvent.getEventIdentifier() == (e.getEventIdentifier()));
        assertTrue("The expected event was not returned!", compareEvents(e, otherEvent));
        assertTrue("The size of events is not right!", eventCollection.count() == 2);
    }

    @Test
    public void deleteEvent_shouldDeleteCorrect() throws DataAccessException {
        assertTrue(eventDAO.deleteEvent(0));
    }

    @Test
    public void deleteEvent_shouldDeleteFalse() throws DataAccessException {
        assertTrue(eventDAO.deleteEvent(100));
    }

    @Test
    public void modifyEvent_shouldModifyCorrect() throws DataAccessException {
        Event e = eventDAO.modifyEvent(0, otherEvent);
        assertNotNull(e);
        assertTrue("The expected event was not returned!", compareEvents(e, otherEvent));
        assertTrue("The size of events is not right!", eventCollection.count() == 1);
    }

    @Test
    public void getUserEvents_shouldReturnEvents() {
        String json2 = "{ \"publication_time\": \"2016-04-06 19:15:45.926\", \"active\": false, \"last_edit_time\": \"2016-04-06 19:15:45.926\", \"type\": { \"type\": \"RANDOM_TEST_EVENT2\" }, \"coordinates\": { \"lon\": \"2.779090\", \"lat\": \"53.043368\" }, \"description\": \"Dit is een test 2 event.\", \"relevant_for_transportation_types\": [ \"train\", \"bike\" ], \"id\": \"1\", \"links\": [ { \"href\": \"https://vopro7.ugent.be/api/event/1\", \"rel\": \"self\" } ], \"jams\": [ { \"delay\": 3000, \"end_node\": { \"lon\": \"4.765745\", \"lat\": \"53.035747\" }, \"start_node\": { \"lon\": \"4.721739\", \"lat\": \"53.075762\" }, \"speed\": 21 } ], \"source\": { \"name\": \"TestEvent2\", \"icon_url\": \"http://www.example.org/random2_icon_url.png\" } }";
        DBObject dbObject2 = (DBObject) JSON.parse(json2);
        eventCollection.insert(dbObject2);

        ArrayList<EventType> types = new ArrayList<>();
        types.add(EventTypeFactory.build("RANDOM_TEST_EVENT"));
        List<Event> events = eventDAO.getUserEvents(types);
        assertNotNull(events.get(0));
        System.out.println("\n\n\n" + expectedEvent.getDescription() + "\n\n\n");
        assertTrue("The id of this event is incorrect!", expectedEvent.getEventIdentifier() == (events.get(0).getEventIdentifier()));
        assertTrue("The expected event was not returned!", compareEvents(events.get(0), expectedEvent));
        assertTrue("Too much events found!", events.size() == 1);
    }

    @Test
    public void getEventType_shouldReturnEventType() {
        EventType e = eventDAO.getEventType("Accident");
        assertNotNull(e);
        assertTrue("The expected event type was not returned!", compareEventTypes(e, expectedEventType));
    }

    @Test
    public void getEventType_shouldReturnNull1() {
        EventType e = eventDAO.getEventType("RANDOM_TEST_EVENT");
        assertNull(e);
    }

    @Test
    public void getEventType_shouldReturnNull2() {
        EventType e = eventDAO.getEventType(null);
        assertNull(e);
    }

    @Test
    public void getEventTypes_shouldReturnEventTypes() {
        List<EventType> eventTypes = eventDAO.getEventTypes();
        assertEquals(expectedEventTypes.size(), eventTypes.size());
        for (int i = 0; i < expectedEventTypes.size(); i++) {
            assertNotNull(eventTypes.get(i));
            assertTrue("The expected event types were not returned!", compareEventTypes(eventTypes.get(i), expectedEventTypes.get(i)));
        }
    }

    @Test
    public void getIdByEvent_shouldReturnId() {
        int res = eventDAO.getIdByEvent(expectedEvent, false);
        assertEquals(res, expectedEvent.getEventIdentifier());
    }

    @Test
    public void getIdByEvent_shouldReturnMinusOne() {
        int res = eventDAO.getIdByEvent(otherEvent, false);
        assertEquals(res, -1);
    }

    @Test
    public void getIdByEvent_shouldReturnId2() {
        int res = eventDAO.getIdByEvent(expectedEvent, true);
        assertEquals(res, expectedEvent.getEventIdentifier());
    }

    @Test
    public void getIdByEvent_shouldReturnMinusOne2() {
        Coordinate coordinates = CoordinateFactory.build(51.043368, 3.779090);
        Address location = AddressFactory.build(null, 0, null, null, null, coordinates);
        Source source = null;
        Timestamp publicationTime = Timestamp.valueOf("2016-04-06 19:15:44.926");
        Timestamp lastEditTime = Timestamp.valueOf("2016-04-06 19:15:44.926");
        EventType type = null;
        ArrayList<Transport> transportTypes = new ArrayList<>();
        Transport transport1 = TransportFactory.build("car");
        Transport transport2 = TransportFactory.build("bus");
        transportTypes.add(transport1);
        transportTypes.add(transport2);
        Coordinate start = null;
        Coordinate end = null;
        ArrayList<Jam> jams = new ArrayList<>();
        Jam jam = JamFactory.build(start, end, 10, 2987);
        jams.add(jam);
        Event expectedEvent3 = EventFactory.build(0, location, true, source, "Dit is een test event.", publicationTime, lastEditTime, type, transportTypes, jams);
        int res = eventDAO.getIdByEvent(expectedEvent3, false);
        assertEquals(res, -1);

    }

    @Test
    public void getNewId_shouldReturnId() {
        int id1 = eventDAO.getNewId();
        int id2 = eventDAO.getNewId();
        assertEquals(id1 + 1, id2);
    }

    @Test
    public void getEventsByIds_shouldReturnEvents() {
        List<Integer> ids = new ArrayList<>();
        ids.add(0);
        List<Event> events = eventDAO.getEventsByIds(ids);
        assertEquals(expectedEvents.size(), events.size());
        for (int i = 0; i < expectedEvents.size(); i++) {
            assertNotNull(events.get(i));
            assertTrue("The id of this event is incorrect!", expectedEvents.get(i).getEventIdentifier() == (events.get(i).getEventIdentifier()));
            assertTrue("The expected event was not returned!", compareEvents(events.get(i), expectedEvents.get(i)));
        }
    }

}
