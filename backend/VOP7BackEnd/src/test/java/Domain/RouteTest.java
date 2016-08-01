package Domain;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.factories.RouteFactory;

/**
 *
 * @author Backend Team
 */
public class RouteTest {

    public RouteTest() {
    }

    @Test
    public void removeWaypoint_() {
        ArrayList<Coordinate> waypoints = new ArrayList<>();
        waypoints.add(new Coordinate(51.3, 3.73));
        Route r = new Route(true, null, true, true, waypoints, null, null);

        r.removeWaypoint(new Coordinate(51.3, 3.73));
        assertTrue(r.getWaypoints().isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setRouteIdentifier_shouldThrowIllegalArgument(){
        Route r = RouteFactory.build(1, true, null, true, true, null, null);
        r.setRouteIdentifier(0);
    }
}
