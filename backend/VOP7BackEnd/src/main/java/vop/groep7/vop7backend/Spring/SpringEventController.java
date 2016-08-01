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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vop.groep7.vop7backend.Controllers.EventController;
import vop.groep7.vop7backend.Exceptions.DatabaseResponseException;
import vop.groep7.vop7backend.Exceptions.InvalidInputResponseException;
import vop.groep7.vop7backend.Exceptions.NonActiveResponseException;
import vop.groep7.vop7backend.Exceptions.NotExistingResponseException;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Backend Team
 */
@MonitoredWithSpring
@CrossOrigin
@RestController
public class SpringEventController extends SpringController {

    /**
     * Static URLs for event operations
     *
     */
    private static final String PARAM_EVENT = "event/{event_id}";
    private static final String BASIC_EVENT = "event";
    private static final String BASIC_EVENTTYPES = "eventtype";

    private static final String BASIC_ERROR = "The database is not available at the moment.";
    private static final String NOT_EXISTS_ERROR = "The event doesn't exist.";
    private static final String NOT_ACTIVE_ERROR = "Event is already marked as non-active.";

    @Autowired
    private EventController eventController;

    /**
     * HTTP GET method for all events
     *
     * @param userId An optional id of a user
     * @see APIEvent
     * @return A list of event objects
     * @throws DatabaseResponseException This is thrown when the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = BASIC_EVENT, method = RequestMethod.GET)
    public @ResponseBody
    List<APIEvent> getAllEvents(@RequestParam(value = "user_id", required = false) Integer userId) throws DatabaseResponseException {
        if (userId == null) {
            return eventController.getAPIEvents();
        } else {
            try {
                return eventController.getUserAPIEvents(userId);
            } catch (DataAccessException ex) {
                Logger.getLogger(SpringEventController.class.getName()).log(Level.SEVERE, "Could not get all events.", ex);
                throw new DatabaseResponseException(BASIC_ERROR);
            }
        }
    }

    /**
     * HTTP POST method for events
     *
     * @param event The event we want to create
     * @see APIEvent
     * @return The event object that has been created
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = BASIC_EVENT, method = RequestMethod.POST)
    public @ResponseBody
    APIEvent createEvent(@RequestBody APIEvent event) {
        checkEventInput(event);

        return eventController.createAPIEvent(event);
    }

    /**
     * HTTP GET method for one event
     *
     * @param eventId the Id of an event
     * @see APIEvent
     * @return The event object with the corresponding Id
     * @throws NotExistingResponseException This is thrown when no event can be
     * found.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = PARAM_EVENT, method = RequestMethod.GET)
    public @ResponseBody
    APIEvent getEvent(@PathVariable("event_id") int eventId) throws NotExistingResponseException {
        APIEvent event = eventController.getAPIEvent(eventId);
        if (event != null) {
            return event;
        } else {
            throw new NotExistingResponseException(NOT_EXISTS_ERROR);
        }
    }

    /**
     * HTTP PUT method for events
     *
     * @param eventId the Id of an event
     * @param event The event we want to modify
     * @see APIEvent
     * @return The event object that has been modified
     * @throws NotExistingResponseException This is thrown when no event can be
     * found.
     * @throws NonActiveResponseException This is thrown when the event is no
     * longer active.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = PARAM_EVENT, method = RequestMethod.PUT)
    public @ResponseBody
    APIEvent modifyEvent(@PathVariable("event_id") int eventId, @RequestBody APIEvent event) throws NotExistingResponseException, NonActiveResponseException {
        try {
            checkEventInput(event);

            APIEvent newEvent = eventController.modifyAPIEvent(eventId, event);
            if (newEvent != null) {
                return newEvent;
            } else {
                newEvent = eventController.getAPIEvent(eventId);
                if (newEvent != null) {
                    throw new NonActiveResponseException(NOT_ACTIVE_ERROR);
                } else {
                    throw new NotExistingResponseException(NOT_EXISTS_ERROR);
                }
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringEventController.class.getName()).log(Level.SEVERE, "Could not modify event.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP DELETE method for users
     *
     * @param eventId The Id of an event
     */
    @RequestMapping(value = PARAM_EVENT, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable("event_id") int eventId) {
        if (!eventController.deleteAPIEvent(eventId)) {
            throw new NotExistingResponseException(NOT_EXISTS_ERROR);
        }
    }

    /**
     * HTTP GET method for all event types
     *
     * @see APIEventType
     * @return A list of event types
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = BASIC_EVENTTYPES, method = RequestMethod.GET)
    public @ResponseBody
    List<APIEventType> getEventTypes() {
        return eventController.getAPIEventTypes();
    }

    private void checkEventInput(APIEvent e) {
        boolean valid = true;
        List<String> invalidFields = new ArrayList<>();

        if (e.getCoordinates() == null
                || e.getCoordinates().getLat() < -180 || e.getCoordinates().getLon() < -180
                || e.getCoordinates().getLat() > 180 || e.getCoordinates().getLon() > 180) {
            valid = false;
            invalidFields.add("coordinates");
        }

        if (e.getDescription() == null || e.getDescription().equals("")) {
            valid = false;
            invalidFields.add("description");
        }

        if (e.getRelevantForTransportationTypes() == null) {
            valid = false;
            invalidFields.add("relevant_for_transportation_types");
        }

        if (!valid) {
            throw new InvalidInputResponseException(invalidFields);
        }
    }
}
