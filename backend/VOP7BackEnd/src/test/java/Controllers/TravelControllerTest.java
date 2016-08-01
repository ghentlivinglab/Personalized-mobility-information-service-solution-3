package Controllers;

import com.google.common.cache.Cache;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Controllers.Controller;
import vop.groep7.vop7backend.Controllers.TravelController;
import vop.groep7.vop7backend.Exceptions.NotExistingResponseException;
import vop.groep7.vop7backend.Models.APIModels.APIAddress;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APITravel;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.TravelDAO;
import vop.groep7.vop7backend.database.sql.UserDAO;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.RouteFactory;
import vop.groep7.vop7backend.factories.TravelFactory;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Jonas Van Wilder
 */
public class TravelControllerTest {

    @Mock
    private UserDAO userDAOMock;
    @Mock
    private TravelDAO travelDAOMock;

    @InjectMocks
    private TravelController travelController;

    // mocked userCache for test
    private static Cache<Integer, User> mockedUserCache;

    private User testUser1;
    private Travel testTravel1;
    private Travel testTravel2;
    private APITravel testAPITravel1;
    private APITravel testAPITravel2;

    /**
     *
     */
    public TravelControllerTest() {
        createTestTravels();
        createTestUsers();
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

        Coordinate startC2 = CoordinateFactory.build(1.12, 2.22);
        Coordinate endC2 = CoordinateFactory.build(2.22, 2.22);
        Address start2 = AddressFactory.build("Gent2", 90002, "Straat2", "Nummer2", "Belgie2", startC2);
        Address end2 = AddressFactory.build("Gent2", 90002, "Straat2", "Nummer2", "Belgie2", endC2);
        String[] interval2 = {"12:02", "13:02"};
        Boolean[] rec2 = {false, false, false, false, false, false, false};
        testTravel2 = TravelFactory.build(1, "TestTravel2", start2, end2, interval2, rec2);

        testAPITravel1 = new APITravel();
        testAPITravel1.setName("TestTravel");
        testAPITravel1.setRecurring(rec);
        testAPITravel1.setTimeInterval(interval);
        APIAddress apiStart = new APIAddress();
        apiStart.setCity("Gent");
        apiStart.setPostalCode(9000);
        apiStart.setCountry("Belgie");
        apiStart.setStreet("Straat");
        apiStart.setHousenumber("Nummer");
        APICoordinate apiStartC = new APICoordinate();
        apiStartC.setLat(1.1);
        apiStartC.setLon(2.2);
        apiStart.setCoordinates(apiStartC);
        testAPITravel1.setStartpoint(apiStart);
        APIAddress apiEnd = new APIAddress();
        apiEnd.setCity("Gent");
        apiEnd.setPostalCode(9000);
        apiEnd.setCountry("Belgie");
        apiEnd.setStreet("Straat");
        apiEnd.setHousenumber("Nummer");
        APICoordinate apiEndC = new APICoordinate();
        apiEndC.setLat(2.2);
        apiEndC.setLon(2.2);
        apiEnd.setCoordinates(apiEndC);
        testAPITravel1.setEndpoint(apiEnd);

        testAPITravel2 = new APITravel();
        testAPITravel2.setId("1");
        testAPITravel2.setName("TestTravel2");
        testAPITravel2.setRecurring(rec2);
        testAPITravel2.setTimeInterval(interval2);
        APIAddress apiStart2 = new APIAddress();
        apiStart2.setCity("Gent2");
        apiStart2.setPostalCode(90002);
        apiStart2.setCountry("Belgie2");
        apiStart2.setStreet("Straat2");
        apiStart2.setHousenumber("Nummer2");
        APICoordinate apiStartC2 = new APICoordinate();
        apiStartC2.setLat(1.12);
        apiStartC2.setLon(2.22);
        apiStart2.setCoordinates(apiStartC2);
        testAPITravel2.setStartpoint(apiStart2);
        APIAddress apiEnd2 = new APIAddress();
        apiEnd2.setCity("Gent2");
        apiEnd2.setPostalCode(90002);
        apiEnd2.setCountry("Belgie2");
        apiEnd2.setStreet("Straat2");
        apiEnd2.setHousenumber("Nummer2");
        APICoordinate apiEndC2 = new APICoordinate();
        apiEndC2.setLat(2.22);
        apiEndC2.setLon(2.22);
        apiEnd2.setCoordinates(apiEndC2);
        testAPITravel2.setEndpoint(apiEnd2);
    }

    private boolean checkExpectedTravel(Travel t1, Travel t2) {
        if (t1 == null || t2 == null) {
            return false;
        }

        return Objects.equals(t1.getName(), t2.getName())
                && Objects.equals(t1.getEnd().getCity().getCity(), t2.getEnd().getCity().getCity())
                && Objects.equals(t1.getEnd().getCity().getCountry(), t2.getEnd().getCity().getCountry())
                && Objects.equals(t1.getEnd().getCity().getPostalCode(), t2.getEnd().getCity().getPostalCode())
                && Objects.equals(t1.getEnd().getHousenumber(), t2.getEnd().getHousenumber())
                && Objects.equals(t1.getEnd().getStreet(), t2.getEnd().getStreet())
                && Objects.equals(t1.getEnd().getCoordinates().getLat(), t2.getEnd().getCoordinates().getLat())
                && Objects.equals(t1.getEnd().getCoordinates().getLon(), t2.getEnd().getCoordinates().getLon())
                && Objects.equals(t1.getInterval().toArray()[0], t2.getInterval().toArray()[0])
                && Objects.equals(t1.getInterval().toArray()[1], t2.getInterval().toArray()[1])
                && Objects.equals(t1.getRecurring().hashCode(), t2.getRecurring().hashCode())
                && Objects.equals(t1.getStart().getCity().getCity(), t2.getStart().getCity().getCity())
                && Objects.equals(t1.getStart().getCity().getCountry(), t2.getStart().getCity().getCountry())
                && Objects.equals(t1.getStart().getCity().getPostalCode(), t2.getStart().getCity().getPostalCode())
                && Objects.equals(t1.getStart().getCoordinates().getLat(), t2.getStart().getCoordinates().getLat())
                && Objects.equals(t1.getStart().getCoordinates().getLon(), t2.getStart().getCoordinates().getLon())
                && Objects.equals(t1.getStart().getHousenumber(), t2.getStart().getHousenumber())
                && Objects.equals(t1.getStart().getStreet(), t2.getStart().getStreet());
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    private void prepareCache() {
        when(mockedUserCache.getIfPresent(1)).thenReturn(testUser1);
        testUser1.addTravel(testTravel1);
    }

    private void prepareDAO() {
        try {
            // prepare dao
            when(userDAOMock.getUser(1)).thenReturn(testUser1);
            List<Travel> list = new ArrayList<>();
            list.add(testTravel1);
            when(travelDAOMock.getTravels(1)).thenReturn(list);
        } catch (DataAccessException ex) {
            fail();
        }
    }

    @Test
    public void getTravelTest_shouldReturnNotExisting() throws DataAccessException {
        when(userDAOMock.getUser(1)).thenReturn(testUser1);
        when(travelDAOMock.getTravels(1)).thenReturn(new ArrayList<Travel>() {
            {
                add(TravelFactory.build(-1, "", null, null, null, null));
            }
        });
        boolean thrown = false;
        try {
            Travel res = travelController.getTravel(1, 1);
        } catch (NotExistingResponseException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void getTravels_returnTravelsFromCache() {
        prepareCache();

        List<Travel> res = null;
        try {
            res = travelController.getTravels(1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertTrue(checkExpectedTravel(res.get(0), testTravel1));
    }

    @Test
    public void getTravels_returnTravelsFromDAO() {
        prepareDAO();

        List<Travel> res = null;
        try {
            res = travelController.getTravels(1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertTrue(checkExpectedTravel(res.get(0), testTravel1));
    }

    @Test
    public void getTravel_returnTravelFromCache() {
        prepareCache();

        Travel res = null;
        try {
            res = travelController.getTravel(1, 1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertTrue(checkExpectedTravel(res, testTravel1));
    }

    @Test
    public void getTravel_returnTravelFromDAO() {
        prepareDAO();

        Travel res = null;
        try {
            res = travelController.getTravel(1, 1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertTrue(checkExpectedTravel(res, testTravel1));
    }

    @Test
    public void getTravel_throwNotExisting() {
        prepareDAO();

        Travel res = null;
        boolean thrown = false;
        try {
            res = travelController.getTravel(1, 2);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        } catch (NotExistingResponseException ex) {
            thrown = true;
        }
        assertTrue("Travel does not exist and exception is not thrown!", thrown);
    }

    @Test
    public void getAPITravels_shouldReturnAPITravels() throws IllegalAccessException {
        prepareCache();

        List<APITravel> res = null;
        try {
            res = travelController.getAPITravels(1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertEquals(res.get(0).getId(), testTravel1.getTravelIdentifier() + "");
        assertEquals(res.get(0).getName(), testTravel1.getName());
        assertEquals(res.get(0).getStartpoint().getCity(), testTravel1.getStart().getCity().getCity());
        assertEquals(res.get(0).getStartpoint().getCountry(), testTravel1.getStart().getCity().getCountry());
        assertEquals(res.get(0).getStartpoint().getHousenumber(), testTravel1.getStart().getHousenumber());
        assertEquals(res.get(0).getStartpoint().getPostalCode(), testTravel1.getStart().getCity().getPostalCode());
        assertEquals(res.get(0).getStartpoint().getStreet(), testTravel1.getStart().getStreet());
        assertEquals(res.get(0).getStartpoint().getCoordinates().getLat(), testTravel1.getStart().getCoordinates().getLat(), 0);
        assertEquals(res.get(0).getStartpoint().getCoordinates().getLon(), testTravel1.getStart().getCoordinates().getLon(), 0);
        assertEquals(res.get(0).getEndpoint().getCity(), testTravel1.getEnd().getCity().getCity());
        assertEquals(res.get(0).getEndpoint().getCountry(), testTravel1.getEnd().getCity().getCountry());
        assertEquals(res.get(0).getEndpoint().getHousenumber(), testTravel1.getEnd().getHousenumber());
        assertEquals(res.get(0).getEndpoint().getPostalCode(), testTravel1.getEnd().getCity().getPostalCode());
        assertEquals(res.get(0).getEndpoint().getStreet(), testTravel1.getEnd().getStreet());
        assertEquals(res.get(0).getEndpoint().getCoordinates().getLat(), testTravel1.getEnd().getCoordinates().getLat(), 0);
        assertEquals(res.get(0).getEndpoint().getCoordinates().getLon(), testTravel1.getEnd().getCoordinates().getLon(), 0);
        assertArrayEquals(res.get(0).getRecurring(), testTravel1.getRecurring().toArray());
        assertArrayEquals(res.get(0).getTimeInterval(), testTravel1.getInterval().toArray());
    }

    @Test
    public void getAPITravel_shouldReturnAPITravel() throws IllegalAccessException {
        prepareCache();

        APITravel res = null;
        try {
            res = travelController.getAPITravel(1, 1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertNotNull(res);
        assertEquals(res.getId(), testTravel1.getTravelIdentifier() + "");
        assertEquals(res.getName(), testTravel1.getName());
        assertEquals(res.getStartpoint().getCity(), testTravel1.getStart().getCity().getCity());
        assertEquals(res.getStartpoint().getCountry(), testTravel1.getStart().getCity().getCountry());
        assertEquals(res.getStartpoint().getHousenumber(), testTravel1.getStart().getHousenumber());
        assertEquals(res.getStartpoint().getPostalCode(), testTravel1.getStart().getCity().getPostalCode());
        assertEquals(res.getStartpoint().getStreet(), testTravel1.getStart().getStreet());
        assertEquals(res.getStartpoint().getCoordinates().getLat(), testTravel1.getStart().getCoordinates().getLat(), 0);
        assertEquals(res.getStartpoint().getCoordinates().getLon(), testTravel1.getStart().getCoordinates().getLon(), 0);
        assertEquals(res.getEndpoint().getCity(), testTravel1.getEnd().getCity().getCity());
        assertEquals(res.getEndpoint().getCountry(), testTravel1.getEnd().getCity().getCountry());
        assertEquals(res.getEndpoint().getHousenumber(), testTravel1.getEnd().getHousenumber());
        assertEquals(res.getEndpoint().getPostalCode(), testTravel1.getEnd().getCity().getPostalCode());
        assertEquals(res.getEndpoint().getStreet(), testTravel1.getEnd().getStreet());
        assertEquals(res.getEndpoint().getCoordinates().getLat(), testTravel1.getEnd().getCoordinates().getLat(), 0);
        assertEquals(res.getEndpoint().getCoordinates().getLon(), testTravel1.getEnd().getCoordinates().getLon(), 0);
        assertArrayEquals(res.getRecurring(), testTravel1.getRecurring().toArray());
        assertArrayEquals(res.getTimeInterval(), testTravel1.getInterval().toArray());
    }

    @Test
    public void createAPITravel_shouldReturnAPITravel() throws IllegalAccessException {
        try {
            when(mockedUserCache.getIfPresent(1)).thenReturn(testUser1);
            when(travelDAOMock.createTravel(eq(1), Matchers.<Travel>anyObject())).thenReturn(1);
        } catch (DataAccessException ex) {
            fail();
        }

        APITravel res = null;
        try {
            res = travelController.createAPITravel(1, testAPITravel1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertNotNull(res);
        assertEquals(res.getId(), testTravel1.getTravelIdentifier() + "");
        assertEquals(res.getName(), testTravel1.getName());
        assertEquals(res.getStartpoint().getCity(), testTravel1.getStart().getCity().getCity());
        assertEquals(res.getStartpoint().getCountry(), testTravel1.getStart().getCity().getCountry());
        assertEquals(res.getStartpoint().getHousenumber(), testTravel1.getStart().getHousenumber());
        assertEquals(res.getStartpoint().getPostalCode(), testTravel1.getStart().getCity().getPostalCode());
        assertEquals(res.getStartpoint().getStreet(), testTravel1.getStart().getStreet());
        assertEquals(res.getStartpoint().getCoordinates().getLat(), testTravel1.getStart().getCoordinates().getLat(), 0);
        assertEquals(res.getStartpoint().getCoordinates().getLon(), testTravel1.getStart().getCoordinates().getLon(), 0);
        assertEquals(res.getEndpoint().getCity(), testTravel1.getEnd().getCity().getCity());
        assertEquals(res.getEndpoint().getCountry(), testTravel1.getEnd().getCity().getCountry());
        assertEquals(res.getEndpoint().getHousenumber(), testTravel1.getEnd().getHousenumber());
        assertEquals(res.getEndpoint().getPostalCode(), testTravel1.getEnd().getCity().getPostalCode());
        assertEquals(res.getEndpoint().getStreet(), testTravel1.getEnd().getStreet());
        assertEquals(res.getEndpoint().getCoordinates().getLat(), testTravel1.getEnd().getCoordinates().getLat(), 0);
        assertEquals(res.getEndpoint().getCoordinates().getLon(), testTravel1.getEnd().getCoordinates().getLon(), 0);
        assertArrayEquals(res.getRecurring(), testTravel1.getRecurring().toArray());
        assertArrayEquals(res.getTimeInterval(), testTravel1.getInterval().toArray());
    }

    @Test
    public void modifyAPITravel_shouldReturnAPITravel() throws IllegalAccessException {
        prepareCache();

        APITravel res = null;
        try {
            res = travelController.modifyAPITravel(1, 1, testAPITravel2);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertNotNull(res);
        assertEquals(res.getId(), testTravel2.getTravelIdentifier() + "");
        assertEquals(res.getName(), testTravel2.getName());
        assertEquals(res.getStartpoint().getCity(), testTravel2.getStart().getCity().getCity());
        assertEquals(res.getStartpoint().getCountry(), testTravel2.getStart().getCity().getCountry());
        assertEquals(res.getStartpoint().getHousenumber(), testTravel2.getStart().getHousenumber());
        assertEquals(res.getStartpoint().getPostalCode(), testTravel2.getStart().getCity().getPostalCode());
        assertEquals(res.getStartpoint().getStreet(), testTravel2.getStart().getStreet());
        assertEquals(res.getStartpoint().getCoordinates().getLat(), testTravel2.getStart().getCoordinates().getLat(), 0);
        assertEquals(res.getStartpoint().getCoordinates().getLon(), testTravel2.getStart().getCoordinates().getLon(), 0);
        assertEquals(res.getEndpoint().getCity(), testTravel2.getEnd().getCity().getCity());
        assertEquals(res.getEndpoint().getCountry(), testTravel2.getEnd().getCity().getCountry());
        assertEquals(res.getEndpoint().getHousenumber(), testTravel2.getEnd().getHousenumber());
        assertEquals(res.getEndpoint().getPostalCode(), testTravel2.getEnd().getCity().getPostalCode());
        assertEquals(res.getEndpoint().getStreet(), testTravel2.getEnd().getStreet());
        assertEquals(res.getEndpoint().getCoordinates().getLat(), testTravel2.getEnd().getCoordinates().getLat(), 0);
        assertEquals(res.getEndpoint().getCoordinates().getLon(), testTravel2.getEnd().getCoordinates().getLon(), 0);
        assertArrayEquals(res.getRecurring(), testTravel2.getRecurring().toArray());
        assertArrayEquals(res.getTimeInterval(), testTravel2.getInterval().toArray());
    }

    @Test
    public void deleteAPITravel_shouldReturnTrue() throws IllegalAccessException {
        prepareCache();
        try {
            when(travelDAOMock.deleteTravel(1, 1)).thenReturn(true);
        } catch (DataAccessException ex) {
            fail();
        }

        boolean res = false;
        try {
            res = travelController.deleteAPITravel(1, 1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertTrue(res);
    }

    @Test
    public void existsAPITravel_shouldReturnTrue() {
        prepareCache();

        try {
            when(travelDAOMock.existsTravel(eq(1), Matchers.<Travel>anyObject())).thenReturn(true);
        } catch (DataAccessException ex) {
            fail();
        }

        boolean res = false;
        try {
            res = travelController.existsAPITravel(1, testAPITravel1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertTrue(res);
    }
}
