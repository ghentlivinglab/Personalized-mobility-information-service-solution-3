package APIModels.DataDump;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APINotify;
import vop.groep7.vop7backend.Models.APIModels.APIPOI;
import vop.groep7.vop7backend.Models.APIModels.APIRoute;
import vop.groep7.vop7backend.Models.APIModels.APITravel;
import vop.groep7.vop7backend.Models.APIModels.APIUser;
import vop.groep7.vop7backend.Models.APIModels.Datadump.APIDataDump;
import vop.groep7.vop7backend.Models.APIModels.Datadump.APITravelDump;
import vop.groep7.vop7backend.Models.APIModels.Datadump.APIUserDump;

/**
 *
 * @author Backend Team
 */
public class APIDataDumpTest {
    
    public APIDataDumpTest() {
    }
    
   @Test
    public void equals_shouldBeTrue() {
        // test data dump 1
        APIDataDump dd1 = new APIDataDump();
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
        dd1.setUsers(userDumps);
        
        APIEvent[] events = new APIEvent[1];
        events[0] = new APIEvent();
        dd1.setEvents(events);
        
        APIEventType[] eventTypes = new APIEventType[1];
        eventTypes[0] = new APIEventType();
        dd1.setEventTypes(eventTypes);
        
        // test datadump 2
        APIDataDump dd2 = new APIDataDump();
        APIUserDump[] userDumps2 = new APIUserDump[1];
        APIUser u2 = new APIUser();
        userDumps2[0] = new APIUserDump();
        userDumps2[0].setUser(u2);
        APITravelDump[] travelDumps2 = new APITravelDump[1];
        APITravel t2 = new APITravel();
        travelDumps2[0] = new APITravelDump();
        travelDumps2[0].setTravel(t2);
        APIRoute[] routes2 = new APIRoute[1];
        routes2[0] = new APIRoute();
        travelDumps2[0].setRoutes(routes2);
        userDumps2[0].setTravels(travelDumps2);
        APIPOI[] pois2 = new APIPOI[1];
        pois2[0] = new APIPOI();
        userDumps2[0].setPointsOfInterest(pois2);
        dd2.setUsers(userDumps2);
        
        APIEvent[] events2 = new APIEvent[1];
        events2[0] = new APIEvent();
        dd2.setEvents(events2);
        
        APIEventType[] eventTypes2 = new APIEventType[1];
        eventTypes2[0] = new APIEventType();
        dd2.setEventTypes(eventTypes2);

        assertTrue(dd1.equals(dd2));
    }

    @Test
    public void equals_shouldBeFalse() {
        // test data dump 1
        APIDataDump dd1 = new APIDataDump();
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
        dd1.setUsers(userDumps);
        
        APIEvent[] events = new APIEvent[1];
        events[0] = new APIEvent();
        dd1.setEvents(events);
        
        APIEventType[] eventTypes = new APIEventType[1];
        eventTypes[0] = new APIEventType();
        dd1.setEventTypes(eventTypes);
        
        // test datadump 2
        APIDataDump dd2 = new APIDataDump();
        APIUserDump[] userDumps2 = new APIUserDump[1];
        APIUser u2 = new APIUser();
        userDumps2[0] = new APIUserDump();
        userDumps2[0].setUser(u2);
        APITravelDump[] travelDumps2 = new APITravelDump[1];
        APITravel t2 = new APITravel();
        travelDumps2[0] = null;
        userDumps2[0].setTravels(travelDumps2);
        APIPOI[] pois2 = new APIPOI[1];
        pois2[0] = null;
        userDumps2[0].setPointsOfInterest(pois2);
        dd2.setUsers(userDumps2);
        
        APIEvent[] events2 = new APIEvent[1];
        events2[0] = new APIEvent();
        dd2.setEvents(events2);
        
        APIEventType[] eventTypes2 = new APIEventType[1];
        eventTypes2[0] = new APIEventType();
        dd2.setEventTypes(eventTypes2);

        assertFalse(dd1.equals(dd2));
    }
    

    @Test
    public void equals_shouldBeFalse2() {
        // test data dump 1
        APIDataDump dd1 = new APIDataDump();
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
        dd1.setUsers(userDumps);
        
        APIEvent[] events = new APIEvent[1];
        events[0] = new APIEvent();
        dd1.setEvents(events);
        
        APIEventType[] eventTypes = new APIEventType[1];
        eventTypes[0] = new APIEventType();
        dd1.setEventTypes(eventTypes);
        
        // test datadump 2
        APIDataDump dd2 = null;

        assertFalse(dd1.equals(dd2));
    }
}
