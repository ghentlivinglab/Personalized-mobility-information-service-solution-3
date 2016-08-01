package Domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Interval;

/**
 *
 * @author Backend Team
 */
public class EventTypeTest {
    
    public EventTypeTest() {
    }
    
    @Test
    public void equals_shouldBeTrue() {
        EventType e1 = new EventType("Test_type");
        EventType e2 = new EventType("Test_type");
        assertTrue(e1.equals(e2));
    }

    @Test
    public void equals_shouldBeFalse() {
        EventType e1 = new EventType("Test_type");
        EventType e2 = new EventType("Other_test_type");
        assertFalse(e1.equals(e2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        EventType e1 = new EventType("Test_type");
        EventType e2 = null;
        assertFalse(e1.equals(e2));
    }
    
    @Test
    public void equals_shouldBeFalse3() {
        EventType e1 = new EventType("Test_type");
        Object o1 = new Object();
        assertFalse(e1.equals(o1));
    }

    @Test
    public void hashCode_shouldBeEqual() {
        EventType e1 = new EventType("Test_type");
        EventType e2 = new EventType("Test_type");
        assertTrue(e1.hashCode() == e2.hashCode());

    }

    @Test
    public void hashCode_shouldBeDifferent() {
        EventType e1 = new EventType("Test_type");
        EventType e2 = new EventType("Other_test_type");
        assertFalse(e1.hashCode() == e2.hashCode());
    }
}
