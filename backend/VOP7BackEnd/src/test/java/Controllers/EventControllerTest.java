package Controllers;

import com.google.common.cache.Cache;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Controllers.Controller;
import vop.groep7.vop7backend.Controllers.EventController;
import vop.groep7.vop7backend.Controllers.Processor;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APIJam;
import vop.groep7.vop7backend.Models.APIModels.APISource;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.AlertEvent;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.Events.JamEvent;
import vop.groep7.vop7backend.Models.Domain.Jam;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.mongodb.EventDAO;
import vop.groep7.vop7backend.database.sql.UserDAO;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.JamFactory;
import vop.groep7.vop7backend.factories.SourceFactory;
import vop.groep7.vop7backend.factories.TransportFactory;

/**
 *
 * @author Jonas Van Wilder
 */
public class EventControllerTest {

    // mocked eventCache for test
    private static Cache<Integer, Event> mockedEventCache;

    @Mock
    private EventDAO eventDAOMock;

    @Mock
    private UserDAO userDAOMock;

    @Mock
    private Processor processorMock;

    @InjectMocks
    private EventController eventController;

    private Event testEvent1;
    private Event testEvent2;
    private Event testEvent3;
    private List<Event> testEventsActive;
    private List<Event> testEventsInactive;
    private APIEvent testAPIEvent;
    private List<APIEvent> testAPIEvents;
    private final List<EventType> testEventTypes;

    private static final String EVENT_URL = "https://vopro7.ugent.be/api/event/";

    /**
     *
     */
    public EventControllerTest() {
        testEventTypes = new ArrayList<>();
        testEventTypes.add(new EventType("Test_event_type1"));
        testEventTypes.add(new EventType("Test_event_type2"));
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // mock event cache
        mockedEventCache = Mockito.mock(Cache.class);
        // replace static eventCache with mocked eventCache
        Field f = Controller.class.getDeclaredField("eventCache");
        f.setAccessible(true);
        f.set(null, mockedEventCache);

        // Mock annotations
        MockitoAnnotations.initMocks(this);

        testAPIEvents = new ArrayList<>();
        testEventsActive = new ArrayList<>();
        testEventsInactive = new ArrayList<>();

        createTestEvent1();
        createTestEvent2();
        createTestEvent3();
        createTestAPIEvent();

        testAPIEvents.add(testAPIEvent);
        testEventsActive.add(testEvent1);
        testEventsActive.add(testEvent2);
        testEventsInactive.add(testEvent3);
    }

    private void createTestEvent1() {
        // create event
        Address a = AddressFactory.build("Ghent", 9000, "Street", "House number", "Belgium", CoordinateFactory.build(50.0, 3.1));
        Source s = SourceFactory.build("Test", "https://www.example.com/test");
        Timestamp t = new Timestamp(1460565235);
        EventType e = EventTypeFactory.build("Test_event_type");
        ArrayList<Transport> trans = new ArrayList<>();
        trans.add(TransportFactory.build("car"));
        ArrayList<Jam> j = new ArrayList<>();
        j.add(JamFactory.build(CoordinateFactory.build(50.0, 3.1), CoordinateFactory.build(50.0, 3.1), 10, 1000));
        testEvent1 = EventFactory.build(1, a, true, s, "Test Event", t, t, e, trans, j);
    }

    private void createTestEvent2() {
        // create event
        Address a = AddressFactory.build("Ghent", 9000, "Street2", "House number2", "Belgium", CoordinateFactory.build(40.3, 5.05));
        Source s = SourceFactory.build("Test2", "https://www.example.com/test2");
        Timestamp t = new Timestamp(1460865239);
        EventType e = EventTypeFactory.build("Test_event_type2");
        ArrayList<Transport> trans = new ArrayList<>();
        trans.add(TransportFactory.build("bike"));
        testEvent2 = EventFactory.build(2, a, true, s, "Test Event2", t, t, e, trans, null);
    }
    
    private void createTestEvent3() {
        // create event
        Address a = AddressFactory.build("Ghent", 9000, "Street3", "House number3", "Belgium", CoordinateFactory.build(40.3, 5.05));
        Source s = SourceFactory.build("Test3", "https://www.example.com/test3");
        Timestamp t = new Timestamp(1460865241);
        EventType e = EventTypeFactory.build("Test_event_type3");
        ArrayList<Transport> trans = new ArrayList<>();
        trans.add(TransportFactory.build("bus"));
        testEvent3 = EventFactory.build(3, a, false, s, "Test Event3", t, t, e, trans, null);
    }

    private void createTestAPIEvent() {
        // create API event
        testAPIEvent = new APIEvent();
        testAPIEvent.setId("1");
        APICoordinate c = new APICoordinate();
        c.setLat(50.0);
        c.setLon(3.1);
        testAPIEvent.setCoordinates(c);
        testAPIEvent.setActive(true);
        APISource s = new APISource();
        s.setName("Test");
        s.setIconUrl("https://www.example.com/test");
        testAPIEvent.setSource(s);
        testAPIEvent.setDescription("Test Event");
        String t = new Timestamp(1460565235).toString();
        testAPIEvent.setPublicationTime(t);
        testAPIEvent.setLastEditTime(t);
        APIEventType e = new APIEventType();
        e.setType("Test_event_type");
        testAPIEvent.setType(e);
        APITransport[] rftt = new APITransport[1];
        rftt[0] = APITransport.CAR;
        testAPIEvent.setRelevantForTransportationTypes(rftt);
        APIJam[] j = new APIJam[1];
        j[0] = new APIJam();
        j[0].setStartNode(c);
        j[0].setEndNode(c);
        j[0].setSpeed(10);
        j[0].setDelay(1000);
        testAPIEvent.setJams(j);

    }

    private void prepareCache() {
        when(mockedEventCache.getIfPresent(1)).thenReturn(testEvent1);
    }

    @Test
    public void getEvent_returnEventFromCache() {
        prepareCache();
        APIEvent res = eventController.getAPIEvent(1);
        assertNotNull(res);
        assertEquals(testEvent1.getEventIdentifier() + "", res.getId());
        assertTrue(testEvent1.getLocation().getCoordinates().getLat() == res.getCoordinates().getLat());
        assertTrue(testEvent1.getLocation().getCoordinates().getLon() == res.getCoordinates().getLon());
        assertEquals(testEvent1.isActive(), res.isActive());
        assertEquals(testEvent1.getSource().getName(), res.getSource().getName());
        assertEquals(testEvent1.getSource().getIconUrl().toString(), res.getSource().getIconUrl());
        assertEquals(testEvent1.getDescription(), res.getDescription());
        assertEquals(testEvent1.getPublicationTime().toString(), res.getPublicationTime());
        assertEquals(testEvent1.getLastEditTime().toString(), res.getLastEditTime());
        assertEquals(testEvent1.getType().getType(), res.getType().getType());
        assertEquals(testEvent1.getRelevantTransportationTypes().size(), res.getRelevantForTransportationTypes().length);
        assertEquals(testEvent1.getRelevantTransportationTypes().get(0).getTransport(), res.getRelevantForTransportationTypes()[0].getType());
        assertTrue(testEvent1 instanceof JamEvent);
        assertEquals(((JamEvent) testEvent1).getJams().size(), res.getJams().length);
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.getJams()[0].getStartNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLon() == res.getJams()[0].getStartNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLat() == res.getJams()[0].getEndNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLon() == res.getJams()[0].getEndNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.getJams()[0].getStartNode().getLat());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getSpeed(), res.getJams()[0].getSpeed());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getDelay(), res.getJams()[0].getDelay());
        assertEquals("self", res.getLinks()[0].getRel());
        assertEquals(EVENT_URL + testEvent1.getEventIdentifier(), res.getLinks()[0].getHref());

        verify(mockedEventCache, times(0)).put(Matchers.eq(1), Matchers.<Event>any());
    }

    @Test
    public void getEvent_returnEventFromDAO() {

        // prepare dao
        when(eventDAOMock.getEvent(1)).thenReturn(testEvent1);

        APIEvent res = eventController.getAPIEvent(1);
        assertNotNull(res);
        assertEquals(testEvent1.getEventIdentifier() + "", res.getId());
        assertTrue(testEvent1.getLocation().getCoordinates().getLat() == res.getCoordinates().getLat());
        assertTrue(testEvent1.getLocation().getCoordinates().getLon() == res.getCoordinates().getLon());
        assertEquals(testEvent1.isActive(), res.isActive());
        assertEquals(testEvent1.getSource().getName(), res.getSource().getName());
        assertEquals(testEvent1.getSource().getIconUrl().toString(), res.getSource().getIconUrl());
        assertEquals(testEvent1.getDescription(), res.getDescription());
        assertEquals(testEvent1.getPublicationTime().toString(), res.getPublicationTime());
        assertEquals(testEvent1.getLastEditTime().toString(), res.getLastEditTime());
        assertEquals(testEvent1.getType().getType(), res.getType().getType());
        assertEquals(testEvent1.getRelevantTransportationTypes().size(), res.getRelevantForTransportationTypes().length);
        assertEquals(testEvent1.getRelevantTransportationTypes().get(0).getTransport(), res.getRelevantForTransportationTypes()[0].getType());
        assertTrue(testEvent1 instanceof JamEvent);
        assertEquals(((JamEvent) testEvent1).getJams().size(), res.getJams().length);
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.getJams()[0].getStartNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLon() == res.getJams()[0].getStartNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLat() == res.getJams()[0].getEndNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLon() == res.getJams()[0].getEndNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.getJams()[0].getStartNode().getLat());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getSpeed(), res.getJams()[0].getSpeed());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getDelay(), res.getJams()[0].getDelay());
        assertEquals("self", res.getLinks()[0].getRel());
        assertEquals(EVENT_URL + testEvent1.getEventIdentifier(), res.getLinks()[0].getHref());

        verify(mockedEventCache, times(1)).put(Matchers.eq(1), Matchers.<Event>any());
    }

    @Test
    public void getUser_throwNotExisting() {
        // prepare dao
        when(eventDAOMock.getEvent(1)).thenReturn(null);

        APIEvent res = eventController.getAPIEvent(1);

        assertNull(res);

        verify(mockedEventCache, times(0)).put(Matchers.eq(1), Matchers.<Event>any());
    }

    @Test
    public void getAPIEvents_shouldReturnCorrectEvents() {
        when(eventDAOMock.getEvents()).thenReturn(testEventsActive);

        List<APIEvent> res = eventController.getAPIEvents();
        assertNotNull(res);
        assertEquals(testEventsActive.size(), res.size());
        // test if only event is correct
        assertNotNull(res.get(0));
        assertEquals(testEvent1.getEventIdentifier() + "", res.get(0).getId());
        assertTrue(testEvent1.getLocation().getCoordinates().getLat() == res.get(0).getCoordinates().getLat());
        assertTrue(testEvent1.getLocation().getCoordinates().getLon() == res.get(0).getCoordinates().getLon());
        assertEquals(testEvent1.isActive(), res.get(0).isActive());
        assertEquals(testEvent1.getSource().getName(), res.get(0).getSource().getName());
        assertEquals(testEvent1.getSource().getIconUrl().toString(), res.get(0).getSource().getIconUrl());
        assertEquals(testEvent1.getDescription(), res.get(0).getDescription());
        assertEquals(testEvent1.getPublicationTime().toString(), res.get(0).getPublicationTime());
        assertEquals(testEvent1.getLastEditTime().toString(), res.get(0).getLastEditTime());
        assertEquals(testEvent1.getType().getType(), res.get(0).getType().getType());
        assertEquals(testEvent1.getRelevantTransportationTypes().size(), res.get(0).getRelevantForTransportationTypes().length);
        assertEquals(testEvent1.getRelevantTransportationTypes().get(0).getTransport(), res.get(0).getRelevantForTransportationTypes()[0].getType());
        assertTrue(testEvent1 instanceof JamEvent);
        assertEquals(((JamEvent) testEvent1).getJams().size(), res.get(0).getJams().length);
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.get(0).getJams()[0].getStartNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLon() == res.get(0).getJams()[0].getStartNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLat() == res.get(0).getJams()[0].getEndNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLon() == res.get(0).getJams()[0].getEndNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.get(0).getJams()[0].getStartNode().getLat());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getSpeed(), res.get(0).getJams()[0].getSpeed());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getDelay(), res.get(0).getJams()[0].getDelay());
        assertEquals("self", res.get(0).getLinks()[0].getRel());
        assertEquals(EVENT_URL + testEvent1.getEventIdentifier(), res.get(0).getLinks()[0].getHref());

        assertNotNull(res.get(1));
        assertEquals(testEvent2.getEventIdentifier() + "", res.get(1).getId());
        assertTrue(testEvent2.getLocation().getCoordinates().getLat() == res.get(1).getCoordinates().getLat());
        assertTrue(testEvent2.getLocation().getCoordinates().getLon() == res.get(1).getCoordinates().getLon());
        assertEquals(testEvent2.isActive(), res.get(1).isActive());
        assertEquals(testEvent2.getSource().getName(), res.get(1).getSource().getName());
        assertEquals(testEvent2.getSource().getIconUrl().toString(), res.get(1).getSource().getIconUrl());
        assertEquals(testEvent2.getDescription(), res.get(1).getDescription());
        assertEquals(testEvent2.getLastEditTime().toString(), res.get(1).getLastEditTime());
        assertEquals(testEvent2.getType().getType(), res.get(1).getType().getType());
        assertEquals(testEvent2.getRelevantTransportationTypes().get(0).getTransport(), res.get(1).getRelevantForTransportationTypes()[0].getType());
        assertTrue(testEvent2 instanceof AlertEvent);
        assertEquals("self", res.get(1).getLinks()[0].getRel());
        assertEquals(EVENT_URL + testEvent2.getEventIdentifier(), res.get(1).getLinks()[0].getHref());
    }
    
    @Test
    public void getInactiveAPIEvents_shouldReturnCorrectEvents() {
        when(eventDAOMock.getEventsWithActiveParameter(false)).thenReturn(testEventsInactive);

        List<APIEvent> res = eventController.getInactiveAPIEvents();
        assertNotNull(res);
        assertEquals(testEventsInactive.size(), res.size());
        // test if only event is correct
        assertNotNull(res.get(0));
        assertEquals(testEvent3.getEventIdentifier() + "", res.get(0).getId());
        assertTrue(testEvent3.getLocation().getCoordinates().getLat() == res.get(0).getCoordinates().getLat());
        assertTrue(testEvent3.getLocation().getCoordinates().getLon() == res.get(0).getCoordinates().getLon());
        assertEquals(testEvent3.isActive(), res.get(0).isActive());
        assertEquals(testEvent3.getSource().getName(), res.get(0).getSource().getName());
        assertEquals(testEvent3.getSource().getIconUrl().toString(), res.get(0).getSource().getIconUrl());
        assertEquals(testEvent3.getDescription(), res.get(0).getDescription());
        assertEquals(testEvent3.getPublicationTime().toString(), res.get(0).getPublicationTime());
        assertEquals(testEvent3.getLastEditTime().toString(), res.get(0).getLastEditTime());
        assertEquals(testEvent3.getType().getType(), res.get(0).getType().getType());
        assertEquals(testEvent3.getRelevantTransportationTypes().size(), res.get(0).getRelevantForTransportationTypes().length);
        assertEquals(testEvent3.getRelevantTransportationTypes().get(0).getTransport(), res.get(0).getRelevantForTransportationTypes()[0].getType());
        assertTrue(testEvent3 instanceof AlertEvent);
        assertEquals("self", res.get(0).getLinks()[0].getRel());
        assertEquals(EVENT_URL + testEvent3.getEventIdentifier(), res.get(0).getLinks()[0].getHref());
    }
    
    @Test
    public void getActiveAPIEvents_shouldReturnCorrectEvents() {
        when(eventDAOMock.getEventsWithActiveParameter(true)).thenReturn(testEventsActive);

        List<APIEvent> res = eventController.getActiveAPIEvents();
        assertNotNull(res);
        assertEquals(testEventsActive.size(), res.size());
        // test if only event is correct
        assertNotNull(res.get(0));
        assertEquals(testEvent1.getEventIdentifier() + "", res.get(0).getId());
        assertTrue(testEvent1.getLocation().getCoordinates().getLat() == res.get(0).getCoordinates().getLat());
        assertTrue(testEvent1.getLocation().getCoordinates().getLon() == res.get(0).getCoordinates().getLon());
        assertEquals(testEvent1.isActive(), res.get(0).isActive());
        assertEquals(testEvent1.getSource().getName(), res.get(0).getSource().getName());
        assertEquals(testEvent1.getSource().getIconUrl().toString(), res.get(0).getSource().getIconUrl());
        assertEquals(testEvent1.getDescription(), res.get(0).getDescription());
        assertEquals(testEvent1.getPublicationTime().toString(), res.get(0).getPublicationTime());
        assertEquals(testEvent1.getLastEditTime().toString(), res.get(0).getLastEditTime());
        assertEquals(testEvent1.getType().getType(), res.get(0).getType().getType());
        assertEquals(testEvent1.getRelevantTransportationTypes().size(), res.get(0).getRelevantForTransportationTypes().length);
        assertEquals(testEvent1.getRelevantTransportationTypes().get(0).getTransport(), res.get(0).getRelevantForTransportationTypes()[0].getType());
        assertTrue(testEvent1 instanceof JamEvent);
        assertEquals(((JamEvent) testEvent1).getJams().size(), res.get(0).getJams().length);
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.get(0).getJams()[0].getStartNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLon() == res.get(0).getJams()[0].getStartNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLat() == res.get(0).getJams()[0].getEndNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLon() == res.get(0).getJams()[0].getEndNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.get(0).getJams()[0].getStartNode().getLat());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getSpeed(), res.get(0).getJams()[0].getSpeed());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getDelay(), res.get(0).getJams()[0].getDelay());
        assertEquals("self", res.get(0).getLinks()[0].getRel());
        assertEquals(EVENT_URL + testEvent1.getEventIdentifier(), res.get(0).getLinks()[0].getHref());
        
        assertNotNull(res.get(1));
        assertEquals(testEvent2.getEventIdentifier() + "", res.get(1).getId());
        assertTrue(testEvent2.getLocation().getCoordinates().getLat() == res.get(1).getCoordinates().getLat());
        assertTrue(testEvent2.getLocation().getCoordinates().getLon() == res.get(1).getCoordinates().getLon());
        assertEquals(testEvent2.isActive(), res.get(1).isActive());
        assertEquals(testEvent2.getSource().getName(), res.get(1).getSource().getName());
        assertEquals(testEvent2.getSource().getIconUrl().toString(), res.get(1).getSource().getIconUrl());
        assertEquals(testEvent2.getDescription(), res.get(1).getDescription());
        assertEquals(testEvent2.getLastEditTime().toString(), res.get(1).getLastEditTime());
        assertEquals(testEvent2.getType().getType(), res.get(1).getType().getType());
        assertEquals(testEvent2.getRelevantTransportationTypes().get(0).getTransport(), res.get(1).getRelevantForTransportationTypes()[0].getType());
        assertTrue(testEvent2 instanceof AlertEvent);
        assertEquals("self", res.get(1).getLinks()[0].getRel());
        assertEquals(EVENT_URL + testEvent2.getEventIdentifier(), res.get(1).getLinks()[0].getHref());
    }

    @Test
    public void getUserAPIEvents_shouldReturnCorrectEvents() {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        try {
            when(userDAOMock.getMatchedEvents(Matchers.eq(1))).thenReturn(ids);
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }

        when(eventDAOMock.getEventsByIds(Matchers.<List<Integer>>any())).thenReturn(testEventsActive);

        List<APIEvent> res = null;
        try {
            res = eventController.getUserAPIEvents(1);
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
        assertNotNull(res);
        assertEquals(testEventsActive.size(), res.size());
        // test if only event is correct
        assertNotNull(res.get(0));
        assertEquals(testEvent1.getEventIdentifier() + "", res.get(0).getId());
        assertTrue(testEvent1.getLocation().getCoordinates().getLat() == res.get(0).getCoordinates().getLat());
        assertTrue(testEvent1.getLocation().getCoordinates().getLon() == res.get(0).getCoordinates().getLon());
        assertEquals(testEvent1.isActive(), res.get(0).isActive());
        assertEquals(testEvent1.getSource().getName(), res.get(0).getSource().getName());
        assertEquals(testEvent1.getSource().getIconUrl().toString(), res.get(0).getSource().getIconUrl());
        assertEquals(testEvent1.getDescription(), res.get(0).getDescription());
        assertEquals(testEvent1.getPublicationTime().toString(), res.get(0).getPublicationTime());
        assertEquals(testEvent1.getLastEditTime().toString(), res.get(0).getLastEditTime());
        assertEquals(testEvent1.getType().getType(), res.get(0).getType().getType());
        assertEquals(testEvent1.getRelevantTransportationTypes().size(), res.get(0).getRelevantForTransportationTypes().length);
        assertEquals(testEvent1.getRelevantTransportationTypes().get(0).getTransport(), res.get(0).getRelevantForTransportationTypes()[0].getType());
        assertTrue(testEvent1 instanceof JamEvent);
        assertEquals(((JamEvent) testEvent1).getJams().size(), res.get(0).getJams().length);
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.get(0).getJams()[0].getStartNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLon() == res.get(0).getJams()[0].getStartNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLat() == res.get(0).getJams()[0].getEndNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLon() == res.get(0).getJams()[0].getEndNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.get(0).getJams()[0].getStartNode().getLat());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getSpeed(), res.get(0).getJams()[0].getSpeed());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getDelay(), res.get(0).getJams()[0].getDelay());
        assertEquals("self", res.get(0).getLinks()[0].getRel());
        assertEquals(EVENT_URL + testEvent1.getEventIdentifier(), res.get(0).getLinks()[0].getHref());
    }

    @Test
    public void createAPIEvent_shouldCreateCorrect() {
        when(eventDAOMock.getIdByEvent(Matchers.<Event>any(), Matchers.eq(false))).thenReturn(-1);
        when(eventDAOMock.getNewId()).thenReturn(1);
        APIEvent res = eventController.createAPIEvent(testAPIEvent);
        verify(eventDAOMock, times(1)).getIdByEvent(Matchers.<Event>any(), Matchers.eq(false));
        verify(eventDAOMock, times(1)).createEvent(Matchers.<Event>any());
        verify(mockedEventCache, times(1)).put(Matchers.anyInt(), Matchers.<Event>any());
        verify(processorMock, times(1)).process(Matchers.<Event>any());
        assertNotNull(res);
        assertEquals(testEvent1.getEventIdentifier() + "", res.getId());
        assertTrue(testEvent1.getLocation().getCoordinates().getLat() == res.getCoordinates().getLat());
        assertTrue(testEvent1.getLocation().getCoordinates().getLon() == res.getCoordinates().getLon());
        assertEquals(testEvent1.isActive(), res.isActive());
        assertEquals(testEvent1.getSource().getName(), res.getSource().getName());
        assertEquals(testEvent1.getSource().getIconUrl().toString(), res.getSource().getIconUrl());
        assertEquals(testEvent1.getDescription(), res.getDescription());
        assertEquals(testEvent1.getType().getType(), res.getType().getType());
        assertEquals(testEvent1.getRelevantTransportationTypes().size(), res.getRelevantForTransportationTypes().length);
        assertEquals(testEvent1.getRelevantTransportationTypes().get(0).getTransport(), res.getRelevantForTransportationTypes()[0].getType());
        assertTrue(testEvent1 instanceof JamEvent);
        assertEquals(((JamEvent) testEvent1).getJams().size(), res.getJams().length);
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.getJams()[0].getStartNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLon() == res.getJams()[0].getStartNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLat() == res.getJams()[0].getEndNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLon() == res.getJams()[0].getEndNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.getJams()[0].getStartNode().getLat());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getSpeed(), res.getJams()[0].getSpeed());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getDelay(), res.getJams()[0].getDelay());
        assertEquals("self", res.getLinks()[0].getRel());
        assertEquals(EVENT_URL + testEvent1.getEventIdentifier(), res.getLinks()[0].getHref());
    }

    @Test
    public void createAPIEvent_shouldReturnNull() {
        when(eventDAOMock.getIdByEvent(testEvent1, false)).thenReturn(1);
        APIEvent res = eventController.createAPIEvent(testAPIEvent);
        assertNull(res);
        verify(eventDAOMock, times(1)).getIdByEvent(Matchers.<Event>any(),Matchers.eq(false));
    }

    @Test
    public void modifyAPIEvent_shouldModifyCorrect() {
        testAPIEvent.setId("1");
        when(mockedEventCache.getIfPresent(Matchers.eq(1))).thenReturn(testEvent1);
        when(eventDAOMock.getEvent(1)).thenReturn(testEvent1);
        when(eventDAOMock.getIdByEvent(Matchers.<Event>any(), Matchers.eq(false))).thenReturn(-1);
        APIEvent res = null;
        try {
            res = eventController.modifyAPIEvent(1, testAPIEvent);
        } catch (DataAccessException ex) {
          fail("Exception should not be thrown!");
        }
        verify(mockedEventCache, times(1)).getIfPresent(Matchers.eq(1));
        verify(mockedEventCache, times(1)).invalidate(Matchers.eq(1));
        verify(mockedEventCache, times(1)).put(Matchers.eq(1), Matchers.<Event>any());
        verify(eventDAOMock, times(1)).modifyEvent(Matchers.eq(1), Matchers.<Event>any());
        verify(processorMock, times(1)).process(Matchers.<Event>any());
        
        assertNotNull(res);
        assertEquals(testEvent1.getEventIdentifier() + "", res.getId());
        assertTrue(testEvent1.getLocation().getCoordinates().getLat() == res.getCoordinates().getLat());
        assertTrue(testEvent1.getLocation().getCoordinates().getLon() == res.getCoordinates().getLon());
        assertEquals(testEvent1.isActive(), res.isActive());
        assertEquals(testEvent1.getSource().getName(), res.getSource().getName());
        assertEquals(testEvent1.getSource().getIconUrl().toString(), res.getSource().getIconUrl());
        assertEquals(testEvent1.getDescription(), res.getDescription());
        assertEquals(testEvent1.getType().getType(), res.getType().getType());
        assertEquals(testEvent1.getRelevantTransportationTypes().size(), res.getRelevantForTransportationTypes().length);
        assertEquals(testEvent1.getRelevantTransportationTypes().get(0).getTransport(), res.getRelevantForTransportationTypes()[0].getType());
        assertTrue(testEvent1 instanceof JamEvent);
        assertEquals(((JamEvent) testEvent1).getJams().size(), res.getJams().length);
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.getJams()[0].getStartNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLon() == res.getJams()[0].getStartNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLat() == res.getJams()[0].getEndNode().getLat());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getEnd().getLon() == res.getJams()[0].getEndNode().getLon());
        assertTrue(((JamEvent) testEvent1).getJams().get(0).getStart().getLat() == res.getJams()[0].getStartNode().getLat());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getSpeed(), res.getJams()[0].getSpeed());
        assertEquals(((JamEvent) testEvent1).getJams().get(0).getDelay(), res.getJams()[0].getDelay());
        assertEquals("self", res.getLinks()[0].getRel());
        assertEquals(EVENT_URL + testEvent1.getEventIdentifier(), res.getLinks()[0].getHref());
    }

    @Test
    public void deleteAPIEvent_shouldDeleteCorrect() {
        when(mockedEventCache.getIfPresent(Matchers.eq(1))).thenReturn(testEvent1);
        when(eventDAOMock.deleteEvent(Matchers.eq(1))).thenReturn(true);

        boolean res = eventController.deleteAPIEvent(1);
        assertTrue(res);

        verify(mockedEventCache, times(1)).getIfPresent(Matchers.eq(1));
        verify(mockedEventCache, times(1)).invalidate(Matchers.eq(1));
        verify(eventDAOMock, times(1)).deleteEvent(Matchers.eq(1));
        try {
            verify(userDAOMock, times(1)).deleteMatchedEvent(Matchers.eq(1));
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
        verifyNoMoreInteractions(eventDAOMock, userDAOMock, mockedEventCache);
    }

    @Test
    public void deleteAPIEvent_shouldDeleteNot() {
        when(mockedEventCache.getIfPresent(Matchers.eq(1))).thenReturn(null);
        when(eventDAOMock.deleteEvent(Matchers.eq(1))).thenReturn(false);

        boolean res = eventController.deleteAPIEvent(1);
        assertFalse(res);

        verify(mockedEventCache, times(1)).getIfPresent(Matchers.eq(1));
        verify(mockedEventCache, times(0)).invalidate(Matchers.eq(1));
        verify(eventDAOMock, times(1)).deleteEvent(Matchers.eq(1));
        try {
            verify(userDAOMock, times(1)).deleteMatchedEvent(Matchers.eq(1));
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
        verifyNoMoreInteractions(eventDAOMock, userDAOMock, mockedEventCache);

    }

    @Test
    public void getAPIEventTypes_shouldReturnEventTypes() {
        when(eventDAOMock.getEventTypes()).thenReturn(testEventTypes);

        List<APIEventType> res = eventController.getAPIEventTypes();

        assertNotNull(res);
        assertEquals(testEventTypes.size(), res.size());
        for (int i = 0; i < testEventTypes.size(); i++) {
            assertEquals(testEventTypes.get(i).getType(), res.get(i).getType());
        }
    }

    @Test
    public void getIdByAPIEvent_shouldReturnId() {
        when(eventDAOMock.getIdByEvent(Matchers.<Event>any(),Matchers.eq(true))).thenReturn(1);

        int res = eventController.getIdByAPIEvent(testAPIEvent);
        assertEquals(1, res);
    }
}
