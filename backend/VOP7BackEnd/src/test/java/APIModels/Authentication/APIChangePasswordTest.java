package APIModels.Authentication;

import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIChangePassword;

/**
 *
 * @author Backend Team
 */
public class APIChangePasswordTest {

    public APIChangePasswordTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APIChangePassword cp1 = new APIChangePassword();
        cp1.setOldPassword("test");
        cp1.setNewPassword("test123");

        APIChangePassword cp2 = new APIChangePassword();
        cp2.setOldPassword("test");
        cp2.setNewPassword("test123");

        assertTrue(cp1.equals(cp2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APIChangePassword cp1 = new APIChangePassword();
        cp1.setOldPassword("test");
        cp1.setNewPassword("test123");

        APIChangePassword cp2 = new APIChangePassword();
        cp2.setOldPassword("test");
        cp2.setNewPassword("test1234");

        assertFalse(cp1.equals(cp2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APIChangePassword cp1 = new APIChangePassword();
        cp1.setOldPassword("test");
        cp1.setNewPassword("test123");

        APIChangePassword cp2 = null;

        assertFalse(cp1.equals(cp2));

    }

    @Test
    public void equals_shouldBeFalse3() {
        APIChangePassword cp1 = new APIChangePassword();
        cp1.setOldPassword("test");
        cp1.setNewPassword("test123");

        Object o1 = new Object();
        assertFalse(cp1.equals(o1));
    }

    @Test
    public void hashCode_shouldBeEqual() {
        APIChangePassword cp1 = new APIChangePassword();
        cp1.setOldPassword("test");
        cp1.setNewPassword("test123");

        APIChangePassword cp2 = new APIChangePassword();
        cp2.setOldPassword("test");
        cp2.setNewPassword("test123");

        assertTrue(cp1.hashCode() == cp2.hashCode());

    }

    @Test
    public void hashCode_shouldBeDifferent() {
        APIChangePassword cp1 = new APIChangePassword();
        cp1.setOldPassword("test");
        cp1.setNewPassword("test123");

        APIChangePassword cp2 = new APIChangePassword();
        cp2.setOldPassword("test");
        cp2.setNewPassword("test1234");

        assertFalse(cp1.hashCode() == cp2.hashCode());
    }
}
