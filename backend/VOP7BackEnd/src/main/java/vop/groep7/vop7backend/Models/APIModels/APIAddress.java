package vop.groep7.vop7backend.Models.APIModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APIAddress extends APIModel {

    private String city;
    @JsonProperty("postal_code")
    private int postalCode;
    private String street;
    private String housenumber;
    private String country;
    private APICoordinate coordinates;

    /**
     * Compare this APIAddress object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIAddress)) {
            return false;
        }
        APIAddress other = (APIAddress) o;

        return Objects.equals(getCity(), other.getCity())
                && Objects.equals(getPostalCode(), other.getPostalCode())
                && Objects.equals(getStreet(), other.getStreet())
                && Objects.equals(getHousenumber(), other.getHousenumber())
                && Objects.equals(getCountry(), other.getCountry())
                && Objects.equals(getCoordinates(), other.getCoordinates());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.city);
        hash = 59 * hash + this.postalCode;
        hash = 59 * hash + Objects.hashCode(this.street);
        hash = 59 * hash + Objects.hashCode(this.housenumber);
        hash = 59 * hash + Objects.hashCode(this.country);
        hash = 59 * hash + Objects.hashCode(this.coordinates);
        return hash;
    }

    /**
     * Get the street of this address
     *
     * @return The street of this address
     */
    public String getStreet() {
        return street;
    }

    /**
     * Set the street of this address
     *
     * @param street The street of this address
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Get the postal code of this address
     *
     * @return The postal code of this address
     */
    @JsonSerialize
    @JsonProperty("postal_code")
    public int getPostalCode() {
        return postalCode;
    }

    /**
     * Set the postal code of this address
     *
     * @param postalCode The postal code of this address
     */
    @JsonSerialize
    @JsonProperty("postal_code")
    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Get the house number of this address
     *
     * @return The house number of this address
     */
    public String getHousenumber() {
        return housenumber;
    }

    /**
     * Set the house number of this address
     *
     * @param housenumber The house number of this address
     */
    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    /**
     * Get the city of this address
     *
     * @return The city of this address
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the city of this address
     *
     * @param city The city of this address
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get the country of this address
     *
     * @return The country of this address
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set country of this address
     *
     * @param country The country of this address
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get coordinates of this address
     *
     * @return The coordinates of this address
     */
    public APICoordinate getCoordinates() {
        return coordinates;
    }

    /**
     * Set coordinates of this address
     *
     * @param coordinates The coordinates of this address
     */
    public void setCoordinates(APICoordinate coordinates) {
        this.coordinates = coordinates;
    }
}
