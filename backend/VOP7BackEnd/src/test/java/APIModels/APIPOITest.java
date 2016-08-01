package APIModels;

import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APIPOI;

/**
 *
 * @author Backend Team
 */
public class APIPOITest {

    public APIPOITest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APIPOI p1 = new APIPOI();

        APIPOI p2 = new APIPOI();

        assertTrue(p1.equals(p2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APIPOI p1 = new APIPOI();
        p1.setName("POI1");

        APIPOI p2 = new APIPOI();
        p2.setName("POI2");

        assertFalse(p1.equals(p2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APIPOI p1 = new APIPOI();
        p1.setName("POI1");

        APIPOI p2 = null;

        assertFalse(p1.equals(p2));
    }
}
