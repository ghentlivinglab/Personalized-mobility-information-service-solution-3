package vop.groep7.vop7backend.Models.APIModels.Datadump;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModels.APIRoute;
import vop.groep7.vop7backend.Models.APIModels.APITravel;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public class APITravelDump implements Serializable, Model {

    private APITravel travel;
    private APIRoute[] routes;

    /**
     * Compare this APITravelDump object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APITravelDump)) {
            return false;
        }
        APITravelDump other = (APITravelDump) o;

        return Objects.equals(getTravel(), other.getTravel())
                && Arrays.deepEquals(getRoutes(), other.getRoutes());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.travel);
        hash = 89 * hash + Arrays.deepHashCode(this.routes);
        return hash;
    }

    /**
     * Get the basic information about the travel
     *
     * @return The basic information about the travel
     */
    public APITravel getTravel() {
        return travel;
    }

    /**
     * Set the basic information about the travel
     *
     * @param travel The basic information about the travel
     */
    public void setTravel(APITravel travel) {
        this.travel = travel;
    }

    /**
     * Get the list of routes
     *
     * @return The list of routes
     */
    public APIRoute[] getRoutes() {
        return routes;
    }

    /**
     * Set the list of routes
     *
     * @param routes The list of routes
     */
    public void setRoutes(APIRoute[] routes) {
        this.routes = routes;
    }
}
