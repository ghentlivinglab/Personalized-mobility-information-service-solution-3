package APIModels;

import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APILinks;
import vop.groep7.vop7backend.Models.APIModels.APISource;

/**
 *
 * @author backend Team
 */
public class APISourceTest {

    public APISourceTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APISource s1 = new APISource();
        s1.setName("Test");
        s1.setIconUrl("http://www.example.com/icon");

        APISource s2 = new APISource();
        s2.setName("Test");
        s2.setIconUrl("http://www.example.com/icon");

        assertTrue(s1.equals(s2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APISource s1 = new APISource();
        s1.setName("Test");
        s1.setIconUrl("http://www.example.com/icon");

        APISource s2 = new APISource();
        s2.setName("Test2");
        s2.setIconUrl("http://www.example.com/icon2");

        assertFalse(s1.equals(s2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APISource s1 = new APISource();
        s1.setName("Test");
        s1.setIconUrl("http://www.example.com/icon");

        APISource s2 = null;

        assertFalse(s1.equals(s2));
    }
}
