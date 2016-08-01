package vop.groep7.vop7backend.Models.Domain;

/**
 *
 * @author Backend Team
 */
public class Credentials {

    private final Password password;
    private final int userIdentifier;
    private final String role;

    /**
     * The public constructor with all fields required to create a Credentials object
     * 
     * @param password the password of the user with identifier 'userIdentifier'
     * @param userIdentifier the unique identifier of a user
     * @param role the role of the user e.g. USER_ROLE when a normal user
     */
    public Credentials(Password password, int userIdentifier, String role) {
        this.password = password;
        this.userIdentifier = userIdentifier;
        this.role = role;
    }

    /**
     * Get the password 
     * 
     * @return the password
     * @see Password
     */
    public Password getPassword() {
        return password;
    }

    /**
     * Get the user identifier of the user to who this credentials belong
     * 
     * @return the user identifier
     */
    public int getUserIdentifier() {
        return userIdentifier;
    }

    /**
     * Get the user role
     * 
     * @return the role
     */
    public String getRole() {
        return role;
    }
}
