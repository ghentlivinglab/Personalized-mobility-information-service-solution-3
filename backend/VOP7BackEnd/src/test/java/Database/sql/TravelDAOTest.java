package Database.sql;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.TravelDAO;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.TravelFactory;

/**
 *
 * @author Backend Team
 */
public class TravelDAOTest {
    
    @Mock
    private Connection connectionMock;

    @InjectMocks
    private TravelDAO travelDAO;
    
    public TravelDAOTest() {
    }
        
    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void getTravel_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                travelDAO.getTravel(1, 1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void getTravels_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                travelDAO.getTravels(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void createTravel_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any(), Matchers.eq(1))).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                travelDAO.createTravel(1, TravelFactory.create("test", AddressFactory.create("test", 0, "test", "test", "test", null), AddressFactory.create("test", 0, "test", "test", "test", null), new String[]{"12:00", "13:00"}, new Boolean[]{false, false, false, false, false, false, false}));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void deleteTravel_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                travelDAO.deleteTravel(1, 1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void modifyTravel_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                travelDAO.modifyTravel(1, 1, TravelFactory.create("test", AddressFactory.create("test", 0, "test", "test", "test", null), AddressFactory.create("test", 0, "test", "test", "test", null), new String[]{"12:00", "13:00"}, new Boolean[]{false, false, false, false, false, false, false}));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void existsTravel_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                travelDAO.existsTravel(1, TravelFactory.create("test", null, null, new String[]{"", ""}, null));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    

    
}
