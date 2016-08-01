package Controllers;

import com.google.common.cache.Cache;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.mockito.Matchers;
import static org.mockito.Matchers.eq;
import vop.groep7.vop7backend.Controllers.Controller;
import vop.groep7.vop7backend.Controllers.POIController;
import vop.groep7.vop7backend.Models.APIModels.APIAddress;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APINotify;
import vop.groep7.vop7backend.Models.APIModels.APIPOI;
import vop.groep7.vop7backend.Models.APIModels.APIUser;
import vop.groep7.vop7backend.Models.APIModels.APIValidation;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.POIDAO;
import vop.groep7.vop7backend.database.sql.UserDAO;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.POIFactory;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Jonas Van Wilder
 */
public class POIControllerTest {

    @Mock
    private UserDAO userDAOMock;
    @Mock
    private POIDAO poiDAOMock;

    @InjectMocks
    private POIController poiController;

    // mocked userCache for test
    private static Cache<Integer, User> mockedUserCache;

    private User testUser1;
    private POI testPOI1;
    private POI testPOI2;
    private APIUser testAPIUser1;
    private APIPOI testAPIPOI1;
    private APIPOI testAPIPOI2;

    /**
     *
     */
    public POIControllerTest() {
        createTestPOIs();
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
        testAPIUser1 = new APIUser();
        testAPIUser1.setId("1000");
        testAPIUser1.setEmail("George.Vermalen@example.com");
        testAPIUser1.setFirstName("George");
        testAPIUser1.setLastName("Vermalen");
        testAPIUser1.setCellNumber(null);
        testAPIUser1.setMuteNotifications(false);
        APIValidation v = new APIValidation();
        v.setCellNumberValidated(true);
        v.setEmailValidated(true);
        testAPIUser1.setValidated(v);
    }

    private void createTestPOIs() {
        Coordinate c = CoordinateFactory.build(1.1, 2.2);
        Address a = AddressFactory.build("Gent", 9000, "Straat", "Nummer", "Belgie", c);
        ArrayList<EventType> list = new ArrayList<>();
        list.add(EventTypeFactory.build("TestType"));
        testPOI1 = POIFactory.build(1, a, "Naam", 10, true, true, true, list);

        Coordinate c2 = CoordinateFactory.build(1.12, 2.22);
        Address a2 = AddressFactory.build("Gent2", 90002, "Straat2", "Nummer2", "Belgie2", c2);
        ArrayList<EventType> list2 = new ArrayList<>();
        list2.add(EventTypeFactory.build("TestType2"));
        testPOI2 = POIFactory.build(1, a2, "Naam2", 12, false, false, false, list2);

        testAPIPOI1 = new APIPOI();
        testAPIPOI1.setActive(true);
        APIAddress apiA = new APIAddress();
        apiA.setCity("Gent");
        apiA.setPostalCode(9000);
        apiA.setCountry("Belgie");
        apiA.setStreet("Straat");
        apiA.setHousenumber("Nummer");
        APICoordinate apiAC = new APICoordinate();
        apiAC.setLat(1.1);
        apiAC.setLon(2.2);
        apiA.setCoordinates(apiAC);
        testAPIPOI1.setAddress(apiA);
        testAPIPOI1.setName("Naam");
        APINotify apiN = new APINotify();
        apiN.setCellNumberNotify(true);
        apiN.setEmailNotify(true);
        testAPIPOI1.setNotify(apiN);
        APIEventType[] apiE = new APIEventType[1];
        APIEventType type = new APIEventType();
        type.setType("TestType");
        apiE[0] = type;
        testAPIPOI1.setNotifyForEventTypes(apiE);
        testAPIPOI1.setRadius(10);

        testAPIPOI2 = new APIPOI();
        testAPIPOI2.setId("1");
        testAPIPOI2.setActive(false);
        APIAddress apiA2 = new APIAddress();
        apiA2.setCity("Gent2");
        apiA2.setPostalCode(90002);
        apiA2.setCountry("Belgie2");
        apiA2.setStreet("Straat2");
        apiA2.setHousenumber("Nummer2");
        APICoordinate apiAC2 = new APICoordinate();
        apiAC2.setLat(1.12);
        apiAC2.setLon(2.22);
        apiA2.setCoordinates(apiAC2);
        testAPIPOI2.setAddress(apiA2);
        testAPIPOI2.setName("Naam2");
        APINotify apiN2 = new APINotify();
        apiN2.setCellNumberNotify(false);
        apiN2.setEmailNotify(false);
        testAPIPOI2.setNotify(apiN2);
        APIEventType[] apiE2 = new APIEventType[1];
        APIEventType type2 = new APIEventType();
        type2.setType("TestType2");
        apiE2[0] = type2;
        testAPIPOI2.setNotifyForEventTypes(apiE2);
        testAPIPOI2.setRadius(12);
    }

    private boolean checkExpectedPOI(POI p1, POI p2) {
        if (p1 == null || p2 == null) {
            return false;
        }

        return Objects.equals(p1.getName(), p2.getName())
                && Objects.equals(p1.getRadius(), p2.getRadius())
                && Objects.equals(p1.isActive(), p2.isActive())
                && Objects.equals(p1.getAddress().getCity().getCity(), p2.getAddress().getCity().getCity())
                && Objects.equals(p1.getAddress().getCity().getCountry(), p2.getAddress().getCity().getCountry())
                && Objects.equals(p1.getAddress().getCity().getPostalCode(), p2.getAddress().getCity().getPostalCode())
                && Objects.equals(p1.getAddress().getCoordinates().getLat(), p2.getAddress().getCoordinates().getLat())
                && Objects.equals(p1.getAddress().getCoordinates().getLon(), p2.getAddress().getCoordinates().getLon())
                && Objects.equals(p1.getAddress().getHousenumber(), p2.getAddress().getHousenumber())
                && Objects.equals(p1.getAddress().getStreet(), p2.getAddress().getStreet())
                && Objects.equals(p1.getNotifications().isNotifyCellPhone(), p2.getNotifications().isNotifyCellPhone())
                && Objects.equals(p1.getNotifications().isNotifyEmail(), p2.getNotifications().isNotifyEmail())
                && Arrays.deepEquals(p1.getNotifications().getNotifyForEventTypes().toArray(), p2.getNotifications().getNotifyForEventTypes().toArray());

    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    private void prepareCache() {
        when(mockedUserCache.getIfPresent(1)).thenReturn(testUser1);
        testUser1.addPOI(testPOI1);
    }

    private void prepareDAO() {
        try {
            // prepare dao
            when(userDAOMock.getUser(1)).thenReturn(testUser1);
            List<POI> list = new ArrayList<>();
            list.add(testPOI1);
            when(poiDAOMock.getPOIs(1)).thenReturn(list);
        } catch (DataAccessException ex) {
            fail();
        }
    }

    @Test
    public void getPOITest_shouldReturnNull() throws DataAccessException {
        when(userDAOMock.getUser(1)).thenReturn(testUser1);
        when(poiDAOMock.getPOIs(1)).thenReturn(new ArrayList<POI>(){{
            add(POIFactory.build(-1, null, "", 1000, true, true, true, null));
        }});
        POI res = poiController.getPOI(1, 1);
        
        assertNull(res);
    }

    @Test
    public void getPOIs_returnPOIsFromCache() {
        prepareCache();

        List<POI> res = null;
        try {
            res = poiController.getPOIs(1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertTrue(checkExpectedPOI(res.get(0), testPOI1));
    }

    @Test
    public void getPOIs_returnPOIsFromDAO() {
        prepareDAO();

        List<POI> res = null;
        try {
            res = poiController.getPOIs(1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertTrue(checkExpectedPOI(res.get(0), testPOI1));
    }

    @Test
    public void getAPIPOIs_shouldReturnAPIPOIs() throws IllegalAccessException {
        prepareCache();

        List<APIPOI> res = null;
        try {
            res = poiController.getAPIPOIs(1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertNotNull(res);
        assertEquals(res.get(0).getId(), testPOI1.getPoiIdentifier() + "");
        assertEquals(res.get(0).getName(), testPOI1.getName());
        assertEquals(res.get(0).getAddress().getCity(), testPOI1.getAddress().getCity().getCity());
        assertEquals(res.get(0).getAddress().getCountry(), testPOI1.getAddress().getCity().getCountry());
        assertEquals(res.get(0).getAddress().getHousenumber(), testPOI1.getAddress().getHousenumber());
        assertEquals(res.get(0).getAddress().getPostalCode(), testPOI1.getAddress().getCity().getPostalCode());
        assertEquals(res.get(0).getAddress().getStreet(), testPOI1.getAddress().getStreet());
        assertEquals(res.get(0).getAddress().getCoordinates().getLat(), testPOI1.getAddress().getCoordinates().getLat(), 0);
        assertEquals(res.get(0).getAddress().getCoordinates().getLon(), testPOI1.getAddress().getCoordinates().getLon(), 0);
        assertEquals(res.get(0).getNotify().isCellNumberNotify(), testPOI1.getNotifications().isNotifyCellPhone());
        assertEquals(res.get(0).getNotify().isEmailNotify(), testPOI1.getNotifications().isNotifyEmail());
        assertEquals(res.get(0).getNotifyForEventTypes()[0].getType(), testPOI1.getNotifications().getNotifyForEventTypes().get(0).getType());
        assertEquals(res.get(0).getRadius(), testPOI1.getRadius());
        assertEquals(res.get(0).isActive(), testPOI1.isActive());
    }

    @Test
    public void getAPIPOI_shouldReturnAPIPOI() throws IllegalAccessException {
        prepareCache();

        APIPOI res = null;
        try {
            res = poiController.getAPIPOI(1, 1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertNotNull(res);
        assertEquals(res.getId(), testPOI1.getPoiIdentifier() + "");
        assertEquals(res.getName(), testPOI1.getName());
        assertEquals(res.getAddress().getCity(), testPOI1.getAddress().getCity().getCity());
        assertEquals(res.getAddress().getCountry(), testPOI1.getAddress().getCity().getCountry());
        assertEquals(res.getAddress().getHousenumber(), testPOI1.getAddress().getHousenumber());
        assertEquals(res.getAddress().getPostalCode(), testPOI1.getAddress().getCity().getPostalCode());
        assertEquals(res.getAddress().getStreet(), testPOI1.getAddress().getStreet());
        assertEquals(res.getAddress().getCoordinates().getLat(), testPOI1.getAddress().getCoordinates().getLat(), 0);
        assertEquals(res.getAddress().getCoordinates().getLon(), testPOI1.getAddress().getCoordinates().getLon(), 0);
        assertEquals(res.getNotify().isCellNumberNotify(), testPOI1.getNotifications().isNotifyCellPhone());
        assertEquals(res.getNotify().isEmailNotify(), testPOI1.getNotifications().isNotifyEmail());
        assertEquals(res.getNotifyForEventTypes()[0].getType(), testPOI1.getNotifications().getNotifyForEventTypes().get(0).getType());
        assertEquals(res.getRadius(), testPOI1.getRadius());
        assertEquals(res.isActive(), testPOI1.isActive());
    }

    @Test
    public void createAPIPOI_shouldReturnAPIPOI() throws IllegalAccessException {
        try {
            when(mockedUserCache.getIfPresent(1)).thenReturn(testUser1);
            when(poiDAOMock.createPOI(eq(1), Matchers.<POI>anyObject())).thenReturn(1);
        } catch (DataAccessException ex) {
            fail();
        }

        APIPOI res = null;
        try {
            res = poiController.createAPIPOI(1, testAPIPOI1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertNotNull(res);
        assertEquals(res.getId(), testPOI1.getPoiIdentifier() + "");
        assertEquals(res.getName(), testPOI1.getName());
        assertEquals(res.getAddress().getCity(), testPOI1.getAddress().getCity().getCity());
        assertEquals(res.getAddress().getCountry(), testPOI1.getAddress().getCity().getCountry());
        assertEquals(res.getAddress().getHousenumber(), testPOI1.getAddress().getHousenumber());
        assertEquals(res.getAddress().getPostalCode(), testPOI1.getAddress().getCity().getPostalCode());
        assertEquals(res.getAddress().getStreet(), testPOI1.getAddress().getStreet());
        assertEquals(res.getAddress().getCoordinates().getLat(), testPOI1.getAddress().getCoordinates().getLat(), 0);
        assertEquals(res.getAddress().getCoordinates().getLon(), testPOI1.getAddress().getCoordinates().getLon(), 0);
        assertEquals(res.getNotify().isCellNumberNotify(), testPOI1.getNotifications().isNotifyCellPhone());
        assertEquals(res.getNotify().isEmailNotify(), testPOI1.getNotifications().isNotifyEmail());
        assertEquals(res.getNotifyForEventTypes()[0].getType(), testPOI1.getNotifications().getNotifyForEventTypes().get(0).getType());
        assertEquals(res.getRadius(), testPOI1.getRadius());
        assertEquals(res.isActive(), testPOI1.isActive());
    }

    @Test
    public void modifyAPIPOI_shouldReturnAPIPOI() throws IllegalAccessException {
        prepareCache();

        APIPOI res = null;
        try {
            res = poiController.modifyAPIPOI(1, 1, testAPIPOI2);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertNotNull(res);
        assertEquals(res.getId(), testPOI2.getPoiIdentifier() + "");
        assertEquals(res.getName(), testPOI2.getName());
        assertEquals(res.getAddress().getCity(), testPOI2.getAddress().getCity().getCity());
        assertEquals(res.getAddress().getCountry(), testPOI2.getAddress().getCity().getCountry());
        assertEquals(res.getAddress().getHousenumber(), testPOI2.getAddress().getHousenumber());
        assertEquals(res.getAddress().getPostalCode(), testPOI2.getAddress().getCity().getPostalCode());
        assertEquals(res.getAddress().getStreet(), testPOI2.getAddress().getStreet());
        assertEquals(res.getAddress().getCoordinates().getLat(), testPOI2.getAddress().getCoordinates().getLat(), 0);
        assertEquals(res.getAddress().getCoordinates().getLon(), testPOI2.getAddress().getCoordinates().getLon(), 0);
        assertEquals(res.getNotify().isCellNumberNotify(), testPOI2.getNotifications().isNotifyCellPhone());
        assertEquals(res.getNotify().isEmailNotify(), testPOI2.getNotifications().isNotifyEmail());
        assertEquals(res.getNotifyForEventTypes()[0].getType(), testPOI2.getNotifications().getNotifyForEventTypes().get(0).getType());
        assertEquals(res.getRadius(), testPOI2.getRadius());
        assertEquals(res.isActive(), testPOI2.isActive());
    }

    @Test
    public void deleteAPIPOI_shouldReturnTrue() throws IllegalAccessException {
        prepareCache();
        try {
            when(poiDAOMock.deletePOI(1, 1)).thenReturn(true);
        } catch (DataAccessException ex) {
            fail();
        }

        boolean res = false;
        try {
            res = poiController.deleteAPIPOI(1, 1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertTrue(res);
    }

    @Test
    public void existsAPIPOI_shouldReturnTrue() {
        prepareCache();

        try {
            when(poiDAOMock.existsPOI(eq(1), Matchers.<POI>anyObject())).thenReturn(true);
        } catch (DataAccessException ex) {
            fail();
        }

        boolean res = false;
        try {
            res = poiController.existsAPIPOI(1, testAPIPOI1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }

        assertTrue(res);
    }
}
