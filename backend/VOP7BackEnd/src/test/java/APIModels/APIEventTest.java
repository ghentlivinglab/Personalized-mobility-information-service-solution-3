package APIModels;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIUser;

/**
 *
 * @author Backend team
 */
public class APIEventTest {
    
    public APIEventTest() {
    }
    
   @Test
    public void equals_shouldBeTrue() {
        APIEvent e1 = new APIEvent();
        
        APIEvent e2 = new APIEvent();

        assertTrue(e1.equals(e2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APIEvent e1 = new APIEvent();
        e1.setDescription("Test event 1");
        
        APIEvent e2 = new APIEvent();
        e2.setDescription("Test event 2");

        assertFalse(e1.equals(e2));
    }

    @Test
    public void equals_shouldBeFalse2() {
         APIEvent e1 = new APIEvent();
        e1.setDescription("Test event 1");
        
        APIEvent e2 = null;

        assertFalse(e1.equals(e2));
    }
}
