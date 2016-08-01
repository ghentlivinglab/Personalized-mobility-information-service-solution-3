package Domain;

import java.text.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import vop.groep7.vop7backend.Models.Domain.Interval;
import vop.groep7.vop7backend.Models.Domain.Transport;

/**
 *
 * @author Backend team
 */
public class IntervalTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public IntervalTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        Interval i1 = new Interval(new String[]{"12:13", "17:28"});
        Interval i2 = new Interval(new String[]{"12:13", "17:28"});
        assertTrue(i1.equals(i2));
    }

    @Test
    public void equals_shouldBeFalse() {
        Interval i1 = new Interval(new String[]{"12:13", "17:28"});
        Interval i2 = new Interval(new String[]{"15:14", "23:52"});
        assertFalse(i1.equals(i2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        Interval i1 = new Interval(new String[]{"12:13", "17:28"});
        Interval i2 = null;
        assertFalse(i1.equals(i2));
    }

    @Test
    public void equals_shouldBeFalse3() {
        Interval i1 = new Interval(new String[]{"12:13", "17:28"});
        Object o1 = new Object();
        assertFalse(i1.equals(o1));
    }

    @Test
    public void hashCode_shouldBeEqual() {
        Interval i1 = new Interval(new String[]{"12:13", "17:28"});
        Interval i2 = new Interval(new String[]{"12:13", "17:28"});
        assertTrue(i1.hashCode() == i2.hashCode());

    }

    @Test
    public void hashCode_shouldBeDifferent() {
        Interval i1 = new Interval(new String[]{"12:13", "17:28"});
        Interval i2 = new Interval(new String[]{"15:14", "23:52"});
        assertFalse(i1.hashCode() == i2.hashCode());
    }

}
