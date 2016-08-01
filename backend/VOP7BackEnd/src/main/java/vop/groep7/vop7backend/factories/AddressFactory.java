package vop.groep7.vop7backend.factories;

import vop.groep7.vop7backend.Models.APIModels.APIAddress;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Coordinate;

/**
 *
 * @author Backend Team
 */
public class AddressFactory {

    /**
     * Create a new Address object
     * 
     * @param city The name of the city of the address
     * @param postalCode The postal code of the city of the address
     * @param street The name of the street of the address
     * @param housenumber The housenumber of the address
     * @param country The country where the address is
     * @param coordinates A Coordinate object that contains the coordinates of the address
     * @return A new Address object
     */
    public static Address create(String city, int postalCode, String street, String housenumber, String country, Coordinate coordinates) {
        Address address = new Address(city, postalCode, street, housenumber, country, coordinates);
        return address;
    }

   /**
     * Build a valid Address object
     * 
     * @param city The name of the city of the address
     * @param postalCode The postal code of the city of the address
     * @param street The name of the street of the address
     * @param housenumber The housenumber of the address
     * @param country The country where the address is
     * @param coordinates A Coordinate object that contains the coordinates of the address
     * @return A valid Address object
     */
    public static Address build(String city, int postalCode, String street, String housenumber, String country, Coordinate coordinates) {
        Address address = new Address(city, postalCode, street, housenumber, country, coordinates);
        return address;
    }

    /**
     * Create an APIAddress object starting from an Address
     * 
     * @param address An Address object
     * @return An APIAddress object
     */
    public static APIAddress toAPIModel(Address address) {
        APIAddress result = new APIAddress();
        if (address.getCity() != null) {
            result.setCity(address.getCity().getCity());
            result.setCountry(address.getCity().getCountry());
            result.setPostalCode(address.getCity().getPostalCode());
        }
        result.setHousenumber(address.getHousenumber());
        result.setStreet(address.getStreet());
        result.setCoordinates(CoordinateFactory.toAPIModel(address.getCoordinates()));
        return result;
    }

    /**
     * Create an Address object starting from an APIAddress
     * 
     * @param address An APIAddress object
     * @return An Address object
     */
    public static Address toDomainModel(APIAddress address) {
        Address result = new Address(address.getCity(), address.getPostalCode(), address.getStreet(), address.getHousenumber(), address.getCountry(), CoordinateFactory.toDomainModel(address.getCoordinates()));
        return result;
    }
}
