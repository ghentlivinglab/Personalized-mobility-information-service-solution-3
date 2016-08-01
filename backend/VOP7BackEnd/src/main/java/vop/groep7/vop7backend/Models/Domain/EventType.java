package vop.groep7.vop7backend.Models.Domain;

import java.util.Objects;
import vop.groep7.vop7backend.Models.DomainModel;

/**
 *
 * @author Backend Team
 */
public class EventType extends DomainModel {

    private final String type;

    /**
     * The public constructor with all fields required to create an EventType object
     * 
     * @param type the type of event
     */
    public EventType(String type) {
        this.type = type;
    }

    /**
     * Get the type of event
     * 
     * @return the event type as String
     */
    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventType other = (EventType) obj;
        return Objects.equals(this.type, other.type);
    }
   
}
