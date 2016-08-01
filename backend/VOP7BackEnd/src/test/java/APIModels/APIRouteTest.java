package APIModels;

import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APILinks;
import vop.groep7.vop7backend.Models.APIModels.APINotify;
import vop.groep7.vop7backend.Models.APIModels.APIRoute;
import vop.groep7.vop7backend.Models.APIModels.APITransport;

/**
 *
 * @author Backend Team
 */
public class APIRouteTest {

    public APIRouteTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APIRoute r1 = new APIRoute();
        r1.setId("1");
        r1.setActive(true);
        APILinks[] l1 = new APILinks[1];
        l1[0] = new APILinks();
        l1[0].setRel("self");
        l1[0].setHref("https://www.example.com/route");
        r1.setLinks(l1);
        APINotify n1 = new APINotify();
        n1.setEmailNotify(true);
        n1.setCellNumberNotify(true);
        r1.setNotify(n1);
        r1.setTransportationType(APITransport.BUS);
        APICoordinate[] waypoints = new APICoordinate[1];
        waypoints[0] = new APICoordinate();
        waypoints[0].setLat(53.2);
        waypoints[0].setLon(3.78);
        r1.setWaypoints(waypoints);
        APIEventType[] e1 = new APIEventType[1];
        e1[0] = new APIEventType();
        e1[0].setType("Test");
        r1.setNotifyForEventTypes(e1);

        APIRoute r2 = new APIRoute();
        r2.setId("1");
        r2.setActive(true);
        APILinks[] l2 = new APILinks[1];
        l2[0] = new APILinks();
        l2[0].setRel("self");
        l2[0].setHref("https://www.example.com/route");
        r2.setLinks(l2);
        APINotify n2 = new APINotify();
        n2.setEmailNotify(true);
        n2.setCellNumberNotify(true);
        r2.setNotify(n1);
        r2.setTransportationType(APITransport.BUS);
        APICoordinate[] waypoints2 = new APICoordinate[1];
        waypoints2[0] = new APICoordinate();
        waypoints2[0].setLat(53.2);
        waypoints2[0].setLon(3.78);
        r2.setWaypoints(waypoints);
        APIEventType[] e2 = new APIEventType[1];
        e2[0] = new APIEventType();
        e2[0].setType("Test");
        r2.setNotifyForEventTypes(e1);

        assertTrue(r1.equals(r2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APIRoute r1 = new APIRoute();
        r1.setId("1");
        r1.setActive(true);
        APILinks[] l1 = new APILinks[1];
        l1[0] = new APILinks();
        l1[0].setRel("self");
        l1[0].setHref("https://www.example.com/route");
        r1.setLinks(l1);
        APINotify n1 = new APINotify();
        n1.setEmailNotify(true);
        n1.setCellNumberNotify(true);
        r1.setNotify(n1);
        r1.setTransportationType(APITransport.BUS);
        APICoordinate[] waypoints = new APICoordinate[1];
        waypoints[0] = new APICoordinate();
        waypoints[0].setLat(53.2);
        waypoints[0].setLon(3.78);
        r1.setWaypoints(waypoints);
        APIEventType[] e1 = new APIEventType[1];
        e1[0] = new APIEventType();
        e1[0].setType("Test");
        r1.setNotifyForEventTypes(e1);

        APIRoute r2 = new APIRoute();
        r2.setId("1");
        r2.setActive(true);
        APILinks[] l2 = new APILinks[1];
        l2[0] = new APILinks();
        l2[0].setRel("self");
        l2[0].setHref("https://www.example.com/route");
        r2.setLinks(l2);
        APINotify n2 = new APINotify();
        n2.setEmailNotify(true);
        n2.setCellNumberNotify(true);
        r2.setNotify(n1);
        r2.setTransportationType(APITransport.BIKE);
        APICoordinate[] waypoints2 = new APICoordinate[1];
        waypoints2[0] = new APICoordinate();
        waypoints2[0].setLat(51.2);
        waypoints2[0].setLon(4.58);
        r2.setWaypoints(waypoints);
        APIEventType[] e2 = new APIEventType[1];
        e2[0] = new APIEventType();
        e2[0].setType("Test2");
        r2.setNotifyForEventTypes(e1);

        assertFalse(r1.equals(r2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APIRoute r1 = new APIRoute();
        r1.setId("1");
        r1.setActive(true);
        APILinks[] l1 = new APILinks[1];
        l1[0] = new APILinks();
        l1[0].setRel("self");
        l1[0].setHref("https://www.example.com/route");
        r1.setLinks(l1);
        APINotify n1 = new APINotify();
        n1.setEmailNotify(true);
        n1.setCellNumberNotify(true);
        r1.setNotify(n1);
        r1.setTransportationType(APITransport.BUS);
        APICoordinate[] waypoints = new APICoordinate[1];
        waypoints[0] = new APICoordinate();
        waypoints[0].setLat(53.2);
        waypoints[0].setLon(3.78);
        r1.setWaypoints(waypoints);
        APIEventType[] e1 = new APIEventType[1];
        e1[0] = new APIEventType();
        e1[0].setType("Test");
        r1.setNotifyForEventTypes(e1);

        APIRoute r2 = null;

        assertFalse(r1.equals(r2));
    }

}
