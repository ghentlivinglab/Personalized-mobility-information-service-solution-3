package vop.groep7.vop7backend.Models.APIModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Arrays;
import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APITravel extends APIModel {

    private String id;
    private APILinks[] links;
    private String name;
    @JsonProperty("time_interval")
    private String[] timeInterval;
    private Boolean[] recurring;
    private APIAddress startpoint;
    private APIAddress endpoint;

    /**
     * Compare this APITravel object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APITravel)) {
            return false;
        }
        APITravel other = (APITravel) o;

        return Objects.equals(getName(), other.getName())
                && Arrays.deepEquals(getLinks(), other.getLinks())
                && Arrays.deepEquals(getTimeInterval(), other.getTimeInterval())
                && Arrays.deepEquals(getRecurring(), other.getRecurring())
                && Objects.equals(getStartpoint(), other.getStartpoint())
                && Objects.equals(getEndpoint(), other.getEndpoint());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Arrays.deepHashCode(this.links);
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Arrays.deepHashCode(this.timeInterval);
        hash = 67 * hash + Arrays.deepHashCode(this.recurring);
        hash = 67 * hash + Objects.hashCode(this.startpoint);
        hash = 67 * hash + Objects.hashCode(this.endpoint);
        return hash;
    }

    /**
     * Getter for id
     *
     * @return The Id of the travel
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id The Id of the travel
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for the links
     *
     * @return The links of this object
     */
    public APILinks[] getLinks() {
        return links;
    }

    /**
     * Setter for the links
     *
     * @param links The links of this object
     */
    public void setLinks(APILinks[] links) {
        this.links = links;
    }

    /**
     * Getter for name
     *
     * @return The name of the travel
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     *
     * @param name The name of the travel
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the time interval for this travel
     *
     * @return The time interval for this travel
     */
    @JsonSerialize
    @JsonProperty("time_interval")
    public String[] getTimeInterval() {
        return timeInterval;
    }

    /**
     * Set the time interval for this travel
     *
     * @param timeInterval The time interval for this travel
     */
    @JsonSerialize
    @JsonProperty("time_interval")
    public void setTimeInterval(String[] timeInterval) {
        this.timeInterval = timeInterval;
    }

    /**
     * Get on when days this travel is recurring
     *
     * @return On when days this travel is recurring
     */
    public Boolean[] getRecurring() {
        return recurring;
    }

    /**
     * Set on when days this travel is recurring
     *
     * @param recurring On when days this travel is recurring
     */
    public void setRecurring(Boolean[] recurring) {
        this.recurring = recurring;
    }

    /**
     * Getter for startpoint
     *
     * @return The start of the travel
     */
    public APIAddress getStartpoint() {
        return startpoint;
    }

    /**
     * Setter for startpoint
     *
     * @param startpoint The start of the travel
     */
    public void setStartpoint(APIAddress startpoint) {
        this.startpoint = startpoint;
    }

    /**
     * Getter for endpoint
     *
     * @return The end of the travel
     */
    public APIAddress getEndpoint() {
        return endpoint;
    }

    /**
     * Setter for endpoint
     *
     * @param endpoint The end of the travel
     */
    public void setEndpoint(APIAddress endpoint) {
        this.endpoint = endpoint;
    }
}
