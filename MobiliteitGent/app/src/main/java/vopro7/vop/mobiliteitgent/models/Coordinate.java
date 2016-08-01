package vopro7.vop.mobiliteitgent.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author App Team
 */
public class Coordinate extends Model {

    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void loadFromJson(JSONObject json) throws JSONException {
        setLatitude(json.optDouble("lat"));
        setLongitude(json.optDouble("lon"));
    }

    @Override
    public String toString() {
        return "(" + getLatitude() + ", " + getLongitude() + ")";
    }
}
