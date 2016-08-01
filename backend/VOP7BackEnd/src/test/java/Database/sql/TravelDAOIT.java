package Database.sql;

import static Other.TestUtils.compareTravels;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.TravelDAO;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.TravelFactory;

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
public class TravelDAOIT {

    private TravelDAO travelDAO;

    private EmbeddedDatabase db;

    @Autowired
    private DataSource dataSource;

    private final List<Travel> expectedTravels;
    private Travel expectedTravel;
    private Travel otherTravel;

    public TravelDAOIT() {
        expectedTravels = new ArrayList<>();
        // create expected travel for getTravel
        createExpectedTravel();
        // create expected travels for getTravels
        createExpectedTravels();
        // create travel for modifyTravel/createTravel
        createTravel();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws SQLException {
        // setup TravelDAO
        travelDAO = new TravelDAO(dataSource.getConnection());
        // setup database
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("/testdb_schema.sql")
                .addScript("/testdb_triggers.sql")
                .addScript("/testdb_data.sql")
                .build();
    }

    @After
    public void tearDown() {
    }

    private void createExpectedTravel() {
        Coordinate c1 = CoordinateFactory.build(51.9356536715, 4.27300638697);
        Address a1 = AddressFactory.build("Lokeren", 9646, "Beekstraat", "747", "Belgie", c1);
        Coordinate c2 = CoordinateFactory.build(51.7000044444, 3.52444296567);
        Address a2 = AddressFactory.build("Antwerpen", 5948, "Bergekouter", "548", "Belgie", c2);
        String[] timeInterval = new String[2];
        timeInterval[0] = "12:00";
        timeInterval[1] = "13:00";
        Boolean[] recurring = {true, true, true, true, true, true, true};
        expectedTravel = TravelFactory.build(1, "LiamVermeir1", a1, a2, timeInterval, recurring);
    }

    private void createExpectedTravels() {
        expectedTravels.add(expectedTravel);
    }

    private void createTravel() {
        Coordinate c1 = CoordinateFactory.build(51.054632, 3.7206907);
        Address a1 = AddressFactory.build("Gent", 9000, "Korenmarkt", "17", "Belgie", c1);
        Coordinate c2 = CoordinateFactory.build(51.057314, 3.7231993);
        Address a2 = AddressFactory.build("Gent", 9000, "Vrijdagmarkt", "12", "Belgie", c2);
        String[] timeInterval = new String[2];
        timeInterval[0] = "08:00";
        timeInterval[1] = "09:00";
        Boolean[] recurring = {true, true, true, true, true, false, false};
        otherTravel = TravelFactory.build(2, "KatjaTolens2", a1, a2, timeInterval, recurring);
    }

    @Test
    public void getTravel_shouldReturnTravel() throws DataAccessException, IllegalAccessException {
        Travel t = travelDAO.getTravel(4242, 1);

        // compare with expected travel
        assertNotNull(t);
        assertTrue("The id of the returned travel is incorrect!", expectedTravel.getTravelIdentifier() == (t.getTravelIdentifier()));
        assertTrue("The expected travel was not returned!", compareTravels(t, expectedTravel));
    }

    @Test
    public void getTravel_shouldReturnNull() throws DataAccessException {
        Travel t = travelDAO.getTravel(4242, 10);
        assertNull(t);
    }

    @Test
    public void getTravels_shouldReturnListOfTravels() throws DataAccessException, IllegalAccessException {
        List<Travel> travels = travelDAO.getTravels(4242);

        // compare travels with expected travels
        assertNotNull(travels);
        assertEquals(expectedTravels.size(), travels.size());
        for (int i = 0; i < expectedTravels.size(); i++) {
            assertNotNull(travels.get(i));
            assertTrue("The id of this travel is incorrect!", expectedTravels.get(i).getTravelIdentifier() == (travels.get(i).getTravelIdentifier()));
            assertTrue("The expected travel was not returned!", compareTravels(travels.get(i), expectedTravels.get(i)));
        }
    }

    @Test
    @ExpectedDatabase("/travel_created.xml")
    public void createTravel_shouldCreateCorrect() throws DataAccessException {
        travelDAO.createTravel(9127, otherTravel);
    }

    @Test
    @ExpectedDatabase("/travel_modified.xml")
    public void modifyTravel_shouldModifyCorrect() throws DataAccessException {
        travelDAO.modifyTravel(2, 9127, otherTravel);
    }

    @Test
    @ExpectedDatabase("/travel_deleted.xml")
    public void deleteTravel_shouldDeleteCorrect() throws DataAccessException {
        travelDAO.deleteTravel(9127, 2);
    }

    @Test
    public void existsTravel_shouldReturnTrue() throws DataAccessException {
        assertTrue(travelDAO.existsTravel(4242, expectedTravel));
    }

    @Test
    public void existsTravel_shouldReturnFalse() throws DataAccessException {
        assertFalse(travelDAO.existsTravel(9127, expectedTravel));
    }
}
