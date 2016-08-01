package Controllers;

import java.util.ArrayList;
import java.util.List;
import org.easymock.EasyMockRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Controllers.UtilController;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICredentials;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIRefreshToken;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIRole;
import vop.groep7.vop7backend.Models.Domain.Credentials;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.Security.TokenManager;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.UserDAO;
import vop.groep7.vop7backend.factories.UserCredentialsFactory;
import vop.groep7.vop7backend.factories.UserFactory;
import static org.mockito.Mockito.verify;
import org.powermock.core.classloader.annotations.PrepareForTest;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIAccessToken;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICode;
import vop.groep7.vop7backend.Models.APIModels.Datadump.APIDataDump;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.database.mongodb.EventDAO;
import vop.groep7.vop7backend.database.sql.POIDAO;
import vop.groep7.vop7backend.database.sql.RouteDAO;
import vop.groep7.vop7backend.database.sql.TravelDAO;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.POIFactory;
import vop.groep7.vop7backend.factories.RouteFactory;
import vop.groep7.vop7backend.factories.TransportFactory;
import vop.groep7.vop7backend.factories.TravelFactory;

/**
 *
 * @author Backend team
 */
//@RunWith(PowerMockRunner.class)
@RunWith(EasyMockRunner.class)
@PrepareForTest(TokenManager.class)
public class UtilControllerTest {

    @Mock
    private UserDAO userDAOMock;

    @Mock
    private POIDAO poiDAOMock;

    @Mock
    private RouteDAO routeDAOMock;

    @Mock
    private TravelDAO travelDAOMock;

    @Mock
    private EventDAO eventDAOMock;

    @Mock
    private TokenManager tokenManagerMock;

    @InjectMocks
    private UtilController utilController;

    private APICredentials testAPICredentials;
    private Credentials testCredentials;
    private Credentials otherTestCredentials;
    private User testUser;
    private APIRefreshToken testRefreshToken;

    public UtilControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);

        createTestCredentials();
        createExpectedRefreshToken();
    }

    @After
    public void tearDown() {
    }

    private void createTestCredentials() {
        testAPICredentials = new APICredentials();
        testAPICredentials.setEmail("test@test.com");
        testAPICredentials.setPassword("test123");

        testUser = UserFactory.build(1, "test@test.com", null, null, null, false, false, false);
        testCredentials = UserCredentialsFactory.create("test123", testUser);

        otherTestCredentials = UserCredentialsFactory.create("test", testUser);
    }

    private void createExpectedRefreshToken() {
        testRefreshToken = new APIRefreshToken();
        testRefreshToken.setUserId("1");
        testRefreshToken.setRole(APIRole.ROLE_USER);
        testRefreshToken.setUserUrl("https://vopro7.ugent.be/api/user/1");
        testRefreshToken.setToken(null);

    }

    @Test
    public void getTransportationTypes_shouldReturnList() {
        List<APITransport> expected = new ArrayList<APITransport>() {
            {
                add(APITransport.BUS);
                add(APITransport.CAR);
                add(APITransport.BIKE);
                add(APITransport.STREETCAR);
            }
        };
        List<APITransport> res = utilController.getTransportationTypes();
        assertNotNull(res);
        assertTrue(expected.size() == res.size());
        expected.stream().forEach((t) -> {
            assertTrue(res.contains(t));
        });
    }

    @Test
    public void getRefreshTokenRegular_shouldReturnToken() throws DataAccessException {
        when(userDAOMock.userExists(Matchers.eq("test@test.com"))).thenReturn(true);
        when(userDAOMock.getCredentials(Matchers.eq("test@test.com"))).thenReturn(testCredentials);
        APIRefreshToken res = utilController.getRefreshTokenRegular(testAPICredentials);
        res.setToken(null);

        assertNotNull(res);
        assertEquals(testRefreshToken, res);
        verify(userDAOMock, times(1)).userExists(Matchers.eq("test@test.com"));
        verify(userDAOMock, times(1)).getCredentials(Matchers.eq("test@test.com"));
    }

    @Test
    public void getRefreshTokenRegular_shouldReturnNull() throws DataAccessException {
        when(userDAOMock.userExists(Matchers.eq("test@test.com"))).thenReturn(true);
        when(userDAOMock.getCredentials(Matchers.eq("test@test.com"))).thenReturn(otherTestCredentials);
        APIRefreshToken res = utilController.getRefreshTokenRegular(testAPICredentials);

        assertNull(res);
        verify(userDAOMock, times(1)).userExists(Matchers.eq("test@test.com"));
        verify(userDAOMock, times(1)).getCredentials(Matchers.eq("test@test.com"));
    }

    @Test
    public void getRefreshTokenRegular_shouldReturnNull2() throws DataAccessException {
        when(userDAOMock.userExists(Matchers.eq("test@test.com"))).thenReturn(false);
        APIRefreshToken res = utilController.getRefreshTokenRegular(testAPICredentials);

        assertNull(res);
        verify(userDAOMock, times(1)).userExists(Matchers.eq("test@test.com"));
    }

    @Test
    public void getRefreshTokenFacebook_ShouldBeUnSupported() {
        boolean thrown = false;
        try {
            utilController.getRefreshTokenFacebook(new APICode());
        } catch (UnsupportedOperationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void logout_shouldLogout() throws DataAccessException {
        utilController.logout(testRefreshToken);
    }

    @Test
    public void getAccessToken_shouldReturnToken() throws DataAccessException {
        String expectedAccessToken = "token:1:RANDOMHASH:1555555555555";
        when(tokenManagerMock.validateRefreshToken(testRefreshToken.getToken())).thenReturn(testUser);
        when(tokenManagerMock.createAccessToken(testUser)).thenReturn(expectedAccessToken);

        APIAccessToken res = utilController.getAccessToken(testRefreshToken);

        assertNotNull(res);
        assertEquals(expectedAccessToken, res.getToken());
        assertEquals("2019-04-18T04:45:55+0200", res.getExp());

    }

    @Test
    public void getAccessToken_shouldReturnNull() throws DataAccessException {
        when(tokenManagerMock.validateRefreshToken(testRefreshToken.getToken())).thenReturn(null);

        APIAccessToken res = utilController.getAccessToken(testRefreshToken);

        assertNull(res);
    }

    @Test
    public void getRoles_shouldReturnRoles() {
        List<APIRole> expected = new ArrayList<APIRole>() {
            {
                add(APIRole.ROLE_ADMIN);
                add(APIRole.ROLE_OPERATOR);
                add(APIRole.ROLE_USER);
            }
        };
        List<APIRole> res = utilController.getRoles();
        assertNotNull(res);
        assertTrue(expected.size() == res.size());
        for (APIRole r : expected) {
            assertTrue(res.contains(r));
        }
    }

    @Test
    public void changePermission_shouldChange() throws DataAccessException {
        when(userDAOMock.getCredentials(Matchers.eq(1))).thenReturn(testCredentials);

        utilController.changePermission(1, APIRole.ROLE_OPERATOR);

        verify(userDAOMock, times(1)).getCredentials(Matchers.eq(1));
        verify(userDAOMock, times(1)).modifyCredentials(Matchers.<Credentials>any());
    }

    @Test
    public void getDataDump_shouldReturnDump() {
        try {
            List<User> users = new ArrayList<>();
            users.add(UserFactory.build(1, "test@test.test", null, null, null, true, true, true));
            when(userDAOMock.getUsers()).thenReturn(users);

            List<POI> pois = new ArrayList<>();
            Address a = AddressFactory.build("Gent", 9000, "Straat", "nr", "BE", CoordinateFactory.build(0.0, 0.0));
            pois.add(POIFactory.build(1, a, "test", 0, true, true, true, null));
            when(poiDAOMock.getPOIs(Matchers.eq(1))).thenReturn(pois);

            List<Travel> travels = new ArrayList<>();
            travels.add(TravelFactory.build(1, "test", a, a, new String[]{"13:00", "18:00"}, new Boolean[7]));
            when(travelDAOMock.getTravels(Matchers.eq(1))).thenReturn(travels);

            List<Route> routes = new ArrayList<>();
            routes.add(RouteFactory.build(1, true, TransportFactory.build("car"), true, true, null, null));
            when(routeDAOMock.getRoutes(Matchers.eq(1), Matchers.eq(1))).thenReturn(routes);

            List<EventType> eventTypes = new ArrayList<>();
            eventTypes.add(EventTypeFactory.create("Test_type"));
            when(eventDAOMock.getEventTypes()).thenReturn(eventTypes);

            List<Event> events = new ArrayList<>();
            events.add(EventFactory.build(1, a, true, null, "test", null, null, null, new ArrayList<Transport>(), null));
            when(eventDAOMock.getEvents()).thenReturn(events);

            APIDataDump res = utilController.getDataDump();

            assertNotNull(res);
            assertNotNull(res.getUsers());
            assertTrue(res.getUsers().length == 1);
            assertNotNull(res.getEventTypes());
            assertNotNull(res.getEvents());
        } catch (DataAccessException ex) {
            fail("DataAccessException should not be thrown!");
        } catch (IllegalAccessException ex) {
            fail("IllegalAccessException should not be thrown!");
        }

    }
}
