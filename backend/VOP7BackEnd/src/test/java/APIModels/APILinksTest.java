package APIModels;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APILinks;

/**
 *
 * @author Backend Team
 */
public class APILinksTest {

    public APILinksTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APILinks l1 = new APILinks();
        l1.setRel("self");
        l1.setHref("http://www.example.com/here");

        APILinks l2 = new APILinks();
        l2.setRel("self");
        l2.setHref("http://www.example.com/here");

        assertTrue(l1.equals(l2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APILinks l1 = new APILinks();
        l1.setRel("self");
        l1.setHref("http://www.example.com/here");

        APILinks l2 = new APILinks();
        l2.setRel("self");
        l2.setHref("http://www.example.com/there");

        assertFalse(l1.equals(l2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APILinks l1 = new APILinks();
        l1.setRel("self");
        l1.setHref("http://www.example.com/here");

        APILinks l2 = null;

        assertFalse(l1.equals(l2));
    }
}
