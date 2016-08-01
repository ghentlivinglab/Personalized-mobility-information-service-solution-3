package APIModels;

import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APINotify;
import vop.groep7.vop7backend.Models.APIModels.APIValidation;

/**
 *
 * @author Backend Team
 */
public class APIValidationTest {

    public APIValidationTest() {
    }

    @Test
    public void equals_shouldBeTrue() {
        APIValidation v1 = new APIValidation();
        v1.setCellNumberValidated(false);
        v1.setEmailValidated(false);

        APIValidation v2 = new APIValidation();
        v2.setCellNumberValidated(false);
        v2.setEmailValidated(false);

        assertTrue(v1.equals(v2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APIValidation v1 = new APIValidation();
        v1.setCellNumberValidated(false);
        v1.setEmailValidated(false);

        APIValidation v2 = new APIValidation();
        v2.setCellNumberValidated(true);
        v2.setEmailValidated(false);

        assertFalse(v1.equals(v2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APIValidation v1 = new APIValidation();
        v1.setCellNumberValidated(false);
        v1.setEmailValidated(false);

        APIValidation v2 = null;

        assertFalse(v1.equals(v2));
    }
}
