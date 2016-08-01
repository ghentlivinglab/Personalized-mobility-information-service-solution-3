package Spring;

import Other.TestUtils;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vop.groep7.vop7backend.Controllers.TravelController;
import vop.groep7.vop7backend.Models.APIModels.APIAddress;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APILinks;
import vop.groep7.vop7backend.Models.APIModels.APITravel;
import vop.groep7.vop7backend.Spring.SpringTravelController;
import vop.groep7.vop7backend.database.DataAccessException;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 *
 * @author Jonas Van Wilder
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SpringTravelControllerTest {

    @Mock
    private TravelController travelControllerMock;

    @InjectMocks
    private SpringTravelController springTravelController;

    private MockMvc mockMvc;

    private final List<APITravel> testTravels;
    private final JSONObject testTravelJson;
    private APITravel modifiedTestTravel1;
    private JSONObject modifiedTestTravelJson;

    private static final String BASIC_ERROR = "The database is not available at the moment.";
    private static final String CREATE_EXISTS_ERROR = "The travel you are trying to create already exists.";
    private static final String EXISTS_ERROR = "The travel already exists.";
    private static final String NOT_EXISTS_ERROR = "The travel doesn't exist.";
    private static final String INVALID_INPUT = "The input of one or more fields was invalid or missing";

    /**
     *
     */
    public SpringTravelControllerTest() {
        testTravels = new ArrayList<>();
        // create json object of a test travel
        testTravelJson = new JSONObject();
        testTravelJson.put("id", "0");
        testTravelJson.put("name", "Test travel 1");
        String[] time_interval = new String[]{"08:00", "09:00"};
        testTravelJson.put("time_interval", time_interval);
        APILinks[] links = new APILinks[1];
        links[0] = new APILinks();
        links[0].setRel("self");
        links[0].setHref("https://vopro7.ugent.be/user/0/travel/0");
        testTravelJson.put("links", links);
        testTravelJson.put("recurring", new Boolean[]{true, true, true, true, true, false, false});
        JSONObject a1 = new JSONObject();
        a1.put("country", "BE");
        a1.put("city", "Gent");
        a1.put("postal_code", 9000);
        a1.put("street", "De straat");
        a1.put("housenumber", "12");
        JSONObject c1 = new JSONObject();
        c1.put("lat", 51.560559);
        c1.put("lon", 3.724928);
        a1.put("coordinates", c1);
        testTravelJson.put("startpoint", a1);
        testTravelJson.put("endpoint", a1);

        createTestTravels();
        createModifiedtestTravel();

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
        this.mockMvc = MockMvcBuilders.standaloneSetup(springTravelController).build();
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    private void createTestTravels() {
        // create test travel 1
        APITravel testTravel1 = new APITravel();
        testTravel1.setId("0");
        testTravel1.setName("Test travel 1");
        String[] time_interval = new String[]{"08:00", "09:00"};
        testTravel1.setTimeInterval(time_interval);
        APILinks[] links1 = new APILinks[1];
        links1[0] = new APILinks();
        links1[0].setRel("self");
        links1[0].setHref("https://vopro7.ugent.be/user/0/travel/0");
        testTravel1.setLinks(links1);
        testTravel1.setRecurring(new Boolean[]{true, true, true, true, true, false, false});
        APIAddress a1 = new APIAddress();
        a1.setCountry("BE");
        a1.setCity("Gent");
        a1.setPostalCode(9000);
        a1.setStreet("De straat");
        a1.setHousenumber("12");
        APICoordinate c1 = new APICoordinate();
        c1.setLat(51.560559);
        c1.setLon(3.724928);
        a1.setCoordinates(c1);
        testTravel1.setStartpoint(a1);
        testTravel1.setEndpoint(a1);
        testTravels.add(testTravel1);

        // create test travel 2
        APITravel testTravel2 = new APITravel();
        testTravel2.setId("1");
        testTravel2.setName("Test travel 2");
        String[] time_interval2 = new String[]{"11:00", "11:30"};
        testTravel2.setTimeInterval(time_interval2);
        APILinks[] links2 = new APILinks[1];
        links2[0] = new APILinks();
        links2[0].setRel("self");
        links2[0].setHref("https://vopro7.ugent.be/user/0/travel/1");
        testTravel2.setLinks(links2);
        testTravel2.setRecurring(new Boolean[]{false, false, false, false, false, true, false});
        testTravel2.setStartpoint(a1);
        testTravel2.setEndpoint(a1);
        testTravels.add(testTravel2);
    }

    private void createModifiedtestTravel() {
        // create modified test travel
        modifiedTestTravel1 = testTravels.get(0);
        modifiedTestTravel1.setName("Other test travel");
        String[] time_interval = new String[]{"12:00", "13:00"};
        modifiedTestTravel1.setTimeInterval(time_interval);
        Boolean[] r = new Boolean[]{false, true, true, true, true, false, false};
        modifiedTestTravel1.setRecurring(r);
        APIAddress a1 = new APIAddress();
        a1.setCountry("BE");
        a1.setCity("Gent");
        a1.setPostalCode(9000);
        a1.setStreet("Andere straat");
        a1.setHousenumber("32");
        APICoordinate c1 = new APICoordinate();
        c1.setLat(50.560559);
        c1.setLon(3.824928);
        a1.setCoordinates(c1);
        modifiedTestTravel1.setStartpoint(a1);
        modifiedTestTravel1.setEndpoint(a1);

        // create json object of modified test travel
        modifiedTestTravelJson = new JSONObject();
        modifiedTestTravelJson.put("id", "0");
        modifiedTestTravelJson.put("name", "Other test travel");
        APILinks[] links = new APILinks[1];
        links[0] = new APILinks();
        links[0].setRel("self");
        links[0].setHref("https://vopro7.ugent.be/user/0/travel/0");
        modifiedTestTravelJson.put("links", links);
        modifiedTestTravelJson.put("time_interval", time_interval);
        modifiedTestTravelJson.put("recurring", r);
        JSONObject a = new JSONObject();
        a.put("country", "BE");
        a.put("city", "Gent");
        a.put("postal_code", 9000);
        a.put("street", "Andere straat");
        a.put("housenumber", "32");
        JSONObject c = new JSONObject();
        c.put("lat", 50.560559);
        c.put("lon", 3.824928);
        a.put("coordinates", c1);
        modifiedTestTravelJson.put("startpoint", a);
        modifiedTestTravelJson.put("endpoint", a);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getAllTravels_ShouldReturnListOfAPITravelsForUser() throws Exception {
        when(travelControllerMock.getAPITravels(0)).thenReturn(testTravels);

        mockMvc.perform(get("/user/0/travel/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(testTravels.size())))
                .andExpect(jsonPath("$[0].id", is(testTravels.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(testTravels.get(0).getName())))
                .andExpect(jsonPath("$[0].time_interval", hasSize(testTravels.get(0).getTimeInterval().length)))
                .andExpect(jsonPath("$[0].time_interval[0]", is(testTravels.get(0).getTimeInterval()[0])))
                .andExpect(jsonPath("$[0].time_interval[1]", is(testTravels.get(0).getTimeInterval()[1])))
                .andExpect(jsonPath("$[0].links", hasSize(testTravels.get(0).getLinks().length)))
                .andExpect(jsonPath("$[0].links[0].rel", is(testTravels.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[0].links[0].href", is(testTravels.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[0].recurring", hasSize(testTravels.get(0).getRecurring().length)))
                .andExpect(jsonPath("$[0].recurring[0]", is(testTravels.get(0).getRecurring()[0])))
                .andExpect(jsonPath("$[0].recurring[1]", is(testTravels.get(0).getRecurring()[1])))
                .andExpect(jsonPath("$[0].recurring[2]", is(testTravels.get(0).getRecurring()[2])))
                .andExpect(jsonPath("$[0].recurring[3]", is(testTravels.get(0).getRecurring()[3])))
                .andExpect(jsonPath("$[0].recurring[4]", is(testTravels.get(0).getRecurring()[4])))
                .andExpect(jsonPath("$[0].recurring[5]", is(testTravels.get(0).getRecurring()[5])))
                .andExpect(jsonPath("$[0].recurring[6]", is(testTravels.get(0).getRecurring()[6])))
                .andExpect(jsonPath("$[0].startpoint.country", is(testTravels.get(0).getStartpoint().getCountry())))
                .andExpect(jsonPath("$[0].startpoint.city", is(testTravels.get(0).getStartpoint().getCity())))
                .andExpect(jsonPath("$[0].startpoint.postal_code", is(testTravels.get(0).getStartpoint().getPostalCode())))
                .andExpect(jsonPath("$[0].startpoint.street", is(testTravels.get(0).getStartpoint().getStreet())))
                .andExpect(jsonPath("$[0].startpoint.housenumber", is(testTravels.get(0).getStartpoint().getHousenumber())))
                .andExpect(jsonPath("$[0].startpoint.coordinates.lat", is(testTravels.get(0).getStartpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$[0].startpoint.coordinates.lon", is(testTravels.get(0).getStartpoint().getCoordinates().getLon())))
                .andExpect(jsonPath("$[0].endpoint.country", is(testTravels.get(0).getEndpoint().getCountry())))
                .andExpect(jsonPath("$[0].endpoint.city", is(testTravels.get(0).getEndpoint().getCity())))
                .andExpect(jsonPath("$[0].endpoint.postal_code", is(testTravels.get(0).getEndpoint().getPostalCode())))
                .andExpect(jsonPath("$[0].endpoint.street", is(testTravels.get(0).getEndpoint().getStreet())))
                .andExpect(jsonPath("$[0].endpoint.housenumber", is(testTravels.get(0).getEndpoint().getHousenumber())))
                .andExpect(jsonPath("$[0].endpoint.coordinates.lat", is(testTravels.get(0).getEndpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$[0].endpoint.coordinates.lon", is(testTravels.get(0).getEndpoint().getCoordinates().getLon())))
                .andExpect(jsonPath("$[1].id", is(testTravels.get(1).getId())))
                .andExpect(jsonPath("$[1].name", is(testTravels.get(1).getName())))
                .andExpect(jsonPath("$[1].time_interval", hasSize(testTravels.get(1).getTimeInterval().length)))
                .andExpect(jsonPath("$[1].time_interval[0]", is(testTravels.get(1).getTimeInterval()[0])))
                .andExpect(jsonPath("$[1].time_interval[1]", is(testTravels.get(1).getTimeInterval()[1])))
                .andExpect(jsonPath("$[1].links", hasSize(testTravels.get(1).getLinks().length)))
                .andExpect(jsonPath("$[1].links[0].rel", is(testTravels.get(1).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[1].links[0].href", is(testTravels.get(1).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[1].recurring", hasSize(testTravels.get(1).getRecurring().length)))
                .andExpect(jsonPath("$[1].recurring[0]", is(testTravels.get(1).getRecurring()[0])))
                .andExpect(jsonPath("$[1].recurring[1]", is(testTravels.get(1).getRecurring()[1])))
                .andExpect(jsonPath("$[1].recurring[2]", is(testTravels.get(1).getRecurring()[2])))
                .andExpect(jsonPath("$[1].recurring[3]", is(testTravels.get(1).getRecurring()[3])))
                .andExpect(jsonPath("$[1].recurring[4]", is(testTravels.get(1).getRecurring()[4])))
                .andExpect(jsonPath("$[1].recurring[5]", is(testTravels.get(1).getRecurring()[5])))
                .andExpect(jsonPath("$[1].recurring[6]", is(testTravels.get(1).getRecurring()[6])))
                .andExpect(jsonPath("$[1].startpoint.country", is(testTravels.get(1).getStartpoint().getCountry())))
                .andExpect(jsonPath("$[1].startpoint.city", is(testTravels.get(1).getStartpoint().getCity())))
                .andExpect(jsonPath("$[1].startpoint.postal_code", is(testTravels.get(1).getStartpoint().getPostalCode())))
                .andExpect(jsonPath("$[1].startpoint.street", is(testTravels.get(1).getStartpoint().getStreet())))
                .andExpect(jsonPath("$[1].startpoint.housenumber", is(testTravels.get(1).getStartpoint().getHousenumber())))
                .andExpect(jsonPath("$[1].startpoint.coordinates.lat", is(testTravels.get(1).getStartpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$[1].startpoint.coordinates.lon", is(testTravels.get(1).getStartpoint().getCoordinates().getLon())))
                .andExpect(jsonPath("$[1].endpoint.country", is(testTravels.get(1).getEndpoint().getCountry())))
                .andExpect(jsonPath("$[1].endpoint.city", is(testTravels.get(1).getEndpoint().getCity())))
                .andExpect(jsonPath("$[1].endpoint.postal_code", is(testTravels.get(1).getEndpoint().getPostalCode())))
                .andExpect(jsonPath("$[1].endpoint.street", is(testTravels.get(1).getEndpoint().getStreet())))
                .andExpect(jsonPath("$[1].endpoint.housenumber", is(testTravels.get(1).getEndpoint().getHousenumber())))
                .andExpect(jsonPath("$[1].endpoint.coordinates.lat", is(testTravels.get(1).getEndpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$[1].endpoint.coordinates.lon", is(testTravels.get(1).getEndpoint().getCoordinates().getLon())));

        verify(travelControllerMock, times(1)).getAPITravels(0);
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getAllTravels_ShouldReturnEmptyListForUser() throws Exception {
        when(travelControllerMock.getAPITravels(0)).thenReturn(new ArrayList());

        mockMvc.perform(get("/user/0/travel/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(travelControllerMock, times(1)).getAPITravels(0);
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     *
     * @throws Exception
     */
    @Test
    public void getAllAPITravels_ShouldReturnWentWrong() throws Exception {
        when(travelControllerMock.getAPITravels(0)).thenThrow(new DataAccessException(""));

        mockMvc.perform(get("/user/0/travel/"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(travelControllerMock, times(1)).getAPITravels(0);
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getTravel_shouldReturnAPITravel() throws Exception {
        when(travelControllerMock.getAPITravel(0, 0)).thenReturn(testTravels.get(0));

        mockMvc.perform(get("/user/0/travel/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testTravels.get(0).getId())))
                .andExpect(jsonPath("$.name", is(testTravels.get(0).getName())))
                .andExpect(jsonPath("$.time_interval", hasSize(testTravels.get(0).getTimeInterval().length)))
                .andExpect(jsonPath("$.time_interval[0]", is(testTravels.get(0).getTimeInterval()[0])))
                .andExpect(jsonPath("$.time_interval[1]", is(testTravels.get(0).getTimeInterval()[1])))
                .andExpect(jsonPath("$.links", hasSize(testTravels.get(0).getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(testTravels.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(testTravels.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$.recurring", hasSize(testTravels.get(0).getRecurring().length)))
                .andExpect(jsonPath("$.recurring[0]", is(testTravels.get(0).getRecurring()[0])))
                .andExpect(jsonPath("$.recurring[1]", is(testTravels.get(0).getRecurring()[1])))
                .andExpect(jsonPath("$.recurring[2]", is(testTravels.get(0).getRecurring()[2])))
                .andExpect(jsonPath("$.recurring[3]", is(testTravels.get(0).getRecurring()[3])))
                .andExpect(jsonPath("$.recurring[4]", is(testTravels.get(0).getRecurring()[4])))
                .andExpect(jsonPath("$.recurring[5]", is(testTravels.get(0).getRecurring()[5])))
                .andExpect(jsonPath("$.recurring[6]", is(testTravels.get(0).getRecurring()[6])))
                .andExpect(jsonPath("$.startpoint.country", is(testTravels.get(0).getStartpoint().getCountry())))
                .andExpect(jsonPath("$.startpoint.city", is(testTravels.get(0).getStartpoint().getCity())))
                .andExpect(jsonPath("$.startpoint.postal_code", is(testTravels.get(0).getStartpoint().getPostalCode())))
                .andExpect(jsonPath("$.startpoint.street", is(testTravels.get(0).getStartpoint().getStreet())))
                .andExpect(jsonPath("$.startpoint.housenumber", is(testTravels.get(0).getStartpoint().getHousenumber())))
                .andExpect(jsonPath("$.startpoint.coordinates.lat", is(testTravels.get(0).getStartpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$.startpoint.coordinates.lon", is(testTravels.get(0).getStartpoint().getCoordinates().getLon())))
                .andExpect(jsonPath("$.endpoint.country", is(testTravels.get(0).getEndpoint().getCountry())))
                .andExpect(jsonPath("$.endpoint.city", is(testTravels.get(0).getEndpoint().getCity())))
                .andExpect(jsonPath("$.endpoint.postal_code", is(testTravels.get(0).getEndpoint().getPostalCode())))
                .andExpect(jsonPath("$.endpoint.street", is(testTravels.get(0).getEndpoint().getStreet())))
                .andExpect(jsonPath("$.endpoint.housenumber", is(testTravels.get(0).getEndpoint().getHousenumber())))
                .andExpect(jsonPath("$.endpoint.coordinates.lat", is(testTravels.get(0).getEndpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$.endpoint.coordinates.lon", is(testTravels.get(0).getEndpoint().getCoordinates().getLon())));

        verify(travelControllerMock, times(1)).getAPITravel(0, 0);
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getTravel_shouldReturnAPITravelNotFound() throws Exception {
        when(travelControllerMock.getAPITravel(0, 0)).thenReturn(null);

        mockMvc.perform(get("/user/0/travel/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(travelControllerMock, times(1)).getAPITravel(0, 0);
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getTravel_shouldReturnWentWrong() throws Exception {
        when(travelControllerMock.getAPITravel(0, 0)).thenThrow(new DataAccessException(""));

        mockMvc.perform(get("/user/0/travel/0"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(travelControllerMock, times(1)).getAPITravel(0, 0);
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createTravel_shouldCreateCorrect() throws Exception {
        when(travelControllerMock.existsAPITravel(eq(0), Matchers.<APITravel>any())).thenReturn(false);
        when(travelControllerMock.createAPITravel(eq(0), Matchers.<APITravel>any())).thenReturn(testTravels.get(0));

        mockMvc.perform(post("/user/0/travel/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testTravelJson.toString().getBytes()))
                .andExpect(status().is(201))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testTravels.get(0).getId())))
                .andExpect(jsonPath("$.name", is(testTravels.get(0).getName())))
                .andExpect(jsonPath("$.time_interval", hasSize(testTravels.get(0).getTimeInterval().length)))
                .andExpect(jsonPath("$.time_interval[0]", is(testTravels.get(0).getTimeInterval()[0])))
                .andExpect(jsonPath("$.time_interval[1]", is(testTravels.get(0).getTimeInterval()[1])))
                .andExpect(jsonPath("$.links", hasSize(testTravels.get(0).getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(testTravels.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(testTravels.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$.recurring", hasSize(testTravels.get(0).getRecurring().length)))
                .andExpect(jsonPath("$.recurring[0]", is(testTravels.get(0).getRecurring()[0])))
                .andExpect(jsonPath("$.recurring[1]", is(testTravels.get(0).getRecurring()[1])))
                .andExpect(jsonPath("$.recurring[2]", is(testTravels.get(0).getRecurring()[2])))
                .andExpect(jsonPath("$.recurring[3]", is(testTravels.get(0).getRecurring()[3])))
                .andExpect(jsonPath("$.recurring[4]", is(testTravels.get(0).getRecurring()[4])))
                .andExpect(jsonPath("$.recurring[5]", is(testTravels.get(0).getRecurring()[5])))
                .andExpect(jsonPath("$.recurring[6]", is(testTravels.get(0).getRecurring()[6])))
                .andExpect(jsonPath("$.startpoint.country", is(testTravels.get(0).getStartpoint().getCountry())))
                .andExpect(jsonPath("$.startpoint.city", is(testTravels.get(0).getStartpoint().getCity())))
                .andExpect(jsonPath("$.startpoint.postal_code", is(testTravels.get(0).getStartpoint().getPostalCode())))
                .andExpect(jsonPath("$.startpoint.street", is(testTravels.get(0).getStartpoint().getStreet())))
                .andExpect(jsonPath("$.startpoint.housenumber", is(testTravels.get(0).getStartpoint().getHousenumber())))
                .andExpect(jsonPath("$.startpoint.coordinates.lat", is(testTravels.get(0).getStartpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$.startpoint.coordinates.lon", is(testTravels.get(0).getStartpoint().getCoordinates().getLon())))
                .andExpect(jsonPath("$.endpoint.country", is(testTravels.get(0).getEndpoint().getCountry())))
                .andExpect(jsonPath("$.endpoint.city", is(testTravels.get(0).getEndpoint().getCity())))
                .andExpect(jsonPath("$.endpoint.postal_code", is(testTravels.get(0).getEndpoint().getPostalCode())))
                .andExpect(jsonPath("$.endpoint.street", is(testTravels.get(0).getEndpoint().getStreet())))
                .andExpect(jsonPath("$.endpoint.housenumber", is(testTravels.get(0).getEndpoint().getHousenumber())))
                .andExpect(jsonPath("$.endpoint.coordinates.lat", is(testTravels.get(0).getEndpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$.endpoint.coordinates.lon", is(testTravels.get(0).getEndpoint().getCoordinates().getLon())));

        verify(travelControllerMock, times(1)).existsAPITravel(eq(0), Matchers.<APITravel>any());
        verify(travelControllerMock, times(1)).createAPITravel(eq(0), Matchers.<APITravel>any());
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createTravel_shouldReturnAlreadyExists() throws Exception {
        when(travelControllerMock.existsAPITravel(eq(0), Matchers.<APITravel>any())).thenReturn(true);

        mockMvc.perform(post("/user/0/travel/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testTravelJson.toString().getBytes()))
                .andExpect(status().is(409))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(CREATE_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(409)));

        verify(travelControllerMock, times(1)).existsAPITravel(eq(0), Matchers.<APITravel>any());
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createTravel_shouldReturnNotExisting() throws Exception {
        when(travelControllerMock.existsAPITravel(eq(0), Matchers.<APITravel>any())).thenReturn(false);
        when(travelControllerMock.createAPITravel(eq(0), Matchers.<APITravel>any())).thenReturn(null);

        mockMvc.perform(post("/user/0/travel/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testTravelJson.toString().getBytes()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(travelControllerMock, times(1)).existsAPITravel(eq(0), Matchers.<APITravel>any());
        verify(travelControllerMock, times(1)).createAPITravel(eq(0), Matchers.<APITravel>any());
        verifyNoMoreInteractions(travelControllerMock);
    }

    @Test
    public void createTravel_shouldReturnInvalidInput() throws Exception {
        JSONObject testTravelInput = new JSONObject();
        JSONObject testAddressInput = new JSONObject();
        testAddressInput.put("country", "BE");
        testAddressInput.put("street", "street");
        testAddressInput.put("housenumber", "12");
        testAddressInput.put("city", "gent");
        JSONObject testCoordinatesInput = new JSONObject();
        testCoordinatesInput.put("lat", 50.0);
        testCoordinatesInput.put("lon", 4.0);
        testAddressInput.put("coordinates", testCoordinatesInput);
        testTravelInput.put("startpoint", testAddressInput);
        testTravelInput.put("endpoint", testAddressInput);
        mockMvc.perform(post("/user/0/travel/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testTravelInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(5)))
                .andExpect(jsonPath("$.fields[0]", is("name")))
                .andExpect(jsonPath("$.fields[1]", is("time_interval")))
                .andExpect(jsonPath("$.fields[2]", is("startpoint.postal_code")))
                .andExpect(jsonPath("$.fields[3]", is("endpoint.postal_code")))
                .andExpect(jsonPath("$.fields[4]", is("recurring")));

        verifyNoMoreInteractions(travelControllerMock);
    }

    @Test
    public void createTravel_shouldReturnInvalidInput2() throws Exception {
        JSONObject testTravelInput = new JSONObject();
        JSONObject testAddressInput = new JSONObject();
        testAddressInput.put("country", "Belgie");
        testAddressInput.put("street", "street");
        testAddressInput.put("city", "Gent");
        testAddressInput.put("housenumber", "12");
        testAddressInput.put("postal_code", 9000);
        JSONObject testCoordinatesInput = new JSONObject();
        testAddressInput.put("coordinates", testCoordinatesInput);
        testTravelInput.put("startpoint", testAddressInput);
        testTravelInput.put("endpoint", testAddressInput);
        testTravelInput.put("name", "");
        testTravelInput.put("time_interval", new String[]{"12:00"});
        testTravelInput.put("recurring", new Boolean[]{false, true});
        mockMvc.perform(post("/user/0/travel/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testTravelInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(5)))
                .andExpect(jsonPath("$.fields[0]", is("name")))
                .andExpect(jsonPath("$.fields[1]", is("time_interval")))
                .andExpect(jsonPath("$.fields[2]", is("startpoint.country")))
                .andExpect(jsonPath("$.fields[3]", is("endpoint.country")))
                .andExpect(jsonPath("$.fields[4]", is("recurring")));

        verifyNoMoreInteractions(travelControllerMock);
    }
    /**
     *
     * @throws Exception
     */
    @Test
    public void createTravel_shouldReturnWentWrong() throws Exception {
        when(travelControllerMock.existsAPITravel(eq(0), Matchers.<APITravel>any())).thenThrow(new DataAccessException(""));

        mockMvc.perform(post("/user/0/travel/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testTravelJson.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(travelControllerMock, times(1)).existsAPITravel(eq(0), Matchers.<APITravel>any());
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyTravel_shouldModifyCorrect() throws Exception {
        when(travelControllerMock.existsAPITravel(eq(0), Matchers.<APITravel>any())).thenReturn(false);
        when(travelControllerMock.modifyAPITravel(eq(0), eq(0), Matchers.<APITravel>any())).thenReturn(modifiedTestTravel1);

        mockMvc.perform(put("/user/0/travel/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestTravelJson.toString().getBytes()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(modifiedTestTravel1.getId())))
                .andExpect(jsonPath("$.name", is(modifiedTestTravel1.getName())))
                .andExpect(jsonPath("$.time_interval", hasSize(modifiedTestTravel1.getTimeInterval().length)))
                .andExpect(jsonPath("$.time_interval[0]", is(modifiedTestTravel1.getTimeInterval()[0])))
                .andExpect(jsonPath("$.time_interval[1]", is(modifiedTestTravel1.getTimeInterval()[1])))
                .andExpect(jsonPath("$.links", hasSize(modifiedTestTravel1.getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(modifiedTestTravel1.getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(modifiedTestTravel1.getLinks()[0].getHref())))
                .andExpect(jsonPath("$.recurring", hasSize(modifiedTestTravel1.getRecurring().length)))
                .andExpect(jsonPath("$.recurring[0]", is(modifiedTestTravel1.getRecurring()[0])))
                .andExpect(jsonPath("$.recurring[1]", is(modifiedTestTravel1.getRecurring()[1])))
                .andExpect(jsonPath("$.recurring[2]", is(modifiedTestTravel1.getRecurring()[2])))
                .andExpect(jsonPath("$.recurring[3]", is(modifiedTestTravel1.getRecurring()[3])))
                .andExpect(jsonPath("$.recurring[4]", is(modifiedTestTravel1.getRecurring()[4])))
                .andExpect(jsonPath("$.recurring[5]", is(modifiedTestTravel1.getRecurring()[5])))
                .andExpect(jsonPath("$.recurring[6]", is(modifiedTestTravel1.getRecurring()[6])))
                .andExpect(jsonPath("$.startpoint.country", is(modifiedTestTravel1.getStartpoint().getCountry())))
                .andExpect(jsonPath("$.startpoint.city", is(modifiedTestTravel1.getStartpoint().getCity())))
                .andExpect(jsonPath("$.startpoint.postal_code", is(modifiedTestTravel1.getStartpoint().getPostalCode())))
                .andExpect(jsonPath("$.startpoint.street", is(modifiedTestTravel1.getStartpoint().getStreet())))
                .andExpect(jsonPath("$.startpoint.housenumber", is(modifiedTestTravel1.getStartpoint().getHousenumber())))
                .andExpect(jsonPath("$.startpoint.coordinates.lat", is(modifiedTestTravel1.getStartpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$.startpoint.coordinates.lon", is(modifiedTestTravel1.getStartpoint().getCoordinates().getLon())))
                .andExpect(jsonPath("$.endpoint.country", is(modifiedTestTravel1.getEndpoint().getCountry())))
                .andExpect(jsonPath("$.endpoint.city", is(modifiedTestTravel1.getEndpoint().getCity())))
                .andExpect(jsonPath("$.endpoint.postal_code", is(modifiedTestTravel1.getEndpoint().getPostalCode())))
                .andExpect(jsonPath("$.endpoint.street", is(modifiedTestTravel1.getEndpoint().getStreet())))
                .andExpect(jsonPath("$.endpoint.housenumber", is(modifiedTestTravel1.getEndpoint().getHousenumber())))
                .andExpect(jsonPath("$.endpoint.coordinates.lat", is(modifiedTestTravel1.getEndpoint().getCoordinates().getLat())))
                .andExpect(jsonPath("$.endpoint.coordinates.lon", is(modifiedTestTravel1.getEndpoint().getCoordinates().getLon())));

        verify(travelControllerMock, times(1)).existsAPITravel(eq(0), Matchers.<APITravel>any());
        verify(travelControllerMock, times(1)).modifyAPITravel(eq(0), eq(0), Matchers.<APITravel>any());
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyTravel_shouldReturnAlreadyCreated() throws Exception {
        when(travelControllerMock.existsAPITravel(eq(0), Matchers.<APITravel>any())).thenReturn(true);

        mockMvc.perform(put("/user/0/travel/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestTravelJson.toString().getBytes()))
                .andExpect(status().is(409))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(CREATE_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(409)));

        verify(travelControllerMock, times(1)).existsAPITravel(eq(0), Matchers.<APITravel>any());
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyTravel_shouldReturnAlreadyExists() throws Exception {
        when(travelControllerMock.existsAPITravel(eq(0), Matchers.<APITravel>any())).thenThrow(new DataAccessException(""));
        when(travelControllerMock.getAPITravel(eq(0), eq(0))).thenReturn(testTravels.get(0));

        mockMvc.perform(put("/user/0/travel/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestTravelJson.toString().getBytes()))
                .andExpect(status().is(409))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(409)));

        verify(travelControllerMock, times(1)).existsAPITravel(eq(0), Matchers.<APITravel>any());
        verify(travelControllerMock, times(1)).getAPITravel(eq(0), eq(0));
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyTravel_shouldReturnNotExisting() throws Exception {
        when(travelControllerMock.existsAPITravel(eq(0), Matchers.<APITravel>any())).thenThrow(new DataAccessException(""));
        when(travelControllerMock.getAPITravel(eq(0), eq(0))).thenReturn(null);

        mockMvc.perform(put("/user/0/travel/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestTravelJson.toString().getBytes()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(travelControllerMock, times(1)).existsAPITravel(eq(0), Matchers.<APITravel>any());
        verify(travelControllerMock, times(1)).getAPITravel(eq(0), eq(0));
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyTravel_shouldReturnWentWrong() throws Exception {
        when(travelControllerMock.existsAPITravel(eq(0), Matchers.<APITravel>any())).thenThrow(new DataAccessException(""));
        when(travelControllerMock.getAPITravel(eq(0), eq(0))).thenThrow(new DataAccessException(""));

        mockMvc.perform(put("/user/0/travel/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestTravelJson.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(travelControllerMock, times(1)).existsAPITravel(eq(0), Matchers.<APITravel>any());
        verify(travelControllerMock, times(1)).getAPITravel(eq(0), eq(0));
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deleteTravel_shouldDeleteCorrect() throws Exception {
        when(travelControllerMock.deleteAPITravel(0, 1)).thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/user/0/travel/1"))
                .andExpect(status().is(204));

        verify(travelControllerMock, times(1)).deleteAPITravel(0, 1);
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deleteTravel_shouldReturnNotExisting() throws Exception {
        when(travelControllerMock.deleteAPITravel(0, 1)).thenReturn(Boolean.FALSE);

        mockMvc.perform(delete("/user/0/travel/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(travelControllerMock, times(1)).deleteAPITravel(0, 1);
        verifyNoMoreInteractions(travelControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deleteTravel_shouldReturnWentWrong() throws Exception {
        when(travelControllerMock.deleteAPITravel(0, 1)).thenThrow(new DataAccessException(""));

        mockMvc.perform(delete("/user/0/travel/1"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(travelControllerMock, times(1)).deleteAPITravel(0, 1);
        verifyNoMoreInteractions(travelControllerMock);
    }

}
