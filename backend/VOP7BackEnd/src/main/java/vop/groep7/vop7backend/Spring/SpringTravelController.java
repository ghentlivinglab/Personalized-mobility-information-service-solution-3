package vop.groep7.vop7backend.Spring;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.bull.javamelody.MonitoredWithSpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vop.groep7.vop7backend.Controllers.TravelController;
import vop.groep7.vop7backend.Exceptions.DatabaseResponseException;
import vop.groep7.vop7backend.Exceptions.ExistingResponseException;
import vop.groep7.vop7backend.Exceptions.InvalidInputResponseException;
import vop.groep7.vop7backend.Exceptions.NotExistingResponseException;
import vop.groep7.vop7backend.Models.APIModels.APITravel;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Backend Team
 */
@MonitoredWithSpring
@CrossOrigin
@RestController
public class SpringTravelController extends SpringController {

    /**
     * Static URLs for user operations
     *
     */
    private static final String PARAM_TRAVEL = "user/{user_id}/travel/{travel_id}";
    private static final String BASIC_TRAVEL = "user/{user_id}/travel";

    private static final String BASIC_ERROR = "The database is not available at the moment.";
    private static final String CREATE_EXISTS_ERROR = "The travel you are trying to create already exists.";
    private static final String EXISTS_ERROR = "The travel already exists.";
    private static final String NOT_EXISTS_ERROR = "The travel doesn't exist.";

    @Autowired
    private TravelController travelController;

    /**
     * HTTP GET method for all travels of a user
     *
     * @param userId The Id of a user
     * @see APITravel
     * @return A list of travel objects
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = BASIC_TRAVEL, method = RequestMethod.GET)
    public @ResponseBody
    List<APITravel> getAllTravels(@PathVariable("user_id") int userId) throws DatabaseResponseException {
        try {
            return travelController.getAPITravels(userId);
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringTravelController.class.getName()).log(Level.SEVERE, "Could not get all travels.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP POST method for travels
     *
     * @param userId the Id of a user
     * @param travel The travel we want to create
     * @see APITravel
     * @return The travel object that has been created
     * @throws NotExistingResponseException This is thrown when no travel or
     * user can be found.
     * @throws ExistingResponseException This is thrown when the travel already
     * exists.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = BASIC_TRAVEL, method = RequestMethod.POST)
    public @ResponseBody
    APITravel createTravel(@PathVariable("user_id") int userId, @RequestBody APITravel travel) throws NotExistingResponseException, ExistingResponseException, DatabaseResponseException {
        checkTravelInput(travel);

        try {
            if (!travelController.existsAPITravel(userId, travel)) {
                APITravel t = travelController.createAPITravel(userId, travel);
                if (t == null) {
                    throw new NotExistingResponseException(NOT_EXISTS_ERROR);
                } else {
                    return t;
                }
            } else {
                throw new ExistingResponseException(CREATE_EXISTS_ERROR);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringTravelController.class.getName()).log(Level.SEVERE, "Could not create travel.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP GET method for a travel of a user
     *
     * @param userId the Id of a user
     * @param travelId the Id of a travel
     * @see APITravel
     * @return The travel object that has the corresponding id
     * @throws NotExistingResponseException This is thrown when no travel or
     * user can be found.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = PARAM_TRAVEL, method = RequestMethod.GET)
    public @ResponseBody
    APITravel getTravel(@PathVariable("user_id") int userId, @PathVariable("travel_id") int travelId) throws NotExistingResponseException, DatabaseResponseException {
        try {
            APITravel travel = travelController.getAPITravel(userId, travelId);
            if (travel == null) {
                throw new NotExistingResponseException(NOT_EXISTS_ERROR);
            }
            return travel;
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringTravelController.class.getName()).log(Level.SEVERE, "Could not get travel.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP PUT method for travels
     *
     * @param userId the Id of a user
     * @param travelId the Id of a travel
     * @param travel The travel we want to modify
     * @see APITravel
     * @return The travel object that has been modified
     * @throws ExistingResponseException Thrown if the travel can't be created
     * because it exists already
     * @throws NotExistingResponseException This is thrown when no travel or
     * user can be found.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = PARAM_TRAVEL, method = RequestMethod.PUT)
    public @ResponseBody
    APITravel modifyTravel(@PathVariable("user_id") int userId, @PathVariable("travel_id") int travelId, @RequestBody APITravel travel) throws ExistingResponseException, NotExistingResponseException, DatabaseResponseException {
        checkTravelInput(travel);

        try {
            if (!travelController.existsAPITravel(userId, travel)) {
                return travelController.modifyAPITravel(userId, travelId, travel);
            } else {
                throw new ExistingResponseException(CREATE_EXISTS_ERROR);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringTravelController.class.getName()).log(Level.SEVERE, "Could not modify travel.", ex);
            try {
                APITravel t = travelController.getAPITravel(userId, travelId);
                if (t != null) {
                    throw new ExistingResponseException(EXISTS_ERROR);
                } else {
                    throw new NotExistingResponseException(NOT_EXISTS_ERROR);
                }
            } catch (DataAccessException ex1) {
                Logger.getLogger(SpringTravelController.class.getName()).log(Level.SEVERE, "Could not modify or get travel.", ex1);
                throw new DatabaseResponseException(BASIC_ERROR);
            }
        }
    }

    /**
     * HTTP DELETE method for travels
     *
     * @param userId the Id of a user
     * @param travelId the Id of the travel
     * @throws NotExistingResponseException This is thrown when no travel or
     * user can be found.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @RequestMapping(value = PARAM_TRAVEL, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTravel(@PathVariable("user_id") int userId, @PathVariable("travel_id") int travelId) throws NotExistingResponseException, DatabaseResponseException {
        try {
            if (!travelController.deleteAPITravel(userId, travelId)) {
                throw new NotExistingResponseException(NOT_EXISTS_ERROR);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringTravelController.class.getName()).log(Level.SEVERE, "Could not delete travel.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    private void checkTravelInput(APITravel t) {
        boolean valid = true;
        List<String> invalidFields = new ArrayList<>();

        if (t.getName() == null || t.getName().equals("")) {
            valid = false;
            invalidFields.add("name");
        }
        if (t.getTimeInterval() == null || t.getTimeInterval().length != 2) {
            valid = false;
            invalidFields.add("time_interval");
        }
        List<String> invalid1 = checkAddressInput(t.getStartpoint(), "startpoint");
        if (!invalid1.isEmpty()) {
            valid = false;
            invalidFields.addAll(invalid1);
        }
        List<String> invalid2 = checkAddressInput(t.getEndpoint(), "endpoint");
        if (!invalid2.isEmpty()) {
            valid = false;
            invalidFields.addAll(invalid2);
        }
        if (t.getRecurring() == null || t.getRecurring().length != 7) {
            valid = false;
            invalidFields.add("recurring");
        }
        if (!valid) {
            throw new InvalidInputResponseException(invalidFields);
        }
    }
}
