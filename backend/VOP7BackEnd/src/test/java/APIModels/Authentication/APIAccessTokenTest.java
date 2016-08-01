package APIModels.Authentication;

import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIAccessToken;

/**
 *
 * @author Backend Team
 */
public class APIAccessTokenTest {

    public APIAccessTokenTest() {

    }

    @Test
    public void equals_shouldBeTrue() {
        APIAccessToken at1 = new APIAccessToken();
        at1.setToken("TOKEN");
        at1.setExp("EXP");

        APIAccessToken at2 = new APIAccessToken();
        at2.setToken("TOKEN");
        at2.setExp("EXP");

        assertTrue(at1.equals(at2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APIAccessToken at1 = new APIAccessToken();
        at1.setToken("TOKEN");
        at1.setExp("EXP");

        APIAccessToken at2 = new APIAccessToken();
        at2.setToken("TOKEN2");
        at2.setExp("EXP2");

        assertFalse(at1.equals(at2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APIAccessToken at1 = new APIAccessToken();
        at1.setToken("TOKEN");
        at1.setExp("EXP");

        APIAccessToken at2 = null;

        assertFalse(at1.equals(at2));

    }

    @Test
    public void equals_shouldBeFalse3() {
        APIAccessToken at1 = new APIAccessToken();
        at1.setToken("TOKEN");
        at1.setExp("EXP");

        Object o1 = new Object();
        assertFalse(at1.equals(o1));
    }

    @Test
    public void hashCode_shouldBeEqual() {
        APIAccessToken at1 = new APIAccessToken();
        at1.setToken("TOKEN");
        at1.setExp("EXP");

        APIAccessToken at2 = new APIAccessToken();
        at2.setToken("TOKEN");
        at2.setExp("EXP");

        assertTrue(at1.hashCode() == at2.hashCode());

    }

    @Test
    public void hashCode_shouldBeDifferent() {
        APIAccessToken at1 = new APIAccessToken();
        at1.setToken("TOKEN");
        at1.setExp("EXP");

        APIAccessToken at2 = new APIAccessToken();
        at2.setToken("TOKEN2");
        at2.setExp("EXP2");

        assertFalse(at1.hashCode() == at2.hashCode());
    }

}
