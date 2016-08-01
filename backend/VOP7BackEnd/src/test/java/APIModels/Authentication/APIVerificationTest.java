package APIModels.Authentication;

import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIVerification;

/**
 *
 * @author Backend Team
 */
public class APIVerificationTest {

    public APIVerificationTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APIVerification v1 = new APIVerification();
        v1.setEmail("test@example.com");
        v1.setEmailVerificationPin("0123456");
        v1.setCellNumberVerificationPin("0123456");

        APIVerification v2 = new APIVerification();
        v2.setEmail("test@example.com");
        v2.setEmailVerificationPin("0123456");
        v2.setCellNumberVerificationPin("0123456");

        assertTrue(v1.equals(v2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APIVerification v1 = new APIVerification();
        v1.setEmail("test@example.com");
        v1.setEmailVerificationPin("0123456");
        v1.setCellNumberVerificationPin("0123456");

        APIVerification v2 = new APIVerification();
        v2.setEmail("test@example.com");
        v2.setEmailVerificationPin("0123457");
        v2.setCellNumberVerificationPin("0123457");

        assertFalse(v1.equals(v2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APIVerification v1 = new APIVerification();
        v1.setEmail("test@example.com");
        v1.setEmailVerificationPin("0123456");
        v1.setCellNumberVerificationPin("0123456");

        APIVerification v2 = null;

        assertFalse(v1.equals(v2));
    }

    @Test
    public void equals_shouldBeFalse3() {
        APIVerification v1 = new APIVerification();
        v1.setEmail("test@example.com");
        v1.setEmailVerificationPin("0123456");
        v1.setCellNumberVerificationPin("0123456");

        Object o1 = new Object();
        assertFalse(v1.equals(o1));
    }

    @Test
    public void hashCode_shouldBeEqual() {
        APIVerification v1 = new APIVerification();
        v1.setEmail("test@example.com");
        v1.setEmailVerificationPin("0123456");
        v1.setCellNumberVerificationPin("0123456");

        APIVerification v2 = new APIVerification();
        v2.setEmail("test@example.com");
        v2.setEmailVerificationPin("0123456");
        v2.setCellNumberVerificationPin("0123456");

        assertTrue(v1.hashCode() == v2.hashCode());

    }

    @Test
    public void hashCode_shouldBeDifferent() {
        APIVerification v1 = new APIVerification();
        v1.setEmail("test@example.com");
        v1.setEmailVerificationPin("0123456");
        v1.setCellNumberVerificationPin("0123456");

        APIVerification v2 = new APIVerification();
        v2.setEmail("test@example.com");
        v2.setEmailVerificationPin("0123457");
        v2.setCellNumberVerificationPin("0123457");

        assertFalse(v1.hashCode() == v2.hashCode());
    }
}
