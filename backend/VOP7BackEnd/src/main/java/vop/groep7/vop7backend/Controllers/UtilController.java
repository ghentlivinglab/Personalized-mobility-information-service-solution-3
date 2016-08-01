package vop.groep7.vop7backend.Controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APIPOI;
import vop.groep7.vop7backend.Models.APIModels.APIRoute;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIAccessToken;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICode;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICredentials;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIRefreshToken;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIRole;
import vop.groep7.vop7backend.Models.APIModels.Datadump.APIDataDump;
import vop.groep7.vop7backend.Models.APIModels.Datadump.APITravelDump;
import vop.groep7.vop7backend.Models.APIModels.Datadump.APIUserDump;
import vop.groep7.vop7backend.Models.Domain.Credentials;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.Models.Domain.Password;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.Security.TokenManager;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.EventFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.POIFactory;
import vop.groep7.vop7backend.factories.RouteFactory;
import vop.groep7.vop7backend.factories.TravelFactory;
import vop.groep7.vop7backend.factories.UserCredentialsFactory;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Backend Team
 */
public class UtilController extends Controller {

    @Autowired
    private TokenManager tokenManager;
    
    private final DateFormat isoDateStringFormat;

    /**
     * Create the util controller and initialize the iso date string format
     * for the expiration time of the access token
     */
    public UtilController() {
        isoDateStringFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    }

    /**
     * Get a list of all API transportation types
     *
     * @return list of API transportation types
     * @see APITransport
     */
    public List<APITransport> getTransportationTypes() {
        return new ArrayList<>(Arrays.asList(APITransport.values()));
    }

    /**
     * Get a refresh token
     *
     * @param credentials the credentials of the user requesting the refresh
     * token
     * @return a refresh token if the credentials were valid, else null
     * @throws DataAccessException This is thrown when the connection to the
     * database fails.
     */
    public APIRefreshToken getRefreshTokenRegular(APICredentials credentials) throws DataAccessException {
        boolean success = false;
        APIRefreshToken token = null;
        // check if ther is a user with this email
        if (getUserDAO().userExists(credentials.getEmail())) {
            // get credentials for this account
            Credentials correctCredentials = getUserDAO().getCredentials(credentials.getEmail());
            // get expected password for this account
            Password expectedPassword = correctCredentials.getPassword();
            // get password of this login attempt
            Password loginPassword = new Password(credentials.getPassword());

            // check if passwords match
            if (expectedPassword.comparePasswords(loginPassword)) {
                success = true;
                // create refresh token
                token = new APIRefreshToken();
                token.setUserId(String.valueOf(correctCredentials.getUserIdentifier()));
                token.setRole(APIRole.valueOf(correctCredentials.getRole()));
                token.setUserUrl(UserFactory.getLinks("user/" + token.getUserId())[0].getHref());
                token.setToken(tokenManager.createRefreshToken(correctCredentials.getUserIdentifier()));
            }
        }
        if (success) {
            return token;
        } else {
            return null;
        }
    }

    /**
     * Logout and remove refresh token
     *
     * @param refreshToken a valid refresh token
     * @throws DataAccessException This is thrown when the connection to the
     * database fails.
     */
    public void logout(APIRefreshToken refreshToken) throws DataAccessException {
        tokenManager.deleteRefreshToken(refreshToken.getToken());
    }

    /**
     * Get an access token
     *
     * @param refreshToken a valid refresh token
     * @return an access token
     * @throws DataAccessException This is thrown when the connection to the
     * database fails.
     */
    public APIAccessToken getAccessToken(APIRefreshToken refreshToken) throws DataAccessException {
        APIAccessToken apiAccessToken = new APIAccessToken();
        User u = tokenManager.validateRefreshToken(refreshToken.getToken());
        if (u == null) {
            // refresh token invalid
            return null;
        }
        // create access token
        String accessToken = tokenManager.createAccessToken(u);
        apiAccessToken.setToken(accessToken);
        // get expiration time and set as iso date string
        long expiration = Long.valueOf(accessToken.split(":")[3]);
        apiAccessToken.setExp(isoDateStringFormat.format(new Date(expiration)));
        return apiAccessToken;
    }
    
    /**
     * Get a refresh token for login by facebook account
     *
     * @param code The authorization code received from facebook
     * @return a refresh token
     */
    public APIRefreshToken getRefreshTokenFacebook(APICode code) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Get a data dump of everything in the database. This uses data dump
     * objects to keep things ordered.
     *
     * @return An APIDataDump object containing the entire database
     * @throws DataAccessException This is thrown when the connection to the
     * database fails.
     * @throws IllegalAccessException This is thrown when illegal id's are being
     * used.
     */
    public APIDataDump getDataDump() throws DataAccessException, IllegalAccessException {
        APIDataDump result = new APIDataDump();

        // get users from db
        List<User> users = getUserDAO().getUsers();
        APIUserDump[] usersDump = new APIUserDump[users.size()];

        for (int i = 0; i < users.size(); i++) {
            APIUserDump userDump = new APIUserDump();
            userDump.setUser(UserFactory.toAPIModel(users.get(i)));

            // get poi's from db
            List<POI> pois = getPOIDAO().getPOIs(users.get(i).getUserIdentifier());
            APIPOI[] poisDump = new APIPOI[pois.size()];
            for (int j = 0; j < pois.size(); j++) {
                poisDump[j] = POIFactory.toAPIModel(pois.get(j), users.get(i).getUserIdentifier());
            }
            userDump.setPointsOfInterest(poisDump);

            // get travels from db
            List<Travel> travels = getTravelDAO().getTravels(users.get(i).getUserIdentifier());
            APITravelDump[] travelsDump = new APITravelDump[travels.size()];
            for (int j = 0; j < travels.size(); j++) {
                APITravelDump travelDump = new APITravelDump();
                travelDump.setTravel(TravelFactory.toAPIModel(travels.get(j), users.get(i).getUserIdentifier()));

                List<Route> routes = getRouteDAO().getRoutes(users.get(i).getUserIdentifier(), travels.get(j).getTravelIdentifier());
                APIRoute[] routesDump = new APIRoute[routes.size()];
                for (int k = 0; k < routes.size(); k++) {
                    routesDump[k] = RouteFactory.toAPIModel(routes.get(k), users.get(i).getUserIdentifier(), travels.get(j).getTravelIdentifier());
                }
                travelDump.setRoutes(routesDump);

                travelsDump[j] = travelDump;
            }

            userDump.setTravels(travelsDump);
            usersDump[i] = userDump;
        }

        result.setUsers(usersDump);

        // get event types from db
        List<EventType> eventTypes = getEventDAO().getEventTypes();
        APIEventType[] eventTypesDump = new APIEventType[eventTypes.size()];
        for (int i = 0; i < eventTypes.size(); i++) {
            eventTypesDump[i] = EventTypeFactory.toAPIModel(eventTypes.get(i));
        }

        result.setEventTypes(eventTypesDump);

        // get events from db
        List<Event> events = getEventDAO().getEvents();
        APIEvent[] eventsDump = new APIEvent[events.size()];
        for (int i = 0; i < events.size(); i++) {
            eventsDump[i] = EventFactory.toAPIModel(events.get(i));
        }

        result.setEvents(eventsDump);

        return result;
    }

    /**
     * Get a list of all API roles
     *
     * @return list of API roles
     * @see APIRole
     */
    public List<APIRole> getRoles() {
        return new ArrayList<>(Arrays.asList(APIRole.values()));
    }

    /**
     * Change the Role of a specific user
     *
     * @param userId The iD of the user
     * @param role The new Role of the user
     * @throws DataAccessException This is thrown when the connection to the
     * database fails.
     * @see APIRole
     */
    public void changePermission(int userId, APIRole role) throws DataAccessException {
        Credentials credentials = getUserDAO().getCredentials(userId);
        Credentials newCredentials = UserCredentialsFactory.build(credentials.getPassword().getBytes(), userId, role.name());

        getUserDAO().modifyCredentials(newCredentials);

        // delete token of user
        tokenManager.deleteRefreshToken(userId);
    }
}
