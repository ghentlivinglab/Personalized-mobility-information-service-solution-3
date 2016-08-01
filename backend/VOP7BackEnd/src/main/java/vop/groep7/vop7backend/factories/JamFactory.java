package vop.groep7.vop7backend.factories;

import vop.groep7.vop7backend.Models.APIModels.APIJam;
import vop.groep7.vop7backend.Models.Domain.Coordinate;
import vop.groep7.vop7backend.Models.Domain.Jam;

/**
 *
 * @author Backend Team
 */
public class JamFactory {

    /**
     * Create a new Jam object
     * 
     * @param start Coordinate of the start point
     * @param end Coordinate of the end point
     * @param speed The average speed of the vehicles in this jam in km/h
     * @param delay The delay due to this jam in seconds
     * @return A new Jam object
     */
    public static Jam create(Coordinate start, Coordinate end, int speed, int delay) {
        Jam jam = new Jam(start, end, speed, delay);
        return jam;
    }

    /**
     * Build a valid Jam object
     * 
     * @param start Coordinate of the start point
     * @param end Coordinate of the end point
     * @param speed The average speed of the vehicles in this jam in km/h
     * @param delay The delay due to this jam in seconds
     * @return A valid Jam object
     */
    public static Jam build(Coordinate start, Coordinate end, int speed, int delay) {
        Jam jam = new Jam(start, end, speed, delay);
        return jam;
    }

    /**
     * Create an APIJam object starting from a Jam
     *
     * @param jam A Jam object
     * @return An APIJam object
     */
    public static APIJam toAPIModel(Jam jam) {
        APIJam result = new APIJam();
        result.setStartNode(CoordinateFactory.toAPIModel(jam.getStart()));
        result.setEndNode(CoordinateFactory.toAPIModel(jam.getEnd()));
        result.setSpeed(jam.getSpeed());
        result.setDelay(jam.getDelay());
        return result;
    }

    /**
     * Create a Jam object starting from an APIJam
     *
     * @param jam An APIJam object
     * @return A Jam object
     */
    public static Jam toDomainModel(APIJam jam) {
        Jam result = new Jam(CoordinateFactory.toDomainModel(jam.getStartNode()), CoordinateFactory.toDomainModel(jam.getEndNode()), jam.getSpeed(), jam.getDelay());
        return result;
    }
}
