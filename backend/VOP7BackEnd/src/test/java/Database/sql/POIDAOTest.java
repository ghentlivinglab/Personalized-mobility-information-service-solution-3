package Database.sql;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.POIDAO;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.POIFactory;

/**
 *
 * @author Backend Team
 */
public class POIDAOTest {

    @Mock
    private Connection connectionMock;

    @InjectMocks
    private POIDAO poiDAO;

    public POIDAOTest() {
    }

    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPOI_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                poiDAO.getPOI(1, 1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void deletePOI_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                poiDAO.deletePOI(1, 1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void existsPOI_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                poiDAO.existsPOI(1, POIFactory.create(AddressFactory.create("test", 0, "test", "test", "test", null), "test", 0, true, true, true, null));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void hasEqualPOIEventTypes_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                poiDAO.hasEqualPOIEventTypes(null, 1, 1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getAddress_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                poiDAO.getAddress(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void createAddress_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any(), Matchers.eq(1))).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                poiDAO.createAddress(AddressFactory.create("", 0, "", "", "", null));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void modifyAddress_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                poiDAO.modifyAddress(1, AddressFactory.create("", 0, "", "", "", null));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void existsAddress_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                poiDAO.existsAddress(null);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getPOIs_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                poiDAO.getPOIs(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void createPOI_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any(), Matchers.eq(1))).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                poiDAO.createPOI(1, POIFactory.create(AddressFactory.create("", 0, "", "", "", null), null, 0, true, true, true, null));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void modifyPOI_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                poiDAO.modifyPOI(1, 1, POIFactory.create(null, null, 0, true, true, true, null));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

}
