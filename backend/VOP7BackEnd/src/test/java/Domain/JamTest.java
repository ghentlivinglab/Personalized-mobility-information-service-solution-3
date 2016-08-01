package Domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Jam;

/**
 *
 * @author Backend Team
 */
public class JamTest {

    public JamTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        Jam j1 = new Jam(new Coordinate(23.1, 78.6), new Coordinate(47.3, 30.8), 20, 3000);
        Jam j2 = new Jam(new Coordinate(23.1, 78.6), new Coordinate(47.3, 30.8), 20, 3000);
        assertTrue(j1.equals(j2));
    }

    @Test
    public void equals_shouldBeFalse() {
        Jam j1 = new Jam(new Coordinate(23.1, 78.6), new Coordinate(47.3, 30.8), 20, 3000);
        Jam j2 = new Jam(new Coordinate(2.3, 78.6), new Coordinate(47.3, 30.8), 20, 4000);
        assertFalse(j1.equals(j2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        Jam j1 = new Jam(new Coordinate(23.1, 78.6), new Coordinate(47.3, 30.8), 20, 3000);
        Jam j2 = null;
        assertFalse(j1.equals(j2));
    }

    @Test
    public void equals_shouldBeFalse3() {
        Jam j1 = new Jam(new Coordinate(23.1, 78.6), new Coordinate(47.3, 30.8), 20, 3000);
        Object o1 = new Object();
        assertFalse(j1.equals(o1));
    }

    @Test
    public void hashCode_shouldBeEqual() {
        Jam j1 = new Jam(new Coordinate(23.1, 78.6), new Coordinate(47.3, 30.8), 20, 3000);
        Jam j2 = new Jam(new Coordinate(23.1, 78.6), new Coordinate(47.3, 30.8), 20, 3000);
        assertTrue(j1.hashCode() == j2.hashCode());

    }

    @Test
    public void hashCode_shouldBeDifferent() {
        Jam j1 = new Jam(new Coordinate(23.1, 78.6), new Coordinate(47.3, 30.8), 20, 3000);
        Jam j2 = new Jam(new Coordinate(2.3, 78.6), new Coordinate(44.3, 20.8), 50, 400);
        assertFalse(j1.hashCode() == j2.hashCode());
    }
}
