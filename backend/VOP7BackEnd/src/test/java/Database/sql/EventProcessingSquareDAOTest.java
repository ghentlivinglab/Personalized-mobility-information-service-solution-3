
package Database.sql;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.database.DataAccessException;
import java.sql.Time;
import static org.junit.Assert.assertTrue;
import vop.groep7.vop7backend.database.sql.EventProcessingSquareDAO;

/**
 *
 * @author Backend Team
 */
public class EventProcessingSquareDAOTest {

    @Mock
    private Connection conMock;

    @InjectMocks
    private EventProcessingSquareDAO eventProcessingSquareDAO;

    public EventProcessingSquareDAOTest() {
    }

    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getTimeTravels_shouldThrowException() {
        try {
            when(conMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                eventProcessingSquareDAO.getTimeTravels(1, new Time(12, 0, 0), "example", new Coordinate(12.32, 4.32));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getMatchingPOIs_shouldThrowException() {
        try {
            when(conMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                eventProcessingSquareDAO.getMatchingPOIs("example", new Coordinate(12.32, 4.32));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

}
