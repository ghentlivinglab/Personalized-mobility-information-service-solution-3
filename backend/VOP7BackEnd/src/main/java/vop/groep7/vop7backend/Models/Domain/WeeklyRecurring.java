package vop.groep7.vop7backend.Models.Domain;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author Backend Team
 */
public class WeeklyRecurring implements Recurring {

    
    private Calendar cal;
    
    /*
     * Array with a boolean for each day of the week
     * monday = index 0 .. sunday = index 6
     */
    private final Boolean[] recurring;

    /**
     * Create a WeeklyRecurring object
     * It's an array with a boolean for each day of the week:
     * monday = index 0, sunday = index 6
     *
     * @param recurring array with a boolean for each day of the week
     */
    public WeeklyRecurring(Boolean[] recurring) {
        this.recurring = recurring;
        this.cal = Calendar.getInstance(TimeZone.getDefault());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Arrays.deepHashCode(this.recurring);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WeeklyRecurring other = (WeeklyRecurring) obj;
        return Arrays.deepEquals(this.recurring, other.recurring);
    }

    /**
     * Get this WeeklyRecurring object as a Boolean array with a boolean for
     * each day of the week. When the boolean for a specific day is true, it
     * means that the recurring is applicable on that day
     *
     * @return Boolean array, true = recurring on this day
     */
    public Boolean[] toArray() {
        return recurring;
    }

    @Override
    public boolean isApplicableNow() {
        // sunday = 1 .. saterday=7
        int currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        // convert to correct index
        int correctIndex;
        if (currentDayOfWeek == 1) {
            // If it is sunday, the correct index is index 6
            correctIndex = 6;
        } else {
            correctIndex = currentDayOfWeek - 2;
        }

        return recurring[correctIndex];
    }

}
