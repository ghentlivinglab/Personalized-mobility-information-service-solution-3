package APIModels;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APIAddress;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;

/**
 *
 * @author Backend Team
 */
public class APICoordinateTest {

    public APICoordinateTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APICoordinate c1 = new APICoordinate();
        c1.setLat(54.2);
        c1.setLon(23.1);
        APICoordinate c2 = new APICoordinate();
        c2.setLat(54.2);
        c2.setLon(23.1);
        assertTrue(c1.equals(c2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APICoordinate c1 = new APICoordinate();
        c1.setLat(63.9);
        c1.setLon(13.4);
        APICoordinate c2 = new APICoordinate();
        c2.setLat(54.2);
        c2.setLon(23.1);
        assertFalse(c1.equals(c2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APICoordinate c1 = new APICoordinate();
        c1.setLat(63.9);
        c1.setLon(13.4);
        APICoordinate c2 = null;
        assertFalse(c1.equals(c2));
    }

}
