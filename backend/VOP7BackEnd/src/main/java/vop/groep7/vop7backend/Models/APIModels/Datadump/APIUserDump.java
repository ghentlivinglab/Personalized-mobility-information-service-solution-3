package vop.groep7.vop7backend.Models.APIModels.Datadump;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModels.APIPOI;
import vop.groep7.vop7backend.Models.APIModels.APIUser;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public class APIUserDump implements Serializable, Model {

    private APIUser user;
    private APITravelDump[] travels;
    @JsonProperty("points_of_interest")
    private APIPOI[] pointsOfInterest;

    /**
     * Compare this APIUserDump object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIUserDump)) {
            return false;
        }
        APIUserDump other = (APIUserDump) o;

        return Objects.equals(getUser(), other.getUser())
                && Arrays.deepEquals(getTravels(), other.getTravels())
                && Arrays.deepEquals(getPointsOfInterest(), other.getPointsOfInterest());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.user);
        hash = 97 * hash + Arrays.deepHashCode(this.travels);
        hash = 97 * hash + Arrays.deepHashCode(this.pointsOfInterest);
        return hash;
    }

    /**
     * Getter for pointsOfInterest
     *
     * @return The pointsOfInterest of the user
     */
    @JsonSerialize
    @JsonProperty("points_of_interest")
    public APIPOI[] getPointsOfInterest() {
        return pointsOfInterest;
    }

    /**
     * Setter for pointsOfInterest
     *
     * @param pointsOfInterest The pointsOfInterest of the user
     */
    @JsonSerialize
    @JsonProperty("points_of_interest")
    public void setPointsOfInterest(APIPOI[] pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    /**
     * Get the basic user information
     *
     * @return The basic user information
     */
    public APIUser getUser() {
        return user;
    }

    /**
     * Set the basic user information
     *
     * @param user The basic user information
     */
    public void setUser(APIUser user) {
        this.user = user;
    }

    /**
     * Get the list of travel dumps of this user
     *
     * @return The list of travel dumps of this user
     */
    public APITravelDump[] getTravels() {
        return travels;
    }

    /**
     * Set the list of travel dumps of this user
     *
     * @param travels The list of travel dumps of this user
     */
    public void setTravels(APITravelDump[] travels) {
        this.travels = travels;
    }
}
