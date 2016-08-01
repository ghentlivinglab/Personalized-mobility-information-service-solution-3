package vop.groep7.vop7backend.Models.Domain;

import vop.groep7.vop7backend.Models.DomainModel;

/**
 *
 * @author Backend Team
 */
public class Address extends DomainModel {

    private final City city;
    private final String street;
    private final String housenumber;
    private final Coordinate coordinates;

    /**
     * The public constructor with all fields required to create an Address object
     * 
     * @param city the name of the city
     * @param postalCode the postal code of the city
     * @param street the name of the street
     * @param housenumber the house number
     * @param country the country code
     * @param coordinates the coordinates of the address
     */
    public Address(String city, int postalCode, String street, String housenumber, String country, Coordinate coordinates) {
        this.city = new City(city, postalCode, country);
        this.street = street;
        this.housenumber = housenumber;
        this.coordinates = coordinates;
    }

    /**
     * Get the city of the address
     * 
     * @return the city 
     * @see City
     */
    public City getCity() {
        return city;
    }

    /**
     * Get the street of the address
     * 
     * @return name of the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Get the house nr of the address
     * 
     * @return house nr as String
     */
    public String getHousenumber() {
        return housenumber;
    }

    /**
     * Get the coordinates of the address
     * 
     * @return the coordinates
     * @see Coordinate
     */
    public Coordinate getCoordinates() {
        return coordinates;
    }
}
