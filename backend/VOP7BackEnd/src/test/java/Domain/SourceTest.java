package Domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.Domain.Source;

/**
 *
 * @author Backend Team
 */
public class SourceTest {
    
    public SourceTest() {
    }
    
    @Test
    public void createWithWrongIconFomat_ShouldSetIconNull(){
        Source s = new Source("Test Source", "example.com");
        assertNotNull(s.getName());
        assertNull(s.getIconUrl());
    }
}
