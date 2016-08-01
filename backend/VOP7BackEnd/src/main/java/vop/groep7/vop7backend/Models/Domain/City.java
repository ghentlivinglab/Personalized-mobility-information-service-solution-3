package vop.groep7.vop7backend.Models.Domain;

import java.io.Serializable;
import java.util.Objects;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public class City implements Model, Serializable {

    private final String city;
    private final int postalCode;
    private final String country;

    /**
     * The public constructor with all fields required to create a City object
     * 
     * @param city the name of the city
     * @param postalCode the postalcode of the city
     * @param country the country iso code of the country: e.g. BE for Belgium
     */
    public City(String city, int postalCode, String country) {
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    /**
     * Get the name of the city
     *
     * @return city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Get the postal code of the city
     *
     * @return the postal code
     */
    public int getPostalCode() {
        return postalCode;
    }

    /**
     * Get the iso country code of the country in which the city is located
     *
     * @return the iso code of the country
     */
    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final City other = (City) obj;
        return this.city.equals(other.city) && this.postalCode == other.postalCode && this.country.equals(other.country);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.city);
        hash = 37 * hash + this.postalCode;
        hash = 37 * hash + Objects.hashCode(this.country);
        return hash;
    }
}
