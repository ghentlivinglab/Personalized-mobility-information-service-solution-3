package vop.groep7.vop7backend.database.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.Domain.Credentials;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.UserCredentialsFactory;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Backend Team
 */
public class UserDAO extends SQLDAO {

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String CELL_NUMBER = "cell_number";
    private static final String MUTE_NOTIFICATIONS = "mute_notifications";
    private static final String VALIDATED_EMAIL = "validated_email";
    private static final String VALIDATED_CELL_NUMBER = "validated_cell_number";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";
    private static final String TOKEN = "token";
    private static final String LAST_USED = "time_last_used";
    private static final String EMAIL_PIN = "email_pin";
    private static final String EVENT_IDENTIFIER = "event_id";
    private static final String PASSWORD_RESET_PIN = "password_reset_pin";
    private static final String UPDATE_EMAIL_PIN_QUERY = "UPDATE userPins SET email_pin = ? WHERE user_id = ?";

    /**
     * Create a UserDAO using a connection to a database
     *
     * @param connection a connection object to a database
     */
    public UserDAO(Connection connection) {
        super(connection);
    }

    /**
     * Retrieve a user object from the database. Get all information about the
     * user except the specific travels and . The travel id's will suffice.
     *
     * @param id The Id of the user
     * @see User
     * @return A user object
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public User getUser(int id) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT first_name, last_name, email, cell_number, mute_notifications, validated_cell_number, validated_email FROM person WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return UserFactory.build(id, rs.getString(EMAIL), rs.getString(FIRST_NAME), rs.getString(LAST_NAME), rs.getString(CELL_NUMBER), rs.getBoolean(MUTE_NOTIFICATIONS), rs.getBoolean(VALIDATED_CELL_NUMBER), rs.getBoolean(VALIDATED_EMAIL));
                }
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get user.", ex);
            throw new DataAccessException("Could not find user.", ex);
        }
    }

    /**
     * Get the credentials of a user by email
     *
     * @param email the email of the user
     * @see Credentials
     * @return the Credentials of the user
     * @see Credentials
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public Credentials getCredentials(String email) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT password, id, role FROM person p INNER JOIN credentials c ON (p.id = c.user_id) WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return UserCredentialsFactory.build(rs.getBytes(PASSWORD), rs.getInt(ID), rs.getString(ROLE));
                }
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get user credentials from email.", ex);
            throw new DataAccessException("Could not get credentials from email.", ex);
        }
    }

    /**
     * Get the credentials of a user by userId
     *
     * @param userId the identifier of the user
     * @see Credentials
     * @return the Credentials of the user
     * @see Credentials
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public Credentials getCredentials(int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT password, role FROM credentials WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return UserCredentialsFactory.build(rs.getBytes(PASSWORD), userId, rs.getString(ROLE));
                }
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get credentials from id.", ex);
            throw new DataAccessException("Could not get credentials from user identifier.", ex);
        }
    }

    /**
     * Get all the users present in the database. Get all information about the
     * user except the specific travels and POI's.
     *
     * @see User
     * @return A collection of user objects
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public List<User> getUsers() throws DataAccessException {
        List<User> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, first_name, last_name, email, cell_number, mute_notifications, validated_email, validated_cell_number FROM person ")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = UserFactory.build(rs.getInt(ID), rs.getString(EMAIL), rs.getString(FIRST_NAME), rs.getString(LAST_NAME), rs.getString(CELL_NUMBER), rs.getBoolean(MUTE_NOTIFICATIONS), rs.getBoolean(VALIDATED_CELL_NUMBER), rs.getBoolean(VALIDATED_EMAIL));
                    result.add(u);
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get all users.", ex);
            throw new DataAccessException("Could not search for users.", ex);
        }
    }

    /**
     * Get a list with all user identifiers
     *
     * @return list of all user identifiers
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public List<Integer> getUserIds() throws DataAccessException {
        List<Integer> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id FROM person")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getInt(ID));
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get all ids.", ex);
            throw new DataAccessException("Could not search for the user ids.", ex);
        }
    }

    /**
     * Check if user already exists in the database
     *
     * @param email the email of the user
     * @return True if user already exists, else false
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean userExists(String email) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id FROM person WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not check user.", ex);
            throw new DataAccessException("Could not check if the user exists.", ex);
        }
    }

    /**
     * Check if userId is already used in the database
     *
     * @param userId the identifier to be checked
     * @return True if userId is already used, else false
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean userIdExists(int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id FROM person where id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not check user id.", ex);
            throw new DataAccessException("Could not check if the user id exists.", ex);
        }
    }

    /**
     * Create the credentials of a user in the database
     *
     * @param credentials the credentials to be created
     * @see Credentials
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void createCredentials(Credentials credentials) throws DataAccessException {
        createCredentials(credentials.getPassword().getBytes(), credentials.getUserIdentifier(), credentials.getRole());
    }

    private void createCredentials(byte[] password, int userId, String role) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO credentials (password, user_id, role) VALUES (?,?,?)")) {
            ps.setBytes(1, password);
            ps.setInt(2, userId);
            ps.setString(3, role);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not create user credentials.", ex);
            throw new DataAccessException("Could not create credentials", ex);
        }
    }

    /**
     * Insert a user into the database
     *
     * @param id The Id of the user
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param email The email of the user
     * @param cellNumber The cell phone number of the user
     * @param muteNotifications Whether the user wants to mute notifications or
     * not
     * @param validatedCellNumber Whether the cell number is verified
     * @param validatedEmail Whether the email is verified
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private void createUser(int id, String email, String firstName, String lastName, String cellNumber, boolean muteNotifications, boolean validatedCellNumber, boolean validatedEmail)
            throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO person (id, first_name, last_name, email, cell_number, mute_notifications, validated_cell_number, validated_email) VALUES (?,?,?,?,?,?,?,?)")) {
            ps.setInt(1, id);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, email);
            ps.setString(5, cellNumber);
            ps.setBoolean(6, muteNotifications);
            ps.setBoolean(7, validatedCellNumber);
            ps.setBoolean(8, validatedEmail);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not create user.", ex);
            throw new DataAccessException("Could not create user", ex);
        }
    }

    /**
     * Insert a user into the database. Insert all basic information including
     * POI's. No travels yet.
     *
     * @param user A user object representing the new user
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void createUser(User user) throws DataAccessException {
        // create user with id
        createUser(user.getUserIdentifier(), user.getEmail(), user.getName().getFirstName(), user.getName().getLastName(), user.getCellPhone(), user.isMute(), user.isCellPhoneValidated(), user.isEmailValidated());
    }

    /**
     * Delete a user from the database. Delete everything!
     *
     * @param id The Id of the user
     * @return If the deletions has succeeded
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deleteUser(int id) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM person WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not delete user.", ex);
            throw new DataAccessException("Could not delete user", ex);
        }
    }

    /**
     * Delete the credentials of a user in the db
     *
     * @param userId the identifier of the user to be deleted
     *
     * @return True if the credentials are found and deleted, else false
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deleteCredentials(int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM credentials WHERE user_id = ?")) {
            ps.setInt(1, userId);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not delete user credentials.", ex);
            throw new DataAccessException("Could not delete credentials", ex);
        }
    }

    /**
     * Modify a user from the database. Modify everything except the travels.
     *
     * @param id The Id of the user
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param cellNumber The cell phone number of the user
     * @param muteNotifications Whether the user wants to mute notifications or
     * not
     * @param validatedCellNumber Whether the cell number is verified
     * @param validatedEmail Whether the email is verified
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    private void modifyUser(int id, String firstName, String lastName, String cellNumber, boolean muteNotifications, boolean validatedCellNumber, boolean validatedEmail)
            throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE person SET first_name = ?, last_name = ?, cell_number = ?, mute_notifications = ?, validated_cell_number = ?, validated_email = ? WHERE id = ?")) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, cellNumber);
            ps.setBoolean(4, muteNotifications);
            ps.setBoolean(5, validatedCellNumber);
            ps.setBoolean(6, validatedEmail);
            ps.setInt(7, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not modify user.", ex);
            throw new DataAccessException("Could not modify user", ex);
        }
    }

    /**
     * Modify the credentials of a user in the database
     *
     * @param credentials The credentials to be modified
     * @see Credentials
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void modifyCredentials(Credentials credentials) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE credentials SET password = ?, role = ? WHERE user_id = ?")) {
            ps.setBytes(1, credentials.getPassword().getBytes());
            ps.setString(2, credentials.getRole());
            ps.setInt(3, credentials.getUserIdentifier());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not modify user credentials.", ex);
            throw new DataAccessException("Could not modify credentials", ex);
        }
    }

    /**
     * Modify a user from the database. Modify everything except the travels.
     *
     * @param id The Id of the user
     * @param user A user object representing the user
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void modifyUser(int id, User user) throws DataAccessException {
        modifyUser(id, user.getName().getFirstName(), user.getName().getLastName(), user.getCellPhone(), user.isMute(), user.isCellPhoneValidated(), user.isEmailValidated());
    }

    /**
     * Create the user pins for a user
     *
     * @param userId the identifier of the user
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void createUserPins(int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO userPins (user_id) VALUES (?)")) {
            ps.setInt(1, userId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not create userPins for user.", ex);
            throw new DataAccessException("Could not create userPins for user", ex);
        }
    }

    /**
     * Delete the user pins of a user
     *
     * @param userId the identifier of the user
     * @return true if deleted, else false
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deleteUserPins(int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM userPins WHERE user_id = ?")) {
            ps.setInt(1, userId);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not delete userPins from user.", ex);
            throw new DataAccessException("Could not delete userPins for user", ex);
        }
    }

    /**
     * Get the email validation pin of a user if available
     *
     * @param userId the identifier of the user
     * @return the email pin as String for the user, else null
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public String getEmailValidationPin(int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT email_pin FROM userPins WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(EMAIL_PIN);
                }
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get user email pin.", ex);
            throw new DataAccessException("Could not get email pin for user.", ex);
        }
    }

    /**
     * Save the email pin of a user in the db
     *
     * @param userId the identifier of the user
     * @param emailPin the pin to verify the email of the user
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void createEmailValidationPin(int userId, String emailPin) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                UPDATE_EMAIL_PIN_QUERY)) {
            ps.setString(1, emailPin);
            ps.setInt(2, userId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not create user email pin.", ex);
            throw new DataAccessException("Could not create user pin", ex);
        }
    }

    /**
     * Delete the email validation pin of a user from the db
     *
     * @param userId the identifier of the user
     * @return true if deleted, else false
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deleteEmailValidationPin(int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                UPDATE_EMAIL_PIN_QUERY)) {
            ps.setString(1, null);
            ps.setInt(2, userId);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not delete user email pin.", ex);
            throw new DataAccessException("Could not delete user email pin", ex);
        }
    }

    /**
     * Check if a user is already matched to an event
     *
     * @param userId The Id of a user
     * @param eventIdentifier The Id of an event
     * @return Whether a user is matched to an event
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean areMatched(int userId, int eventIdentifier) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT event_id, user_id FROM matchedEvents WHERE user_id = ? AND event_id = ?")) {
            ps.setInt(1, userId);
            ps.setInt(2, eventIdentifier);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not check if user is already matched.", ex);
            throw new DataAccessException("Could not if user is already matched.", ex);
        }
    }

    /**
     * Get the identifiers of the matched events for a user
     *
     * @param userId the identifier of the user
     * @return the ids of the matched events for the user
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public List<Integer> getMatchedEvents(int userId) throws DataAccessException {
        List<Integer> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT event_id FROM matchedEvents WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getInt(EVENT_IDENTIFIER));
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get user events.", ex);
            throw new DataAccessException("Could not get the matched events for this user.", ex);
        }
    }

    /**
     * Get the identifiers of the matched users for an event
     *
     * @param eventId the identifier of the event
     * @return the ids of the matched users for the event
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public List<Integer> getMatchedUsers(int eventId) throws DataAccessException {
        List<Integer> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT user_id FROM matchedEvents WHERE event_id = ?")) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getInt(USER_ID));
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get matched users for event.", ex);
            throw new DataAccessException("Could not get the matched users for this event.", ex);
        }
    }

    /**
     * Add a matched event to a user in the db by identifiers
     *
     * @param userId the identifier of the user
     * @param eventId the identifier of the event
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void createMatch(int userId, int eventId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO matchedEvents (user_id, event_id) VALUES (?, ?)")) {
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not create user events.", ex);
            throw new DataAccessException("Could not create matched event for user", ex);
        }
    }

    /**
     * Remove a matched event from the db
     *
     * @param eventId The identifier of the event
     * @return True if (all) deleted, else false
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deleteMatchedEvent(int eventId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM matchedEvents WHERE event_id = ?")) {
            ps.setInt(1, eventId);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not delete user events from event id.", ex);
            throw new DataAccessException("Could not delete matched event from db", ex);
        }
    }

    /**
     * Remove a match from the db
     *
     * @param eventId The identifier of the event
     * @param userId The identifier of the user
     * @return True if deleted, else false
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deleteMatch(int eventId, int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM matchedEvents WHERE event_id = ? AND user_id = ?")) {
            ps.setInt(1, eventId);
            ps.setInt(2, userId);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not delete match.", ex);
            throw new DataAccessException("Could not delete match from db", ex);
        }
    }

    /**
     * Remove the matched events for a user from the db
     *
     * @param userId the identifier of the user
     * @return true if all deleted, else false
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deleteMatchedEvents(int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM matchedEvents WHERE user_id = ?")) {
            ps.setInt(1, userId);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not delete user events from user id.", ex);
            throw new DataAccessException("Could not delete matched events of user from db", ex);
        }
    }

    /**
     * Create a password reset pin
     *
     * @param userId The Id of a user
     * @param token The pin to reset a password
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void createPasswordResetPin(int userId, String token) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE userPins SET password_reset_pin = ? WHERE user_id = ? ")) {
            ps.setString(1, token);
            ps.setInt(2, userId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could create password reset pin.", ex);
            throw new DataAccessException("Could not create reset pin", ex);
        }
    }

    /**
     * Delete a password reset pin
     *
     * @param userId The Id of a user
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void deletePasswordResetPin(int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE userPins SET password_reset_pin = ? WHERE user_id = ?")) {
            ps.setString(1, null);
            ps.setInt(2, userId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could delete password reset pin.", ex);
            throw new DataAccessException("Could not delete reset pin", ex);
        }
    }

    /**
     * Get the email pin of a user if available
     *
     * @param userId the identifier of the user
     * @return the email pin as String for the user, else null
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public String getPasswordResetPin(int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT password_reset_pin FROM userPins WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(PASSWORD_RESET_PIN);
                }
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get password reset pin.", ex);
            throw new DataAccessException("Could not get password pin for user.", ex);
        }
    }

    /**
     * Get the user Id based on a forgot password pin
     *
     * @param token The token a user got in his mailbox
     * @return The Id of the user
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public int getUserIdFromPasswordPin(String token) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT user_id FROM userPins WHERE password_reset_pin = ?")) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(USER_ID);
                }
            }
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get user id from token.", ex);
            throw new DataAccessException("Could not get id from token.", ex);
        }
    }

    /**
     * Get a map of all users and their corresponding role
     *
     * @return A list of users for an admin
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public Map<User, String> getUsersWithRole() throws DataAccessException {
        Map<User, String> result = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, first_name, last_name, email, cell_number, mute_notifications, validated_email, validated_cell_number, role FROM person p INNER JOIN credentials c ON (p.id=c.user_id)")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = UserFactory.build(rs.getInt(ID), rs.getString(EMAIL), rs.getString(FIRST_NAME), rs.getString(LAST_NAME), rs.getString(CELL_NUMBER), rs.getBoolean(MUTE_NOTIFICATIONS), rs.getBoolean(VALIDATED_CELL_NUMBER), rs.getBoolean(VALIDATED_EMAIL));
                    result.put(u, rs.getString(ROLE));
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get all users with role.", ex);
            throw new DataAccessException("Could not search for users with role.", ex);
        }
    }

    /**
     * Get the user Id from a refresh token
     *
     * @param token The refresh token
     * @return The Id of the user if the token exists
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public int getIdFromRefreshToken(String token) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT user_id FROM refreshToken WHERE token = ?")) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(USER_ID);
                }
                return -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get Id from refresh token.", ex);
            throw new DataAccessException("Could not get the Id from refresh token.", ex);
        }
    }

    /**
     * Update the last used time of a refresh token
     *
     * @param token The refresh token
     * @param userId The Id of the user
     * @param lastUsed When this refresh token was last used
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void updateRefreshToken(String token, int userId, Timestamp lastUsed) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE refreshToken SET time_last_used = ? WHERE token = ? AND user_id = ?")) {
            ps.setTimestamp(1, lastUsed);
            ps.setString(2, token);
            ps.setInt(3, userId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not update refresh token.", ex);
            throw new DataAccessException("Could not update refresh token", ex);
        }
    }

    /**
     * Create a refresh token for a user
     *
     * @param token The refresh token
     * @param userId The Id of the user
     * @param lastUsed When this refresh token was last used
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public void createRefreshToken(String token, int userId, Timestamp lastUsed) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO refreshToken(token, user_id, time_last_used) VALUES (?,?,?)")) {
            ps.setString(1, token);
            ps.setInt(2, userId);
            ps.setTimestamp(3, lastUsed);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not insert refresh token.", ex);
            throw new DataAccessException("Could not insert refresh token", ex);
        }
    }

    /**
     * Delete a refresh token based on a token
     *
     * @param token The refresh token
     * @return Whether the token has been deleted
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deleteRefreshToken(String token) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM refreshToken WHERE token = ?")) {
            ps.setString(1, token);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not delete refresh token on token.", ex);
            throw new DataAccessException("Could not delete refresh token on token", ex);
        }
    }

    /**
     * Delete a refresh token based on a user Id
     *
     * @param userId The Id of a user
     * @return Whether the token has been deleted
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public boolean deleteRefreshToken(int userId) throws DataAccessException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM refreshToken WHERE user_id = ?")) {
            ps.setInt(1, userId);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not delete refresh token on Id.", ex);
            throw new DataAccessException("Could not delete refresh token on Id", ex);
        }
    }

    /**
     * Get all refresh tokens of a user
     *
     * @param userId The Id of a user
     * @return A map of tokens and times
     * @throws DataAccessException This is thrown when the connection to the
     * database fails
     */
    public Map<String, Timestamp> getAllTokens(int userId) throws DataAccessException {
        Map<String, Timestamp> result = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT token, time_last_used FROM refreshToken WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString(TOKEN), rs.getTimestamp(LAST_USED));
                }
                return result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Could not get refresh tokens.", ex);
            throw new DataAccessException("Could not get the refresh tokens.", ex);
        }
    }
}
