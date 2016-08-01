package Spring;

import Other.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONObject;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vop.groep7.vop7backend.Controllers.UtilController;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APIPOI;
import vop.groep7.vop7backend.Models.APIModels.APIRoute;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Models.APIModels.APITravel;
import vop.groep7.vop7backend.Models.APIModels.APIUser;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIAccessToken;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICode;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICredentials;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIRefreshToken;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIRole;
import vop.groep7.vop7backend.Models.APIModels.Datadump.APIDataDump;
import vop.groep7.vop7backend.Models.APIModels.Datadump.APITravelDump;
import vop.groep7.vop7backend.Models.APIModels.Datadump.APIUserDump;
import vop.groep7.vop7backend.Spring.SpringUtilController;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Jonas Van Wilder
 */
public class SpringUtilControllerTest {

    @Mock
    private UtilController utilControllerMock;

    @InjectMocks
    private SpringUtilController springUtilController;

    private MockMvc mockMvc;

    private List<APITransport> transportationTypes;
    private List<APIRole> roles;
    private JSONObject testAPICredentialsJson;
    private APIRefreshToken testAPIRefreshToken;
    private JSONObject testAPIRefreshTokenJson;
    private String testAPIPermissionJson;
    private APIAccessToken testAPIAccessToken;
    private APIDataDump testAPIDataDump;
    private JSONObject testAPICodeJson;

    private static final String AUTH_FAILED = "The email and/or password were incorrect.";
    private static final String BASIC_ERROR = "The database is not available at the moment.";
    private static final String REFRESH_INVALID = "The refresh token was invalid";
    private static final String INVALID_INPUT = "The input of one or more fields was invalid or missing";

    public SpringUtilControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws JsonProcessingException {
        // Mock annotations
        MockitoAnnotations.initMocks(this);

        // Setup as standalone
        this.mockMvc = MockMvcBuilders.standaloneSetup(springUtilController).build();

        transportationTypes = new ArrayList<>();
        transportationTypes.add(APITransport.CAR);
        transportationTypes.add(APITransport.BIKE);
        transportationTypes.add(APITransport.BUS);
        transportationTypes.add(APITransport.STREETCAR);

        roles = new ArrayList<>();
        roles.add(APIRole.ROLE_USER);
        roles.add(APIRole.ROLE_OPERATOR);
        roles.add(APIRole.ROLE_ADMIN);

        testAPICredentialsJson = new JSONObject();
        testAPICredentialsJson.put("email", "test@example.com");
        testAPICredentialsJson.put("password", "test123");

        testAPIRefreshToken = new APIRefreshToken();
        testAPIRefreshToken.setRole(APIRole.ROLE_USER);
        testAPIRefreshToken.setUserId("1");
        testAPIRefreshToken.setUserUrl("https://vopro7.ugent.be/api/user/1");
        testAPIRefreshToken.setToken("TOKEN");

        testAPIRefreshTokenJson = new JSONObject();
        testAPIRefreshTokenJson.put("token", "TOKEN");

        testAPIPermissionJson = APIRole.ROLE_OPERATOR.getRole();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        testAPIPermissionJson = ow.writeValueAsString(testAPIPermissionJson);

        testAPIAccessToken = new APIAccessToken();
        testAPIAccessToken.setToken("TOKEN");
        testAPIAccessToken.setExp("EXP_DATE");

        testAPICodeJson = new JSONObject();
        testAPICodeJson.put("code", "669519819");

        createTestDataDump();
    }

    private void createTestDataDump() {
        testAPIDataDump = new APIDataDump();
        APIUserDump[] userDumps = new APIUserDump[1];
        APIUser u = new APIUser();
        userDumps[0] = new APIUserDump();
        userDumps[0].setUser(u);
        APITravelDump[] travelDumps = new APITravelDump[1];
        APITravel t = new APITravel();
        travelDumps[0] = new APITravelDump();
        travelDumps[0].setTravel(t);
        APIRoute[] routes = new APIRoute[1];
        routes[0] = new APIRoute();
        travelDumps[0].setRoutes(routes);
        userDumps[0].setTravels(travelDumps);
        APIPOI[] pois = new APIPOI[1];
        pois[0] = new APIPOI();
        userDumps[0].setPointsOfInterest(pois);
        testAPIDataDump.setUsers(userDumps);

        APIEvent[] events = new APIEvent[1];
        events[0] = new APIEvent();
        testAPIDataDump.setEvents(events);

        APIEventType[] eventTypes = new APIEventType[1];
        eventTypes[0] = new APIEventType();
        testAPIDataDump.setEventTypes(eventTypes);
    }

    @Test
    public void getAllTransporationTypes_shouldReturnList() throws Exception {
        when(utilControllerMock.getTransportationTypes()).thenReturn(transportationTypes);

        mockMvc.perform(get("/transportationtype"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(transportationTypes.size())))
                .andExpect(jsonPath("$", hasItem(APITransport.BIKE.toString())))
                .andExpect(jsonPath("$", hasItem(APITransport.CAR.toString())))
                .andExpect(jsonPath("$", hasItem(APITransport.BUS.toString())))
                .andExpect(jsonPath("$", hasItem(APITransport.STREETCAR.toString())));

        verify(utilControllerMock, times(1)).getTransportationTypes();
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void getAllRoles_shouldReturnList() throws Exception {
        when(utilControllerMock.getRoles()).thenReturn(roles);

        mockMvc.perform(get("/role"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(roles.size())))
                .andExpect(jsonPath("$", hasItem(APIRole.ROLE_USER.toString())))
                .andExpect(jsonPath("$", hasItem(APIRole.ROLE_OPERATOR.toString())))
                .andExpect(jsonPath("$", hasItem(APIRole.ROLE_ADMIN.toString())));

        verify(utilControllerMock, times(1)).getRoles();
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void getRefreshTokenRegular_shouldReturnRefreshToken() throws Exception {
        when(utilControllerMock.getRefreshTokenRegular(Matchers.<APICredentials>any())).thenReturn(testAPIRefreshToken);

        mockMvc.perform(post("/refresh_token/regular")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPICredentialsJson.toString().getBytes()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id", is(testAPIRefreshToken.getUserId())))
                .andExpect(jsonPath("$.user_url", is(testAPIRefreshToken.getUserUrl())))
                .andExpect(jsonPath("$.role", is(testAPIRefreshToken.getRole().toString())))
                .andExpect(jsonPath("$.token", is(testAPIRefreshToken.getToken())));

        verify(utilControllerMock, times(1)).getRefreshTokenRegular(Matchers.<APICredentials>any());
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void getRefreshTokenRegular_shouldReturnAuthFailed() throws Exception {
        when(utilControllerMock.getRefreshTokenRegular(Matchers.<APICredentials>any())).thenReturn(null);

        mockMvc.perform(post("/refresh_token/regular")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPICredentialsJson.toString().getBytes()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(AUTH_FAILED)))
                .andExpect(jsonPath("$.code", is(401)))
                .andExpect(jsonPath("$.fields", hasSize(2)))
                .andExpect(jsonPath("$.fields[0]", is("email")))
                .andExpect(jsonPath("$.fields[1]", is("password")));

        verify(utilControllerMock, times(1)).getRefreshTokenRegular(Matchers.<APICredentials>any());
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void getRefreshTokenFacebook_shouldReturnRefreshToken() throws Exception {
        when(utilControllerMock.getRefreshTokenFacebook(Matchers.<APICode>any())).thenReturn(testAPIRefreshToken);

        mockMvc.perform(post("/refresh_token/facebook")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPICodeJson.toString().getBytes()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id", is(testAPIRefreshToken.getUserId())))
                .andExpect(jsonPath("$.user_url", is(testAPIRefreshToken.getUserUrl())))
                .andExpect(jsonPath("$.role", is(testAPIRefreshToken.getRole().toString())))
                .andExpect(jsonPath("$.token", is(testAPIRefreshToken.getToken())));

        verify(utilControllerMock, times(1)).getRefreshTokenFacebook(Matchers.<APICode>any());
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void logout_shouldLogout() throws Exception {
        mockMvc.perform(post("/refresh_token/logout")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIRefreshTokenJson.toString().getBytes()))
                .andExpect(status().isOk());

        verify(utilControllerMock, times(1)).logout(Matchers.<APIRefreshToken>any());
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void logout_shouldReturnInvalidInput() throws Exception {
        mockMvc.perform(post("/refresh_token/logout")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(new JSONObject().toString().getBytes()))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(1)))
                .andExpect(jsonPath("$.fields[0]", is("token")));

        verify(utilControllerMock, times(0)).logout(Matchers.<APIRefreshToken>any());
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void logout_shouldReturnWentWrong() throws Exception {
        Mockito.doThrow(new DataAccessException("")).when(utilControllerMock).logout(Matchers.<APIRefreshToken>any());

        mockMvc.perform(post("/refresh_token/logout")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIRefreshTokenJson.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(utilControllerMock, times(1)).logout(Matchers.<APIRefreshToken>any());
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void changePermission_shouldChange() throws Exception {
        mockMvc.perform(post("/admin/permission/1")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIPermissionJson))
                .andDo(print())
                .andExpect(status().isOk());

        verify(utilControllerMock, times(1)).changePermission(Matchers.eq(1), Matchers.<APIRole>any());
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void changePermission_shouldReturnWentWrong() throws Exception {
        Mockito.doThrow(new DataAccessException("")).when(utilControllerMock).changePermission(Matchers.eq(1), Matchers.<APIRole>any());

        mockMvc.perform(post("/admin/permission/1")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIPermissionJson))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(utilControllerMock, times(1)).changePermission(Matchers.eq(1), Matchers.<APIRole>any());
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void getAccessToken_shouldReturnAccessToken() throws Exception {
        when(utilControllerMock.getAccessToken(Matchers.<APIRefreshToken>any())).thenReturn(testAPIAccessToken);

        mockMvc.perform(post("/access_token")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIRefreshTokenJson.toString().getBytes()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.token", is(testAPIAccessToken.getToken())))
                .andExpect(jsonPath("$.exp", is(testAPIAccessToken.getExp())));

        verify(utilControllerMock, times(1)).getAccessToken(Matchers.<APIRefreshToken>any());
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void getAccessToken_shouldReturnAuthFailed() throws Exception {
        when(utilControllerMock.getAccessToken(Matchers.<APIRefreshToken>any())).thenReturn(null);

        mockMvc.perform(post("/access_token")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPICredentialsJson.toString().getBytes()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(REFRESH_INVALID)))
                .andExpect(jsonPath("$.code", is(401)))
                .andExpect(jsonPath("$.fields", hasSize(1)))
                .andExpect(jsonPath("$.fields[0]", is("token")));

        verify(utilControllerMock, times(1)).getAccessToken(Matchers.<APIRefreshToken>any());
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void getAccessToken_shouldReturnWentWrong() throws Exception {
        when(utilControllerMock.getAccessToken(Matchers.<APIRefreshToken>any())).thenThrow(new DataAccessException(""));

        mockMvc.perform(post("/access_token")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPICredentialsJson.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(utilControllerMock, times(1)).getAccessToken(Matchers.<APIRefreshToken>any());
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void getDataDump_shouldReturnDump() throws Exception {
        when(utilControllerMock.getDataDump()).thenReturn(testAPIDataDump);

        mockMvc.perform(post("/admin/data_dump")
                .contentType(TestUtils.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(utilControllerMock, times(1)).getDataDump();
        verifyNoMoreInteractions(utilControllerMock);
    }

    @Test
    public void getDataDump_shouldReturnWentWrong() throws Exception {
        Mockito.doThrow(new DataAccessException("")).when(utilControllerMock).getDataDump();

        mockMvc.perform(post("/admin/data_dump")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(APIRole.ROLE_OPERATOR.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(utilControllerMock, times(1)).getDataDump();
        verifyNoMoreInteractions(utilControllerMock);
    }

}
