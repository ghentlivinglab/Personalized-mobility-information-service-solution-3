package vop.groep7.vop7backend.Models.APIModels;

import vop.groep7.vop7backend.Models.APIModels.Authentication.APIRole;

/**
 *
 * @author Backend Team
 */
public class APIUserAdmin extends APIUser {

    private APIRole role;

    /**
     * Create an APIUserAdmin object from an APIUser
     * 
     * @param user A user from the database
     */
    public APIUserAdmin(APIUser user) {
        this.setCellNumber(user.getCellNumber());
        this.setEmail(user.getEmail());
        this.setFirstName(user.getFirstName());
        this.setId(user.getId());
        this.setLastName(user.getLastName());
        this.setLinks(user.getLinks());
        this.setMuteNotifications(user.isMuteNotifications());
        this.setPassword(user.getPassword());
        this.setValidated(user.getValidated());
    }

    /**
     * Get the role of this user
     * 
     * @return The role of this user
     */
    public APIRole getRole() {
        return role;
    }

    /**
     * Set the role of this user
     * 
     * @param role The role of this user
     */
    public void setRole(APIRole role) {
        this.role = role;
    }
}
