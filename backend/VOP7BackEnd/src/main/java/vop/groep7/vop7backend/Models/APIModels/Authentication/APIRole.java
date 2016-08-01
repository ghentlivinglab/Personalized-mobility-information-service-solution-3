package vop.groep7.vop7backend.Models.APIModels.Authentication;

import java.io.Serializable;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public enum APIRole implements Serializable, Model {

    /**
     * An admin role
     */
    ROLE_ADMIN("ROLE_ADMIN"),
    /**
     * An operator role
     */
    ROLE_OPERATOR("ROLE_OPERATOR"),
    /**
     * A user role
     */
    ROLE_USER("ROLE_USER");
    
    private String role;
    
    private APIRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
