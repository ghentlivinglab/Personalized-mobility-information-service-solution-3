package APIModels;

import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APITravel;

/**
 *
 * @author Backend Team
 */
public class APITravelTest {

    public APITravelTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APITravel t1 = new APITravel();

        APITravel t2 = new APITravel();

        assertTrue(t1.equals(t2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APITravel t1 = new APITravel();
        t1.setName("Travel1");

        APITravel t2 = new APITravel();
        t2.setName("Travel2");

        assertFalse(t1.equals(t2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APITravel t1 = new APITravel();
        t1.setName("Travel1");

        APITravel t2 = null;

        assertFalse(t1.equals(t2));
    }
}
