package Spring;

import Other.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vop.groep7.vop7backend.Controllers.RouteController;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APILinks;
import vop.groep7.vop7backend.Models.APIModels.APINotify;
import vop.groep7.vop7backend.Models.APIModels.APIRoute;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Spring.SpringRouteController;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Jonas Van Wilder
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SpringRouteControllerTest {

    @Mock
    private RouteController routeControllerMock;

    @InjectMocks
    private SpringRouteController springRouteController;

    private MockMvc mockMvc;

    private final List<APIRoute> testRoutes;
    private APIRoute testRoute1;
    private String testRouteJson;
    private APIRoute modifiedTestRoute1;
    private JSONObject modifiedTestRouteJson;

    private static final String BASIC_ERROR = "The database is not available at the moment.";
    private static final String CREATE_EXISTS_ERROR = "The route you are trying to create already exists.";
    private static final String EXISTS_ERROR = "The route already exists.";
    private static final String NOT_EXISTS_ERROR = "The route doesn't exist.";
    private static final String INVALID_INPUT = "The input of one or more fields was invalid or missing";

    /**
     *
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public SpringRouteControllerTest() throws JsonProcessingException {
        testRoutes = new ArrayList<>();

        createTestRoutes();
        createModifiedtestRoute();
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

    /**
     *
     */
    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);

        // Setup as standalone
        this.mockMvc = MockMvcBuilders.standaloneSetup(springRouteController).build();
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    private void createTestRoutes() throws JsonProcessingException {
        // create test route 1
        testRoute1 = new APIRoute();
        testRoute1.setId("0");
        APILinks[] links1 = new APILinks[1];
        links1[0] = new APILinks();
        links1[0].setRel("self");
        links1[0].setHref("https://vopro7.ugent.be/user/0/travel/0/route/0");
        testRoute1.setLinks(links1);
        APICoordinate[] c = new APICoordinate[1];
        APICoordinate c1 = new APICoordinate();
        c1.setLat(3.0);
        c1.setLon(50.0);
        c[0] = c1;
        testRoute1.setWaypoints(c);
        APITransport t1 = APITransport.BIKE;
        testRoute1.setTransportationType(t1);
        APIEventType[] e = new APIEventType[1];
        APIEventType type = new APIEventType();
        type.setType("Type");
        e[0] = type;
        testRoute1.setNotifyForEventTypes(e);
        testRoute1.setActive(true);
        APINotify n = new APINotify();
        n.setEmailNotify(true);
        n.setCellNumberNotify(true);
        testRoute1.setNotify(n);
        testRoutes.add(testRoute1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        testRouteJson = ow.writeValueAsString(testRoute1);
    }

    private void createModifiedtestRoute() {
        // create modified test route
        modifiedTestRoute1 = testRoutes.get(0);
        APITransport t1 = APITransport.CAR;
        modifiedTestRoute1.setTransportationType(t1);
        modifiedTestRoute1.setActive(false);
        modifiedTestRoute1.setNotify(new APINotify());

        // create json object of modified test route
        modifiedTestRouteJson = new JSONObject();
        modifiedTestRouteJson.put("id", "0");
        APILinks[] links1 = new APILinks[1];
        links1[0] = new APILinks();
        links1[0].setRel("self");
        links1[0].setHref("https://vopro7.ugent.be/user/0/travel/0/route/0");
        modifiedTestRouteJson.put("links", links1);
        APICoordinate[] c = new APICoordinate[1];
        c[0] = null;
        modifiedTestRouteJson.put("waypoints", c);
        String t2 = APITransport.CAR.toString();
        modifiedTestRouteJson.put("transportation_type", t2);
        APIEventType[] e = new APIEventType[1];
        e[0] = null;
        modifiedTestRouteJson.put("notify_for_event_types", e);
        modifiedTestRouteJson.put("active", false);
        modifiedTestRouteJson.put("notify", new APINotify());
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getAllRoutes_ShouldReturnListOfRoutesForTravel() throws Exception {
        when(routeControllerMock.getAPIRoutes(0, 0)).thenReturn(testRoutes);

        mockMvc.perform(get("/user/0/travel/0/route/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(testRoutes.size())))
                .andExpect(jsonPath("$[0].id", is(testRoutes.get(0).getId())))
                .andExpect(jsonPath("$[0].links", hasSize(testRoutes.get(0).getLinks().length)))
                .andExpect(jsonPath("$[0].links[0].rel", is(testRoutes.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[0].links[0].href", is(testRoutes.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[0].waypoints", hasSize(testRoutes.get(0).getWaypoints().length)))
                .andExpect(jsonPath("$[0].waypoints[0].lat", is(testRoutes.get(0).getWaypoints()[0].getLat())))
                .andExpect(jsonPath("$[0].waypoints[0].lon", is(testRoutes.get(0).getWaypoints()[0].getLon())))
                .andExpect(jsonPath("$[0].transportation_type", is(testRoutes.get(0).getTransportationType().toString())))
                .andExpect(jsonPath("$[0].notify_for_event_types", hasSize(testRoutes.get(0).getNotifyForEventTypes().length)))
                .andExpect(jsonPath("$[0].notify_for_event_types[0].type", is(testRoutes.get(0).getNotifyForEventTypes()[0].getType())))
                .andExpect(jsonPath("$[0].notify.email", is(testRoutes.get(0).getNotify().isEmailNotify())))
                .andExpect(jsonPath("$[0].notify.cell_number", is(testRoutes.get(0).getNotify().isCellNumberNotify())))
                .andExpect(jsonPath("$[0].active", is(testRoutes.get(0).isActive())));

        verify(routeControllerMock, times(1)).getAPIRoutes(0, 0);
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getAllRoutes_ShouldReturnEmptyListForTravel() throws Exception {
        when(routeControllerMock.getAPIRoutes(0, 0)).thenReturn(new ArrayList());

        mockMvc.perform(get("/user/0/travel/0/route/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(routeControllerMock, times(1)).getAPIRoutes(0, 0);
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     *
     * @throws Exception
     */
    @Test
    public void getAllRoutes_ShouldReturnWentWrong() throws Exception {
        when(routeControllerMock.getAPIRoutes(0, 0)).thenThrow(new DataAccessException(""));

        mockMvc.perform(get("/user/0/travel/0/route/"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(routeControllerMock, times(1)).getAPIRoutes(0, 0);
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getRoute_shouldReturnRoute() throws Exception {
        when(routeControllerMock.getAPIRoute(0, 0, 0)).thenReturn(testRoutes.get(0));

        mockMvc.perform(get("/user/0/travel/0/route/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRoutes.get(0).getId())))
                .andExpect(jsonPath("$.links", hasSize(testRoutes.get(0).getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(testRoutes.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(testRoutes.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$.waypoints", hasSize(testRoutes.get(0).getWaypoints().length)))
                .andExpect(jsonPath("$.waypoints[0].lat", is(testRoutes.get(0).getWaypoints()[0].getLat())))
                .andExpect(jsonPath("$.waypoints[0].lon", is(testRoutes.get(0).getWaypoints()[0].getLon())))
                .andExpect(jsonPath("$.transportation_type", is(testRoutes.get(0).getTransportationType().toString())))
                .andExpect(jsonPath("$.notify_for_event_types", hasSize(testRoutes.get(0).getNotifyForEventTypes().length)))
                .andExpect(jsonPath("$.notify_for_event_types[0].type", is(testRoutes.get(0).getNotifyForEventTypes()[0].getType())))
                .andExpect(jsonPath("$.notify.email", is(testRoutes.get(0).getNotify().isEmailNotify())))
                .andExpect(jsonPath("$.notify.cell_number", is(testRoutes.get(0).getNotify().isCellNumberNotify())))
                .andExpect(jsonPath("$.active", is(testRoutes.get(0).isActive())));

        verify(routeControllerMock, times(1)).getAPIRoute(0, 0, 0);
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     *
     */
    @Test
    public void getRoute_shouldReturnRouteNotFound() throws Exception {
        when(routeControllerMock.getAPIRoute(0, 0, 0)).thenReturn(null);

        mockMvc.perform(get("/user/0/travel/0/route/0"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(routeControllerMock, times(1)).getAPIRoute(0, 0, 0);
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getRoute_shouldReturnWentWrong() throws Exception {
        when(routeControllerMock.getAPIRoute(0, 0, 0)).thenThrow(new DataAccessException(""));

        mockMvc.perform(get("/user/0/travel/0/route/0"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(routeControllerMock, times(1)).getAPIRoute(0, 0, 0);
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createRoute_shouldCreateCorrect() throws Exception {
        when(routeControllerMock.existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any())).thenReturn(false);
        when(routeControllerMock.createAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any())).thenReturn(testRoutes.get(0));

        mockMvc.perform(post("/user/0/travel/0/route")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testRouteJson))
                .andExpect(status().is(201))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRoutes.get(0).getId())))
                .andExpect(jsonPath("$.links", hasSize(testRoutes.get(0).getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(testRoutes.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(testRoutes.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$.waypoints", hasSize(testRoutes.get(0).getWaypoints().length)))
                .andExpect(jsonPath("$.waypoints[0].lat", is(testRoutes.get(0).getWaypoints()[0].getLat())))
                .andExpect(jsonPath("$.waypoints[0].lon", is(testRoutes.get(0).getWaypoints()[0].getLon())))
                .andExpect(jsonPath("$.transportation_type", is(testRoutes.get(0).getTransportationType().toString())))
                .andExpect(jsonPath("$.notify_for_event_types", hasSize(testRoutes.get(0).getNotifyForEventTypes().length)))
                .andExpect(jsonPath("$.notify_for_event_types[0].type", is(testRoutes.get(0).getNotifyForEventTypes()[0].getType())))
                .andExpect(jsonPath("$.notify.email", is(testRoutes.get(0).getNotify().isEmailNotify())))
                .andExpect(jsonPath("$.notify.cell_number", is(testRoutes.get(0).getNotify().isCellNumberNotify())))
                .andExpect(jsonPath("$.active", is(testRoutes.get(0).isActive())));

        verify(routeControllerMock, times(1)).existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any());
        verify(routeControllerMock, times(1)).createAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any());
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createRoute_shouldReturnAlreadyExisting() throws Exception {
        when(routeControllerMock.existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any())).thenReturn(true);

        mockMvc.perform(post("/user/0/travel/0/route")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testRouteJson))
                .andExpect(status().is(409))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(CREATE_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(409)));

        verify(routeControllerMock, times(1)).existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any());
        verifyNoMoreInteractions(routeControllerMock);
    }

    @Test
    public void createRoute_shouldReturnInvalidInput() throws Exception {
        JSONObject testRouteInput = new JSONObject();
        mockMvc.perform(post("/user/0/travel/0/route")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testRouteInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(4)))
                .andExpect(jsonPath("$.fields[0]", is("transportation_type")))
                .andExpect(jsonPath("$.fields[1]", is("waypoints")))
                .andExpect(jsonPath("$.fields[2]", is("notify_for_event_types")))
                .andExpect(jsonPath("$.fields[3]", is("notify")));

        verifyNoMoreInteractions(routeControllerMock);
    }
    
    @Test
    public void createRoute_shouldReturnInvalidInput2() throws Exception {
        JSONObject testRouteInput = new JSONObject();
        testRouteInput.put("notify", new APINotify());
        mockMvc.perform(post("/user/0/travel/0/route")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testRouteInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(3)))
                .andExpect(jsonPath("$.fields[0]", is("transportation_type")))
                .andExpect(jsonPath("$.fields[1]", is("waypoints")))
                .andExpect(jsonPath("$.fields[2]", is("notify_for_event_types")));

        verifyNoMoreInteractions(routeControllerMock);
    }
    
    @Test
    public void createRoute_shouldReturnInvalidInput3() throws Exception {
        JSONObject testRouteInput = new JSONObject();
        testRouteInput.put("notify", new APINotify());
        testRouteInput.put("notify_for_event_types", new ArrayList<>());
        mockMvc.perform(post("/user/0/travel/0/route")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testRouteInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(2)))
                .andExpect(jsonPath("$.fields[0]", is("transportation_type")))
                .andExpect(jsonPath("$.fields[1]", is("waypoints")));

        verifyNoMoreInteractions(routeControllerMock);
    }
    
    @Test
    public void createRoute_shouldReturnInvalidInput4() throws Exception {
        JSONObject testRouteInput = new JSONObject();
        testRouteInput.put("notify", new APINotify());
        testRouteInput.put("notify_for_event_types", new ArrayList<>());
        testRouteInput.put("waypoints", new ArrayList<>());
        mockMvc.perform(post("/user/0/travel/0/route")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testRouteInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(1)))
                .andExpect(jsonPath("$.fields[0]", is("transportation_type")));

        verifyNoMoreInteractions(routeControllerMock);
    }
    
    /**
     *
     * @throws Exception
     */
    @Test
    public void createRoute_shouldReturnNotExisting() throws Exception {
        when(routeControllerMock.existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any())).thenReturn(false);
        when(routeControllerMock.createAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any())).thenReturn(null);

        mockMvc.perform(post("/user/0/travel/0/route")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testRouteJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(routeControllerMock, times(1)).existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any());
        verify(routeControllerMock, times(1)).createAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any());
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createRoute_shouldReturnWentWrong() throws Exception {
        when(routeControllerMock.existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any())).thenThrow(new DataAccessException(""));

        mockMvc.perform(post("/user/0/travel/0/route")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testRouteJson))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(routeControllerMock, times(1)).existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any());
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyRoute_shouldModifyCorrect() throws Exception {
        when(routeControllerMock.existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any())).thenReturn(false);
        when(routeControllerMock.modifyAPIRoute(eq(0), eq(0), eq(0), Matchers.<APIRoute>any())).thenReturn(modifiedTestRoute1);

        mockMvc.perform(put("/user/0/travel/0/route/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestRouteJson.toString().getBytes()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(modifiedTestRoute1.getId())))
                .andExpect(jsonPath("$.links", hasSize(modifiedTestRoute1.getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(modifiedTestRoute1.getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(modifiedTestRoute1.getLinks()[0].getHref())))
                .andExpect(jsonPath("$.waypoints", hasSize(modifiedTestRoute1.getWaypoints().length)))
                .andExpect(jsonPath("$.waypoints[0].lat", is(modifiedTestRoute1.getWaypoints()[0].getLat())))
                .andExpect(jsonPath("$.waypoints[0].lon", is(modifiedTestRoute1.getWaypoints()[0].getLon())))
                .andExpect(jsonPath("$.transportation_type", is(modifiedTestRoute1.getTransportationType().toString())))
                .andExpect(jsonPath("$.notify_for_event_types", hasSize(modifiedTestRoute1.getNotifyForEventTypes().length)))
                .andExpect(jsonPath("$.notify_for_event_types[0].type", is(modifiedTestRoute1.getNotifyForEventTypes()[0].getType())))
                .andExpect(jsonPath("$.notify.email", is(modifiedTestRoute1.getNotify().isEmailNotify())))
                .andExpect(jsonPath("$.notify.cell_number", is(modifiedTestRoute1.getNotify().isCellNumberNotify())))
                .andExpect(jsonPath("$.active", is(modifiedTestRoute1.isActive())));

        verify(routeControllerMock, times(1)).existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any());
        verify(routeControllerMock, times(1)).modifyAPIRoute(eq(0), eq(0), eq(0), Matchers.<APIRoute>any());
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyRoute_shouldReturnAlreadyCreated() throws Exception {
        when(routeControllerMock.existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any())).thenReturn(true);

        mockMvc.perform(put("/user/0/travel/0/route/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestRouteJson.toString().getBytes()))
                .andExpect(status().is(409))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(CREATE_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(409)));

        verify(routeControllerMock, times(1)).existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any());
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyRoute_shouldReturnAlreadyExisting() throws Exception {
        when(routeControllerMock.existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any())).thenThrow(new DataAccessException(""));
        when(routeControllerMock.getAPIRoute(eq(0), eq(0), eq(0))).thenReturn(modifiedTestRoute1);

        mockMvc.perform(put("/user/0/travel/0/route/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestRouteJson.toString().getBytes()))
                .andExpect(status().is(409))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(409)));

        verify(routeControllerMock, times(1)).existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any());
        verify(routeControllerMock, times(1)).getAPIRoute(eq(0), eq(0), eq(0));
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyRoute_shouldReturnNotExisting() throws Exception {
        when(routeControllerMock.existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any())).thenThrow(new DataAccessException(""));
        when(routeControllerMock.getAPIRoute(eq(0), eq(0), eq(0))).thenReturn(null);

        mockMvc.perform(put("/user/0/travel/0/route/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestRouteJson.toString().getBytes()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(routeControllerMock, times(1)).existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any());
        verify(routeControllerMock, times(1)).getAPIRoute(eq(0), eq(0), eq(0));
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyRoute_shouldReturnWentWrong() throws Exception {
        when(routeControllerMock.existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any())).thenThrow(new DataAccessException(""));
        when(routeControllerMock.getAPIRoute(eq(0), eq(0), eq(0))).thenThrow(new DataAccessException(""));

        mockMvc.perform(put("/user/0/travel/0/route/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestRouteJson.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(routeControllerMock, times(1)).existsAPIRoute(eq(0), eq(0), Matchers.<APIRoute>any());
        verify(routeControllerMock, times(1)).getAPIRoute(eq(0), eq(0), eq(0));
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deleteRoute_shouldDeleteCorrect() throws Exception {
        when(routeControllerMock.deleteAPIRoute(0, 0, 1)).thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/user/0/travel/0/route/1"))
                .andExpect(status().is(204));

        verify(routeControllerMock, times(1)).deleteAPIRoute(0, 0, 1);
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deleteRoute_shouldReturnNotExisting() throws Exception {
        when(routeControllerMock.deleteAPIRoute(0, 0, 1)).thenReturn(Boolean.FALSE);

        mockMvc.perform(delete("/user/0/travel/0/route/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(routeControllerMock, times(1)).deleteAPIRoute(0, 0, 1);
        verifyNoMoreInteractions(routeControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deleteRoute_shouldReturnWentWrong() throws Exception {
        when(routeControllerMock.deleteAPIRoute(0, 0, 1)).thenThrow(new DataAccessException(""));

        mockMvc.perform(delete("/user/0/travel/0/route/1"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(routeControllerMock, times(1)).deleteAPIRoute(0, 0, 1);
        verifyNoMoreInteractions(routeControllerMock);
    }

}
