package vop.groep7.vop7backend.Models.APIModels;

import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public enum APITransport implements Serializable, Model {

    /**
     * A car transport type
     */
    CAR("car"),
    /**
     * A bus transport type
     */
    BUS("bus"),
    /**
     * A bike transport type
     */
    BIKE("bike"),
    /**
     * A train transport type
     */
    //TRAIN("train"),
    /**
     * A streetcar transport type
     */
    STREETCAR("streetcar");

    private final String type;

    private APITransport(String type) {
        this.type = type;
    }

    /**
     * Get the type of transportation
     *
     * @return The type of transportation
     */
    @JsonValue
    public String getType() {
        return type;
    }

    /**
     * Get the type of transportation based on the string representation
     *
     * @param value A string of transport type
     * @return The type of transportation
     */
    public static APITransport getTransport(String value) {
        if (value == null) {
            return null;
        }
        for (APITransport v : values()) {
            if (v.getType().equalsIgnoreCase(value.trim())) {
                return v;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return type;
    }
}
