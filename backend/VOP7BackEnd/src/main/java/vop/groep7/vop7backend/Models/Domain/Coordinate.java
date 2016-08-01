package vop.groep7.vop7backend.Models.Domain;

import vop.groep7.vop7backend.Models.DomainModel;

/**
 *
 * @author Backend Team
 */
public class Coordinate extends DomainModel {

    private final double lat;
    private final double lon;

    /**
     * The public constructor with all fields required to create a Coordinate object
     * 
     * @param lat latitude of the coordinate
     * @param lon longitude of the coordinate
     */
    public Coordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.lat) ^ (Double.doubleToLongBits(this.lat) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.lon) ^ (Double.doubleToLongBits(this.lon) >>> 32));
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
        final Coordinate other = (Coordinate) obj;
        return Double.compare(lat, other.lat) == 0 && Double.compare(this.lon, other.lon) == 0;
    }

    /**
     * Get the latitude of the coordinate
     *
     * @return the latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Get the longitude
     *
     * @return the longitude
     */
    public double getLon() {
        return lon;
    }
}
