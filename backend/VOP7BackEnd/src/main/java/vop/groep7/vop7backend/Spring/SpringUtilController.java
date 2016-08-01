package vop.groep7.vop7backend.Spring;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.bull.javamelody.MonitoredWithSpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vop.groep7.vop7backend.Controllers.UtilController;
import vop.groep7.vop7backend.Exceptions.AuthenticationFailedResponseException;
import vop.groep7.vop7backend.Exceptions.DatabaseResponseException;
import vop.groep7.vop7backend.Exceptions.InvalidInputResponseException;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIAccessToken;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICode;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICredentials;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIRefreshToken;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIRole;
import vop.groep7.vop7backend.Models.APIModels.Datadump.APIDataDump;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Backend Team
 */
@MonitoredWithSpring
@CrossOrigin
@RestController
public class SpringUtilController extends SpringController {

    /**
     * Static URLs for operations
     *
     */
    private static final String BASIC_TRANSPORT = "transportationtype";
    private static final String BASIC_ROLE = "role";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN_REGULAR = "refresh_token/regular";
    private static final String REFRESH_TOKEN_LOGOUT = "refresh_token/logout";
    private static final String REFRESH_TOKEN_FACEBOOK = "refresh_token/facebook";
    private static final String DATA_DUMP = "admin/data_dump";
    private static final String CHANGE_PERMISSION = "admin/permission/{user_id}";

    private static final String TOKEN = "token";
    
    private static final String BASIC_ERROR = "The database is not available at the moment.";

    @Autowired
    private UtilController utilController;

    /**
     * HTTP GET method for all transportation types
     *
     * @see APITransport
     * @return A list of transportation objects
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = BASIC_TRANSPORT, method = RequestMethod.GET)
    public @ResponseBody
    List<APITransport> getAllTransportationTypes() {
        return utilController.getTransportationTypes();
    }

    /**
     * HTTP GET method for all roles
     *
     * @see APIRole
     * @return A list of roles
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = BASIC_ROLE, method = RequestMethod.GET)
    public @ResponseBody
    List<APIRole> getAllRoles() {
        return utilController.getRoles();
    }

    /**
     * HTTP POST method for login
     *
     * @param credentials The email and password of the user
     * @throws DataAccessException This is thrown when the database fails.
     * @see APIRefreshToken
     * @return A refresh token
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = REFRESH_TOKEN_REGULAR, method = RequestMethod.POST)
    public @ResponseBody
    APIRefreshToken getRefreshTokenRegular(@RequestBody APICredentials credentials) throws DataAccessException {
        APIRefreshToken refreshToken = utilController.getRefreshTokenRegular(credentials);
        if (refreshToken != null) {
            return refreshToken;
        } else {
            throw new AuthenticationFailedResponseException("The email and/or password were incorrect.", new ArrayList<String>() {
                {
                    add("email");
                    add("password");
                }
            });
        }
    }

    /**
     * HTTP POST method for logout
     *
     * @param refreshToken the refresh token of the user
     * @see APIRefreshToken
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = REFRESH_TOKEN_LOGOUT, method = RequestMethod.POST)
    public void logout(@RequestBody APIRefreshToken refreshToken) {
        List<String> invalidFields = new ArrayList<>();
        try {
            if (refreshToken.getToken() != null) {
                utilController.logout(refreshToken);
            } else {
                invalidFields.add(TOKEN);
                throw new InvalidInputResponseException(invalidFields);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringRouteController.class.getName()).log(Level.SEVERE, "Could not log out.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP POST method for changing permissions
     *
     * @param userId The user who will have his permissions changed
     * @param role The new role of the user
     * @see APIRole
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = CHANGE_PERMISSION, method = RequestMethod.POST)
    public void changePermission(@PathVariable("user_id") int userId, @RequestBody APIRole role) {
        try {
            utilController.changePermission(userId, role);
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringUtilController.class.getName()).log(Level.SEVERE, "Could not change permissions.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP POST method for login in with facebook
     *
     * @param code The authorization code received from facebook
     * @see APIRefreshToken
     * @return A refresh token
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = REFRESH_TOKEN_FACEBOOK, method = RequestMethod.POST)
    public @ResponseBody
    APIRefreshToken getRefreshTokenFacebook(@RequestBody APICode code) {
        return utilController.getRefreshTokenFacebook(code);
    }

    /**
     * HTTP POST method to create a new access token
     *
     * @param refreshToken The refresh token obtained from POST'ing on
     * /refresh_token/{something}
     * @see APIAccessToken
     * @return An access token
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = ACCESS_TOKEN, method = RequestMethod.POST)
    public @ResponseBody
    APIAccessToken getAccessToken(@RequestBody APIRefreshToken refreshToken) {
        try {
            APIAccessToken accessToken = utilController.getAccessToken(refreshToken);
            if (accessToken != null) {
                return accessToken;
            } else {
                throw new AuthenticationFailedResponseException("The refresh token was invalid", new ArrayList<String>() {
                    {
                        add(TOKEN);
                    }
                });
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SpringRouteController.class.getName()).log(Level.SEVERE, "Could not get access token.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }

    /**
     * HTTP POST method for a data dump
     *
     * @see APIDataDump
     * @return A data dump
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = DATA_DUMP, method = RequestMethod.POST)
    public @ResponseBody
    APIDataDump getDataDump() {
        try {
            return utilController.getDataDump();
        } catch (DataAccessException | IllegalAccessException ex) {
            Logger.getLogger(SpringUserController.class.getName()).log(Level.SEVERE, "Could not get datadump.", ex);
            throw new DatabaseResponseException(BASIC_ERROR);
        }
    }
}
