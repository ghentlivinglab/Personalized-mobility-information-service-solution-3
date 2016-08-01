package vop.groep7.vop7backend.Models.APIModels.Authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APIRefreshToken extends APIModel {

    private String token;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("user_url")
    private String userUrl;
    private APIRole role;

    /**
     * Compare this APIRefreshToken object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIRefreshToken)) {
            return false;
        }
        APIRefreshToken other = (APIRefreshToken) o;

        return Objects.equals(getToken(), other.getToken())
                && Objects.equals(getUserId(), other.getUserId())
                && Objects.equals(getUserUrl(), other.getUserUrl())
                && Objects.equals(getRole(), other.getRole());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.token);
        hash = 97 * hash + Objects.hashCode(this.userId);
        hash = 97 * hash + Objects.hashCode(this.userUrl);
        hash = 97 * hash + Objects.hashCode(this.role);
        return hash;
    }
    
    /**
     * Get the token
     *
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the token
     *
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Get the user id
     *
     * @return The user id
     */
    @JsonSerialize
    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    /**
     * Set the user id
     *
     * @param userId The user id
     */
    @JsonSerialize
    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Get the user url
     *
     * @return The user url
     */
    @JsonSerialize
    @JsonProperty("user_url")
    public String getUserUrl() {
        return userUrl;
    }

    /**
     * Set the user url
     *
     * @param userUrl The user url
     */
    @JsonSerialize
    @JsonProperty("user_url")
    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    /**
     * Get the role
     *
     * @return The role
     */
    public APIRole getRole() {
        return role;
    }

    /**
     * Set the role
     *
     * @param role The role
     */
    public void setRole(APIRole role) {
        this.role = role;
    }
}
