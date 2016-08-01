package Database.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.RouteDAO;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.RouteFactory;

/**
 *
 * @author Backend Team
 */
public class RouteDAOTest {
    
    @Mock
    private Connection connectionMock;

    @InjectMocks
    private RouteDAO routeDAO;
    
    public RouteDAOTest() {
    }
    
    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void getRoute_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                routeDAO.getRoute(1, 1, 1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void getRoutes_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                routeDAO.getRoutes(1, 1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void createRoute_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any(), Matchers.eq(1))).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                routeDAO.createRoute(1, 1, RouteFactory.create(true, new Transport("car"), true, true, null, new ArrayList<>(), null));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void deleteRoute_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                routeDAO.deleteRoute(1, 1, 1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void modifyRoute_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                routeDAO.modifyRoute(1, 1, 1, RouteFactory.create(true, new Transport("car"), true, true, null, new ArrayList<>(), null));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void existsRoute_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                routeDAO.existsRoute(1, 1, RouteFactory.create(true, new Transport("car"), true, true, null, new ArrayList<>(), null));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void hasEqualRouteEventTypes_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                routeDAO.hasEqualRouteEventTypes(null, 1, 1, 1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
    @Test
    public void hasEqualWaypoints_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                routeDAO.hasEqualWaypoints(null, 1, 1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
    
}
