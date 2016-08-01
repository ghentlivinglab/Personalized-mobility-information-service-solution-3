package Domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Notifications;

/**
 *
 * @author Backend Team
 */
public class NotificationsTest {

    public NotificationsTest() {
    }

    @Test
    public void addType() {
        ArrayList<EventType> types = new ArrayList<>();
        Notifications n = new Notifications(true, true, types);
        EventType e = new EventType("Test_type");
        n.addType(e);
        assertTrue(n.getNotifyForEventTypes().size() == 1);
        assertTrue(n.getNotifyForEventTypes().get(0).getType().equals(e.getType()));
    }

    @Test
    public void addType_null() {
        ArrayList<EventType> types = new ArrayList<>();
        Notifications n = new Notifications(true, true, types);
        EventType e = null;
        n.addType(e);
        assertTrue(n.getNotifyForEventTypes().isEmpty());
    }

    @Test
    public void removeType() {
        ArrayList<EventType> types = new ArrayList<>();
        EventType e = new EventType("Test_type");
        types.add(e);
        Notifications n = new Notifications(true, true, types);
        n.removeType(e);
        assertTrue(n.getNotifyForEventTypes().isEmpty());
    }

    @Test
    public void removeType_null() {
        ArrayList<EventType> types = new ArrayList<>();
        EventType e = new EventType("Test_type");
        types.add(e);
        Notifications n = new Notifications(true, true, types);
        n.removeType(null);
        assertTrue(n.getNotifyForEventTypes().size() == 1);
        assertTrue(n.getNotifyForEventTypes().get(0).getType().equals(e.getType()));
    }

    @Test
    public void equals_shouldBeTrue() {
        ArrayList<EventType> types = new ArrayList<>();
        EventType e = new EventType("Test_type");
        types.add(e);
        Notifications n1 = new Notifications(true, true, types);
        Notifications n2 = new Notifications(true, true, types);
        assertTrue(n1.equals(n2));
    }

    @Test
    public void equals_shouldBeFalse() {
        ArrayList<EventType> types = new ArrayList<>();
        EventType e = new EventType("Test_type");
        types.add(e);
        Notifications n1 = new Notifications(true, true, types);
        Notifications n2 = new Notifications(true, false, types);
        assertFalse(n1.equals(n2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        ArrayList<EventType> types = new ArrayList<>();
        EventType e = new EventType("Test_type");
        types.add(e);
        Notifications n1 = new Notifications(true, true, types);
        Notifications n2 = null;
        assertFalse(n1.equals(n2));
    }

    @Test
    public void equals_shouldBeFalse3() {
        ArrayList<EventType> types = new ArrayList<>();
        EventType e = new EventType("Test_type");
        types.add(e);
        Notifications n1 = new Notifications(true, true, types);
        Notifications n2 = new Notifications(false, true, types);
        assertFalse(n1.equals(n2));
    }

    @Test
    public void equals_shouldBeFalse4() {
        ArrayList<EventType> types = new ArrayList<>();
        EventType e = new EventType("Test_type");
        types.add(e);
        Notifications n1 = new Notifications(true, true, types);
        Notifications n2 = new Notifications(false, false, types);
        assertFalse(n1.equals(n2));
    }

    @Test
    public void equals_shouldBeFalse5() {
        ArrayList<EventType> types = new ArrayList<>();
        EventType e = new EventType("Test_type");
        types.add(e);
        ArrayList<EventType> types2 = new ArrayList<>();
        EventType e2 = new EventType("Other_test_type");
        types.add(e2);
        Notifications n1 = new Notifications(true, true, types);
        Notifications n2 = new Notifications(true, true, types2);
        assertFalse(n1.equals(n2));
    }

    @Test
    public void equals_shouldBeFalse6() {
        ArrayList<EventType> types = new ArrayList<>();
        EventType e = new EventType("Test_type");
        types.add(e);
        Notifications n1 = new Notifications(true, true, types);
        Object o1 = new Object();
        assertFalse(n1.equals(o1));
    }
    
        @Test
    public void hashCode_shouldBeEqual() {
        ArrayList<EventType> types = new ArrayList<>();
        EventType e = new EventType("Test_type");
        types.add(e);
        Notifications n1 = new Notifications(true, true, types);
        Notifications n2 = new Notifications(true, true, types);
        assertTrue(n1.hashCode() == n2.hashCode());
    }

    @Test
    public void hashCode_shouldBeDifferent() {
        ArrayList<EventType> types = new ArrayList<>();
        EventType e = new EventType("Test_type");
        types.add(e);
        Notifications n1 = new Notifications(true, true, types);
        Notifications n2 = new Notifications(true, false, types);
        assertFalse(n1.hashCode() == n2.hashCode());
    }
}
