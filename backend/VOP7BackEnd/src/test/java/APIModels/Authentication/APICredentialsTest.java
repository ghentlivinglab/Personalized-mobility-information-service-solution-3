package APIModels.Authentication;

import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICredentials;

/**
 *
 * @author Backend Team
 */
public class APICredentialsTest {

    public APICredentialsTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APICredentials c1 = new APICredentials();
        c1.setEmail("test@test.test");
        c1.setPassword("test");

        APICredentials c2 = new APICredentials();
        c2.setEmail("test@test.test");
        c2.setPassword("test");

        assertTrue(c1.equals(c2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APICredentials c1 = new APICredentials();
        c1.setEmail("test@test.test");
        c1.setPassword("test");

        APICredentials c2 = new APICredentials();
        c2.setEmail("test2@test.test");
        c2.setPassword("test");

        assertFalse(c1.equals(c2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APICredentials c1 = new APICredentials();
        c1.setEmail("test@test.test");
        c1.setPassword("test");

        APICredentials c2 = null;

        assertFalse(c1.equals(c2));

    }

    @Test
    public void equals_shouldBeFalse3() {
        APICredentials c1 = new APICredentials();
        c1.setEmail("test@test.test");
        c1.setPassword("test");

        Object o1 = new Object();
        assertFalse(c1.equals(o1));
    }

    @Test
    public void hashCode_shouldBeEqual() {
        APICredentials c1 = new APICredentials();
        c1.setEmail("test@test.test");
        c1.setPassword("test");

        APICredentials c2 = new APICredentials();
        c2.setEmail("test@test.test");
        c2.setPassword("test");

        assertTrue(c1.hashCode() == c2.hashCode());

    }

    @Test
    public void hashCode_shouldBeDifferent() {
        APICredentials c1 = new APICredentials();
        c1.setEmail("test@test.test");
        c1.setPassword("test");

        APICredentials c2 = new APICredentials();
        c2.setEmail("test2@test.test");
        c2.setPassword("test");

        assertFalse(c1.hashCode() == c2.hashCode());
    }
}
