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
public class APIEvent extends APIModel {

    private String id;
    private APILinks[] links;
    private APICoordinate coordinates;
    private String street;
    private String city;
    private String country;
    private boolean active;
    @JsonProperty("publication_time")
    private String publicationTime;
    @JsonProperty("last_edit_time")
    private String lastEditTime;
    private String description;
    private APIJam[] jams;
    private APISource source;
    private APIEventType type;
    @JsonProperty("relevant_for_transportation_types")
    private APITransport[] relevantForTransportationTypes;

    /**
     * Compare this APIEvent object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIEvent)) {
            return false;
        }
        APIEvent other = (APIEvent) o;

        return Objects.equals(getCoordinates(), other.getCoordinates())
                && (isActive() == other.isActive())
                && Objects.equals(getPublicationTime(), other.getPublicationTime())
                && Objects.equals(getCity(), other.getCity())
                && Objects.equals(getCountry(), other.getCountry())
                && Objects.equals(getStreet(), other.getStreet())
                && Objects.equals(getLastEditTime(), other.getLastEditTime())
                && Objects.equals(getDescription(), other.getDescription())
                && Arrays.deepEquals(getJams(), other.getJams())
                && Objects.equals(getSource(), other.getSource())
                && Objects.equals(getType(), other.getType())
                && Arrays.deepEquals(getLinks(), other.getLinks())
                && Arrays.deepEquals(getRelevantForTransportationTypes(), getRelevantForTransportationTypes());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Arrays.deepHashCode(this.links);
        hash = 59 * hash + Objects.hashCode(this.coordinates);
        hash = 59 * hash + Objects.hashCode(this.street);
        hash = 59 * hash + Objects.hashCode(this.city);
        hash = 59 * hash + Objects.hashCode(this.country);
        hash = 59 * hash + (this.active ? 1 : 0);
        hash = 59 * hash + Objects.hashCode(this.publicationTime);
        hash = 59 * hash + Objects.hashCode(this.lastEditTime);
        hash = 59 * hash + Objects.hashCode(this.description);
        hash = 59 * hash + Arrays.deepHashCode(this.jams);
        hash = 59 * hash + Objects.hashCode(this.source);
        hash = 59 * hash + Objects.hashCode(this.type);
        hash = 59 * hash + Arrays.deepHashCode(this.relevantForTransportationTypes);
        return hash;
    }

    /**
     * Getter for id
     *
     * @return The Id of the event
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id The Id of the event
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
     * Get the coordinates of the event
     *
     * @return The coordinates of the event
     */
    public APICoordinate getCoordinates() {
        return coordinates;
    }

    /**
     * Set the coordinates of the event
     *
     * @param coordinates The coordinates of the event
     */
    public void setCoordinates(APICoordinate coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Get if the event is still active
     *
     * @return If the event is still active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set if the event is still active
     *
     * @param active If the event is still active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Get when the event was published
     *
     * @return When the event was published
     */
    @JsonSerialize
    @JsonProperty("publication_time")
    public String getPublicationTime() {
        return publicationTime;
    }

    /**
     * Set when the event was published
     *
     * @param publicationTime When the event was published
     */
    @JsonSerialize
    @JsonProperty("publication_time")
    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

    /**
     * Get when the event was edited
     *
     * @return When the event was edited
     */
    @JsonSerialize
    @JsonProperty("last_edit_time")
    public String getLastEditTime() {
        return lastEditTime;
    }

    /**
     * Set when the event was edited
     *
     * @param lastEditTime When the event was edited
     */
    @JsonSerialize
    @JsonProperty("last_edit_time")
    public void setLastEditTime(String lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    /**
     * Get the source of the event
     *
     * @return The source of the event
     */
    public APISource getSource() {
        return source;
    }

    /**
     * Set the source of the event
     *
     * @param source The source of the event
     */
    public void setSource(APISource source) {
        this.source = source;
    }

    /**
     * Get the type of the event
     *
     * @return The type of the event
     */
    public APIEventType getType() {
        return type;
    }

    /**
     * Set the type of the event
     *
     * @param type The type of the event
     */
    public void setType(APIEventType type) {
        this.type = type;
    }

    /**
     * Get the description of the event
     *
     * @return The description of the event
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the event
     *
     * @param description The description of the event
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the traffic jams this event caused
     *
     * @return The traffic jams this event caused
     */
    public APIJam[] getJams() {
        return jams;
    }

    /**
     * Set the traffic jams this event caused
     *
     * @param jams The traffic jams this event caused
     */
    public void setJams(APIJam[] jams) {
        this.jams = jams;
    }

    /**
     * Get on which types of transport this event type has impact
     *
     * @return On which types of transport this event type has impact
     */
    @JsonSerialize
    @JsonProperty("relevant_for_transportation_types")
    public APITransport[] getRelevantForTransportationTypes() {
        return relevantForTransportationTypes;
    }

    /**
     * Set on which types of transport this event type has impact
     *
     * @param relevantForTransportationTypes On which types of transport this
     * event type has impact
     */
    @JsonSerialize
    @JsonProperty("relevant_for_transportation_types")
    public void setRelevantForTransportationTypes(APITransport[] relevantForTransportationTypes) {
        this.relevantForTransportationTypes = relevantForTransportationTypes;
    }

    /**
     * Get the street name of this event
     * 
     * @return The street name
     */
    public String getStreet() {
        return street;
    }

    /**
     * Set the street name of this event
     * 
     * @param street The street name
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Get the city of this event
     * 
     * @return The city
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the city of this event
     * 
     * @param city The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get the country of this event
     * 
     * @return The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the country of this event
     * 
     * @param country The country
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
