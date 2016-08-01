package vop.groep7.vop7backend.Models.Domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public class Interval implements Serializable, Model {

    private Date start;
    private Date end;
    private static final SimpleDateFormat df = new SimpleDateFormat("HH:mm");

    /**
     * The public constructor with all fields required to create an Interval object
     * 
     * @param interval String array with two strings: the start and end of the
     * time interval in format HH:mm
     */
    public Interval(String[] interval) {
        try {
            this.start = df.parse(interval[0]);
            this.end = df.parse(interval[1]);
        } catch (ParseException ex) {
            Logger.getLogger(Interval.class.getName()).log(Level.SEVERE, "Time interval cannot be parsed!", ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.start);
        hash = 89 * hash + Objects.hashCode(this.end);
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
        final Interval other = (Interval) obj;
        if (!Objects.equals(this.start, other.start)) {
            return false;
        }
        return Objects.equals(this.end, other.end);
    }
    
    

    /**
     * Get an array representation of this interval
     *
     * @return String array with two strings: the start and end of the time
     * interval in format HH:mm
     */
    public String[] toArray() {
        String[] result = new String[2];
        result[0] = df.format(start);
        result[1] = df.format(end);

        return result;
    }
}
