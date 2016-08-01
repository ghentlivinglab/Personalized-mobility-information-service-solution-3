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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vop.groep7.vop7backend.Controllers.POIController;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APILinks;
import vop.groep7.vop7backend.Models.APIModels.APINotify;
import vop.groep7.vop7backend.Models.APIModels.APIPOI;
import vop.groep7.vop7backend.Spring.SpringPOIController;
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
public class SpringPOIControllerTest {

    @Mock
    private POIController poiControllerMock;

    @InjectMocks
    private SpringPOIController springPOIController;

    private MockMvc mockMvc;

    private final List<APIPOI> testPOIs;
    private final JSONObject testPOIJson;
    private APIPOI modifiedTestPOI;
    private JSONObject modifiedTestPOIJson;

    private static final String BASIC_ERROR = "The database is not available at the moment.";
    private static final String CREATE_EXISTS_ERROR = "The point of interest you are trying to create already exists.";
    private static final String EXISTS_ERROR = "The point of interest already exists.";
    private static final String NOT_EXISTS_ERROR = "The point of interest doesn't exist.";
    private static final String INVALID_INPUT = "The input of one or more fields was invalid or missing";

    /**
     *
     */
    public SpringPOIControllerTest() {
        testPOIs = new ArrayList<>();
        // create json object of test POI
        testPOIJson = new JSONObject();
        testPOIJson.put("id", "0");
        testPOIJson.put("active", true);
        testPOIJson.put("name", "test POI 1");
        testPOIJson.put("radius", 100);
        APILinks[] links1 = new APILinks[1];
        links1[0] = new APILinks();
        links1[0].setRel("self");
        links1[0].setHref("https://vopro7.ugent.be/user/0/point_of_interest/0");
        testPOIJson.put("links", links1);
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
        testPOIJson.put("address", a1);
        APIEventType[] e = new APIEventType[1];
        e[0] = null;
        testPOIJson.put("notify_for_event_types", e);
        JSONObject n = new JSONObject();
        n.put("email", false);
        n.put("cell_number", false);
        testPOIJson.put("notify", n);
        createTestPOIs();
        createModifiedTestPOI();
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(springPOIController).build();
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    private void createTestPOIs() {
        // create test poi 1
        APIPOI testPOI1 = new APIPOI();
        testPOI1.setId("0");
        testPOI1.setActive(true);
        testPOI1.setName("Test POI 1");
        testPOI1.setRadius(100);
        APILinks[] links1 = new APILinks[1];
        links1[0] = new APILinks();
        links1[0].setRel("self");
        links1[0].setHref("https://vopro7.ugent.be/user/0/point_of_interest/0");
        testPOI1.setLinks(links1);
        testPOI1.setAddress(null);
        APIEventType[] e = new APIEventType[1];
        e[0] = null;
        testPOI1.setNotifyForEventTypes(e);
        APINotify n = new APINotify();
        n.setEmailNotify(false);
        n.setCellNumberNotify(false);
        testPOI1.setNotify(n);
        testPOIs.add(testPOI1);

        // create test poi 2
        APIPOI testPOI2 = new APIPOI();
        testPOI2.setId("1");
        testPOI2.setActive(true);
        testPOI2.setName("Test POI 2");
        testPOI2.setRadius(1000);
        APILinks[] links2 = new APILinks[1];
        links2[0] = new APILinks();
        links2[0].setRel("self");
        links2[0].setHref("https://vopro7.ugent.be/user/0/point_of_interest/1");
        testPOI2.setLinks(links2);
        testPOI2.setAddress(null);
        testPOI2.setNotifyForEventTypes(e);
        APINotify n2 = new APINotify();
        n2.setEmailNotify(false);
        n2.setCellNumberNotify(false);
        testPOI2.setNotify(n);
        testPOIs.add(testPOI2);
    }

    private void createModifiedTestPOI() {
        // create modified test poi
        modifiedTestPOI = testPOIs.get(0);
        modifiedTestPOI.setActive(false);
        modifiedTestPOI.setName("Andere test POI");
        APINotify n = new APINotify();
        n.setEmailNotify(true);
        n.setCellNumberNotify(true);
        modifiedTestPOI.setNotify(n);
        modifiedTestPOI.setRadius(1000);

        // create json object of modified test poi
        modifiedTestPOIJson = new JSONObject();
        modifiedTestPOIJson.put("id", "0");
        modifiedTestPOIJson.put("active", false);
        modifiedTestPOIJson.put("name", "Andere test POI");
        modifiedTestPOIJson.put("radius", 1000);
        APILinks[] links1 = new APILinks[1];
        links1[0] = new APILinks();
        links1[0].setRel("self");
        links1[0].setHref("https://vopro7.ugent.be/user/0/point_of_interest/0");
        modifiedTestPOIJson.put("links", links1);
        JSONObject a1 = new JSONObject();
        a1.put("country", "BE");
        a1.put("city", "Gent");
        a1.put("postal_code", 9000);
        a1.put("street", "Andere straat");
        a1.put("housenumber", "32");
        JSONObject c1 = new JSONObject();
        c1.put("lat", 50.540559);
        c1.put("lon", 3.824978);
        a1.put("coordinates", c1);
        modifiedTestPOIJson.put("address", a1);
        APIEventType[] e = new APIEventType[1];
        e[0] = null;
        JSONObject n2 = new JSONObject();
        n2.put("email", true);
        n2.put("cell_number", true);
        modifiedTestPOIJson.put("notify", n2);
        modifiedTestPOIJson.put("notify", n);
        modifiedTestPOIJson.put("notify_for_event_types", e);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getAllPOIs_ShouldReturnListOfPOIs() throws Exception {
        when(poiControllerMock.getAPIPOIs(0)).thenReturn(testPOIs);

        mockMvc.perform(get("/user/0/point_of_interest/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(testPOIs.size())))
                .andExpect(jsonPath("$[0].id", is(testPOIs.get(0).getId())))
                .andExpect(jsonPath("$[0].active", is(testPOIs.get(0).isActive())))
                .andExpect(jsonPath("$[0].name", is(testPOIs.get(0).getName())))
                .andExpect(jsonPath("$[0].radius", is(testPOIs.get(0).getRadius())))
                .andExpect(jsonPath("$[0].links", hasSize(testPOIs.get(0).getLinks().length)))
                .andExpect(jsonPath("$[0].links[0].rel", is(testPOIs.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[0].links[0].href", is(testPOIs.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[0].address", is(testPOIs.get(0).getAddress())))
                .andExpect(jsonPath("$[0].notify_for_event_types", hasSize(testPOIs.get(0).getNotifyForEventTypes().length)))
                .andExpect(jsonPath("$[0].notify_for_event_types[0]", is(testPOIs.get(0).getNotifyForEventTypes()[0])))
                .andExpect(jsonPath("$[0].notify.email", is(testPOIs.get(0).getNotify().isEmailNotify())))
                .andExpect(jsonPath("$[0].notify.cell_number", is(testPOIs.get(0).getNotify().isCellNumberNotify())))
                .andExpect(jsonPath("$[1].id", is(testPOIs.get(1).getId())))
                .andExpect(jsonPath("$[1].active", is(testPOIs.get(1).isActive())))
                .andExpect(jsonPath("$[1].name", is(testPOIs.get(1).getName())))
                .andExpect(jsonPath("$[1].radius", is(testPOIs.get(1).getRadius())))
                .andExpect(jsonPath("$[1].links", hasSize(testPOIs.get(1).getLinks().length)))
                .andExpect(jsonPath("$[1].links[0].rel", is(testPOIs.get(1).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[1].links[0].href", is(testPOIs.get(1).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[1].address", is(testPOIs.get(1).getAddress())))
                .andExpect(jsonPath("$[1].notify_for_event_types", hasSize(testPOIs.get(1).getNotifyForEventTypes().length)))
                .andExpect(jsonPath("$[1].notify_for_event_types[0]", is(testPOIs.get(1).getNotifyForEventTypes()[0])))
                .andExpect(jsonPath("$[1].notify.email", is(testPOIs.get(1).getNotify().isEmailNotify())))
                .andExpect(jsonPath("$[1].notify.cell_number", is(testPOIs.get(1).getNotify().isCellNumberNotify())));

        verify(poiControllerMock, times(1)).getAPIPOIs(0);
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getAllPOIs_ShouldReturnEmptyList() throws Exception {
        when(poiControllerMock.getAPIPOIs(0)).thenReturn(new ArrayList());

        mockMvc.perform(get("/user/0/point_of_interest/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(poiControllerMock, times(1)).getAPIPOIs(0);
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     *
     * @throws Exception
     */
    @Test
    public void getAllPOIs_ShouldReturnWentWrong() throws Exception {
        when(poiControllerMock.getAPIPOIs(0)).thenThrow(new DataAccessException(""));

        mockMvc.perform(get("/user/0/point_of_interest/"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(poiControllerMock, times(1)).getAPIPOIs(0);
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getPOI_shouldReturnPOI() throws Exception {
        when(poiControllerMock.getAPIPOI(0, 0)).thenReturn(testPOIs.get(0));

        mockMvc.perform(get("/user/0/point_of_interest/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testPOIs.get(0).getId())))
                .andExpect(jsonPath("$.active", is(testPOIs.get(0).isActive())))
                .andExpect(jsonPath("$.name", is(testPOIs.get(0).getName())))
                .andExpect(jsonPath("$.radius", is(testPOIs.get(0).getRadius())))
                .andExpect(jsonPath("$.links", hasSize(testPOIs.get(0).getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(testPOIs.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(testPOIs.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$.address", is(testPOIs.get(0).getAddress())))
                .andExpect(jsonPath("$.notify_for_event_types", hasSize(testPOIs.get(0).getNotifyForEventTypes().length)))
                .andExpect(jsonPath("$.notify_for_event_types[0]", is(testPOIs.get(0).getNotifyForEventTypes()[0])))
                .andExpect(jsonPath("$.notify.email", is(testPOIs.get(0).getNotify().isEmailNotify())))
                .andExpect(jsonPath("$.notify.cell_number", is(testPOIs.get(0).getNotify().isCellNumberNotify())));
        verify(poiControllerMock, times(1)).getAPIPOI(0, 0);
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getPOI_shouldReturnPOINotFound() throws Exception {
        when(poiControllerMock.getAPIPOI(0, 0)).thenReturn(null);

        mockMvc.perform(get("/user/0/point_of_interest/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(poiControllerMock, times(1)).getAPIPOI(0, 0);
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getPOI_shouldReturnWentWrong() throws Exception {
        when(poiControllerMock.getAPIPOI(0, 0)).thenThrow(new DataAccessException(""));

        mockMvc.perform(get("/user/0/point_of_interest/0"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(poiControllerMock, times(1)).getAPIPOI(0, 0);
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createPOI_shouldCreateCorrect() throws Exception {
        when(poiControllerMock.existsAPIPOI(eq(0), Matchers.<APIPOI>any())).thenReturn(false);
        when(poiControllerMock.createAPIPOI(eq(0), Matchers.<APIPOI>any())).thenReturn(testPOIs.get(0));

        mockMvc.perform(post("/user/0/point_of_interest/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testPOIJson.toString().getBytes()))
                .andExpect(status().is(201))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testPOIs.get(0).getId())))
                .andExpect(jsonPath("$.active", is(testPOIs.get(0).isActive())))
                .andExpect(jsonPath("$.name", is(testPOIs.get(0).getName())))
                .andExpect(jsonPath("$.radius", is(testPOIs.get(0).getRadius())))
                .andExpect(jsonPath("$.links", hasSize(testPOIs.get(0).getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(testPOIs.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(testPOIs.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$.address", is(testPOIs.get(0).getAddress())))
                .andExpect(jsonPath("$.notify_for_event_types", hasSize(testPOIs.get(0).getNotifyForEventTypes().length)))
                .andExpect(jsonPath("$.notify_for_event_types[0]", is(testPOIs.get(0).getNotifyForEventTypes()[0])))
                .andExpect(jsonPath("$.notify.email", is(testPOIs.get(0).getNotify().isEmailNotify())))
                .andExpect(jsonPath("$.notify.cell_number", is(testPOIs.get(0).getNotify().isCellNumberNotify())));

        verify(poiControllerMock, times(1)).existsAPIPOI(eq(0), Matchers.<APIPOI>any());
        verify(poiControllerMock, times(1)).createAPIPOI(eq(0), Matchers.<APIPOI>any());
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createPOI_shouldReturnAlreadyExisting() throws Exception {
        when(poiControllerMock.existsAPIPOI(eq(0), Matchers.<APIPOI>any())).thenReturn(true);

        mockMvc.perform(post("/user/0/point_of_interest/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testPOIJson.toString().getBytes()))
                .andExpect(status().is(409))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(CREATE_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(409)));

        verify(poiControllerMock, times(1)).existsAPIPOI(eq(0), Matchers.<APIPOI>any());
        verifyNoMoreInteractions(poiControllerMock);
    }

    @Test
    public void createPOI_shouldReturnInvalidInput() throws Exception {
        JSONObject testPOIInput = new JSONObject();
        JSONObject testAddressInput = new JSONObject();
        testAddressInput.put("postal_code", -5);
        testPOIInput.put("address", testAddressInput);
        mockMvc.perform(post("/user/0/point_of_interest/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testPOIInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(10)))
                .andExpect(jsonPath("$.fields[0]", is("name")))
                .andExpect(jsonPath("$.fields[1]", is("address.city")))
                .andExpect(jsonPath("$.fields[2]", is("address.postal_code")))
                .andExpect(jsonPath("$.fields[3]", is("address.street")))
                .andExpect(jsonPath("$.fields[4]", is("address.housenumber")))
                .andExpect(jsonPath("$.fields[5]", is("address.country")))
                .andExpect(jsonPath("$.fields[6]", is("address.coordinates")))
                .andExpect(jsonPath("$.fields[7]", is("radius")))
                .andExpect(jsonPath("$.fields[8]", is("notify_for_event_types")))
                .andExpect(jsonPath("$.fields[9]", is("notify")));

        verifyNoMoreInteractions(poiControllerMock);
    }

    @Test
    public void createPOI_shouldReturnInvalidInput2() throws Exception {
        JSONObject testPOIInput = new JSONObject();
        testPOIInput.put("name", "");
        mockMvc.perform(post("/user/0/point_of_interest/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testPOIInput.toString().getBytes()))
                .andExpect(status().is(400))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(5)))
                .andExpect(jsonPath("$.fields[0]", is("name")))
                .andExpect(jsonPath("$.fields[1]", is("address")))
                .andExpect(jsonPath("$.fields[2]", is("radius")))
                .andExpect(jsonPath("$.fields[3]", is("notify_for_event_types")))
                .andExpect(jsonPath("$.fields[4]", is("notify")));

        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createPOI_shouldReturnNotExisting() throws Exception {
        when(poiControllerMock.existsAPIPOI(eq(0), Matchers.<APIPOI>any())).thenReturn(false);
        when(poiControllerMock.createAPIPOI(eq(0), Matchers.<APIPOI>any())).thenReturn(null);

        mockMvc.perform(post("/user/0/point_of_interest/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testPOIJson.toString().getBytes()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(poiControllerMock, times(1)).existsAPIPOI(eq(0), Matchers.<APIPOI>any());
        verify(poiControllerMock, times(1)).createAPIPOI(eq(0), Matchers.<APIPOI>any());
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createPOI_shouldReturnWentWrong() throws Exception {
        when(poiControllerMock.existsAPIPOI(eq(0), Matchers.<APIPOI>any())).thenThrow(new DataAccessException(""));

        mockMvc.perform(post("/user/0/point_of_interest/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testPOIJson.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(poiControllerMock, times(1)).existsAPIPOI(eq(0), Matchers.<APIPOI>any());
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyPOI_shouldModifyCorrect() throws Exception {
        when(poiControllerMock.existsAPIPOI(eq(0), Matchers.<APIPOI>any())).thenReturn(false);
        when(poiControllerMock.modifyAPIPOI(eq(0), eq(0), Matchers.<APIPOI>any())).thenReturn(modifiedTestPOI);

        mockMvc.perform(put("/user/0/point_of_interest/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestPOIJson.toString().getBytes()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(modifiedTestPOI.getId())))
                .andExpect(jsonPath("$.active", is(modifiedTestPOI.isActive())))
                .andExpect(jsonPath("$.name", is(modifiedTestPOI.getName())))
                .andExpect(jsonPath("$.radius", is(modifiedTestPOI.getRadius())))
                .andExpect(jsonPath("$.links", hasSize(modifiedTestPOI.getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(modifiedTestPOI.getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(modifiedTestPOI.getLinks()[0].getHref())))
                .andExpect(jsonPath("$.address", is(modifiedTestPOI.getAddress())))
                .andExpect(jsonPath("$.notify_for_event_types", hasSize(modifiedTestPOI.getNotifyForEventTypes().length)))
                .andExpect(jsonPath("$.notify_for_event_types[0]", is(modifiedTestPOI.getNotifyForEventTypes()[0])))
                .andExpect(jsonPath("$.notify.email", is(modifiedTestPOI.getNotify().isEmailNotify())))
                .andExpect(jsonPath("$.notify.cell_number", is(modifiedTestPOI.getNotify().isCellNumberNotify())));

        verify(poiControllerMock, times(1)).existsAPIPOI(eq(0), Matchers.<APIPOI>any());
        verify(poiControllerMock, times(1)).modifyAPIPOI(eq(0), eq(0), Matchers.<APIPOI>any());
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyPOI_shouldReturnAlreadyCreated() throws Exception {
        when(poiControllerMock.existsAPIPOI(eq(0), Matchers.<APIPOI>any())).thenReturn(true);

        mockMvc.perform(put("/user/0/point_of_interest/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testPOIJson.toString().getBytes()))
                .andExpect(status().is(409))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(CREATE_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(409)));

        verify(poiControllerMock, times(1)).existsAPIPOI(eq(0), Matchers.<APIPOI>any());
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyPOI_shouldReturnAlreadyExisting() throws Exception {
        when(poiControllerMock.existsAPIPOI(eq(0), Matchers.<APIPOI>any())).thenThrow(new DataAccessException(""));
        when(poiControllerMock.getAPIPOI(eq(0), eq(0))).thenReturn(testPOIs.get(0));

        mockMvc.perform(put("/user/0/point_of_interest/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testPOIJson.toString().getBytes()))
                .andExpect(status().is(409))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(409)));

        verify(poiControllerMock, times(1)).existsAPIPOI(eq(0), Matchers.<APIPOI>any());
        verify(poiControllerMock, times(1)).getAPIPOI(eq(0), eq(0));
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyPOI_shouldReturnNotExisting() throws Exception {
        when(poiControllerMock.existsAPIPOI(eq(0), Matchers.<APIPOI>any())).thenThrow(new DataAccessException(""));
        when(poiControllerMock.getAPIPOI(eq(0), eq(0))).thenReturn(null);

        mockMvc.perform(put("/user/0/point_of_interest/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testPOIJson.toString().getBytes()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(poiControllerMock, times(1)).existsAPIPOI(eq(0), Matchers.<APIPOI>any());
        verify(poiControllerMock, times(1)).getAPIPOI(eq(0), eq(0));
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyPOI_shouldReturnWentWrong() throws Exception {
        when(poiControllerMock.existsAPIPOI(eq(0), Matchers.<APIPOI>any())).thenThrow(new DataAccessException(""));
        when(poiControllerMock.getAPIPOI(eq(0), eq(0))).thenThrow(new DataAccessException(""));

        mockMvc.perform(put("/user/0/point_of_interest/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testPOIJson.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(poiControllerMock, times(1)).existsAPIPOI(eq(0), Matchers.<APIPOI>any());
        verify(poiControllerMock, times(1)).getAPIPOI(eq(0), eq(0));
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deletePOI_shouldDeleteCorrect() throws Exception {
        when(poiControllerMock.deleteAPIPOI(1, 1)).thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/user/1/point_of_interest/1"))
                .andExpect(status().is(204));

        verify(poiControllerMock, times(1)).deleteAPIPOI(1, 1);
        verifyNoMoreInteractions(poiControllerMock);

    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deletePOI_shouldReturnNotExisting() throws Exception {
        when(poiControllerMock.deleteAPIPOI(1, 1)).thenReturn(Boolean.FALSE);

        mockMvc.perform(delete("/user/1/point_of_interest/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(poiControllerMock, times(1)).deleteAPIPOI(1, 1);
        verifyNoMoreInteractions(poiControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deletePOI_shouldReturnWentWrong() throws Exception {
        when(poiControllerMock.deleteAPIPOI(1, 1)).thenThrow(new DataAccessException(""));

        mockMvc.perform(delete("/user/1/point_of_interest/1"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(poiControllerMock, times(1)).deleteAPIPOI(1, 1);
        verifyNoMoreInteractions(poiControllerMock);
    }

}
