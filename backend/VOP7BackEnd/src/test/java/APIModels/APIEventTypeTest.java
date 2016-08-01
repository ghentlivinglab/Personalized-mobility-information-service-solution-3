package APIModels;

import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.Domain.EventType;

/**
 *
 * @author Backend Team
 */
public class APIEventTypeTest {
    
    public APIEventTypeTest() {
    }
    
    @Test
    public void equals_shouldBeTrue() {
        APIEventType e1 = new APIEventType();
        e1.setType("Test_type");
        APIEventType e2 = new APIEventType();
        e2.setType("Test_type");
        assertTrue(e1.equals(e2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APIEventType e1 = new APIEventType();
        e1.setType("Test_type");
        APIEventType e2 = new APIEventType();
        e2.setType("Other_test_type");
        assertFalse(e1.equals(e2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APIEventType e1 = new APIEventType();
        e1.setType("Test_type");
        APIEventType e2 = null;
        assertFalse(e1.equals(e2));
    }
}
