package vop.groep7.vop7backend.factories;

import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.Domain.EventType;

/**
 *
 * @author Backend Team
 */
public class EventTypeFactory {

    /**
     * Create a new EventType object
     * 
     * @param type The type of event
     * @return A new EventType object
     */
    public static EventType create(String type) {
        EventType eventType = new EventType(type);
        return eventType;
    }

    /**
     * Build a valid EventType object
     * 
     * @param type The type of event
     * @return A valid EventType object
     */
    public static EventType build(String type) {
        EventType eventType = new EventType(type);
        return eventType;
    }

    /**
     * Create an EventType object starting from an APIEventType
     * 
     * @param type An APIEventType object
     * @return An EventType object
     */
    public static EventType toDomainModel(APIEventType type) {
        EventType eventType = new EventType(type.getType());
        return eventType;
    }

    /**
     * Create an APIEventType object starting from an EventType
     * 
     * @param type An EventType object
     * @return An APIEventType object
     */
    public static APIEventType toAPIModel(EventType type) {
        APIEventType eventType = new APIEventType();
        eventType.setType(type.getType());
        return eventType;
    }
}
