package vop.groep7.vop7backend.Models.APIModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APIJam extends APIModel {

    @JsonProperty("start_node")
    private APICoordinate startNode;
    @JsonProperty("end_node")
    private APICoordinate endNode;
    private int speed;
    private int delay;

    /**
     * Compare this APIJam object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIJam)) {
            return false;
        }
        APIJam other = (APIJam) o;

        return Objects.equals(getStartNode(), other.getStartNode())
                && Objects.equals(getEndNode(), other.getEndNode())
                && (getSpeed() == other.getSpeed())
                && (getDelay() == other.getDelay());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.startNode);
        hash = 67 * hash + Objects.hashCode(this.endNode);
        hash = 67 * hash + this.speed;
        hash = 67 * hash + this.delay;
        return hash;
    }

    /**
     * Get the start coordinates of the jam
     *
     * @return The start coordinates of the jam
     */
    @JsonSerialize
    @JsonProperty("start_node")
    public APICoordinate getStartNode() {
        return startNode;
    }

    /**
     * Set the start coordinates of the jam
     *
     * @param startNode The start coordinates of the jam
     */
    @JsonSerialize
    @JsonProperty("start_node")
    public void setStartNode(APICoordinate startNode) {
        this.startNode = startNode;
    }

    /**
     * Get the end coordinates of the jam
     *
     * @return The end coordinates of the jam
     */
    @JsonSerialize
    @JsonProperty("end_node")
    public APICoordinate getEndNode() {
        return endNode;
    }

    /**
     * Set the end coordinates of the jam
     *
     * @param endNode The end coordinates of the jam
     */
    @JsonSerialize
    @JsonProperty("end_node")
    public void setEndNode(APICoordinate endNode) {
        this.endNode = endNode;
    }

    /**
     * Get the average speed of the jam
     *
     * @return The average speed of the jam
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Set the average speed of the jam
     *
     * @param speed The average speed of the jam
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Get the delay of jam, compared to free flow (in seconds)
     *
     * @return The delay of jam, compared to free flow (in seconds)
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Set the delay of jam, compared to free flow (in seconds)
     *
     * @param delay The delay of jam, compared to free flow (in seconds)
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }
}
