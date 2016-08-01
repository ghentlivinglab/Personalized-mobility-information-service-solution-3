package Domain.Events;

import static java.lang.Thread.sleep;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.Events.AlertEvent;
import vop.groep7.vop7backend.Models.Domain.Transport;

/**
 *
 * @author Backend Team
 */
public class AlertEventTest {

    public AlertEventTest() {
    }

    @Test
    public void addTransportationType_() {
        ArrayList<Transport> types = new ArrayList<>();
        AlertEvent e = new AlertEvent(1, null, true, null, null, null, null, null, types);
        e.addTransportationType(new Transport("bike"));

        assertTrue(e.getRelevantTransportationTypes().size() == 1);
        assertTrue(e.getRelevantTransportationTypes().get(0).getTransport().equals("bike"));
    }

    @Test
    public void removeTransportationType_() {
        ArrayList<Transport> types = new ArrayList<>();
        types.add(new Transport("bike"));
        AlertEvent e = new AlertEvent(1, null, true, null, null, null, null, null, types);

        e.removeTransportationType(new Transport(("bike")));

        assertTrue(e.getRelevantTransportationTypes().isEmpty());

    }

    @Test
    public void finish_() {
        AlertEvent e = new AlertEvent(1, null, true, null, null, null, null, null, null);
        e.finish();
        assertFalse(e.isActive());
    }

    @Test
    public void update_() throws InterruptedException {
        ArrayList<Transport> types = new ArrayList<>();
        AlertEvent e = new AlertEvent(1, null, true, null, null, null, null, null, types);
        Timestamp t = (Timestamp) e.getLastEditTime().clone();

        sleep(5000);

        e.addTransportationType(new Transport("train"));

        assertNotNull(e.getLastEditTime());
        assertFalse(t.equals(e.getLastEditTime()));
    }
}
