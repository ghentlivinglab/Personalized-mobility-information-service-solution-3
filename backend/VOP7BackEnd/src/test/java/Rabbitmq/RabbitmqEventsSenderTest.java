package Rabbitmq;

import com.rabbitmq.client.AMQP;
import org.junit.Before;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.Jam;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.JamFactory;
import vop.groep7.vop7backend.factories.SourceFactory;
import vop.groep7.vop7backend.factories.TransportFactory;
import vop.groep7.vop7backend.rabbitmq.RabbitmqEventsSender;

/**
 *
 * @author Backend Team
 */
public class RabbitmqEventsSenderTest {

    @Mock
    private Channel channelMock;

    @InjectMocks
    private RabbitmqEventsSender rabbitmq;

    private Event testEvent;

    public RabbitmqEventsSenderTest() {

    }

    @Before
    public void setUp() throws IOException, TimeoutException {
        rabbitmq = new RabbitmqEventsSender();
        // Mock annotations
        MockitoAnnotations.initMocks(this);

        createEvent();

    }

    private void createEvent() {
        Coordinate coordinates = CoordinateFactory.build(53.043368, 2.779090);
        Address location = AddressFactory.build(null, 0, null, null, null, coordinates);
        Source source = SourceFactory.build("TestEvent2", "http://www.example.org/random2_icon_url.png");
        Timestamp publicationTime = Timestamp.valueOf("2016-04-06 19:15:45.926");
        Timestamp lastEditTime = Timestamp.valueOf("2016-04-06 19:15:45.926");
        EventType type = EventTypeFactory.build("RANDOM_TEST_EVENT2");
        ArrayList<Transport> transportTypes = new ArrayList<>();
        Transport transport1 = TransportFactory.build("train");
        Transport transport2 = TransportFactory.build("bike");
        transportTypes.add(transport1);
        transportTypes.add(transport2);
        Coordinate start = CoordinateFactory.build(53.075762, 4.721739);
        Coordinate end = CoordinateFactory.build(53.035747, 4.765745);
        ArrayList<Jam> jams = new ArrayList<>();
        Jam jam = JamFactory.build(start, end, 21, 3000);
        jams.add(jam);
        testEvent = EventFactory.build(1, location, false, source, "Dit is een test 2 event.", publicationTime, lastEditTime, type, transportTypes, jams);
    }

    @Test
    public void sendWithUserIds_shouldSendCorrect() {
        List<Integer> userIds = new ArrayList<>();
        userIds.add(1);
        rabbitmq.sendWithUserIds(userIds, testEvent);
    }

    @Test
    public void sendWithUserIds_shouldNotSend() throws IOException {
        List<Integer> userIds = new ArrayList<>();
        userIds.add(1);  
        rabbitmq.sendWithUserIds(userIds, null);
    }
    
    @Test
    public void send_shouldThrowException() throws IOException{
        Mockito.doThrow(new IOException()).when(channelMock).basicPublish(Matchers.<String>any(), Matchers.<String>any(), Matchers.<AMQP.BasicProperties>any(), Matchers.<byte[]>any());
        rabbitmq.send(1, "Test");
    }

}
