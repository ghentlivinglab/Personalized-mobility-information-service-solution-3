package vop.groep7.vop7backend.Models.Domain;

import java.util.Objects;
import vop.groep7.vop7backend.Models.DomainModel;

/**
 *
 * @author Backend Team
 */
public class Jam extends DomainModel {

    private final Coordinate start;
    private final Coordinate end;
    private final int speed;
    private final int delay;

    /**
     * The public constructor with all fields required to create a Jam object
     * 
     * @param start Coordinate of the start point
     * @param end Coordinate of the end point
     * @param speed The average speed of the vehicles in this jam in km/h
     * @param delay The delay due to this jam in seconds
     */
    public Jam(Coordinate start, Coordinate end, int speed, int delay) {
        this.start = start;
        this.end = end;
        this.speed = speed;
        this.delay = delay;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.start);
        hash = 37 * hash + Objects.hashCode(this.end);
        hash = 37 * hash + this.speed;
        hash = 37 * hash + this.delay;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Jam other = (Jam) obj;
        if (!Objects.equals(this.start, other.start)) {
            return false;
        }
        if (!Objects.equals(this.end, other.end)) {
            return false;
        }
        return (this.speed == other.speed) && (this.delay == other.delay);
    }
    
    /**
     * Get the coordinates of the start point
     * 
     * @return coordinates of the start point
     * @see Coordinate
     */
    public Coordinate getStart() {
        return start;
    }

    /**
     * Get the coordinates of the end point
     * 
     * @return coordinates of the end point
     * @see Coordinate
     */
    public Coordinate getEnd() {
        return end;
    }

    /**
     * The average speed of the vehicles in this jam in km/h
     * 
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Get the delay due to this jam
     * 
     * @return the delay in seconds
     */
    public int getDelay() {
        return delay;
    }
}
