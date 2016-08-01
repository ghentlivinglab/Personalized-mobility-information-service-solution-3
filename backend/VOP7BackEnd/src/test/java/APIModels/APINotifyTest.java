package APIModels;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APINotify;
import vop.groep7.vop7backend.Models.APIModels.APISource;

/**
 *
 * @author Backend Team
 */
public class APINotifyTest {
    
    public APINotifyTest() {
    }
    
    @Test
    public void equals_shouldBeTrue() {
        APINotify n1 = new APINotify();
        n1.setCellNumberNotify(true);
        n1.setEmailNotify(true);

        APINotify n2 = new APINotify();
        n2.setCellNumberNotify(true);
        n2.setEmailNotify(true);

        assertTrue(n1.equals(n2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APINotify n1 = new APINotify();
        n1.setCellNumberNotify(true);
        n1.setEmailNotify(true);

        APINotify n2 = new APINotify();
        n2.setCellNumberNotify(true);
        n2.setEmailNotify(false);

        assertFalse(n1.equals(n2));
    }
    

    @Test
    public void equals_shouldBeFalse2() {
        APINotify n1 = new APINotify();
        n1.setCellNumberNotify(true);
        n1.setEmailNotify(true);

        APINotify n2 = null;

        assertFalse(n1.equals(n2));
    }
    
}
