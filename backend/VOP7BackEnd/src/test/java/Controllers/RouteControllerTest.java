package Controllers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import vop.groep7.vop7backend.Controllers.RouteController;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.database.sql.RouteDAO;
import vop.groep7.vop7backend.database.sql.TravelDAO;
import vop.groep7.vop7backend.database.sql.UserDAO;
import com.google.common.cache.Cache;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import vop.groep7.vop7backend.Controllers.Controller;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APINotify;
import vop.groep7.vop7backend.Models.APIModels.APIRoute;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.POIFactory;
import vop.groep7.vop7backend.factories.RouteFactory;
import vop.groep7.vop7backend.factories.TransportFactory;
import vop.groep7.vop7backend.factories.TravelFactory;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Jonas Van Wilder
 */
public class RouteControllerTest {

    @Mock
    private UserDAO userDAOMock;
    @Mock
    private TravelDAO travelDAOMock;
    @Mock
    private RouteDAO routeDAOMock;

    @InjectMocks
    private RouteController routeController;

    // mocked userCache for test
    private static Cache<Integer, User> mockedUserCache;

    private User testUser1;
    private Travel testTravel1;
    private Route testRoute1;
    private Route testRoute2;
    private APIRoute testAPIRoute1;
    private APIRoute testAPIRoute2;

    /**
     *
     */
    public RouteControllerTest() {
        createTestRoutes();
        createTestTravels();
        createTestUsers();
    }

    private void createTestUsers() {
        testUser1 = UserFactory.build(1000, "George.Vermalen@example.com", "George", "Vermalen", null, false, true, true);
    }

    private void createTestTravels() {
        Coordinate startC = CoordinateFactory.build(1.1, 2.2);
        Coordinate endC = CoordinateFactory.build(2.2, 2.2);
        Address start = AddressFactory.build("Gent", 9000, "Straat", "Nummer", "Belgie", startC);
        Address end = AddressFactory.build("Gent", 9000, "Straat", "Nummer", "Belgie", endC);
        String[] interval = {"12:00", "13:00"};
        Boolean[] rec = {true, true, true, true, true, true, true};
        testTravel1 = TravelFactory.build(1, "TestTravel", start, end, interval, rec);
    }

    private void createTestRoutes() {
        Transport t = TransportFactory.build("car");
        ArrayList<EventType> e = new ArrayList<>();
        e.add(EventTypeFactory.build("TestType"));
        testRoute1 = RouteFactory.build(1, true, t, true, true, new ArrayList<>(), e);
        testRoute1.addWaypoint(CoordinateFactory.build(1.1, 2.2));
        testRoute1.addWaypoint(CoordinateFactory.build(2.2, 2.2));
        
        Transport t2 = TransportFactory.build("bus");
        ArrayList<Coordinate> c2 = new ArrayList<>();
        c2.add(CoordinateFactory.build(1.3, 2.3));
        c2.add(CoordinateFactory.build(2.3, 2.3));
        ArrayList<EventType> e2 = new ArrayList<>();
        e2.add(EventTypeFactory.build("TestType2"));
        testRoute2 = RouteFactory.build(1, false, t2, false, false, c2, e2);

        testAPIRoute1 = new APIRoute();
        testAPIRoute1.setActive(true);
        APINotify apiN = new APINotify();
        apiN.setCellNumberNotify(true);
        apiN.setEmailNotify(true);
        testAPIRoute1.setNotify(apiN);
        APIEventType[] apiE = new APIEventType[1];
        APIEventType type = new APIEventType();
        type.setType("TestType");
        apiE[0] = type;
        testAPIRoute1.setNotifyForEventTypes(apiE);
        testAPIRoute1.setTransportationType(APITransport.CAR);
        APICoordinate[] apiC = new APICoordinate[2];
        APICoordinate apiC1 = new APICoordinate();
        apiC1.setLat(1.1);
        apiC1.setLon(2.2);
        apiC[0] = apiC1;
        APICoordinate apiC2 = new APICoordinate();
        apiC2.setLat(2.2);
        apiC2.setLon(2.2);
        apiC[1] = apiC2;
        testAPIRoute1.setWaypoints(apiC);
        testAPIRoute1.setInterpolatedWaypoints(apiC);

        testAPIRoute2 = new APIRoute();
        testAPIRoute2.setId("1");
        testAPIRoute2.setActive(false);
        APINotify apiN2 = new APINotify();
        apiN2.setCellNumberNotify(false);
        apiN2.setEmailNotify(false);
        testAPIRoute2.setNotify(apiN2);
        APIEventType[] apiE2 = new APIEventType[1];
        APIEventType type2 = new APIEventType();
        type2.setType("TestType2");
        apiE2[0] = type2;
        testAPIRoute2.setNotifyForEventTypes(apiE2);
        testAPIRoute2.setTransportationType(APITransport.BUS);
        APICoordinate[] apiCo2 = new APICoordinate[2];
        APICoordinate apiC11 = new APICoordinate();
        apiC11.setLat(1.3);
        apiC11.setLon(2.3);
        apiCo2[0] = apiC11;
        APICoordinate apiC22 = new APICoordinate();
        apiC22.setLat(2.3);
        apiC22.setLon(2.3);
        apiCo2[1] = apiC22;
        testAPIRoute2.setWaypoints(apiCo2);
        testAPIRoute2.setInterpolatedWaypoints(apiCo2);

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
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // Mock annotations
        MockitoAnnotations.initMocks(this);

        mockedUserCache = Mockito.mock(Cache.class);
        // replace static userCache with mocked userCache
        Field f = Controller.class.getDeclaredField("userCache");
        f.setAccessible(true);
        f.set(null, mockedUserCache);
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    private boolean checkExpectedRoute(Route r1, Route r2) {
        if (r1 == null || r2 == null) {
            return false;
        }

        return Objects.equals(r1.isActive(), r2.isActive())
                && Objects.equals(r1.getTransportationType().getTransport(), r2.getTransportationType().getTransport())
                && Arrays.deepEquals(r1.getWaypoints().toArray(), r2.getWaypoints().toArray())
                && Objects.equals(r1.getNotifications().isNotifyCellPhone(), r2.getNotifications().isNotifyCellPhone())
                && Objects.equals(r1.getNotifications().isNotifyEmail(), r2.getNotifications().isNotifyEmail())
                && Arrays.deepEquals(r1.getNotifications().getNotifyForEventTypes().toArray(), r2.getNotifications().getNotifyForEventTypes().toArray());
    }

    private void prepareCache() {
        when(mockedUserCache.getIfPresent(1)).thenReturn(testUser1);
        testTravel1.addRoute(testRoute1);
        testUser1.addTravel(testTravel1);

    }

    private void prepareDAO() {
        try {
            // prepare dao
            when(userDAOMock.getUser(1)).thenReturn(testUser1);
            List<Travel> list1 = new ArrayList<>();
            list1.add(testTravel1);
            when(travelDAOMock.getTravels(1)).thenReturn(list1);
            List<Route> list2 = new ArrayList<>();
            list2.add(testRoute1);
            when(routeDAOMock.getRoutes(1, 1)).thenReturn(list2);
        } catch (DataAccessException ex) {
            fail();
        }
    }

    @Test
    public void getRouteTest_shouldReturnNull() throws DataAccessException {
        when(userDAOMock.getUser(1)).thenReturn(testUser1);
        when(travelDAOMock.getTravels(1)).thenReturn(new ArrayList<Travel>() {
            {
                add(TravelFactory.build(1, "", null, null, null, null));
            }
        });
        when(routeDAOMock.getRoutes(1, 1)).thenReturn(new ArrayList<Route>() {
            {
                add(RouteFactory.build(-1, true, null, true, true, null, null));
            }
        });
        Route res = routeController.getRoute(1, 1, 1);

        assertNull(res);
    }

    @Test
    public void getRoutes_returnRoutesFromCache() {
        prepareCache();

        List<Route> res = null;
        try {
            res = routeController.getRoutes(1, 1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertTrue(checkExpectedRoute(res.get(0), testRoute1));
    }

    @Test
    public void getRoutes_returnRoutesFromDAO() {
        prepareDAO();

        List<Route> res = null;
        try {
            res = routeController.getRoutes(1, 1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertTrue(checkExpectedRoute(res.get(0), testRoute1));
    }

    @Test
    public void getAPIRoutes_shouldReturnAPIRoutes() throws IllegalAccessException {
        prepareCache();

        List<APIRoute> res = null;
        try {
            res = routeController.getAPIRoutes(1, 1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertNotNull(res);
        assertEquals(res.get(0).getId(), testRoute1.getRouteIdentifier() + "");
        assertEquals(res.get(0).getNotify().isCellNumberNotify(), testRoute1.getNotifications().isNotifyCellPhone());
        assertEquals(res.get(0).getNotify().isEmailNotify(), testRoute1.getNotifications().isNotifyEmail());
        assertEquals(res.get(0).getNotifyForEventTypes()[0].getType(), testRoute1.getNotifications().getNotifyForEventTypes().get(0).getType());
        assertEquals(res.get(0).getTransportationType().getType(), testRoute1.getTransportationType().getTransport());
        assertEquals(res.get(0).isActive(), testRoute1.isActive());
        assertEquals(res.get(0).getWaypoints()[0].getLat(), testRoute1.getWaypoints().get(0).getLat(), 0);
        assertEquals(res.get(0).getWaypoints()[0].getLon(), testRoute1.getWaypoints().get(0).getLon(), 0);
        assertEquals(res.get(0).getWaypoints()[1].getLat(), testRoute1.getWaypoints().get(1).getLat(), 0);
        assertEquals(res.get(0).getWaypoints()[1].getLon(), testRoute1.getWaypoints().get(1).getLon(), 0);
    }

    @Test
    public void getAPIRoute_shouldReturnAPIRoute() throws IllegalAccessException {
        prepareCache();

        APIRoute res = null;
        try {
            res = routeController.getAPIRoute(1, 1, 1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertNotNull(res);
        assertEquals(res.getId(), testRoute1.getRouteIdentifier() + "");
        assertEquals(res.getNotify().isCellNumberNotify(), testRoute1.getNotifications().isNotifyCellPhone());
        assertEquals(res.getNotify().isEmailNotify(), testRoute1.getNotifications().isNotifyEmail());
        assertEquals(res.getNotifyForEventTypes()[0].getType(), testRoute1.getNotifications().getNotifyForEventTypes().get(0).getType());
        assertEquals(res.getTransportationType().getType(), testRoute1.getTransportationType().getTransport());
        assertEquals(res.isActive(), testRoute1.isActive());
        assertEquals(res.getWaypoints()[0].getLat(), testRoute1.getWaypoints().get(0).getLat(), 0);
        assertEquals(res.getWaypoints()[0].getLon(), testRoute1.getWaypoints().get(0).getLon(), 0);
        assertEquals(res.getWaypoints()[1].getLat(), testRoute1.getWaypoints().get(1).getLat(), 0);
        assertEquals(res.getWaypoints()[1].getLon(), testRoute1.getWaypoints().get(1).getLon(), 0);
    }

    @Test
    public void createAPIRoute_shouldReturnAPIRoute() throws IllegalAccessException {
        try {
            testUser1.addTravel(testTravel1);
            when(mockedUserCache.getIfPresent(1)).thenReturn(testUser1);
            when(routeDAOMock.createRoute(eq(1), eq(1), Matchers.<Route>anyObject())).thenReturn(1);
        } catch (DataAccessException ex) {
            fail();
        }

        APIRoute res = null;
        try {
            res = routeController.createAPIRoute(1, 1, testAPIRoute1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertNotNull(res);
        assertEquals(res.getId(), testRoute1.getRouteIdentifier() + "");
        assertEquals(res.getNotify().isCellNumberNotify(), testRoute1.getNotifications().isNotifyCellPhone());
        assertEquals(res.getNotify().isEmailNotify(), testRoute1.getNotifications().isNotifyEmail());
        assertEquals(res.getNotifyForEventTypes()[0].getType(), testRoute1.getNotifications().getNotifyForEventTypes().get(0).getType());
        assertEquals(res.getTransportationType().getType(), testRoute1.getTransportationType().getTransport());
        assertEquals(res.isActive(), testRoute1.isActive());
        assertEquals(res.getWaypoints()[0].getLat(), testRoute1.getWaypoints().get(0).getLat(), 0);
        assertEquals(res.getWaypoints()[0].getLon(), testRoute1.getWaypoints().get(0).getLon(), 0);
        assertEquals(res.getWaypoints()[1].getLat(), testRoute1.getWaypoints().get(1).getLat(), 0);
        assertEquals(res.getWaypoints()[1].getLon(), testRoute1.getWaypoints().get(1).getLon(), 0);
    }

    @Test
    public void modifyAPIRoute_shouldReturnAPIRoute() throws IllegalAccessException {
        prepareCache();

        APIRoute res = null;
        try {
            res = routeController.modifyAPIRoute(1, 1, 1, testAPIRoute2);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertNotNull(res);
        assertEquals(res.getId(), testRoute2.getRouteIdentifier() + "");
        assertEquals(res.getNotify().isCellNumberNotify(), testRoute2.getNotifications().isNotifyCellPhone());
        assertEquals(res.getNotify().isEmailNotify(), testRoute2.getNotifications().isNotifyEmail());
        assertEquals(res.getNotifyForEventTypes()[0].getType(), testRoute2.getNotifications().getNotifyForEventTypes().get(0).getType());
        assertEquals(res.getTransportationType().getType(), testRoute2.getTransportationType().getTransport());
        assertEquals(res.isActive(), testRoute2.isActive());
        assertEquals(res.getWaypoints()[0].getLat(), testRoute2.getWaypoints().get(0).getLat(), 0);
        assertEquals(res.getWaypoints()[0].getLon(), testRoute2.getWaypoints().get(0).getLon(), 0);
        assertEquals(res.getWaypoints()[1].getLat(), testRoute2.getWaypoints().get(1).getLat(), 0);
        assertEquals(res.getWaypoints()[1].getLon(), testRoute2.getWaypoints().get(1).getLon(), 0);
    }

    @Test
    public void deleteAPIRoute_shouldReturnTrue() throws IllegalAccessException {
        prepareCache();

        try {
            when(routeDAOMock.deleteRoute(1, 1, 1)).thenReturn(true);
        } catch (DataAccessException ex) {
            fail();
        }

        boolean res = false;
        try {
            res = routeController.deleteAPIRoute(1, 1, 1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertTrue(res);
    }

    @Test
    public void existsAPIRoute_shouldReturnTrue() {
        prepareCache();

        try {
            when(routeDAOMock.existsRoute(eq(1), eq(1), Matchers.<Route>anyObject())).thenReturn(true);
        } catch (DataAccessException ex) {
            fail();
        }

        boolean res = false;
        try {
            res = routeController.existsAPIRoute(1, 1, testAPIRoute1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertTrue(res);
    }
}
