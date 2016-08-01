package vop.groep7.vop7backend.Controllers;

import java.util.ArrayList;
import java.util.List;
import vop.groep7.vop7backend.Models.APIModels.APITravel;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.TravelFactory;

/**
 *
 * @author Backend Team
 */
public class TravelController extends Controller {

    /**
     * Get a APITravel object based on it's Id and the Id of the User
     *
     * @param userId Id of a user
     * @param travelId Id of the travel
     * @see APITravel
     * @return The API travel object
     * @throws DataAccessException This is thrown when no travel can be found.
     */
    public APITravel getAPITravel(int userId, int travelId) throws DataAccessException {
        Travel travel = getTravel(userId, travelId);

        return TravelFactory.toAPIModel(travel, userId);
    }

    /**
     * Get all APITravel objects of a User
     *
     * @param userId Id of a user
     * @see APITravel
     * @return A list of all API travels
     * @throws DataAccessException This is thrown when no travels can be found.
     */
    public List<APITravel> getAPITravels(int userId) throws DataAccessException {
        List<Travel> travels = getTravels(userId);
        // D2A travels
        List<APITravel> res = new ArrayList<>();
        travels.stream().forEach((t) -> {
            res.add(TravelFactory.toAPIModel(t, userId));
        });
        return res;
    }

    /**
     * Insert a APITravel object in the database
     *
     * @param userId Id of a user
     * @param travel An API travel object that has to be created
     * @see APITravel
     * @return The created API travel object
     * @throws DataAccessException This is thrown when no travel can be created.
     */
    public APITravel createAPITravel(int userId, APITravel travel) throws DataAccessException {
        User user = getUser(userId);

        Address start = AddressFactory.toDomainModel(travel.getStartpoint());
        Address end = AddressFactory.toDomainModel(travel.getEndpoint());
        Travel newTravel = TravelFactory.create(travel.getName(), start, end, travel.getTimeInterval(), travel.getRecurring());

        // create travel in db
        int generatedId = getTravelDAO().createTravel(userId, newTravel);
        // use generatedId as travel identifier
        newTravel.setTravelIdentifier(generatedId);
        // load newTravel into user
        user.addTravel(newTravel);

        return TravelFactory.toAPIModel(newTravel, userId);
    }

    /**
     * Delete a travel object from the database
     *
     * @param userId Id of a user
     * @param travelId Id of the travel
     * @return If the deleting has succeeded
     * @throws DataAccessException This is thrown when the travel can't be
     * deleted.
     */
    public boolean deleteAPITravel(int userId, int travelId) throws DataAccessException {
        User user = getUser(userId);
        // delete travel in cache
        user.removeTravel(travelId);

        // clear already matched events of user
        getUserDAO().deleteMatchedEvents(userId);

        // delete travel in db
        return getTravelDAO().deleteTravel(userId, travelId);
    }

    /**
     * Modify a travel object in the database
     *
     * @param userId Id of a user
     * @param travelId Id of the travel
     * @param travel An API travel object that has to be modified
     * @see APITravel
     * @return The modified API travel object
     * @throws DataAccessException This is thrown when the travel can't be
     * modified.
     */
    public APITravel modifyAPITravel(int userId, int travelId, APITravel travel) throws DataAccessException {
        User user = getUser(userId);

        Travel modifiedTravel = TravelFactory.toDomainModel(travel);
        // load modified travel into user
        user.removeTravel(travelId);
        user.addTravel(modifiedTravel);

        // modify travel in db
        getTravelDAO().modifyTravel(userId, travelId, modifiedTravel);

        // clear already matched events of user
        getUserDAO().deleteMatchedEvents(userId);

        return travel;
    }

    /**
     * Check if the API travel already exists
     *
     * @param userId Id of a user
     * @param travel An APITravel object
     * @see APITravel
     * @return If the API travel already exists
     * @throws DataAccessException This is thrown when the travel can't be
     * checked.
     */
    public boolean existsAPITravel(int userId, APITravel travel) throws DataAccessException {
        return getTravelDAO().existsTravel(userId, TravelFactory.toDomainModel(travel));
    }
}
