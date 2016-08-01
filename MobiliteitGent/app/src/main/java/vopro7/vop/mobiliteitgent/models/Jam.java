package vopro7.vop.mobiliteitgent.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author App Team
 */
public class Jam extends Model{

    private Coordinate start;
    private Coordinate end;
    private int speed;
    private int delay;

    public Coordinate getStart() {
        return start;
    }

    public void setStart(Coordinate start) {
        this.start = start;
    }

    public Coordinate getEnd() {
        return end;
    }

    public void setEnd(Coordinate end) {
        this.end = end;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void loadFromJson(JSONObject json) throws JSONException {
        JSONObject startCoordinates = json.optJSONObject("start_node");
        if(startCoordinates != null) {
            Coordinate c = new Coordinate();
            c.loadFromJson(startCoordinates);
            setStart(c);
        }

        JSONObject endCoordinates = json.optJSONObject("end_node");
        if(endCoordinates != null) {
            Coordinate c = new Coordinate();
            c.loadFromJson(endCoordinates);
            setEnd(c);
        }

        setSpeed(json.optInt("speed"));
        setDelay(json.optInt("delay"));
    }

    @Override
    public String toString() {
        String result = "From " + getStart().toString() + " to " + getEnd().toString() + "\n";
        if(getDelay() != -1) {
            int minutes = getDelay()/60;
            if(minutes == 0) {
                minutes = 1;
            }
            result += "Delay: " + minutes + " minutes, ";
        }
        result += "Speed: " + getSpeed() + " km/u";
        return result;
    }
}
