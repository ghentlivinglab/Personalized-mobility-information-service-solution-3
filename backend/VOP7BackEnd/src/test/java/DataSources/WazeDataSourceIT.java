package DataSources;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import vop.groep7.vop7backend.Controllers.EventController;
import vop.groep7.vop7backend.Datasources.WazeDataSource;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APIJam;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Backend Team
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class WazeDataSourceIT {

    @Mock
    private EventController eventController;
    
    @InjectMocks
    private WazeDataSource wds;
    
    private APIEvent testEvent;
    private APIEvent testInactiveEvent;
    private List<APIEvent> inactiveEvents;
    private List<APIEvent> activeEvents;
    
    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);
                
        testEvent = new APIEvent();
        testEvent.setId("1");
        testEvent.setActive(true);
        testEvent.setDescription("Dit is een ander test event.");
        Timestamp pub = new Timestamp(System.currentTimeMillis() - (4 * 60 * 60 * 1000));
        testEvent.setPublicationTime(pub.toString());
        Timestamp edit = new Timestamp(System.currentTimeMillis() - (3 * 60 * 60 * 1000));
        testEvent.setLastEditTime(edit.toString());
        APICoordinate c = new APICoordinate();
        c.setLat(51.560559);
        c.setLon(3.785948);
        testEvent.setCoordinates(c);
        APIJam[] j = new APIJam[1];
        j[0] = new APIJam();
        j[0].setStartNode(c);
        j[0].setEndNode(c);
        j[0].setSpeed(57);
        j[0].setDelay(11 * 60);
        testEvent.setJams(j);
        APITransport[] rtt = new APITransport[2];
        rtt[0] = APITransport.getTransport("car");
        rtt[1] = APITransport.getTransport("bus");
        testEvent.setRelevantForTransportationTypes(rtt);
        APIEventType type = new APIEventType();
        type.setType("JAM");
        testEvent.setType(type);
        
        testInactiveEvent = new APIEvent();
        testInactiveEvent.setId("2");
        testInactiveEvent.setActive(false);
        testInactiveEvent.setDescription("Dit is een ander test event.");
        testInactiveEvent.setPublicationTime(pub.toString());
        Timestamp edit2 = new Timestamp(System.currentTimeMillis() - (15 * 24 * 60 * 60 * 1000));
        testInactiveEvent.setLastEditTime(edit2.toString());
        testInactiveEvent.setCoordinates(c);
        testInactiveEvent.setJams(j);
        testInactiveEvent.setRelevantForTransportationTypes(rtt);
        testInactiveEvent.setType(type);
        
        activeEvents = new ArrayList<>();
        inactiveEvents = new ArrayList<>();
        
        activeEvents.add(testEvent);
        inactiveEvents.add(testInactiveEvent);
    }

    /* This test uses the actual waze api */
    @Test
    public void dataSourceCheckTest1() throws DataAccessException {
        when(eventController.getIdByAPIEvent(Matchers.<APIEvent>any())).thenReturn(1);
        when(eventController.getAPIEvent(1)).thenReturn(testEvent);
        when(eventController.modifyAPIEvent(1, testEvent)).thenReturn(testEvent);

        boolean success = true;
        try {
            wds.checkDataSource();
        } catch (Exception ex) {
            success = false;
        }
        assertTrue(success);
    }
    
    /* This test uses the actual waze api */
    @Test
    public void dataSourceCheckTest2() {
        when(eventController.getIdByAPIEvent(Matchers.<APIEvent>any())).thenReturn(-1);
        when(eventController.createAPIEvent(Matchers.<APIEvent>any())).thenReturn(testEvent);

        boolean success = true;
        try {
            wds.checkDataSource();
        } catch (Exception ex) {
            success = false;
        }
        assertTrue(success);
    }
    
    @Test
    public void dataSourceMakeInactive() {
        when(eventController.getActiveAPIEvents()).thenReturn(activeEvents);
        
        boolean success = true;
        try {
            wds.updateInactive();
        } catch (Exception ex) {
            success = false;
        }
        assertTrue(success);
        verify(eventController, times(1)).getActiveAPIEvents();
    }
    
    @Test
    public void dataSourceDeleteInactive() {
        when(eventController.getInactiveAPIEvents()).thenReturn(inactiveEvents);
        
        boolean success = true;
        try {
            wds.deleteInactive();
        } catch (Exception ex) {
            success = false;
        }
        assertTrue(success);
        verify(eventController, times(1)).getInactiveAPIEvents();
    }
}
