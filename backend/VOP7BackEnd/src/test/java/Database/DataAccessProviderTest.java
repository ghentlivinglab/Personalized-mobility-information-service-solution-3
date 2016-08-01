package Database;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import vop.groep7.vop7backend.database.DataAccessProvider;

/**
 *
 * @author Backend Team
 */
public class DataAccessProviderTest {
    
    private Properties propertiesMock;
    
    public DataAccessProviderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        propertiesMock = mock(Properties.class);
    }
    
    @After
    public void tearDown() {
    }
    
//    @Test
//    public void dataAccessProviderConstructor_shouldThrowRuntime(){
//        when(propertiesMock.getProperty("user")).thenReturn("fakeUser");
//        when(propertiesMock.getProperty("userurl")).thenReturn("fakeUrl");
//        
//        DataAccessProvider dap = new DataAccessProvider(propertiesMock);
//    }
    
    
    
}
