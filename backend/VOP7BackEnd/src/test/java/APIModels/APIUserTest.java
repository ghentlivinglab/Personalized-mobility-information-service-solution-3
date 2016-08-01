package APIModels;

import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APIPOI;
import vop.groep7.vop7backend.Models.APIModels.APIUser;

/**
 *
 * @author Backend Team
 */
public class APIUserTest {

    public APIUserTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APIUser u1 = new APIUser();

        APIUser u2 = new APIUser();

        assertTrue(u1.equals(u2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APIUser u1 = new APIUser();
        u1.setEmail("test@test.com");

        APIUser u2 = new APIUser();
        u2.setEmail("test2@test.com");

        assertFalse(u1.equals(u2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APIUser u1 = new APIUser();
        u1.setEmail("test@test.com");

        APIUser u2 = null;

        assertFalse(u1.equals(u2));
    }
}
