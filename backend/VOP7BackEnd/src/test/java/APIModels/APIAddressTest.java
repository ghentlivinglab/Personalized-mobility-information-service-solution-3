package APIModels;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vop.groep7.vop7backend.Models.APIModels.APIAddress;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;

/**
 * 
 * @author Backend Team
 */
public class APIAddressTest {
    
    public APIAddressTest() {
    }
    
    @Test
    public void equals_shouldBeTrue() {
        APIAddress a1 = new APIAddress();
        a1.setCity("city");
        a1.setCountry("country");
        a1.setPostalCode(5161);
        a1.setHousenumber("housenumber");
        APICoordinate c1 = new APICoordinate();
        c1.setLat(54.2);
        c1.setLon(23.1);
        a1.setCoordinates(c1);
        
        APIAddress a2 = new APIAddress();
        a2.setCity("city");
        a2.setCountry("country");
        a2.setPostalCode(5161);
        a2.setHousenumber("housenumber");
        APICoordinate c2 = new APICoordinate();
        c2.setLat(54.2);
        c2.setLon(23.1);
        a2.setCoordinates(c2);
        
        assertTrue(a1.equals(a2));
    }

    @Test
    public void equals_shouldBeFalse() {
        APIAddress a1 = new APIAddress();
        a1.setCity("city");
        a1.setCountry("country");
        a1.setPostalCode(5161);
        a1.setHousenumber("housenumber");
        APICoordinate c1 = new APICoordinate();
        c1.setLat(54.2);
        c1.setLon(23.1);
        a1.setCoordinates(c1);
        
        APIAddress a2 = new APIAddress();
        a2.setCity("city2");
        a2.setCountry("country2");
        a2.setPostalCode(5161);
        a2.setHousenumber("housenumber2");
        APICoordinate c2 = new APICoordinate();
        c2.setLat(14.2);
        c2.setLon(29.1);
        a2.setCoordinates(c2);
        
        assertFalse(a1.equals(a2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        APIAddress a1 = new APIAddress();
        a1.setCity("city");
        a1.setCountry("country");
        a1.setPostalCode(5161);
        a1.setHousenumber("housenumber");
        APICoordinate c1 = new APICoordinate();
        c1.setLat(54.2);
        c1.setLon(23.1);
        a1.setCoordinates(c1);
        
        APIAddress a2 = null;
        
        assertFalse(a1.equals(a2));
    }
    
}
