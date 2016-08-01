package vop.groep7.vop7backend.Models.APIModels.Authentication;

import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APICode extends APIModel {

    private String code;

    /**
     * Compare this APICode object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APICode)) {
            return false;
        }
        APICode other = (APICode) o;

        return Objects.equals(getCode(), other.getCode());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.code);
        return hash;
    }

    /**
     * Get the authorization code received from facebook
     *
     * @return The authorization code received from facebook
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the authorization code received from facebook
     *
     * @param code The authorization code received from facebook
     */
    public void setCode(String code) {
        this.code = code;
    }
}
