package APIModels;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APIJam;
import vop.groep7.vop7backend.Models.APIModels.APIValidation;

/**
 *
 * @author Backend Team
 */
public class APIJamTest {

    public APIJamTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APIJam j1 = new APIJam();
        j1.setDelay(1000);
        j1.setSpeed(15);
        j1.setStartNode(null);
        j1.setEndNode(null);

        APIJam j2 = new APIJam();
        j2.setDelay(1000);
        j2.setSpeed(15);
        j2.setStartNode(null);
        j2.setEndNode(null);

        assertTrue(j1.equals(j2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APIJam j1 = new APIJam();
        j1.setDelay(1000);
        j1.setSpeed(15);
        j1.setStartNode(null);
        j1.setEndNode(null);

        APIJam j2 = new APIJam();
        j2.setDelay(100);
        j2.setSpeed(78);
        j2.setStartNode(null);
        j2.setEndNode(null);

        assertFalse(j1.equals(j2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APIJam j1 = new APIJam();
        j1.setDelay(1000);
        j1.setSpeed(15);
        j1.setStartNode(null);
        j1.setEndNode(null);

        APIJam j2 = null;

        assertFalse(j1.equals(j2));
    }

}
