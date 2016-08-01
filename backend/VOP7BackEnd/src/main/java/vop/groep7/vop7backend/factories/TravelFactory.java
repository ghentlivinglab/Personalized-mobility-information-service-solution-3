package vop.groep7.vop7backend.factories;

import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.APIModels.APITravel;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Interval;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.Models.Domain.WeeklyRecurring;

/**
 *
 * @author Backend Team
 */
public class TravelFactory extends Factory {

    /**
     * Create a new Travel object
     * 
     * @param name The name of the travel
     * @param start The start address of the travel
     * @param end The end address of the travel
     * @param interval The interval of the travel
     * @param recurring When this travel recurs
     * @return A new Travel object
     */
    public static Travel create(String name, Address start, Address end, String[] interval, Boolean[] recurring) {
        // set weekly recurring
        WeeklyRecurring wr = null;
        if (recurring != null) {
            wr = new WeeklyRecurring(recurring);
        }
        // set time interval
        Interval timeInterval = null;
        if (interval != null) {
            timeInterval = new Interval(interval);
        }
        Travel travel = new Travel(name, start, end, timeInterval, wr);
        return travel;
    }

    /**
     * Build a valid Travel object
     * 
     * @param travelIdentifier The identifier of this travel
     * @param name The name of the travel
     * @param start The start address of the travel
     * @param end The end address of the travel
     * @param interval The interval of the travel
     * @param recurring When this travel recurs
     * @return A valid Travel object
     */
    public static Travel build(int travelIdentifier, String name, Address start, Address end, String[] interval, Boolean[] recurring) {
        // set weekly recurring
        WeeklyRecurring wr = null;
        if (recurring != null) {
            wr = new WeeklyRecurring(recurring);
        }
        // set time interval
        Interval timeInterval = null;
        if (interval != null) {
            timeInterval = new Interval(interval);
        }
        Travel travel = new Travel(name, start, end, timeInterval, wr);
        travel.setTravelIdentifier(travelIdentifier);
        return travel;
    }

    /**
     * Create an APITravel object starting from a Travel
     * 
     * @param travel A Travel object
     * @param userId The identifier of the owner of the travel
     * @return A APITravel object
     */
    public static APITravel toAPIModel(Travel travel, int userId) {
        APITravel result = new APITravel();
        result.setStartpoint(AddressFactory.toAPIModel(travel.getStart()));
        result.setEndpoint(AddressFactory.toAPIModel(travel.getEnd()));
        result.setName(travel.getName());
        if (travel.getInterval() != null) {
            result.setTimeInterval(travel.getInterval().toArray());
        }
        if (travel.getRecurring() != null) {
            result.setRecurring(travel.getRecurring().toArray());
        }

        try {
            result.setId(String.valueOf(travel.getTravelIdentifier()));
            result.setLinks(getLinks(USER + userId + TRAVEL + travel.getTravelIdentifier()));
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TravelFactory.class.getName()).log(Level.SEVERE, "Travel id cannot be null!", ex);
            return null;
        }
        return result;
    }

    /**
     * Create a Travel object starting from an APITravel
     * 
     * @param travel An APITravel object
     * @return A Travel object
     */
    public static Travel toDomainModel(APITravel travel) {
        // set weekly recurring
        WeeklyRecurring recurring = null;
        if (travel.getRecurring() != null) {
            recurring = new WeeklyRecurring(travel.getRecurring());
        }
        // set time interval
        Interval timeInterval = null;
        if (travel.getTimeInterval() != null) {
            timeInterval = new Interval(travel.getTimeInterval());
        }
        Travel result = new Travel(travel.getName(), AddressFactory.toDomainModel(travel.getStartpoint()), AddressFactory.toDomainModel(travel.getEndpoint()), timeInterval, recurring);
        String id = travel.getId();
        if (id != null) {
            result.setTravelIdentifier(Integer.valueOf(id));
        }
        return result;
    }
}
