package vop.groep7.vop7backend.Models.APIModels.Datadump;

import java.io.Serializable;
import java.util.Arrays;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public class APIDataDump implements Serializable, Model {

    private APIUserDump[] users;
    private APIEvent[] events;
    private APIEventType[] eventTypes;

    /**
     * Compare this APIDataDump object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIDataDump)) {
            return false;
        }
        APIDataDump other = (APIDataDump) o;

        return Arrays.deepEquals(getUsers(), other.getUsers())
                && Arrays.deepEquals(getEvents(), other.getEvents())
                && Arrays.deepEquals(getEventTypes(), other.getEventTypes());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Arrays.deepHashCode(this.users);
        hash = 29 * hash + Arrays.deepHashCode(this.events);
        hash = 29 * hash + Arrays.deepHashCode(this.eventTypes);
        return hash;
    }

    /**
     * Get a list of user dumps
     *
     * @return A list of user dumps
     */
    public APIUserDump[] getUsers() {
        return users;
    }

    /**
     * Set a list of user dumps
     *
     * @param users A list of user dumps
     */
    public void setUsers(APIUserDump[] users) {
        this.users = users;
    }

    /**
     * Get a list of events
     *
     * @return A list of events
     */
    public APIEvent[] getEvents() {
        return events;
    }

    /**
     * Set a list of events
     *
     * @param events A list of events
     */
    public void setEvents(APIEvent[] events) {
        this.events = events;
    }

    /**
     * Get a list of event types
     *
     * @return A list of event types
     */
    public APIEventType[] getEventTypes() {
        return eventTypes;
    }

    /**
     * Set a list of event types
     *
     * @param eventTypes A list of event types
     */
    public void setEventTypes(APIEventType[] eventTypes) {
        this.eventTypes = eventTypes;
    }
}
