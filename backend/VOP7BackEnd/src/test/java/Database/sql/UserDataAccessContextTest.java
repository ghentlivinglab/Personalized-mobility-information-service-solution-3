package Database.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.AssertTrue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.EventProcessingDAO;
import vop.groep7.vop7backend.database.sql.POIDAO;
import vop.groep7.vop7backend.database.sql.RouteDAO;
import vop.groep7.vop7backend.database.sql.TravelDAO;
import vop.groep7.vop7backend.database.sql.UserDAO;
import vop.groep7.vop7backend.database.sql.UserDataAccessContext;

/**
 *
 * @author Bacend Team
 */
public class UserDataAccessContextTest {

    private Connection connectionMock;

    private UserDataAccessContext udac;

    public UserDataAccessContextTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        connectionMock = null; //TODO
        udac = new UserDataAccessContext(connectionMock);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getUserDAO_shouldReturnDAO() {
        UserDAO ud = udac.getUserDAO();
        assertNotNull(ud);
    }

    @Test
    public void getPOIDAO_shouldReturnDAO() {
        POIDAO pd = udac.getPOIDAO();
        assertNotNull(pd);
    }

    @Test
    public void getRouteDAO_shouldReturnDAO() {
        RouteDAO rd = udac.getRouteDAO();
        assertNotNull(rd);
    }

    @Test
    public void getTravelDAO_shouldReturnDAO() {
        TravelDAO td = udac.getTravelDAO();
        assertNotNull(td);
    }

    @Test
    public void getEventProcessingDAO_shouldReturnDAO() {
        EventProcessingDAO epd = udac.getEventProcessingDAO();
        assertNotNull(epd);
    }
//
//    @Test
//    public void destroy_shouldThrowException() {
//        try {
//            Mockito.doThrow(new SQLException("")).when(connectionMock).close();
//        } catch (SQLException ex) {
//            fail("SQLException should not be thrown here!");
//        }
//        
//        boolean thrown = false;
//        try {
//            udac.destroy();
//        } catch (DataAccessException ex) {
//            thrown = true;
//        }
//        assertTrue(thrown);
//    }

}
