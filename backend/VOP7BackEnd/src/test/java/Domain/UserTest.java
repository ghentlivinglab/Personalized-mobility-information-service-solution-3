package Domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.factories.POIFactory;
import vop.groep7.vop7backend.factories.TravelFactory;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Backend Team
 */
public class UserTest {
    
    private User testUser;
    
    public UserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testUser = UserFactory.build(1, "test@example.com", "", "", "", true, true, true);
        Travel testTravel = TravelFactory.create("test", null, null, null, null);
        testUser.addTravel(testTravel);
        POI testPOI = POIFactory.create(null, "", 1000, true, true, true, null);
        testUser.addPOI(testPOI);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void removeTravel_shouldCatchException(){
        testUser.removeTravel(1);
    }
    
    @Test
    public void removePOI_shouldCatchException(){
        testUser.removePOI(1);
    }
    
    
    
}
