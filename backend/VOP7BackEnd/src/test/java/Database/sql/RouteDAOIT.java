package Database.sql;

import static Other.TestUtils.compareRoutes;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.RouteDAO;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.RouteFactory;
import vop.groep7.vop7backend.factories.TransportFactory;

/**
 *
 * @author Jonas Van Wilder
 * @author Nick De Smedt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/testContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
public class RouteDAOIT {

    private RouteDAO routeDAO;

    private EmbeddedDatabase db;

    @Autowired
    private DataSource dataSource;

    private final List<Route> expectedRoutes;
    private Route expectedRoute;
    private Route otherRoute;

    public RouteDAOIT() {
        expectedRoutes = new ArrayList<>();
        // create expected route for getRoute
        createExpectedRoute();
        // create expected routes for getRoutes
        createExpectedRoutes();
        // create route for modifyRoute/createRoute
        createRoute();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws SQLException {
        // setup UserDAO
        routeDAO = new RouteDAO(dataSource.getConnection());
        // setup database
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("/testdb_schema.sql")
                .addScript("/testdb_data.sql")
                .addScript("/testdb_triggers.sql")
                .build();
    }

    @After
    public void tearDown() {
    }

    private void createExpectedRoute() {
        Transport transportationType = TransportFactory.build("CAR");
        ArrayList<Coordinate> waypoints = new ArrayList<>();
        Coordinate c = CoordinateFactory.build(49.1370093293, 3.05829755662);
        waypoints.add(c);
        ArrayList<EventType> eventTypes = new ArrayList<>();
        EventType e = EventTypeFactory.build("JAM_MODERATE_TRAFFIC");
        eventTypes.add(e);
        expectedRoute = RouteFactory.build(1, true, transportationType, false, false, waypoints, eventTypes);
    }

    private void createExpectedRoutes() {
        expectedRoutes.add(expectedRoute);
    }

    private void createRoute() {
        Transport transportationType = TransportFactory.build("STREETCAR");
        ArrayList<Coordinate> waypoints = new ArrayList<>();
        Coordinate c = CoordinateFactory.build(50.4168071223, 2.98424755641);
        waypoints.add(c);
        ArrayList<Coordinate> interpolatedWaypoints = new ArrayList<>();
        interpolatedWaypoints.add(c);
        ArrayList<EventType> eventTypes = new ArrayList<>();
        EventType e = EventTypeFactory.build("JAM_HEAVY_TRAFFIC");
        eventTypes.add(e);
        otherRoute = RouteFactory.create(true, transportationType, true, false, waypoints, interpolatedWaypoints, eventTypes);
    }

    @Test
    public void getRoute_shouldReturnRoute() throws DataAccessException, IllegalAccessException {
        Route r = routeDAO.getRoute(4242, 1, 1);

        // compare with expected route
        assertNotNull(r);
        assertTrue("The id of the returned route is incorrect!", expectedRoute.getRouteIdentifier() == (r.getRouteIdentifier()));
        assertTrue("The expected route was not returned!", compareRoutes(r, expectedRoute));
    }

    @Test
    public void getRoute_shouldReturnNull() throws DataAccessException {
        Route r = routeDAO.getRoute(4242, 1, 10);
        assertNull(r);
    }

    @Test
    public void getRoutes_shouldReturnListOfRoutes() throws DataAccessException, IllegalAccessException {
        List<Route> routes = routeDAO.getRoutes(4242, 1);

        // compare routes with expected routes
        assertNotNull(routes);
        assertEquals(expectedRoutes.size(), routes.size());
        for (int i = 0; i < expectedRoutes.size(); i++) {
            assertNotNull(routes.get(i));
            assertTrue("The id of this route is incorrect!", expectedRoutes.get(i).getRouteIdentifier() == (routes.get(i).getRouteIdentifier()));
            assertTrue("The expected route was not returned!", compareRoutes(routes.get(i), expectedRoutes.get(i)));
        }
    }

    @Test
    @ExpectedDatabase("/route_created.xml")
    public void createRoute_shouldCreateCorrect() throws DataAccessException {
        routeDAO.createRoute(9127, 2, otherRoute);
    }

    @Test
    @ExpectedDatabase("/route_modified.xml")
    public void modifyRoute_shouldModifyCorrect() throws DataAccessException {
        routeDAO.modifyRoute(9127, 2, 2, otherRoute);
    }

    @Test
    @ExpectedDatabase("/route_deleted.xml")
    public void deleteRoute_shouldDeleteCorrect() throws DataAccessException {
        routeDAO.deleteRoute(9127, 2, 2);
    }

    @Test
    public void existsRoute_shouldReturnTrue() throws DataAccessException {
        assertTrue(routeDAO.existsRoute(4242, 1, expectedRoute));
    }

    @Test
    public void existsRoute_shouldReturnFalse() throws DataAccessException {
        assertFalse(routeDAO.existsRoute(9127, 2, expectedRoute));
    }

    @Test
    public void hasEqualRouteEventTypes_shouldReturnFalse() throws DataAccessException {
        Set<EventType> types = new HashSet<>();
        EventType e = EventTypeFactory.build("DIT_TYPE_BESTAAT_NIET");
        types.add(e);
        boolean test = routeDAO.hasEqualRouteEventTypes(types, 4242, 1, 1);
        assertFalse(test);

    }

    @Test
    public void hasEqualWaypoints_shouldReturnFalse() throws DataAccessException {
        Set<Coordinate> waypoints = new HashSet<>();
        Coordinate c = CoordinateFactory.build(0.0, 0.0);
        waypoints.add(c);
        boolean test = routeDAO.hasEqualWaypoints(waypoints, 4242, 1);
        assertFalse(test);
    }

}
