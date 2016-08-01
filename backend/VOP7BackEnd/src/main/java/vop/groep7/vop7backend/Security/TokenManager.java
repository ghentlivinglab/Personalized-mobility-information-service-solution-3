package vop.groep7.vop7backend.Security;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import vop.groep7.vop7backend.AppConfig;
import vop.groep7.vop7backend.Controllers.UserController;
import vop.groep7.vop7backend.Exceptions.NotExistingResponseException;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.UserDAO;

/**
 *
 * @author Backend Team
 */
public class TokenManager {

    private final SecureRandom randomGenerator;

    @Autowired
    private UserController userController;

    private UserDAO userDAO;

    private static Double MAGIC_NUMBER;
    private static long TOKEN_EXPIRATION;

    /**
     * Create the token manager and set the basic expiring time
     */
    public TokenManager() {
        randomGenerator = new SecureRandom();
        TOKEN_EXPIRATION = 1 * 3600 * 1000;
    }

    /**
     * Create a refresh token for a specific user
     *
     * @param userId The Id of user
     * @return A refresh token
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public String createRefreshToken(int userId) throws DataAccessException {
        String token = new BigInteger(130, randomGenerator).toString(32).toUpperCase();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Map<String, Timestamp> tokens = getUserDAO().getAllTokens(userId);
        if (tokens.size() >= Integer.valueOf(AppConfig.getApplicationProperty("refresh.max"))) {
            String oldestToken = getOldestToken(tokens);
            getUserDAO().deleteRefreshToken(oldestToken);
        }
        getUserDAO().createRefreshToken(token, userId, time);
        return token;
    }

    private String getOldestToken(Map<String, Timestamp> tokens) {
        Comparator<String> comparator = new ValueComparator(tokens);
        TreeMap<String, Timestamp> result = new TreeMap<>(comparator);
        result.putAll(tokens);

        return result.firstKey();
    }

    private class ValueComparator implements Comparator<String> {

        HashMap<String, Timestamp> map = new HashMap<>();

        /**
         * Create a comparator to find the oldest timestamp
         * 
         * @param map the map with values to be compared
         */
        public ValueComparator(Map<String, Timestamp> map) {
            this.map.putAll(map);
        }

        @Override
        public int compare(String s1, String s2) {
            return map.get(s1).compareTo(map.get(s2));
        }
    }

    private UserDAO getUserDAO() throws DataAccessException {
        if (userDAO == null) {
            userDAO = AppConfig.getDataAccessProvider().getUserDataAccessContext().getUserDAO();
        }
        return userDAO;
    }

    /**
     * Remove a refresh token from the in memory token store
     *
     * @param refreshToken The refresh token that has to be removed
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void deleteRefreshToken(String refreshToken) throws DataAccessException {
        getUserDAO().deleteRefreshToken(refreshToken);
    }

    /**
     * Remove a refresh token from the in memory token store
     *
     * @param userId The id of a user
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void deleteRefreshToken(int userId) throws DataAccessException {
        getUserDAO().deleteRefreshToken(userId);
    }

    /**
     * Validate a refresh token
     *
     * @param refreshToken The refresh token
     * @return The User to which the token belongs
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public User validateRefreshToken(String refreshToken) throws DataAccessException {
        int userId = getUserDAO().getIdFromRefreshToken(refreshToken);
        if (userId == -1) {
            return null;
        } else {
            try {
                Timestamp time = new Timestamp(System.currentTimeMillis());
                getUserDAO().updateRefreshToken(refreshToken, userId, time);
                return userController.getUser(userId);
            } catch (DataAccessException ex) {
                Logger.getLogger(TokenManager.class.getName()).log(Level.SEVERE, "User does not exist.", ex);
                return null;
            }
        }
    }

    /**
     * Create an access token
     *
     * @param u The user who wants to create an access token
     * @return The access token
     */
    public String createAccessToken(User u) {
        long expirationTime = getExpirationTime();
        String token = "token:" + u.getUserIdentifier() + ":";
        token += calculateMagicKey(u, expirationTime);
        token += ":" + String.valueOf(expirationTime);
        return token;
    }

    /**
     * Validate an access token
     *
     * @param token The token that has to be validated
     * @return Whether the token is valid or not
     */
    public boolean validateAccessToken(String token) {
        if (token == null) {
            return false;
        }
        try {
            String[] parts = token.split(":");
            if (parts.length != 4) {
                return false;
            }
            int userId = Integer.valueOf(parts[1]);
            User u = userController.getUser(userId);
            long expirationTime = Long.valueOf(parts[3]);
            String value = calculateMagicKey(u, expirationTime);

            // token is valid if user is logged in, if hash is correct and if token is not expired
            return value.equals(parts[2]) && System.currentTimeMillis() <= expirationTime;
        } catch (NumberFormatException | DataAccessException | NotExistingResponseException ex) {
            Logger.getLogger(TokenManager.class.getName()).log(Level.SEVERE, "Token was invalid", ex);
            return false;
        }
    }

    private String calculateMagicKey(User u, long expirationTime) {
        String value = u.getEmail() + getMagicNumber() + expirationTime;
        byte[] hash = Hasher.getInstance().hash(value);
        if (hash.length != 0) {
            return new HexBinaryAdapter().marshal(hash);
        } else {
            return null;
        }
    }

    private double getMagicNumber() {
        if (MAGIC_NUMBER == null) {
            MAGIC_NUMBER = new SecureRandom().nextDouble();
        }
        return MAGIC_NUMBER;
    }

    private long getExpirationTime() {
        return System.currentTimeMillis() + TOKEN_EXPIRATION;
    }
}
