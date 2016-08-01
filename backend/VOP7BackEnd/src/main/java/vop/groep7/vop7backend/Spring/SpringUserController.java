package vop.groep7.vop7backend.Spring;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.bull.javamelody.MonitoredWithSpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vop.groep7.vop7backend.Controllers.UserController;
import vop.groep7.vop7backend.Exceptions.DatabaseResponseException;
import vop.groep7.vop7backend.Exceptions.ExistingResponseException;
import vop.groep7.vop7backend.Exceptions.IncorrectValidationResponseException;
import vop.groep7.vop7backend.Exceptions.InvalidInputResponseException;
import vop.groep7.vop7backend.Exceptions.NotExistingResponseException;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIVerification;
import vop.groep7.vop7backend.Models.APIModels.APIUser;
import vop.groep7.vop7backend.Models.APIModels.APIUserAdmin;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIChangePassword;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICredentials;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Backend Team
 */
@MonitoredWithSpring
@CrossOrigin
@RestController
public class SpringUserController extends SpringController {

    /**
     * Static URLs for user operations
     *
     */
    private static final String PARAM_USER = "user/{user_id}";
    private static final String BASIC_USER = "user";
    private static final String ADMIN_USER = "admin/user";
    private static final String FORGOT_PASSWORD_REQUEST = "user/forgot_password_request";
    private static final String FORGOT_PASSWORD_RESPONSE = "user/forgot_password";
    private static final String MODIFY_PASSWORD = "user/{user_id}/modify_password";
    private static final String VERIFY_USER = "/user/verify";

    private static final String BASIC_ERROR = "The database is not available at the moment.";
    private static final String EXISTS_ERROR = "The user already exists.";
    private static final String NOT_EXISTS_ERROR = "The user doesn't exist.";
    private static final String VALIDATION_FAILED = "A verification pin is not (longer) valid or the email does not exist.";

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String OLD_PASSWORD = "old_password";
    private static final String NEW_PASSWORD = "new_password";

    private static final Pattern VALID_EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private UserController userController;

    /**
     * HTTP GET method for all users
     *
     * @see APIUser
     * @return A list of user objects
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = BASIC_USER, method = RequestMethod.GET)
    public @ResponseBody
    List<APIUser> getAllUsers() throws DatabaseResponseException {
        try {
            return userController.getAPIUsers();
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringUserController.class.getName()).log(Level.SEVERE, "Could not get all users.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP GET method for all users for an admin
     *
     * @see APIUserAdmin
     * @return A list of user objects
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = ADMIN_USER, method = RequestMethod.GET)
    public @ResponseBody
    List<APIUserAdmin> getAllUsersAdmin() throws DatabaseResponseException {
        try {
            return userController.getAPIUsersAdmin();
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringUserController.class.getName()).log(Level.SEVERE, "Could not get all users for an admin.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP POST method for users
     *
     * @param user The user we want to create
     * @see APIUser
     * @return The user object that has been created
     * @throws ExistingResponseException Thrown if the user can't be created
     * because it exists already.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = BASIC_USER, method = RequestMethod.POST)
    public @ResponseBody
    APIUser createUser(@RequestBody APIUser user) throws ExistingResponseException, DatabaseResponseException {
        checkUserInput(user, true);
        try {
            APIUser u = userController.createAPIUser(user);
            if (u == null) {
                throw new ExistingResponseException(EXISTS_ERROR);
            } else {
                return u;
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringUserController.class.getName()).log(Level.SEVERE, "Could not create users.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP GET method for one user
     *
     * @param userId the Id of a user
     * @see APIUser
     * @return The user object with the corresponding Id
     * @throws NotExistingResponseException This is thrown when no user can be
     * found.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = PARAM_USER, method = RequestMethod.GET)
    public @ResponseBody
    APIUser getUser(@PathVariable("user_id") int userId) throws NotExistingResponseException, DatabaseResponseException {
        try {
            APIUser user = userController.getAPIUser(userId);
            if (user == null) {
                throw new NotExistingResponseException(NOT_EXISTS_ERROR);
            }
            return user;
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringUserController.class.getName()).log(Level.SEVERE, "Could not get user.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP PUT method for users
     *
     * @param userId the Id of a user
     * @param user The user we want to modify
     * @see APIUser
     * @return The user object that has been modified
     * @throws ExistingResponseException Thrown if the user can't be created
     * because it exists already
     * @throws NotExistingResponseException This is thrown when no user can be
     * found.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = PARAM_USER, method = RequestMethod.PUT)
    public @ResponseBody
    APIUser modifyUser(@PathVariable("user_id") int userId, @RequestBody APIUser user) throws ExistingResponseException, NotExistingResponseException, DatabaseResponseException {
        checkUserInput(user, false);
        try {
            return userController.modifyAPIUser(userId, user);
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringUserController.class.getName()).log(Level.SEVERE, "Could not modify users.", ex);
            try {
                APIUser u = userController.getAPIUser(userId);
                if (u != null) {
                    throw new ExistingResponseException(EXISTS_ERROR);
                } else {
                    throw new NotExistingResponseException(NOT_EXISTS_ERROR);
                }
            } catch (DataAccessException ex1) {
                Logger.getLogger(SpringUserController.class.getName()).log(Level.SEVERE, "Could not modify or get user.", ex1);
                throw new DatabaseResponseException(BASIC_ERROR);
            }
        }
    }

    /**
     * HTTP DELETE method for users
     *
     * @param userId the Id of a user
     * @throws NotExistingResponseException This is thrown when no user can be
     * found.
     * @throws DatabaseResponseException This is thrown when the connection to
     * the database fails.
     */
    @RequestMapping(value = PARAM_USER, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("user_id") int userId) throws NotExistingResponseException, DatabaseResponseException {
        try {
            if (!userController.deleteAPIUser(userId)) {
                throw new NotExistingResponseException(NOT_EXISTS_ERROR);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringUserController.class.getName()).log(Level.SEVERE, "Could not delete user.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP POST method for a user that forgot his password
     *
     * @param credentials A credentials object containing the email of the user
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = FORGOT_PASSWORD_REQUEST, method = RequestMethod.POST)
    public void forgotPasswordRequest(@RequestBody APICredentials credentials) {
        if (credentials.getEmail() == null) {
            List<String> invalidFields = new ArrayList<>();
            invalidFields.add(EMAIL);
            throw new InvalidInputResponseException(invalidFields);
        } else {
            try {
                userController.forgotPasswordRequest(credentials);
            } catch (DataAccessException ex) {
                Logger.getLogger(SpringUserController.class.getName()).log(Level.SEVERE, "Could not initiate the password reset flow", ex);
                throw new DatabaseResponseException(BASIC_ERROR);
            }
        }
    }

    /**
     * HTTP POST method for a user that forgot his password
     *
     * @param token The unique token
     * @param credentials The new credentials of the user
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = FORGOT_PASSWORD_RESPONSE, method = RequestMethod.POST)
    public void forgotPasswordResponse(@RequestParam(value = "password_token", required = true) String token, @RequestBody APICredentials credentials) {
        if (credentials.getPassword() == null) {
            List<String> invalidFields = new ArrayList<>();
            invalidFields.add(PASSWORD);
            throw new InvalidInputResponseException(invalidFields);
        } else if (!userController.forgotPasswordResponse(token, credentials)) {
            throw new IncorrectValidationResponseException(VALIDATION_FAILED, new ArrayList<String>() {
                {
                    add("password_token");
                }
            });
        }
    }

    /**
     * HTTP POST method for a user that wants to change his password
     *
     * @param userId the Id of a user
     * @param credentials The credentials in which the old and new password can
     * be found
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = MODIFY_PASSWORD, method = RequestMethod.POST)
    public void modifyPassword(@PathVariable("user_id") int userId, @RequestBody APIChangePassword credentials) {
        try {
            if (credentials.getOldPassword() == null
                    || credentials.getNewPassword() == null
                    || !userController.modifyPassword(userId, credentials.getOldPassword(), credentials.getNewPassword())) {
                List<String> invalidFields = new ArrayList<>();
                invalidFields.add(OLD_PASSWORD);
                invalidFields.add(NEW_PASSWORD);
                throw new InvalidInputResponseException(invalidFields);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringUserController.class.getName()).log(Level.SEVERE, "Could not modify password.", ex);
            try {
                APIUser u = userController.getAPIUser(userId);
                if (u == null) {
                    throw new NotExistingResponseException(NOT_EXISTS_ERROR);
                } else {
                    List<String> invalidFields = new ArrayList<>();
                    invalidFields.add(OLD_PASSWORD);
                    invalidFields.add(NEW_PASSWORD);
                    throw new InvalidInputResponseException(invalidFields);
                }
            } catch (DataAccessException ex1) {
                Logger.getLogger(SpringUserController.class.getName()).log(Level.SEVERE, "Could not modify password or get user.", ex1);
                throw new DatabaseResponseException(BASIC_ERROR);
            }
        }
    }

    /**
     * HTTP PUT method for verifying a user
     *
     * @param verification PIN codes which the user wishes to verify
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = VERIFY_USER, method = RequestMethod.POST)
    public void verifyUser(@RequestBody APIVerification verification
    ) {
        if (!userController.verify(verification)) {
            throw new IncorrectValidationResponseException(VALIDATION_FAILED, new ArrayList<String>() {
                {
                    add(EMAIL);
                    add("email_verification_pin");
                }
            });
        }
    }

    private void checkUserInput(APIUser u, boolean create) {
        boolean valid = true;
        List<String> invalidFields = new ArrayList<>();
        //Check email only if creating an account. While editing, it won't be checked because it can't be edited
        if (create && !VALID_EMAIL_REGEX.matcher(u.getEmail()).find()) {
            valid = false;
            invalidFields.add(EMAIL);
        }

        if (!valid) {
            throw new InvalidInputResponseException(invalidFields);
        }
    }
}
