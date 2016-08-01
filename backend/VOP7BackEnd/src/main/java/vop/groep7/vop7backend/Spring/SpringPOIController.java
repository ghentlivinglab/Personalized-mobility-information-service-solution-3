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
import vop.groep7.vop7backend.Controllers.POIController;
import vop.groep7.vop7backend.Exceptions.DatabaseResponseException;
import vop.groep7.vop7backend.Exceptions.ExistingResponseException;
import vop.groep7.vop7backend.Exceptions.InvalidInputResponseException;
import vop.groep7.vop7backend.Exceptions.NotExistingResponseException;
import vop.groep7.vop7backend.Models.APIModels.APIPOI;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Backend Team
 */
@MonitoredWithSpring
@CrossOrigin
@RestController
public class SpringPOIController extends SpringController {

    /**
     * Static URLs for user operations
     *
     */
    private static final String PARAM_POI = "user/{user_id}/point_of_interest/{poi_id}";
    private static final String BASIC_POI = "user/{user_id}/point_of_interest";

    private static final String BASIC_ERROR = "The database is not available at the moment.";
    private static final String CREATE_EXISTS_ERROR = "The point of interest you are trying to create already exists.";
    private static final String EXISTS_ERROR = "The point of interest already exists.";
    private static final String NOT_EXISTS_ERROR = "The point of interest doesn't exist.";

    @Autowired
    private POIController poiController;

    /**
     * HTTP GET method for all poi's of a user
     *
     * @param userId The Id of a user
     * @see APIPOI
     * @return A list of poi objects
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = BASIC_POI, method = RequestMethod.GET)
    public @ResponseBody
    List<APIPOI> getAllPOIs(@PathVariable("user_id") int userId) throws DatabaseResponseException {
        try {
            return poiController.getAPIPOIs(userId);
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringPOIController.class.getName()).log(Level.SEVERE, "Could not get all POIs.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP POST method for pois
     *
     * @param userId the Id of a user
     * @param poi The poi we want to create
     * @see APIPOI
     * @return The poi object that has been created
     * @throws NotExistingResponseException This is thrown when no poi or user
     * can be found.
     * @throws ExistingResponseException This is thrown when the poi already
     * exists.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = BASIC_POI, method = RequestMethod.POST)
    public @ResponseBody
    APIPOI createPOI(@PathVariable("user_id") int userId, @RequestBody APIPOI poi) throws NotExistingResponseException, ExistingResponseException, DatabaseResponseException {
        checkPOIInput(poi);

        try {
            if (!poiController.existsAPIPOI(userId, poi)) {
                APIPOI p = poiController.createAPIPOI(userId, poi);
                if (p == null) {
                    throw new NotExistingResponseException(NOT_EXISTS_ERROR);
                } else {
                    return p;
                }
            } else {
                throw new ExistingResponseException(CREATE_EXISTS_ERROR);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringPOIController.class.getName()).log(Level.SEVERE, "Could not create POI.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP GET method for a poi of a user
     *
     * @param userId the Id of a user
     * @param poiId the Id of a poi
     * @see APIPOI
     * @return The poi object that has the corresponding id
     * @throws NotExistingResponseException This is thrown when no poi or user
     * can be found.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = PARAM_POI, method = RequestMethod.GET)
    public @ResponseBody
    APIPOI getPOI(@PathVariable("user_id") int userId, @PathVariable("poi_id") int poiId) throws NotExistingResponseException, DatabaseResponseException {
        try {
            APIPOI poi = poiController.getAPIPOI(userId, poiId);
            if (poi == null) {
                throw new NotExistingResponseException(NOT_EXISTS_ERROR);
            }
            return poi;
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringPOIController.class.getName()).log(Level.SEVERE, "Could not get POI.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP PUT method for pois
     *
     * @param userId the Id of a user
     * @param poiId the Id of a poi
     * @param poi The poi we want to modify
     * @see APIPOI
     * @return The poi object that has been modified
     * @throws ExistingResponseException Thrown if the poi can't be created
     * because it exists already
     * @throws NotExistingResponseException This is thrown when no poi or user
     * can be found.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = PARAM_POI, method = RequestMethod.PUT)
    public @ResponseBody
    APIPOI modifyPOI(@PathVariable("user_id") int userId, @PathVariable("poi_id") int poiId, @RequestBody APIPOI poi) throws ExistingResponseException, NotExistingResponseException, DatabaseResponseException {
        checkPOIInput(poi);

        try {
            if (!poiController.existsAPIPOI(userId, poi)) {
                return poiController.modifyAPIPOI(userId, poiId, poi);
            } else {
                throw new ExistingResponseException(CREATE_EXISTS_ERROR);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringPOIController.class.getName()).log(Level.SEVERE, "Could not modify POI.", ex);
            try {
                APIPOI p = poiController.getAPIPOI(userId, poiId);
                if (p != null) {
                    throw new ExistingResponseException(EXISTS_ERROR);
                } else {
                    throw new NotExistingResponseException(NOT_EXISTS_ERROR);
                }
            } catch (DataAccessException ex1) {
                Logger.getLogger(SpringPOIController.class.getName()).log(Level.SEVERE, "Could not get or modify POI.", ex1);
                throw new DatabaseResponseException(BASIC_ERROR);
            }
        }
    }

    /**
     * HTTP DELETE method for pois
     *
     * @param userId the Id of a user
     * @param poiId the Id of the poi
     * @throws NotExistingResponseException This is thrown when no poi or user
     * can be found.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @RequestMapping(value = PARAM_POI, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePOI(@PathVariable("user_id") int userId, @PathVariable("poi_id") int poiId) throws NotExistingResponseException, DatabaseResponseException {
        try {
            if (!poiController.deleteAPIPOI(userId, poiId)) {
                throw new NotExistingResponseException(NOT_EXISTS_ERROR);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringPOIController.class.getName()).log(Level.SEVERE, "Could not delete POI.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    private void checkPOIInput(APIPOI p) {
        boolean valid = true;
        List<String> invalidFields = new ArrayList<>();

        if (p.getName() == null || p.getName().equals("")) {
            valid = false;
            invalidFields.add("name");
        }
        List<String> invalid1 = checkAddressInput(p.getAddress(), "address");
        if (!invalid1.isEmpty()) {
            valid = false;
            invalidFields.addAll(invalid1);
        }
        if (p.getRadius() == 0) {
            valid = false;
            invalidFields.add("radius");
        }
        if (p.getNotifyForEventTypes() == null) {
            valid = false;
            invalidFields.add("notify_for_event_types");
        }
        if (p.getNotify() == null) {
            valid = false;
            invalidFields.add("notify");
        }

        if (!valid) {
            throw new InvalidInputResponseException(invalidFields);
        }
    }
}
