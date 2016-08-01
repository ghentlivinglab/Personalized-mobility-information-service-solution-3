package vop.groep7.vop7backend.Models.APIModels;

import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APIEventType extends APIModel {

    private String type;

    /**
     * Compare this APIEventType object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIEventType)) {
            return false;
        }
        APIEventType other = (APIEventType) o;

        return Objects.equals(getType(), other.getType());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.type);
        return hash;
    }

    /**
     * Get the type of the event
     *
     * @return type The type of the event
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the event
     *
     * @param type The type of the event
     */
    public void setType(String type) {
        this.type = type;
    }
}
