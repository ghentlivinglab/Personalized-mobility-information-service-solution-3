package vop.groep7.vop7backend.Models.APIModels;

import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APICoordinate extends APIModel {

    private double lat;
    private double lon;

    /**
     * Compare this APICoordinate object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APICoordinate)) {
            return false;
        }
        APICoordinate other = (APICoordinate) o;

        return (Double.compare(getLat(), other.getLat()) == 0)
                && (Double.compare(getLon(), other.getLon()) == 0);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.lat) ^ (Double.doubleToLongBits(this.lat) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.lon) ^ (Double.doubleToLongBits(this.lon) >>> 32));
        return hash;
    }

    /**
     * Get the latitude of this coordinate
     *
     * @return The latitude of this coordinate
     */
    public double getLat() {
        return lat;
    }

    /**
     * Set the latitude of this coordinate
     *
     * @param lat The latitude of this coordinate
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Get the longitude of this coordinate
     *
     * @return The longitude of this coordinate
     */
    public double getLon() {
        return lon;
    }

    /**
     * Set the longitude of this coordinate
     *
     * @param lon The longitude of this coordinate
     */
    public void setLon(double lon) {
        this.lon = lon;
    }
}
