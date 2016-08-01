package Domain;

import java.util.Calendar;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Models.Domain.WeeklyRecurring;

/**
 *
 * @author Backend Team
 */
public class WeeklyRecurringTest {

    @Mock
    private Calendar cal;

    @InjectMocks
    private WeeklyRecurring wr;

    public WeeklyRecurringTest() {
    }

    @Before
    public void setUp() {
        wr = new WeeklyRecurring(new Boolean[]{true, true, true, true, true, false, false});
        // Mock annotations
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void equals_shouldBeTrue() {
        WeeklyRecurring wr1 = new WeeklyRecurring(new Boolean[]{true, true, true, true, true, false, false});
        WeeklyRecurring wr2 = new WeeklyRecurring(new Boolean[]{true, true, true, true, true, false, false});
        assertTrue(wr1.equals(wr2));
    }

    @Test
    public void equals_shouldBeFalse() {
        WeeklyRecurring wr1 = new WeeklyRecurring(new Boolean[]{true, true, true, true, true, false, false});
        WeeklyRecurring wr2 = new WeeklyRecurring(new Boolean[]{false, false, false, false, false, true, true});
        assertFalse(wr1.equals(wr2));
    }

    @Test
    public void equals_shouldBeFalse2() {
        WeeklyRecurring wr1 = new WeeklyRecurring(new Boolean[]{true, true, true, true, true, false, false});
        WeeklyRecurring wr2 = null;
        assertFalse(wr1.equals(wr2));
    }

    @Test
    public void equals_shouldBeFalse3() {
        WeeklyRecurring wr1 = new WeeklyRecurring(new Boolean[]{true, true, true, true, true, false, false});
        Object o = new Object();
        assertFalse(wr1.equals(o));
    }

    @Test
    public void hashCode_shouldBeEqual() {
        WeeklyRecurring wr1 = new WeeklyRecurring(new Boolean[]{true, true, true, true, true, false, false});
        WeeklyRecurring wr2 = new WeeklyRecurring(new Boolean[]{true, true, true, true, true, false, false});
        assertTrue(wr1.hashCode() == wr2.hashCode());
    }

    @Test
    public void hashCode_shouldBeDifferent() {
        WeeklyRecurring wr1 = new WeeklyRecurring(new Boolean[]{true, true, true, true, true, false, false});
        WeeklyRecurring wr2 = new WeeklyRecurring(new Boolean[]{false, false, false, false, false, true, true});
        assertFalse(wr1.hashCode() == wr2.hashCode());
    }

    @Test
    public void isApplicableNow_shouldReturnTrue() {
        when(cal.get(Calendar.DAY_OF_WEEK)).thenReturn(2);
        boolean res = wr.isApplicableNow();
        assertTrue(res);
    }

    @Test
    public void isApplicableNow_shouldReturnFalse() {
        when(cal.get(Calendar.DAY_OF_WEEK)).thenReturn(1);
        boolean res = wr.isApplicableNow();
        assertFalse(res);
    }
}
