package Factories;

import Other.TestUtils;
import java.sql.Timestamp;
import java.util.ArrayList;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APISource;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.AlertEventFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.SourceFactory;
import vop.groep7.vop7backend.factories.TransportFactory;

/**
 *
 * @author Backend team
 */
public class AlertEventFactoryTest {
    
    
    @Test
    public void toDomainModel_shouldBeEquivalent(){
        APIEvent testAPIEvent = new APIEvent();
        testAPIEvent.setId("2");
        APICoordinate c = new APICoordinate();
        c.setLat(40.3);
        c.setLon(5.05);
        testAPIEvent.setCoordinates(c);
        testAPIEvent.setActive(true);
        APISource s = new APISource();
        s.setName("Test");
        s.setIconUrl("https://www.example.com/test");
        testAPIEvent.setSource(s);
        testAPIEvent.setDescription("Test Event");
        String t = new Timestamp(1460865239).toString();
        testAPIEvent.setPublicationTime(t);
        testAPIEvent.setLastEditTime(t);
        APIEventType e = new APIEventType();
        e.setType("Test_event_type");
        testAPIEvent.setType(e);
        APITransport[] rftt = new APITransport[1];
        rftt[0] = APITransport.CAR;
        testAPIEvent.setRelevantForTransportationTypes(rftt);
        
        Address a2 = AddressFactory.build(null, 0, null, null, null, CoordinateFactory.build(40.3, 5.05));
        Source s2 = SourceFactory.build("Test", "https://www.example.com/test");
        Timestamp t2 = new Timestamp(1460865239);
        EventType e2 = EventTypeFactory.build("Test_event_type");
        ArrayList<Transport> trans = new ArrayList<>();
        trans.add(TransportFactory.build("car"));
        Event testEvent = EventFactory.build(2, a2, true, s2, "Test Event", t2, t2, e2, trans, null);
        
        Event res = AlertEventFactory.toDomainModel(testAPIEvent);
        
        assertTrue(TestUtils.compareEvents(res, testEvent));
    }
}
