package Domain;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.factories.RouteFactory;
import vop.groep7.vop7backend.factories.TravelFactory;

/**
 *
 * @author Backend Team
 */
public class TravelTest {

    private Travel testTravel;

    public TravelTest() {
    }

    @Before
    public void setUp() {
        testTravel = TravelFactory.build(1, "test", null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTravelIdentifier_shouldThrowIllegalArgument() {
        testTravel.setTravelIdentifier(0);

    }

    @Test
    public void removeRoute_shouldCatchException() {
        testTravel = TravelFactory.build(1, "", null, null, null, null);
        Route r = RouteFactory.create(true, null, true, true, null, new ArrayList<>(), null);
        testTravel.addRoute(r);
        testTravel.removeRoute(1);
    }

    @Test
    public void addRoute_shouldNotAdd() {
        testTravel.addRoute(null);
        assertFalse(testTravel.getRoutes().contains(null));
    }

}
