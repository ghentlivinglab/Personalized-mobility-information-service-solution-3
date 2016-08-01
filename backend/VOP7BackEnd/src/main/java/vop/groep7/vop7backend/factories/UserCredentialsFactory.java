package vop.groep7.vop7backend.factories;

import vop.groep7.vop7backend.Models.Domain.Credentials;
import vop.groep7.vop7backend.Models.Domain.Password;
import vop.groep7.vop7backend.Models.Domain.User;

/**
 *
 * @author Backend Team
 */
public class UserCredentialsFactory {

    /**
     * Create a new Credentials object 
     * 
     * @param password A plain text password
     * @param user The user to whom these credentials belong
     * @return A new Credentials object
     */
    public static Credentials create(String password, User user) {
        return new Credentials(new Password(password), user.getUserIdentifier(), "ROLE_USER");
    }
    
    /**
     * Build a valid Credentials object to use when modifying the credentials
     * 
     * @param password A password in the form of a byte array
     * @param userIdentifier The identifier of the user to whom these credentials belong
     * @param role The role of the user
     * @return The valid Credentials object
     */
    public static Credentials build(String password, int userIdentifier, String role) {
        return new Credentials(new Password(password), userIdentifier, role);
    }

    /**
     * Build a valid Credentials object
     * 
     * @param password A password in the form of a byte array
     * @param userIdentifier The identifier of the user to whom these credentials belong
     * @param role The role of the user
     * @return The valid Credentials object
     */
    public static Credentials build(byte[] password, int userIdentifier, String role) {
        return new Credentials(new Password(password), userIdentifier, role);
    }

}
