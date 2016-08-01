package Database.sql;

import static Other.TestUtils.comparePOIs;
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
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.POIDAO;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.CoordinateFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.POIFactory;

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
public class POIDAOIT {

    private POIDAO poiDAO;

    private EmbeddedDatabase db;

    @Autowired
    private DataSource dataSource;

    private final List<POI> expectedPOIs;
    private POI expectedPOI;
    private POI otherPOI;

    public POIDAOIT() {
        expectedPOIs = new ArrayList<>();
        // create expected poi for getPOI
        createExpectedPOI();
        // create expected pois for getPOIs
        createExpectedPOIs();
        // create poi for modifyPOI/createPOI
        createPOIs();
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
        poiDAO = new POIDAO(dataSource.getConnection());
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

    private void createExpectedPOI() {
        Coordinate c = CoordinateFactory.build(50.4460734672, 3.51132552398);
        Address a = AddressFactory.build("Brussel", 2805, "Babbelaarstraat", "298", "Belgie", c);
        ArrayList<EventType> eventTypes = new ArrayList<>();
        EventType e = EventTypeFactory.build("ROAD_CLOSED");
        eventTypes.add(e);
        expectedPOI = POIFactory.build(1, a, "LiamVermeir1", 0, false, false, false, eventTypes);
    }

    private void createExpectedPOIs() {
        expectedPOIs.add(expectedPOI);
    }

    private void createPOIs() {
        Coordinate c = CoordinateFactory.build(51.054632, 3.7206907);
        Address a = AddressFactory.build("Gent", 9000, "Korenmarkt", "17", "Belgie", c);
        ArrayList<EventType> eventTypes = new ArrayList<>();
        EventType e = EventTypeFactory.build("WEATHER_HAZARD");
        eventTypes.add(e);
        otherPOI = POIFactory.build(1, a, "LiamVermeir2", 100, true, true, true, eventTypes);
    }

    @Test
    public void getPOI_shouldReturnPOI() throws DataAccessException, IllegalAccessException {
        POI p = poiDAO.getPOI(4242, 1);

        // compare with expected poi
        assertNotNull(p);
        assertTrue("The id of the returned poi is incorrect!", expectedPOI.getPoiIdentifier() == (p.getPoiIdentifier()));
        assertTrue("The expected poi was not returned!", comparePOIs(p, expectedPOI));
    }

    @Test
    public void getPOI_shouldReturnNull() throws DataAccessException {
        POI p = poiDAO.getPOI(4242, 10);
        assertNull(p);
    }

    @Test
    public void getPOIs_shouldReturnListOfPOIs() throws DataAccessException, IllegalAccessException {
        List<POI> pois = poiDAO.getPOIs(4242);

        // compare pois with expected pois
        assertNotNull(pois);
        assertEquals(expectedPOIs.size(), pois.size());
        for (int i = 0; i < expectedPOIs.size(); i++) {
            assertNotNull(pois.get(i));
            assertTrue("The id of this poi is incorrect!", expectedPOIs.get(i).getPoiIdentifier() == (pois.get(i).getPoiIdentifier()));
            assertTrue("The expected poi was not returned!", comparePOIs(pois.get(i), expectedPOIs.get(i)));
        }
    }

    @Test
    @ExpectedDatabase("/poi_created.xml")
    public void createPOI_shouldCreateCorrect() throws DataAccessException {
        poiDAO.createPOI(4242, otherPOI);
    }

    @Test
    @ExpectedDatabase("/poi_modified.xml")
    public void modifyPOI_shouldModifyCorrect() throws DataAccessException {
        poiDAO.modifyPOI(4242, 1, otherPOI);
    }

    @Test
    @ExpectedDatabase("/poi_deleted.xml")
    public void deletePOI_shouldDeleteCorrect() throws DataAccessException {
        poiDAO.deletePOI(9127, 2);
    }

    @Test
    public void existsPoi_shouldReturnTrue() throws DataAccessException {
        assertTrue(poiDAO.existsPOI(4242, expectedPOI));
    }

    @Test
    public void existsPoi_shouldReturnFalse() throws DataAccessException {
        assertFalse(poiDAO.existsPOI(9127, expectedPOI));
    }

    @Test
    public void existsPoi_shouldReturnFalse2() throws DataAccessException {
        assertFalse(poiDAO.existsPOI(1, otherPOI));
    }
    
    @Test
    public void existsAddress_shouldReturnTrue() throws DataAccessException{
        assertTrue(poiDAO.existsAddress(AddressFactory.build("Brussel", 2805, "Babbelaarstraat", "298", "Belgie", CoordinateFactory.build(50.4460734672, 3.51132552398))));
    }
    
    @Test
    public void existsAddress_shouldReturnFalse() throws DataAccessException{
        assertFalse(poiDAO.existsAddress(AddressFactory.build("Gent", 9000, "De pintelaan", "105", "Belgie", CoordinateFactory.build(50.4560734672, 3.71132552398))));
    }
}
