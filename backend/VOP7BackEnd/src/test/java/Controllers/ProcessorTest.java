package Controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Controllers.MailService;
import vop.groep7.vop7backend.Controllers.Processor;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.Jam;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.database.sql.EventProcessingDAO;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.JamFactory;
import vop.groep7.vop7backend.factories.SourceFactory;
import vop.groep7.vop7backend.factories.TransportFactory;

/**
 *
 * @author Backend Team
 */
public class ProcessorTest {

    @Mock
    private MailService mailServiceMock;

    @Mock
    private EventProcessingDAO eventProcessingDAOMock;

    @InjectMocks
    private Processor processor;

    public ProcessorTest() {
    }

    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void proccess_shouldProcess(){
        Address a = AddressFactory.build("Ghent", 9000, "Street", "House number", "Belgium", CoordinateFactory.build(50.0, 3.1));
        Source s = SourceFactory.build("Test", "https://www.example.com/test");
        Timestamp t = new Timestamp(1460565235);
        EventType e = EventTypeFactory.build("Test_event_type");
        ArrayList<Transport> trans = new ArrayList<>();
        trans.add(TransportFactory.build("car"));
        ArrayList<Jam> j = new ArrayList<>();
        j.add(JamFactory.build(CoordinateFactory.build(50.0, 3.1), CoordinateFactory.build(50.0, 3.1), 10, 1000));
        Event testEvent = EventFactory.build(1, a, true, s, "Test Event", t, t, e, trans, j);
    
        processor.process(testEvent);
    }
    
}
