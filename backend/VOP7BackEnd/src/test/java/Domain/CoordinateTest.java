package Domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import vop.groep7.vop7backend.Models.Domain.Coordinate;

/**
 *
 * @author Backend Team
 */
public class CoordinateTest {

    public CoordinateTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        Coordinate c1 = new Coordinate(45.7, 37.2);
        Coordinate c2 = new Coordinate(45.7, 37.2);
        assertTrue(c1.equals(c2));
    }

    @Test
    public void equals_shouldBeFalse() {
        Coordinate c1 = new Coordinate(45.7, 37.2);
        Coordinate c2 = new Coordinate(30.7, 57.2);
        assertFalse(c1.equals(c2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        Coordinate c1 = new Coordinate(45.7, 37.2);
        Coordinate c2 = null;
        assertFalse(c1.equals(c2));
    }

    @Test
    public void equals_shouldBeFalse3() {
        Coordinate c1 = new Coordinate(45.7, 37.2);
        Object o1 = new Object();
        assertFalse(c1.equals(o1));
    }

    @Test
    public void hashCode_shouldBeEqual() {
        Coordinate c1 = new Coordinate(45.7, 37.2);
        Coordinate c2 = new Coordinate(45.7, 37.2);
        assertTrue(c1.hashCode() == c2.hashCode());

    }

    @Test
    public void hashCode_shouldBeDifferent() {
        Coordinate c1 = new Coordinate(45.7, 37.2);
        Coordinate c2 = new Coordinate(30.7, 57.2);
        assertFalse(c1.hashCode() == c2.hashCode());
    }
}
