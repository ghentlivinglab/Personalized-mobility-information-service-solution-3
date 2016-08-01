package vop.groep7.vop7backend.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import vop.groep7.vop7backend.AppConfig;
import vop.groep7.vop7backend.Models.APIModels.APIUser;
import vop.groep7.vop7backend.Models.APIModels.APIUserAdmin;
import vop.groep7.vop7backend.Models.APIModels.APIValidation;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICredentials;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIRole;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIVerification;
import vop.groep7.vop7backend.Models.Domain.Credentials;
import vop.groep7.vop7backend.Models.Domain.Password;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.Security.TokenManager;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.UserCredentialsFactory;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Backend Team
 */
public class UserController extends Controller {

    private static final int USER_ID_MIN = 100;
    private static final int USER_ID_MAX = 999999;
    private final Random userIdGenerator;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private Validator validator;

    @Autowired
    private MailService mailService;

    /**
     * Constructor that will set the random generator
     */
    public UserController() {
        userIdGenerator = new Random();
    }

    /**
     * Get a APIUser object based on it's Id
     *
     * @param userId Id of a user
     * @see APIUser
     * @return A user object
     * @throws DataAccessException This is thrown when no user can be found.
     */
    public APIUser getAPIUser(int userId) throws DataAccessException {
        User user = getUser(userId);
        return UserFactory.toAPIModel(user);
    }

    /**
     * Get all APIUser objects
     *
     * @see APIUser
     * @return A list of all API users
     * @throws DataAccessException This is thrown when no users can be found.
     */
    public List<APIUser> getAPIUsers() throws DataAccessException {
        // get users from db
        List<User> users = getUserDAO().getUsers();

        // D2A all users
        List<APIUser> res = new ArrayList<>();
        users.stream().forEach((u) -> {
            res.add(UserFactory.toAPIModel(u));
        });

        return res;
    }

    /**
     * Insert a APIUser object
     *
     * @param user An API user object that has to be created
     * @see APIUser
     * @return The created API user object
     * @throws DataAccessException This is thrown when no user can be created.
     */
    public APIUser createAPIUser(APIUser user) throws DataAccessException {
        int newId = getNewUserId();
        User newUser = UserFactory.create(newId, user.getEmail(), user.getFirstName(), user.getLastName(), user.getCellNumber(), user.isMuteNotifications());
        Credentials userCredentials = UserCredentialsFactory.create(user.getPassword(), newUser);

        // if user exists in db: return null
        if (getUserDAO().userExists(user.getEmail())) {
            return null;
        }

        // create user, credentials and userPins in db
        getUserDAO().createUser(newUser);
        getUserDAO().createCredentials(userCredentials);
        getUserDAO().createUserPins(newId);
        // load user into cache
        userCache.put(newId, newUser);

        // start validation process to validate user information
        validator.askForValidation(newUser);

        return UserFactory.toAPIModel(newUser);
    }

    private int getNewUserId() throws DataAccessException {
        int newId = generateUserIdentifier();
        while (getUserDAO().userIdExists(newId)) {
            newId = generateUserIdentifier();
        }
        return newId;
    }

    private int generateUserIdentifier() {
        return userIdGenerator.nextInt((USER_ID_MAX - USER_ID_MIN) + 1) + USER_ID_MIN;
    }

    private String generateUserPasswordToken() {
        return validator.getNewValidationCode();
    }

    /**
     * Delete a API user object
     *
     * @param userId Id of a user
     * @return If the deleting has succeeded
     * @throws DataAccessException This is thrown when the user can't be
     * deleted.
     */
    public boolean deleteAPIUser(int userId) throws DataAccessException {
        User user = userCache.getIfPresent(userId);
        // if user in cache: delete
        if (user != null) {
            userCache.invalidate(userId);
        }

        // remove event matches
        getUserDAO().deleteMatchedEvents(userId);
        // delete refresh token of user
        tokenManager.deleteRefreshToken(userId);
        // delete credentials of user
        getUserDAO().deleteCredentials(userId);
        // delete userPins of user
        getUserDAO().deleteUserPins(userId);

        // delete user in db
        return getUserDAO().deleteUser(userId);
    }

    /**
     * Modify a APIUser object
     *
     * @param userId Id of a user
     * @see APIUser
     * @param user A user object that has to be modified
     * @return The modified user object
     * @throws DataAccessException This is thrown when the user can't be
     * modified.
     */
    public APIUser modifyAPIUser(int userId, APIUser user) throws DataAccessException {
        User modifiedUser = UserFactory.build(Integer.valueOf(user.getId()), user.getEmail(), user.getFirstName(), user.getLastName(), user.getCellNumber(), user.isMuteNotifications(), user.getValidated().isCellNumberValidated(), user.getValidated().isEmailValidated());

        // if user in cache: invalidate
        if (userCache.getIfPresent(userId) != null) {
            userCache.invalidate(userId);
        }
        // load modified user into cache
        userCache.put(userId, modifiedUser);
        // modify user in db
        getUserDAO().modifyUser(userId, modifiedUser);

        return UserFactory.toAPIModel(modifiedUser);
    }

    /**
     * Modify the password of a user.
     *
     * @param userId The Id of a user
     * @param oldPassword The old password for verification
     * @param newPassword The new password
     * @return true, if modified, else false
     * @throws DataAccessException This is thrown when the user can't be
     * modified.
     */
    public boolean modifyPassword(int userId, String oldPassword, String newPassword) throws DataAccessException {
        Credentials credentials = getUserDAO().getCredentials(userId);
        Credentials modifiedCredentials = null;
        if (newPassword != null && credentials != null) {
            if (oldPassword != null) {
                Password old = new Password(oldPassword);
                if (!old.comparePasswords(credentials.getPassword())) {
                    return false;
                }
            }
            modifiedCredentials = UserCredentialsFactory.build(newPassword, userId, credentials.getRole());
        }

        // modify creadentials in db
        if (modifiedCredentials != null) {
            getUserDAO().modifyCredentials(modifiedCredentials);
            getUserDAO().deleteRefreshToken(userId);
            return true;
        }
        return false;
    }

    /**
     * This will be called when the user has forgotten his password and asked to
     * start the reset procedure.
     *
     * @param credentials A credentials object containing the email of a user
     * @throws DataAccessException This is thrown when the password can't be
     * reset.
     */
    public void forgotPasswordRequest(APICredentials credentials) throws DataAccessException {
        String email = credentials.getEmail();
        Credentials c = getUserDAO().getCredentials(email);
        if (c != null) {
            int id = c.getUserIdentifier();
            String token = generateUserPasswordToken();

            getUserDAO().createPasswordResetPin(id, token);

            Thread t = new Thread() {
                @Override
                public void run() {
                    String message = AppConfig.getApplicationProperty("message.forgot.password.email.main")
                            + AppConfig.getApplicationProperty("message.forgot.password.email.link.base")
                            + email + "/" + token
                            + AppConfig.getApplicationProperty("message.tail") + AppConfig.getApplicationProperty("message.footer");
                    mailService.sendMailToOne(email, AppConfig.getApplicationProperty("message.forgot.password.email.subject"), message);
                }
            };
            t.start();
        }
    }

    /**
     * This will be called when the user has forgotten his password and has
     * received a mail to finish the reset procedure.
     *
     * @param token The unique token
     * @param credentials The new credentials of the user
     * @return Whether the procedure was successful or not
     */
    public boolean forgotPasswordResponse(String token, APICredentials credentials) {
        boolean reset = false;

        // get reset password pin and verify with pin in db
        try {
            int id = getUserDAO().getUserIdFromPasswordPin(token);

            Credentials oldCredentials = getUserDAO().getCredentials(id);

            if (oldCredentials == null) {
                Logger.getLogger(UserController.class
                        .getName()).log(Level.SEVERE, "Credentials could not be found.");
                return false;
            }

            String expectedPin = getUserDAO().getPasswordResetPin(id);
            if (expectedPin != null && Objects.equals(expectedPin, token)) {
                modifyPassword(id, null, credentials.getPassword());
                reset = true;
                getUserDAO().deletePasswordResetPin(id);

            }
        } catch (DataAccessException ex) {
            Logger.getLogger(UserController.class
                    .getName()).log(Level.SEVERE, "Something went wrong while resetting the password of a user", ex);
        }

        return reset;
    }

    /**
     * Verifies the codes entered by the user to validate his email address
     * and/or cell phone number
     *
     * @param verification An APIVerification object
     * @return true if verified, else false
     * @see APIVerification
     */
    public boolean verify(APIVerification verification) {
        boolean verified = false;
        // get email pin and verify with pin in db
        try {
            Credentials c = getUserDAO().getCredentials(verification.getEmail());
            if (c != null) {
                int userId = c.getUserIdentifier();
                String expectedEmailPin = getUserDAO().getEmailValidationPin(userId);
                String emailPin = verification.getEmailVerificationPin();
                // check if pin is correct
                if (expectedEmailPin != null && Objects.equals(expectedEmailPin, emailPin)) {
                    // set email as validated in user
                    APIUser u = getAPIUser(userId);
                    APIValidation v = new APIValidation();
                    v.setEmailValidated(true);
                    u.setValidated(v);
                    // modify user
                    modifyAPIUser(userId, u);
                    verified = true;
                    // delete the email pin
                    getUserDAO().deleteEmailValidationPin(userId);
                }
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(UserController.class
                    .getName()).log(Level.SEVERE, "Something went wrong while verifying the email pin of a user", ex);
        }
        return verified;
    }

    /**
     * Get all APIUser objects for an admin
     *
     * @see APIUser
     * @return A list of all API users for an admin
     * @throws DataAccessException This is thrown when no users can be found.
     */
    public List<APIUserAdmin> getAPIUsersAdmin() throws DataAccessException {
        // get users from db
        Map<User, String> usersWithRole = getUserDAO().getUsersWithRole();

        // D2A all users
        List<APIUserAdmin> res = new ArrayList<>();
        usersWithRole.keySet().stream().forEach((u) -> {
            APIUserAdmin apiUA = new APIUserAdmin(UserFactory.toAPIModel(u));
            apiUA.setRole(APIRole.valueOf(usersWithRole.get(u)));
            res.add(apiUA);
        });

        return res;
    }

}
