package APIModels;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APITransport;

/**
 *
 * @author Backend Team
 */
public class APITransportTest {
    
    public APITransportTest() {
    }
    
     @Test
    public void valueOf_ShouldReturnValue(){
       assertEquals(APITransport.valueOf("BIKE"), APITransport.BIKE);
    }
    
    @Test
    public void getTransport_ShouldReturnNull(){
       assertNull(APITransport.getTransport(null));
    }
    
    @Test
    public void getTransport_ShouldReturnNull2(){
       assertNull(APITransport.getTransport("wrong"));
    }
}
