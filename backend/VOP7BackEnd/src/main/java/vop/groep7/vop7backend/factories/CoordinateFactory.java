package vop.groep7.vop7backend.factories;

import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.Domain.Coordinate;

/**
 *
 * @author Backend Team
 */
public class CoordinateFactory {

    /**
     * Create a new Coordinate object
     * 
     * @param lat The latitude of the coordinate
     * @param lon The longitude of the coordinate
     * @return A new coordinate object
     */
    public static Coordinate create(double lat, double lon) {
        Coordinate coordinate = new Coordinate(lat, lon);
        return coordinate;
    }

    /**
     * Build a valid Coordinate object
     * 
     * @param lat The latitude of the coordinate
     * @param lon The longitude of the coordinate
     * @return A valid coordinate object
     */
    public static Coordinate build(double lat, double lon) {
        Coordinate coordinate = new Coordinate(lat, lon);
        return coordinate;
    }

    /**
     * Create an APICoordinate object starting from a Coordinate
     *
     * @param coordinate A Coordinate object
     * @return An APICoordinate object
     */
    public static APICoordinate toAPIModel(Coordinate coordinate) {
        APICoordinate result = new APICoordinate();
        result.setLat(coordinate.getLat());
        result.setLon(coordinate.getLon());
        return result;
    }

    /**
     * Create a Coordinate object starting from an APICoordinate
     *
     * @param coordinate An APICoordinate object
     * @return A Coordinate object
     */
    public static Coordinate toDomainModel(APICoordinate coordinate) {
        Coordinate result = new Coordinate(coordinate.getLat(), coordinate.getLon());
        return result;
    }
}
