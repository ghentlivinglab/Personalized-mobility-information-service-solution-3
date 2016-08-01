package Spring;

import Other.TestUtils;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONObject;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vop.groep7.vop7backend.Controllers.EventController;
import vop.groep7.vop7backend.Models.APIModels.APILinks;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIJam;
import vop.groep7.vop7backend.Models.APIModels.APISource;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Spring.SpringEventController;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Jonas Van Wilder
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SpringEventControllerTest {

    @Mock
    private EventController eventControllerMock;

    @InjectMocks
    private SpringEventController springEventController;

    private MockMvc mockMvc;

    private final List<APIEvent> testEvents;
    private final List<APIEventType> testEventTypes;
    private final String testEventJson;
    private APIEvent modifiedTestEvent;
    private JSONObject modifiedTestEventJson;

    private static final String BASIC_ERROR = "The database is not available at the moment.";
    private static final String NOT_VALID_ERROR = "The event is not valid.";
    private static final String NOT_EXISTS_ERROR = "The event doesn't exist.";
    private static final String NOT_ACTIVE_ERROR = "Event is already marked as non-active.";
    private static final String INVALID_INPUT = "The input of one or more fields was invalid or missing";

    /**
     *
     */
    public SpringEventControllerTest() {
        testEvents = new ArrayList<>();
        testEventTypes = new ArrayList<>();

        // create json string of test event 
        testEventJson = "{ "
                + "\"id\": \"0\","
                + " \"links\": [{"
                + " \"rel\": \"self\","
                + "\"href\": \"https://vopro7.ugent.be/event/0\" "
                + "}],"
                + "\"coordinates\": {"
                + " \"lat\": 52.560559,"
                + "\"lon\": 3.885928"
                + "},"
                + "\"active\": true,"
                + "\"publication_time\": \"2016-03-03 17:30:46.695635\","
                + "\"last_edit_time\": \"2016-03-03 17:30:46.695635\","
                + "\"description\": \"Dit is een test event.\","
                + "\"jams\": ["
                + "{"
                + "\"start_node\": {"
                + "\"lat\": 52.560559,"
                + "\"lon\": 3.885928"
                + "},"
                + "\"end_node\": {"
                + "\"lat\": 52.560559,"
                + "\"lon\": 3.885928"
                + "},"
                + "\"speed\": 16,"
                + "\"delay\": 1860"
                + "}"
                + "],"
                + "\"source\": {"
                + "\"name\": \"TestEvent\","
                + "\"icon_url\": \"www.example.com/random_icon_url.png\""
                + "},"
                + "\"relevant_for_transportation_types\": [\"car\",\"bus\"],"
                + "\"type\": {"
                + "\"id\": \"1\","
                + "\"type\": \"JAM\""
                + "}"
                + "}";

        createTestEvents();
        createTestEventTypes();
        createModifiedTestEvent();
    }

    /**
     *
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     *
     */
    @AfterClass
    public static void tearDownClass() {
    }

    private void createTestEvents() {
        // create test event 1
        APIEvent testEvent1 = new APIEvent();
        testEvent1.setId("0");
        testEvent1.setActive(true);
        testEvent1.setDescription("Dit is een test event.");
        testEvent1.setPublicationTime("2016-03-03 17:30:46.695635");
        testEvent1.setLastEditTime("2016-03-03 17:30:46.695635");
        APILinks[] links1 = new APILinks[1];
        links1[0] = new APILinks();
        links1[0].setRel("self");
        links1[0].setHref("https://vopro7.ugent.be/event/0");
        testEvent1.setLinks(links1);
        APICoordinate c = new APICoordinate();
        c.setLat(51.560559);
        c.setLon(3.724928);
        testEvent1.setCoordinates(c);
        APIJam[] j = new APIJam[1];
        j[0] = new APIJam();
        j[0].setStartNode(c);
        j[0].setEndNode(c);
        j[0].setSpeed(43);
        j[0].setDelay(14 * 60);
        testEvent1.setJams(j);
        APISource s = new APISource();
        s.setName("TestEvent");
        s.setIconUrl("www.example.com/random_icon_url.png");
        testEvent1.setSource(s);
        APITransport[] rtt = new APITransport[2];
        rtt[0] = APITransport.getTransport("car");
        rtt[1] = APITransport.getTransport("bus");
        testEvent1.setRelevantForTransportationTypes(rtt);
        APIEventType t = new APIEventType();
        t.setType("JAM");
        testEvent1.setType(t);
        testEvents.add(testEvent1);

        // create test event 2
        APIEvent testEvent2 = new APIEvent();
        testEvent2.setId("1");
        testEvent2.setActive(true);
        testEvent2.setDescription("Dit is een test event.");
        testEvent2.setPublicationTime("2016-03-03 17:30:46.695635");
        testEvent2.setLastEditTime("2016-03-03 17:30:46.695635");
        APILinks[] links2 = new APILinks[1];
        links2[0] = new APILinks();
        links2[0].setRel("self");
        links2[0].setHref("https://vopro7.ugent.be/event/1");
        testEvent2.setLinks(links2);
        APICoordinate c2 = new APICoordinate();
        c2.setLat(52.560559);
        c2.setLon(3.885928);
        testEvent2.setCoordinates(c2);
        APIJam[] j2 = new APIJam[1];
        j2[0] = new APIJam();
        j2[0].setStartNode(c2);
        j2[0].setEndNode(c2);
        j2[0].setSpeed(16);
        j2[0].setDelay(31 * 60);
        testEvent2.setJams(j2);
        APISource s2 = new APISource();
        s2.setName("TestEvent");
        s2.setIconUrl("www.example.com/random_icon_url.png");
        testEvent2.setSource(s2);
        APITransport[] rtt2 = new APITransport[2];
        rtt2[0] = APITransport.getTransport("car");
        rtt2[1] = APITransport.getTransport("bike");
        testEvent2.setRelevantForTransportationTypes(rtt2);
        APIEventType t2 = new APIEventType();
        t2.setType("JAM");
        testEvent2.setType(t2);
        testEvents.add(testEvent2);
    }

    private void createTestEventTypes() {
        // create test event type 1
        APIEventType testEventType1 = new APIEventType();
        testEventType1.setType("JAM");
        testEventTypes.add(testEventType1);

        // create test event type 2
        APIEventType testEventType2 = new APIEventType();
        testEventType2.setType("WEATHER - STORM");
        testEventTypes.add(testEventType2);
    }

    private void createModifiedTestEvent() {
        // create modified test event
        modifiedTestEvent = testEvents.get(1);
        modifiedTestEvent.setActive(false);
        modifiedTestEvent.setDescription("Dit is een ander test event.");
        modifiedTestEvent.setPublicationTime("2016-03-04 07:30:46.695635");
        modifiedTestEvent.setLastEditTime("2016-03-05 11:30:46.695635");
        APICoordinate c2 = new APICoordinate();
        c2.setLat(51.560559);
        c2.setLon(3.785948);
        modifiedTestEvent.setCoordinates(c2);
        APIJam[] j2 = new APIJam[1];
        j2[0] = new APIJam();
        j2[0].setStartNode(c2);
        j2[0].setEndNode(c2);
        j2[0].setSpeed(57);
        j2[0].setDelay(11 * 60);
        modifiedTestEvent.setJams(j2);
        APITransport[] rtt1 = new APITransport[2];
        rtt1[0] = APITransport.getTransport("car");
        rtt1[1] = APITransport.getTransport("bus");
        modifiedTestEvent.setRelevantForTransportationTypes(rtt1);
        APIEventType type = new APIEventType();
        type.setType("JAM");
        modifiedTestEvent.setType(type);

        // create json object of modified test event
        modifiedTestEventJson = new JSONObject();
        modifiedTestEventJson.put("id", "1");
        modifiedTestEventJson.put("active", "false");
        modifiedTestEventJson.put("description", "Dit is een ander test event.");
        APILinks[] links2 = new APILinks[1];
        links2[0] = new APILinks();
        links2[0].setRel("self");
        links2[0].setHref("https://vopro7.ugent.be/event/1");
        modifiedTestEventJson.put("links", links2);
        modifiedTestEventJson.put("coordinates", c2);
        modifiedTestEventJson.put("jams", j2);
        APISource s2 = new APISource();
        s2.setName("TestEvent");
        s2.setIconUrl("www.example.com/random_icon_url.png");
        modifiedTestEventJson.put("source", s2);
        String[] rtt2 = new String[2];
        rtt2[0] = APITransport.getTransport("car").toString();
        rtt2[1] = APITransport.getTransport("bus").toString();
        modifiedTestEventJson.put("relevant_for_transportation_types", rtt2);
        modifiedTestEventJson.put("type", type);
    }

    /**
     *
     */
    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);

        // Setup as standalone
        this.mockMvc = MockMvcBuilders.standaloneSetup(springEventController).build();
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getAllEvents_ShouldReturnListOfEvents() throws Exception {
        when(eventControllerMock.getAPIEvents()).thenReturn(testEvents);

        mockMvc.perform(get("/event/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(testEvents.size())))
                .andExpect(jsonPath("$[0].id", is(testEvents.get(0).getId())))
                .andExpect(jsonPath("$[0].links", hasSize(testEvents.get(0).getLinks().length)))
                .andExpect(jsonPath("$[0].links[0].rel", is(testEvents.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[0].links[0].href", is(testEvents.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[0].active", is(testEvents.get(0).isActive())))
                .andExpect(jsonPath("$[0].description", is(testEvents.get(0).getDescription())))
                .andExpect(jsonPath("$[0].publication_time", is(testEvents.get(0).getPublicationTime())))
                .andExpect(jsonPath("$[0].last_edit_time", is(testEvents.get(0).getLastEditTime())))
                .andExpect(jsonPath("$[0].coordinates.lat", is(testEvents.get(0).getCoordinates().getLat())))
                .andExpect(jsonPath("$[0].coordinates.lon", is(testEvents.get(0).getCoordinates().getLon())))
                .andExpect(jsonPath("$[0].jams", hasSize(testEvents.get(0).getJams().length)))
                .andExpect(jsonPath("$[0].jams[0].start_node.lat", is(testEvents.get(0).getJams()[0].getStartNode().getLat())))
                .andExpect(jsonPath("$[0].jams[0].start_node.lon", is(testEvents.get(0).getJams()[0].getStartNode().getLon())))
                .andExpect(jsonPath("$[0].jams[0].end_node.lat", is(testEvents.get(0).getJams()[0].getEndNode().getLat())))
                .andExpect(jsonPath("$[0].jams[0].end_node.lon", is(testEvents.get(0).getJams()[0].getEndNode().getLon())))
                .andExpect(jsonPath("$[0].jams[0].speed", is(testEvents.get(0).getJams()[0].getSpeed())))
                .andExpect(jsonPath("$[0].jams[0].delay", is(testEvents.get(0).getJams()[0].getDelay())))
                .andExpect(jsonPath("$[0].source.name", is(testEvents.get(0).getSource().getName())))
                .andExpect(jsonPath("$[0].source.icon_url", is(testEvents.get(0).getSource().getIconUrl())))
                .andExpect(jsonPath("$[0].type.type", is(testEvents.get(0).getType().getType())))
                .andExpect(jsonPath("$[0].relevant_for_transportation_types", hasSize(testEvents.get(0).getRelevantForTransportationTypes().length)))
                .andExpect(jsonPath("$[0].relevant_for_transportation_types[0]", is(testEvents.get(0).getRelevantForTransportationTypes()[0].toString())))
                .andExpect(jsonPath("$[0].relevant_for_transportation_types[1]", is(testEvents.get(0).getRelevantForTransportationTypes()[1].toString())))
                .andExpect(jsonPath("$[1].id", is(testEvents.get(1).getId())))
                .andExpect(jsonPath("$[1].links", hasSize(testEvents.get(1).getLinks().length)))
                .andExpect(jsonPath("$[1].links[0].rel", is(testEvents.get(1).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[1].links[0].href", is(testEvents.get(1).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[1].active", is(testEvents.get(1).isActive())))
                .andExpect(jsonPath("$[1].description", is(testEvents.get(1).getDescription())))
                .andExpect(jsonPath("$[1].publication_time", is(testEvents.get(1).getPublicationTime())))
                .andExpect(jsonPath("$[1].last_edit_time", is(testEvents.get(1).getLastEditTime())))
                .andExpect(jsonPath("$[1].coordinates.lat", is(testEvents.get(1).getCoordinates().getLat())))
                .andExpect(jsonPath("$[1].coordinates.lon", is(testEvents.get(1).getCoordinates().getLon())))
                .andExpect(jsonPath("$[1].jams", hasSize(testEvents.get(1).getJams().length)))
                .andExpect(jsonPath("$[1].jams[0].start_node.lat", is(testEvents.get(1).getJams()[0].getStartNode().getLat())))
                .andExpect(jsonPath("$[1].jams[0].start_node.lon", is(testEvents.get(1).getJams()[0].getStartNode().getLon())))
                .andExpect(jsonPath("$[1].jams[0].end_node.lat", is(testEvents.get(1).getJams()[0].getEndNode().getLat())))
                .andExpect(jsonPath("$[1].jams[0].end_node.lon", is(testEvents.get(1).getJams()[0].getEndNode().getLon())))
                .andExpect(jsonPath("$[1].jams[0].speed", is(testEvents.get(1).getJams()[0].getSpeed())))
                .andExpect(jsonPath("$[1].jams[0].delay", is(testEvents.get(1).getJams()[0].getDelay())))
                .andExpect(jsonPath("$[1].source.name", is(testEvents.get(1).getSource().getName())))
                .andExpect(jsonPath("$[1].source.icon_url", is(testEvents.get(1).getSource().getIconUrl())))
                .andExpect(jsonPath("$[1].type.type", is(testEvents.get(1).getType().getType())))
                .andExpect(jsonPath("$[1].relevant_for_transportation_types", hasSize(testEvents.get(1).getRelevantForTransportationTypes().length)))
                .andExpect(jsonPath("$[1].relevant_for_transportation_types[0]", is(testEvents.get(1).getRelevantForTransportationTypes()[0].toString())))
                .andExpect(jsonPath("$[1].relevant_for_transportation_types[1]", is(testEvents.get(1).getRelevantForTransportationTypes()[1].toString())));

        verify(eventControllerMock, times(1)).getAPIEvents();
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getAllEvents_ShouldReturnEmptyList() throws Exception {
        when(eventControllerMock.getAPIEvents()).thenReturn(new ArrayList());

        mockMvc.perform(get("/event/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(eventControllerMock, times(1)).getAPIEvents();
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     *
     * @throws Exception
     */
    @Test
    public void getAllEvents_ShouldReturnAllEventsForUserCorrect() throws Exception {
        List<APIEvent> testEventsForUser = new ArrayList<>();
        testEventsForUser.add(testEvents.get(0));

        when(eventControllerMock.getUserAPIEvents(0)).thenReturn(testEventsForUser);

        mockMvc.perform(get("/event/?user_id=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(testEventsForUser.size())))
                .andExpect(jsonPath("$[0].id", is(testEventsForUser.get(0).getId())))
                .andExpect(jsonPath("$[0].links", hasSize(testEventsForUser.get(0).getLinks().length)))
                .andExpect(jsonPath("$[0].links[0].rel", is(testEventsForUser.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[0].links[0].href", is(testEventsForUser.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[0].active", is(testEventsForUser.get(0).isActive())))
                .andExpect(jsonPath("$[0].description", is(testEventsForUser.get(0).getDescription())))
                .andExpect(jsonPath("$[0].publication_time", is(testEventsForUser.get(0).getPublicationTime())))
                .andExpect(jsonPath("$[0].last_edit_time", is(testEventsForUser.get(0).getLastEditTime())))
                .andExpect(jsonPath("$[0].coordinates.lat", is(testEventsForUser.get(0).getCoordinates().getLat())))
                .andExpect(jsonPath("$[0].coordinates.lon", is(testEventsForUser.get(0).getCoordinates().getLon())))
                .andExpect(jsonPath("$[0].jams", hasSize(testEvents.get(0).getJams().length)))
                .andExpect(jsonPath("$[0].jams[0].start_node.lat", is(testEventsForUser.get(0).getJams()[0].getStartNode().getLat())))
                .andExpect(jsonPath("$[0].jams[0].start_node.lon", is(testEventsForUser.get(0).getJams()[0].getStartNode().getLon())))
                .andExpect(jsonPath("$[0].jams[0].end_node.lat", is(testEventsForUser.get(0).getJams()[0].getEndNode().getLat())))
                .andExpect(jsonPath("$[0].jams[0].end_node.lon", is(testEventsForUser.get(0).getJams()[0].getEndNode().getLon())))
                .andExpect(jsonPath("$[0].jams[0].speed", is(testEventsForUser.get(0).getJams()[0].getSpeed())))
                .andExpect(jsonPath("$[0].jams[0].delay", is(testEventsForUser.get(0).getJams()[0].getDelay())))
                .andExpect(jsonPath("$[0].source.name", is(testEventsForUser.get(0).getSource().getName())))
                .andExpect(jsonPath("$[0].source.icon_url", is(testEventsForUser.get(0).getSource().getIconUrl())))
                .andExpect(jsonPath("$[0].type.type", is(testEventsForUser.get(0).getType().getType())))
                .andExpect(jsonPath("$[0].relevant_for_transportation_types", hasSize(testEventsForUser.get(0).getRelevantForTransportationTypes().length)))
                .andExpect(jsonPath("$[0].relevant_for_transportation_types[0]", is(testEventsForUser.get(0).getRelevantForTransportationTypes()[0].toString())))
                .andExpect(jsonPath("$[0].relevant_for_transportation_types[1]", is(testEventsForUser.get(0).getRelevantForTransportationTypes()[1].toString())));

        verify(eventControllerMock, times(1)).getUserAPIEvents(0);
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     *
     * @throws Exception
     */
    @Test
    public void getAllEvents_ShouldReturnEmptyListForNotExistingUser() throws Exception {
        when(eventControllerMock.getUserAPIEvents(0)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/event/?user_id=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(eventControllerMock, times(1)).getUserAPIEvents(0);
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getAllEvents_ShouldReturnWentWrongForUser() throws Exception {
        when(eventControllerMock.getUserAPIEvents(0)).thenThrow(new DataAccessException(""));
        mockMvc.perform(get("/event/?user_id=0"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(eventControllerMock, times(1)).getUserAPIEvents(0);
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getEvent_shouldReturnEvent() throws Exception {
        when(eventControllerMock.getAPIEvent(0)).thenReturn(testEvents.get(0));

        mockMvc.perform(get("/event/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testEvents.get(0).getId())))
                .andExpect(jsonPath("$.links", hasSize(testEvents.get(0).getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(testEvents.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(testEvents.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$.active", is(testEvents.get(0).isActive())))
                .andExpect(jsonPath("$.description", is(testEvents.get(0).getDescription())))
                .andExpect(jsonPath("$.publication_time", is(testEvents.get(0).getPublicationTime())))
                .andExpect(jsonPath("$.last_edit_time", is(testEvents.get(0).getLastEditTime())))
                .andExpect(jsonPath("$.coordinates.lat", is(testEvents.get(0).getCoordinates().getLat())))
                .andExpect(jsonPath("$.coordinates.lon", is(testEvents.get(0).getCoordinates().getLon())))
                .andExpect(jsonPath("$.jams", hasSize(testEvents.get(0).getJams().length)))
                .andExpect(jsonPath("$.jams[0].start_node.lat", is(testEvents.get(0).getJams()[0].getStartNode().getLat())))
                .andExpect(jsonPath("$.jams[0].start_node.lon", is(testEvents.get(0).getJams()[0].getStartNode().getLon())))
                .andExpect(jsonPath("$.jams[0].end_node.lat", is(testEvents.get(0).getJams()[0].getEndNode().getLat())))
                .andExpect(jsonPath("$.jams[0].end_node.lon", is(testEvents.get(0).getJams()[0].getEndNode().getLon())))
                .andExpect(jsonPath("$.jams[0].speed", is(testEvents.get(0).getJams()[0].getSpeed())))
                .andExpect(jsonPath("$.jams[0].delay", is(testEvents.get(0).getJams()[0].getDelay())))
                .andExpect(jsonPath("$.source.name", is(testEvents.get(0).getSource().getName())))
                .andExpect(jsonPath("$.source.icon_url", is(testEvents.get(0).getSource().getIconUrl())))
                .andExpect(jsonPath("$.type.type", is(testEvents.get(0).getType().getType())))
                .andExpect(jsonPath("$.relevant_for_transportation_types", hasSize(testEvents.get(0).getRelevantForTransportationTypes().length)))
                .andExpect(jsonPath("$.relevant_for_transportation_types[0]", is(testEvents.get(0).getRelevantForTransportationTypes()[0].toString())))
                .andExpect(jsonPath("$.relevant_for_transportation_types[1]", is(testEvents.get(0).getRelevantForTransportationTypes()[1].toString())));

        verify(eventControllerMock, times(1)).getAPIEvent(0);
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getEvent_shouldReturnEventNotFound() throws Exception {
        when(eventControllerMock.getAPIEvent(0)).thenReturn(null);

        mockMvc.perform(get("/event/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(eventControllerMock, times(1)).getAPIEvent(0);
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createEvent_shouldCreateCorrect() throws Exception {
        when(eventControllerMock.createAPIEvent(Matchers.<APIEvent>any())).thenReturn(testEvents.get(1));

        mockMvc.perform(post("/event/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testEventJson.getBytes()))
                .andExpect(status().is(201))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testEvents.get(1).getId())))
                .andExpect(jsonPath("$.links", hasSize(testEvents.get(1).getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(testEvents.get(1).getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(testEvents.get(1).getLinks()[0].getHref())))
                .andExpect(jsonPath("$.active", is(testEvents.get(1).isActive())))
                .andExpect(jsonPath("$.description", is(testEvents.get(1).getDescription())))
                .andExpect(jsonPath("$.publication_time", is(testEvents.get(1).getPublicationTime())))
                .andExpect(jsonPath("$.last_edit_time", is(testEvents.get(1).getLastEditTime())))
                .andExpect(jsonPath("$.coordinates.lat", is(testEvents.get(1).getCoordinates().getLat())))
                .andExpect(jsonPath("$.coordinates.lon", is(testEvents.get(1).getCoordinates().getLon())))
                .andExpect(jsonPath("$.jams", hasSize(testEvents.get(1).getJams().length)))
                .andExpect(jsonPath("$.jams[0].start_node.lat", is(testEvents.get(1).getJams()[0].getStartNode().getLat())))
                .andExpect(jsonPath("$.jams[0].start_node.lon", is(testEvents.get(1).getJams()[0].getStartNode().getLon())))
                .andExpect(jsonPath("$.jams[0].end_node.lat", is(testEvents.get(1).getJams()[0].getEndNode().getLat())))
                .andExpect(jsonPath("$.jams[0].end_node.lon", is(testEvents.get(1).getJams()[0].getEndNode().getLon())))
                .andExpect(jsonPath("$.jams[0].speed", is(testEvents.get(1).getJams()[0].getSpeed())))
                .andExpect(jsonPath("$.jams[0].delay", is(testEvents.get(1).getJams()[0].getDelay())))
                .andExpect(jsonPath("$.source.name", is(testEvents.get(1).getSource().getName())))
                .andExpect(jsonPath("$.source.icon_url", is(testEvents.get(1).getSource().getIconUrl())))
                .andExpect(jsonPath("$.type.type", is(testEvents.get(1).getType().getType())))
                .andExpect(jsonPath("$.relevant_for_transportation_types", hasSize(testEvents.get(1).getRelevantForTransportationTypes().length)))
                .andExpect(jsonPath("$.relevant_for_transportation_types[0]", is(testEvents.get(1).getRelevantForTransportationTypes()[0].toString())))
                .andExpect(jsonPath("$.relevant_for_transportation_types[1]", is(testEvents.get(1).getRelevantForTransportationTypes()[1].toString())));

        verify(eventControllerMock, times(1)).createAPIEvent(Matchers.<APIEvent>any());
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyEvent_shouldModifyCorrect() throws Exception {
        when(eventControllerMock.modifyAPIEvent(eq(1), Matchers.<APIEvent>any())).thenReturn(modifiedTestEvent);

        mockMvc.perform(put("/event/1")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestEventJson.toString().getBytes()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(modifiedTestEvent.getId())))
                .andExpect(jsonPath("$.links", hasSize(modifiedTestEvent.getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(modifiedTestEvent.getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(modifiedTestEvent.getLinks()[0].getHref())))
                .andExpect(jsonPath("$.active", is(modifiedTestEvent.isActive())))
                .andExpect(jsonPath("$.description", is(modifiedTestEvent.getDescription())))
                .andExpect(jsonPath("$.publication_time", is(modifiedTestEvent.getPublicationTime())))
                .andExpect(jsonPath("$.last_edit_time", is(modifiedTestEvent.getLastEditTime())))
                .andExpect(jsonPath("$.coordinates.lat", is(modifiedTestEvent.getCoordinates().getLat())))
                .andExpect(jsonPath("$.coordinates.lon", is(modifiedTestEvent.getCoordinates().getLon())))
                .andExpect(jsonPath("$.jams", hasSize(modifiedTestEvent.getJams().length)))
                .andExpect(jsonPath("$.jams[0].start_node.lat", is(modifiedTestEvent.getJams()[0].getStartNode().getLat())))
                .andExpect(jsonPath("$.jams[0].start_node.lon", is(modifiedTestEvent.getJams()[0].getStartNode().getLon())))
                .andExpect(jsonPath("$.jams[0].end_node.lat", is(modifiedTestEvent.getJams()[0].getEndNode().getLat())))
                .andExpect(jsonPath("$.jams[0].end_node.lon", is(modifiedTestEvent.getJams()[0].getEndNode().getLon())))
                .andExpect(jsonPath("$.jams[0].speed", is(modifiedTestEvent.getJams()[0].getSpeed())))
                .andExpect(jsonPath("$.jams[0].delay", is(modifiedTestEvent.getJams()[0].getDelay())))
                .andExpect(jsonPath("$.source.name", is(modifiedTestEvent.getSource().getName())))
                .andExpect(jsonPath("$.source.icon_url", is(modifiedTestEvent.getSource().getIconUrl())))
                .andExpect(jsonPath("$.type.type", is(modifiedTestEvent.getType().getType())))
                .andExpect(jsonPath("$.relevant_for_transportation_types", hasSize(modifiedTestEvent.getRelevantForTransportationTypes().length)))
                .andExpect(jsonPath("$.relevant_for_transportation_types[0]", is(modifiedTestEvent.getRelevantForTransportationTypes()[0].toString())))
                .andExpect(jsonPath("$.relevant_for_transportation_types[1]", is(modifiedTestEvent.getRelevantForTransportationTypes()[1].toString())));

        verify(eventControllerMock, times(1)).modifyAPIEvent(eq(1), Matchers.<APIEvent>any());
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyEvent_shouldReturnNotExists() throws Exception {
        when(eventControllerMock.modifyAPIEvent(eq(1), Matchers.<APIEvent>any())).thenReturn(null);
        when(eventControllerMock.getAPIEvent(eq(1))).thenReturn(null);

        mockMvc.perform(put("/event/1")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestEventJson.toString().getBytes()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(eventControllerMock, times(1)).modifyAPIEvent(eq(1), Matchers.<APIEvent>any());
        verify(eventControllerMock, times(1)).getAPIEvent(eq(1));
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyEvent_shouldReturnNotActive() throws Exception {
        when(eventControllerMock.modifyAPIEvent(eq(1), Matchers.<APIEvent>any())).thenReturn(null);
        when(eventControllerMock.getAPIEvent(eq(1))).thenReturn(testEvents.get(1));

        mockMvc.perform(put("/event/1")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestEventJson.toString().getBytes()))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_ACTIVE_ERROR)))
                .andExpect(jsonPath("$.code", is(406)));

        verify(eventControllerMock, times(1)).modifyAPIEvent(eq(1), Matchers.<APIEvent>any());
        verify(eventControllerMock, times(1)).getAPIEvent(eq(1));
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyEvent_shouldReturnWentWrong() throws Exception {
        when(eventControllerMock.modifyAPIEvent(eq(1), Matchers.<APIEvent>any())).thenThrow(new DataAccessException(""));

        mockMvc.perform(put("/event/1")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestEventJson.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(eventControllerMock, times(1)).modifyAPIEvent(eq(1), Matchers.<APIEvent>any());
        verify(eventControllerMock, times(0)).getAPIEvent(eq(1));
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deleteEvent_shouldDeleteCorrect() throws Exception {
        when(eventControllerMock.deleteAPIEvent(1)).thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/event/1"))
                .andExpect(status().is(204));

        verify(eventControllerMock, times(1)).deleteAPIEvent(1);
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deleteEvent_shouldReturnNotExisting() throws Exception {
        when(eventControllerMock.deleteAPIEvent(1)).thenReturn(Boolean.FALSE);

        mockMvc.perform(delete("/event/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(eventControllerMock, times(1)).deleteAPIEvent(1);
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getEventTypes_ShouldReturnListOfEventTypes() throws Exception {
        when(eventControllerMock.getAPIEventTypes()).thenReturn(testEventTypes);

        mockMvc.perform(get("/eventtype/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(testEventTypes.size())))
                .andExpect(jsonPath("$[0].type", is(testEventTypes.get(0).getType())))
                .andExpect(jsonPath("$[1].type", is(testEventTypes.get(1).getType())));

        verify(eventControllerMock, times(1)).getAPIEventTypes();
        verifyNoMoreInteractions(eventControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getEventTypes_ShouldReturnEmptyList() throws Exception {
        when(eventControllerMock.getAPIEventTypes()).thenReturn(new ArrayList());

        mockMvc.perform(get("/eventtype/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(eventControllerMock, times(1)).getAPIEventTypes();
        verifyNoMoreInteractions(eventControllerMock);
    }

    @Test
    public void createEvent_shouldReturnInvalidInput() throws Exception {
        JSONObject testEventInput = new JSONObject();

        mockMvc.perform(post("/event/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testEventInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(3)))
                .andExpect(jsonPath("$.fields[0]", is("coordinates")))
                .andExpect(jsonPath("$.fields[1]", is("description")))
                .andExpect(jsonPath("$.fields[2]", is("relevant_for_transportation_types")));

        verifyNoMoreInteractions(eventControllerMock);
    }

    @Test
    public void createEvent_shouldReturnInvalidInput2() throws Exception {
        JSONObject testEventInput = new JSONObject();
        testEventInput.put("description", "");
        JSONObject testCoordinatesInput = new JSONObject();
        testCoordinatesInput.put("lon", 200.3);
        testEventInput.put("coordinates", testCoordinatesInput);
        mockMvc.perform(post("/event/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testEventInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(3)))
                .andExpect(jsonPath("$.fields[0]", is("coordinates")))
                .andExpect(jsonPath("$.fields[1]", is("description")))
                .andExpect(jsonPath("$.fields[2]", is("relevant_for_transportation_types")));

        verifyNoMoreInteractions(eventControllerMock);
    }

    @Test
    public void createEvent_shouldReturnInvalidInput3() throws Exception {
        JSONObject testEventInput = new JSONObject();
        testEventInput.put("description", "");
        JSONObject testCoordinatesInput = new JSONObject();
        testCoordinatesInput.put("lat", 200.3);
        testEventInput.put("coordinates", testCoordinatesInput);
        mockMvc.perform(post("/event/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testEventInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(3)))
                .andExpect(jsonPath("$.fields[0]", is("coordinates")))
                .andExpect(jsonPath("$.fields[1]", is("description")))
                .andExpect(jsonPath("$.fields[2]", is("relevant_for_transportation_types")));

        verifyNoMoreInteractions(eventControllerMock);
    }

    @Test
    public void createEvent_shouldReturnInvalidInput4() throws Exception {
        JSONObject testEventInput = new JSONObject();
        JSONObject testCoordinatesInput = new JSONObject();
        testCoordinatesInput.put("lat", -200.3);
        testEventInput.put("coordinates", testCoordinatesInput);
        mockMvc.perform(post("/event/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testEventInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(3)))
                .andExpect(jsonPath("$.fields[0]", is("coordinates")))
                .andExpect(jsonPath("$.fields[1]", is("description")))
                .andExpect(jsonPath("$.fields[2]", is("relevant_for_transportation_types")));

        verifyNoMoreInteractions(eventControllerMock);
    }

    @Test
    public void createEvent_shouldReturnInvalidInput5() throws Exception {
        JSONObject testEventInput = new JSONObject();
        JSONObject testCoordinatesInput = new JSONObject();
        testCoordinatesInput.put("lon", -200.3);
        testEventInput.put("coordinates", testCoordinatesInput);
        mockMvc.perform(post("/event/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testEventInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(3)))
                .andExpect(jsonPath("$.fields[0]", is("coordinates")))
                .andExpect(jsonPath("$.fields[1]", is("description")))
                .andExpect(jsonPath("$.fields[2]", is("relevant_for_transportation_types")));

        verifyNoMoreInteractions(eventControllerMock);
    }

}
