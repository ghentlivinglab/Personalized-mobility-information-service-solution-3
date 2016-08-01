package vop.groep7.vop7backend.Models.APIModels.Authentication;

import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APIAccessToken extends APIModel {

    private String token;
    private String exp;

    /**
     * Compare this APIAccessToken object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIAccessToken)) {
            return false;
        }
        APIAccessToken other = (APIAccessToken) o;

        return Objects.equals(getToken(), other.getToken())
                && Objects.equals(getExp(), other.getExp());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.token);
        hash = 79 * hash + Objects.hashCode(this.exp);
        return hash;
    }

    /**
     * Getter for the token
     *
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter for the token
     * 
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Get when this access token expires
     *
     * @return When this access token expires
     */
    public String getExp() {
        return exp;
    }

    /**
     * Set when this access token expires
     *
     * @param exp When this access token expires
     */
    public void setExp(String exp) {
        this.exp = exp;
    }
}
