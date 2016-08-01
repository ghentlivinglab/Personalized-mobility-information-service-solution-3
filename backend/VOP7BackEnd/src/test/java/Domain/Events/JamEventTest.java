package Domain.Events;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.Events.AlertEvent;
import vop.groep7.vop7backend.Models.Domain.Events.JamEvent;
import vop.groep7.vop7backend.Models.Domain.Jam;
import vop.groep7.vop7backend.Models.Domain.Transport;

/**
 *
 * @author Backend team
 */
public class JamEventTest {

    public JamEventTest() {
    }

    @Test
    public void addJam_() {
        ArrayList<Jam> jams = new ArrayList<>();
        JamEvent e = new JamEvent(1, null, true, null, null, null, null, null, null, jams);
        Coordinate c = new Coordinate(50.1, 34.9);
        Jam j = new Jam(c, c, 50, 1000);
        e.addJam(j);

        assertTrue(e.getJams().size() == 1);
        assertTrue(e.getJams().get(0).getDelay() == 1000);
    }

    @Test
    public void removeJam_() {
        ArrayList<Jam> jams = new ArrayList<>();
        Coordinate c = new Coordinate(50.1, 34.9);
        Jam j = new Jam(c, c, 50, 1000);
        jams.add(j);
        JamEvent e = new JamEvent(1, null, true, null, null, null, null, null, null, jams);

        Coordinate c2 = new Coordinate(50.1, 34.9);
        Jam j2 = new Jam(c, c, 50, 1000);
        
        e.removeJam(j2);
        assertTrue(e.getJams().isEmpty());
    }
}
