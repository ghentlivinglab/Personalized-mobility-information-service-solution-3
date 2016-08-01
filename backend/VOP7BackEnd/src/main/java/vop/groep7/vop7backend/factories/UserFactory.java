package vop.groep7.vop7backend.factories;

import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.APIModels.APIUser;
import vop.groep7.vop7backend.Models.APIModels.APIValidation;
import vop.groep7.vop7backend.Models.Domain.User;

/**
 *
 * @author Backend Team
 */
public class UserFactory extends Factory {

    private static final String USER_ID_ERROR = "User id should be set!";
    
    /**
     * Create a new User object 
     * 
     * @param userIdentifier The unique identifier of the user
     * @param email The unique email address of the user
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param cellPhone The cell number of the user
     * @param mute Whether or not the user wants notifications
     * @return The new User object
     */
    public static User create(int userIdentifier, String email, String firstName, String lastName, String cellPhone, boolean mute) {
        User user = new User(userIdentifier, email, firstName, lastName, cellPhone, mute, false, false);
        // TODO: validate cellphone
        user.setCellPhone(cellPhone);

        return user;
    }

    /**
     * Create a User object starting from an APIUser
     * 
     * @param user An APIUser object
     * @return A User object
     */
    public static User toDomainModel(APIUser user) {
        if(user.getId()==null){
            Logger.getLogger(UserFactory.class.getName()).log(Level.SEVERE, USER_ID_ERROR);
        }
        User result = new User(Integer.valueOf(user.getId()), user.getEmail(), user.getFirstName(), user.getLastName(), user.getCellNumber(), user.isMuteNotifications(), user.getValidated().isCellNumberValidated(), user.getValidated().isEmailValidated());
        return result;
    }

    /**
     * Create an APIUser object starting from a User
     * 
     * @param user An User object
     * @return A APIUser object
     */
    public static APIUser toAPIModel(User user) {
        if(user.getUserIdentifier()==-1){
            Logger.getLogger(UserFactory.class.getName()).log(Level.SEVERE, USER_ID_ERROR);
        }
        APIUser result = new APIUser();

        result.setFirstName(user.getName().getFirstName());
        result.setLastName(user.getName().getLastName());
        result.setCellNumber(user.getCellPhone());
        result.setMuteNotifications(user.isMute());
        APIValidation validation = new APIValidation();
        validation.setCellNumberValidated(user.isCellPhoneValidated());
        validation.setEmailValidated(user.isEmailValidated());
        result.setValidated(validation);
        result.setEmail(user.getEmail());
        result.setId(String.valueOf(user.getUserIdentifier()));
        result.setLinks(getLinks(USER + user.getUserIdentifier()));
        return result;
    }

    /**
     * Build a valid User object 
     * 
     * @param userIdentifier The unique identifier of the user
     * @param email The unique email address of the user
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param cellPhone The cell number of the user
     * @param mute Whether or not the user wants notifications
     * @param cellPhoneValidated Whether or not the user has validated his cell number
     * @param emailValidated Whether or not the user has validated his email address
     * @return The User object
     */
    public static User build(int userIdentifier, String email, String firstName, String lastName, String cellPhone, boolean mute, boolean cellPhoneValidated, boolean emailValidated) {
        User user = new User(userIdentifier, email, firstName, lastName, cellPhone, mute, cellPhoneValidated, emailValidated);
        return user;
    }
}
