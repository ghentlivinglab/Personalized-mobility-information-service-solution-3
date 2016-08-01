package Database.sql;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
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
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Jam;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.Domain.Transport;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.EventProcessingCircleDAO;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.JamFactory;
import vop.groep7.vop7backend.factories.SourceFactory;
import vop.groep7.vop7backend.factories.TransportFactory;

/**
 *
 * @author Nick
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/testContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
public class EventProcessingCircleDAOIT {

    private EventProcessingCircleDAO eventProcessingCircleDAO;

    private EmbeddedDatabase db;

    @Autowired
    private DataSource dataSource;

    public EventProcessingCircleDAOIT() {

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws SQLException {
        eventProcessingCircleDAO = new EventProcessingCircleDAO(dataSource.getConnection());
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

    private Event getRouteEvent() {
        Coordinate coordinates = CoordinateFactory.build(51.8039984503, 4.44875563993); //matcht ten minste met tweede waypoint
        Address location = AddressFactory.build(null, 0, null, null, null, coordinates);
        Source source = SourceFactory.build("TestEvent", "http://www.example.org/random_icon_url.png");
        Timestamp publicationTime = Timestamp.valueOf("2016-04-06 12:18:44.926");
        Timestamp lastEditTime = Timestamp.valueOf("2016-04-06 12:18:44.926");
        EventType type = EventTypeFactory.build("HAZARD_ON_ROAD_CONSTRUCTION");
        ArrayList<Transport> transportTypes = new ArrayList<>();
        Transport transport1 = TransportFactory.build("car");
        Transport transport2 = TransportFactory.build("BIKE");
        transportTypes.add(transport1);
        transportTypes.add(transport2);
        return EventFactory.build(0, location, true, source, "Dit is een test event.", publicationTime, lastEditTime, type, transportTypes, null);
    }

    @Test
    public void getTimeTravels_shouldReturnMap() throws DataAccessException {
        Event expectedEvent = getRouteEvent();
        Map<Integer, Map<Integer, List<Integer>>> result = eventProcessingCircleDAO.getTimeTravels(3, new Time(expectedEvent.getLastEditTime().getTime()), expectedEvent.getType().getType(), expectedEvent.getLocation().getCoordinates());
        assertEquals(result.size(), 1);
    }

    private Event getPOIEvent() {
        Coordinate coordinates = CoordinateFactory.build(49.307166, 3.863761); //matcht ten minste met tweede poi
        Address location = AddressFactory.build(null, 0, null, null, null, coordinates);
        Source source = SourceFactory.build("TestEvent", "http://www.example.org/random_icon_url.png");
        Timestamp publicationTime = Timestamp.valueOf("2016-04-06 19:15:44.926");
        Timestamp lastEditTime = Timestamp.valueOf("2016-04-06 19:15:44.926");
        EventType type = EventTypeFactory.build("JAM_HEAVY_TRAFFIC");
        ArrayList<Transport> transportTypes = new ArrayList<>();
        Transport transport1 = TransportFactory.build("car");
        Transport transport2 = TransportFactory.build("bus");
        transportTypes.add(transport1);
        transportTypes.add(transport2);
        Coordinate start = CoordinateFactory.build(51.056510, 3.722945);
        Coordinate end = CoordinateFactory.build(51.057060, 3.723991);
        ArrayList<Jam> jams = new ArrayList<>();
        Jam jam = JamFactory.build(start, end, 10, 2987);
        jams.add(jam);
        return EventFactory.build(0, location, true, source, "Dit is een test event.", publicationTime, lastEditTime, type, transportTypes, jams);
    }

    @Test
    public void getMatchingPOIs_shouldReturnMap() throws DataAccessException {
        Event expectedEvent = getPOIEvent();
        Map<Integer, List<Integer>> result = eventProcessingCircleDAO.getMatchingPOIs(expectedEvent.getType().getType(), expectedEvent.getLocation().getCoordinates());
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

}
