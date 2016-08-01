package Database.sql;

import static Other.TestUtils.compareUsers;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.sql.DataSource;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import vop.groep7.vop7backend.Models.Domain.Credentials;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.UserDAO;
import vop.groep7.vop7backend.factories.UserCredentialsFactory;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Jonas Van Wilder
 * @author Nick De Smedt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/testContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
public class UserDAOIT {

    private UserDAO userDAO;

    private EmbeddedDatabase db;

    @Autowired
    private DataSource dataSource;

    private List<User> expectedUsers;
    private User expectedUser;
    private Credentials expectedCredentials;
    private User otherUser;
    private Credentials otherUserCredentials;

    private List<Integer> expectedUserMatchedEvents;

    public UserDAOIT() {
        expectedUsers = new ArrayList<>();
    }

    @Before
    public void setUp() throws SQLException, DataAccessException {
        // setup UserDAO
        userDAO = new UserDAO(dataSource.getConnection());
        // setup database
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("/testdb_schema.sql")
                .addScript("/testdb_triggers.sql")
                .addScript("/testdb_data.sql")
                .build();

        expectedUsers = new ArrayList<>();
        // create expected user for getUser
        createExpectedUser();
        // create expected credentials for getCredentials
        createExpectedCredentials();
        // create expected users for getUsers
        createExpectedUsers();
        // create modified user for modifyUser/createUser
        createUser();
    }

    private void createExpectedCredentials() {
        expectedCredentials = UserCredentialsFactory.build("test123", 4242, "ROLE_OPERATOR");
    }

    private void createExpectedUser() {
        // create expected user 1
        expectedUser = UserFactory.build(4242, "Liam.Vermeir1@hotmail.com", "Liam", "Vermeir", "+32498047339", true, true, true);
        expectedUserMatchedEvents = new ArrayList<>();
        expectedUserMatchedEvents.add(1);
        expectedUserMatchedEvents.add(2);
    }

    private void createExpectedUsers() {
        // create expected user 1
        expectedUsers.add(expectedUser);

        // create expected user 2
        User expectedUser2 = UserFactory.build(9127, "Katja.Tolens@skynet.be", "Katja", "Tolens", "+32478951761", false, true, true);
        expectedUsers.add(expectedUser2);
    }

    private void createUser() {
        otherUser = UserFactory.build(55555, "George.Vermeulen@hotmail.com", "George", "Vermeulen", "+32498047339", false, true, true);
        otherUserCredentials = UserCredentialsFactory.create("test123", otherUser);
    }

    @Test
    public void getUser_shouldReturnUser() throws DataAccessException {
        User u = userDAO.getUser(4242);

        // compare with expected user
        assertNotNull(u);
        assertTrue("The id of the returned user is incorrect!", expectedUser.getUserIdentifier() == (u.getUserIdentifier()));
        assertTrue("The expected user was not returned!", compareUsers(u, expectedUser));
    }

    @Test
    public void getUser_shouldReturnNull() throws DataAccessException {
        User u = userDAO.getUser(10);
        assertNull(u);
    }

    @Test
    public void getUsers_shouldReturnListOfUsers() throws DataAccessException {
        List<User> users = userDAO.getUsers();

        // compare users with expected users
        assertNotNull(users);
        assertEquals(expectedUsers.size(), users.size());
        for (int i = 0; i < expectedUsers.size(); i++) {
            assertNotNull(users.get(i));
            assertTrue("The id of this user is incorrect!", expectedUsers.get(i).getUserIdentifier() == (users.get(i).getUserIdentifier()));
            assertTrue("The expected user was not returned!", compareUsers(users.get(i), expectedUsers.get(i)));
        }
    }

    @Test
    public void getUsersWithRole_shouldReturnListOfUsers() throws DataAccessException {
        Map<User, String> users = userDAO.getUsersWithRole();

        //We know there will be 2 users
        assertNotNull(users);
        assertEquals(2, users.size());

        users.keySet().stream().map((element) -> {
            assertNotNull(users.get(element));
            return element;
        }).forEach((element) -> {
            assertTrue(users.get(element).contains("ROLE_"));
        });
    }

    @Test
    @ExpectedDatabase("/user_created.xml")
    public void createUser_shouldCreateCorrect() throws DataAccessException {
        userDAO.createUser(otherUser);
    }

    @Test
    public void createUserCredentials_shouldCreateCorrect() throws DataAccessException {
        userDAO.createUser(otherUser);
        userDAO.createCredentials(otherUserCredentials);
        Credentials res = userDAO.getCredentials(otherUserCredentials.getUserIdentifier());

        assertTrue(Objects.equals(res.getRole(), otherUserCredentials.getRole()));
        assertTrue(Objects.equals(res.getUserIdentifier(), otherUserCredentials.getUserIdentifier()));
        assertNotNull(res.getPassword());
    }

    @Test
    @ExpectedDatabase("/user_deleted.xml")
    public void deleteUser_shouldDeleteCorrect() throws DataAccessException {
        userDAO.deleteUser(9127);
    }

    @Test
    public void deleteUser_shouldDeleteFalse() throws DataAccessException {
        assertFalse(userDAO.deleteUser(6969));
    }

    @Test
    @ExpectedDatabase("/user_modified.xml")
    public void modifyUser_shouldModifyCorrect() throws DataAccessException {
        userDAO.modifyUser(9127, otherUser);
    }

    @Test
    public void getCredentialsByEmail_shouldReturnCredentials() throws DataAccessException {
        Credentials res = userDAO.getCredentials("Liam.Vermeir1@hotmail.com");
        assertTrue(Objects.equals(res.getRole(), expectedCredentials.getRole()));
        assertTrue(Objects.equals(res.getUserIdentifier(), expectedCredentials.getUserIdentifier()));
        assertNotNull(res.getPassword());
    }

    @Test
    public void getCredentialsByEmail_shouldReturnNull() throws DataAccessException {
        Credentials res = userDAO.getCredentials("DezeEmailBestaatNiet@hotmail.com");
        assertNull(res);
    }

    @Test
    public void getCredentialsById_shouldReturnCredentials() throws DataAccessException {
        Credentials res = userDAO.getCredentials(4242);
        assertTrue(Objects.equals(res.getRole(), expectedCredentials.getRole()));
        assertTrue(Objects.equals(res.getUserIdentifier(), expectedCredentials.getUserIdentifier()));
        assertNotNull(res.getPassword());
    }

    @Test
    public void getCredentialsById_shouldReturnNull() throws DataAccessException {
        Credentials res = userDAO.getCredentials(0);
        assertNull(res);
    }

    @Test
    public void getUserIds_shouldReturnIds() throws DataAccessException {
        List<Integer> expected = new ArrayList<>();
        expected.add(4242);
        expected.add(9127);
        List<Integer> res = userDAO.getUserIds();
        assertNotNull(res);
        assertTrue(res.size() == expected.size());
        for (int i = 0; i < res.size(); i++) {
            assertTrue(Objects.equals(res.get(i), expected.get(i)));
        }
    }

    @Test
    public void userExists_userDoesExist() throws DataAccessException {
        boolean res = userDAO.userExists("Liam.Vermeir1@hotmail.com");
        assertTrue(res);
    }

    @Test
    public void userExists_userDoesntExist() throws DataAccessException {
        boolean res = userDAO.userExists("unknown@hotmail.com");
        assertFalse(res);
    }

    @Test
    public void userIdExists_idDoesExist() throws DataAccessException {
        boolean res = userDAO.userIdExists(9127);
        assertTrue(res);
    }

    @Test
    public void userIdExists_idDoesntExist() throws DataAccessException {
        boolean res = userDAO.userIdExists(21536);
        assertFalse(res);
    }

    @Test
    @ExpectedDatabase("/credentials_modified.xml")
    public void modifyCredentials_shouldModifyCorrect() throws DataAccessException {
        Credentials modifiedCredentials = UserCredentialsFactory.build(expectedCredentials.getPassword().getBytes(), 4242, "ROLE_ADMIN");
        userDAO.modifyCredentials(modifiedCredentials);
    }

    @Test
    @ExpectedDatabase("/credentials_deleted.xml")
    public void deleteCredentials_shouldDeleteCorrect() throws DataAccessException {
        userDAO.deleteCredentials(4242);
    }

    @Test
    public void deleteCredentials_shouldDeleteFalse() throws DataAccessException {
        userDAO.deleteCredentials(6969);
    }

    @Test
    @ExpectedDatabase("/user_pins_created.xml")
    public void createUserPins_shouldCreateCorrect() throws DataAccessException {
        userDAO.createUser(otherUser);
        userDAO.createUserPins(otherUser.getUserIdentifier());
    }

    @Test
    @ExpectedDatabase("/user_pins_deleted.xml")
    public void deleteUserPins_shouldDeleteCorrect() throws DataAccessException {
        userDAO.createUser(otherUser);
        userDAO.deleteUserPins(otherUser.getUserIdentifier());
    }

    @Test
    public void getEmailValidationPin_shouldReturnEmailValidationPin() throws DataAccessException {
        userDAO.createEmailValidationPin(expectedUser.getUserIdentifier(), "email_pin");
        String result = userDAO.getEmailValidationPin(expectedUser.getUserIdentifier());

        // compare with expected pin
        assertNotNull(result);
        assertTrue("email_pin".equals(result));
    }

    @Test
    public void getEmailValidationPin_shouldReturnNull() throws DataAccessException {
        String result = userDAO.getEmailValidationPin(otherUser.getUserIdentifier());
        assertNull(result);
    }

    @Test
    @ExpectedDatabase("/user_email_pin_created.xml")
    public void createEmailValidationPin_shouldCreateCorrect() throws DataAccessException {
        userDAO.createEmailValidationPin(expectedUser.getUserIdentifier(), "email_pin");
    }

    @Test
    @ExpectedDatabase("/user_email_pin_deleted.xml")
    public void deleteEmailValidationPin_shouldDeleteCorrect() throws DataAccessException {
        userDAO.createEmailValidationPin(expectedUser.getUserIdentifier(), "email_pin");
        String result = userDAO.getEmailValidationPin(expectedUser.getUserIdentifier());

        assertNotNull(result);
        assertTrue("email_pin".equals(result));

        userDAO.deleteEmailValidationPin(expectedUser.getUserIdentifier());
    }

    @Test
    @ExpectedDatabase("/user_password_pin_created.xml")
    public void createPasswordResetPin_shouldCreateCorrect() throws DataAccessException {
        userDAO.createPasswordResetPin(expectedUser.getUserIdentifier(), "password_pin");
    }

    @Test
    @ExpectedDatabase("/user_password_pin_deleted.xml")
    public void deletePasswordResetPin_shouldDeleteCorrect() throws DataAccessException {
        userDAO.createPasswordResetPin(expectedUser.getUserIdentifier(), "password_pin");
        String result = userDAO.getPasswordResetPin(expectedUser.getUserIdentifier());

        assertNotNull(result);
        assertTrue("password_pin".equals(result));

        userDAO.deletePasswordResetPin(expectedUser.getUserIdentifier());
    }

    @Test
    public void getPasswordResetPin_shouldReturnPasswordResetPin() throws DataAccessException {
        userDAO.createPasswordResetPin(expectedUser.getUserIdentifier(), "password_pin");
        String result = userDAO.getPasswordResetPin(expectedUser.getUserIdentifier());

        // compare with expected pin
        assertNotNull(result);
        assertTrue("password_pin".equals(result));
    }

    @Test
    public void getPasswordResetPin_shouldReturnNull() throws DataAccessException {
        String result = userDAO.getPasswordResetPin(otherUser.getUserIdentifier());
        assertNull(result);
    }

    @Test
    public void getUserIdFromPasswordPin_shouldReturnUserId() throws DataAccessException {
        userDAO.createPasswordResetPin(expectedUser.getUserIdentifier(), "password_pin");
        String result = userDAO.getPasswordResetPin(expectedUser.getUserIdentifier());

        // compare with expected pin
        assertNotNull(result);
        assertTrue("password_pin".equals(result));

        int id = userDAO.getUserIdFromPasswordPin("password_pin");
        assertTrue(id == expectedUser.getUserIdentifier());
    }

    @Test
    public void getUserIdFromPasswordPin_shouldReturnMinusOne() throws DataAccessException {
        int id = userDAO.getUserIdFromPasswordPin("password_pin");
        assertTrue(id == -1);
    }

    @Test
    public void getMatchedEvents_shouldReturnListOfIds() throws DataAccessException {
        List<Integer> ids = userDAO.getMatchedEvents(expectedUser.getUserIdentifier());

        assertNotNull(ids);
        assertEquals(ids.size(), expectedUserMatchedEvents.size());
        for (int i = 0; i < expectedUserMatchedEvents.size(); i++) {
            assertNotNull(ids.get(i));
            assertTrue("The expected event was not returned!", Objects.equals(ids.get(i), expectedUserMatchedEvents.get(i)));
        }
    }

    @Test
    @ExpectedDatabase("/matched_event_created.xml")
    public void createMatch_shouldCreateCorrect() throws DataAccessException {
        userDAO.createMatch(expectedUser.getUserIdentifier(), 3);
    }

    @Test
    @ExpectedDatabase("/match_deleted.xml")
    public void deleteMatch_shouldDeleteCorrect() throws DataAccessException {
        userDAO.deleteMatch(1, 4242);
    }

    @Test
    @ExpectedDatabase("/matched_event_deleted.xml")
    public void deleteMatchedEvent_shouldDeleteCorrect() throws DataAccessException {
        userDAO.deleteMatchedEvent(1);
    }

    @Test
    @ExpectedDatabase("/matched_events_deleted.xml")
    public void deleteMatchedEvents_shouldDeleteCorrect() throws DataAccessException {
        userDAO.deleteMatchedEvents(4242);
    }

    @Test
    public void getIdFromRefreshToken_shouldReturnId() throws DataAccessException {
        int id = userDAO.getIdFromRefreshToken("123456789");

        assertNotNull(id);
        assertTrue(id == expectedUser.getUserIdentifier());
    }

    @Test
    public void getIdFromRefreshToken_shouldReturnMinusOne() throws DataAccessException {
        int id = userDAO.getIdFromRefreshToken("dezetokenbestaatniet123");

        assertTrue(id == -1);
    }

    @Test
    @ExpectedDatabase("/refreshtoken_updated.xml")
    public void updateRefreshToken_shouldUpdateCorrect() throws DataAccessException {
        userDAO.updateRefreshToken("123456789", 4242, Timestamp.valueOf("2012-05-05 20:00:00"));
    }

    @Test
    @ExpectedDatabase("/refreshtoken_created.xml")
    public void createRefreshToken_shouldCreateCorrect() throws DataAccessException {
        userDAO.createRefreshToken("789123456", 9127, Timestamp.valueOf("2012-05-05 20:00:00"));
    }

    @Test
    @ExpectedDatabase("/refreshtoken_deleted.xml")
    public void deleteRefreshTokenByToken_shouldDeleteCorrect() throws DataAccessException {
        userDAO.deleteRefreshToken("123456789");
    }

    @Test
    @ExpectedDatabase("/refreshtoken_deleted.xml")
    public void deleteRefreshTokenById_shouldDeleteCorrect() throws DataAccessException {
        userDAO.deleteRefreshToken(4242);
    }

    @Test
    public void getTokens_shouldReturnMapOfTokens() throws DataAccessException {
        Map<String, Timestamp> tokens = userDAO.getAllTokens(4242);

        // compare tokens with expected tokens
        assertNotNull(tokens);
        Map<String, Timestamp> expectedTokens = new HashMap<>();
        expectedTokens.put("123456789", Timestamp.valueOf("2001-09-11 15:00:00"));
        assertEquals(expectedTokens.size(), tokens.size());
        assertNotNull(tokens.get("123456789"));
        assertTrue(expectedTokens.get("123456789").equals((tokens.get("123456789"))));

    }

    @Test
    public void areMatched_shouldReturnTrue() throws DataAccessException {
        boolean test = userDAO.areMatched(4242, 1);
        assertTrue(test);
    }

    @Test
    public void areMatched_shouldReturnFalse() throws DataAccessException {
        boolean test = userDAO.areMatched(9127, 2);
        assertFalse(test);
    }
}
