package Domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.City;
import vop.groep7.vop7backend.Models.Domain.Coordinate;

/**
 *
 * @author Backend Team
 */
public class CityTest {

    public CityTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        City c1 = new City("Gent", 9000, "BE");
        City c2 = new City("Gent", 9000, "BE");
        assertTrue(c1.equals(c2));
    }

    @Test
    public void equals_shouldBeFalse() {
        City c1 = new City("Gent", 9000, "BE");
        City c2 = new City("Gent", 9000, "NL");
        assertFalse(c1.equals(c2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        City c1 = new City("Gent", 9000, "BE");
        City c2 = null;
        assertFalse(c1.equals(c2));
    }

    @Test
    public void equals_shouldBeFalse3() {
        City c1 = new City("Gent", 9000, "BE");
        Object o1 = new Object();
        assertFalse(c1.equals(o1));
    }

    @Test
    public void hashCode_shouldBeEqual() {
        City c1 = new City("Gent", 9000, "BE");
        City c2 = new City("Gent", 9000, "BE");
        assertTrue(c1.hashCode() == c2.hashCode());

    }

    @Test
    public void hashCode_shouldBeDifferent() {
        City c1 = new City("Gent", 9000, "BE");
        City c2 = new City("Gent", 9000, "NL");
        assertFalse(c1.hashCode() == c2.hashCode());
    }

}
