package Controllers;

import Other.TestUtils;
import com.google.common.cache.Cache;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Controllers.Controller;
import vop.groep7.vop7backend.Controllers.UserController;
import vop.groep7.vop7backend.Controllers.Validator;
import vop.groep7.vop7backend.Exceptions.NotExistingResponseException;
import vop.groep7.vop7backend.Models.APIModels.APIUser;
import vop.groep7.vop7backend.Models.APIModels.APIUserAdmin;
import vop.groep7.vop7backend.Models.APIModels.APIValidation;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICredentials;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIVerification;
import vop.groep7.vop7backend.Models.Domain.Credentials;
import vop.groep7.vop7backend.Models.Domain.Password;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.Security.TokenManager;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.UserDAO;
import vop.groep7.vop7backend.factories.UserCredentialsFactory;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Jonas Van Wilder
 */
public class UserControllerTest {

    @Mock
    private UserDAO userDAOMock;

    @Mock
    private Validator validatorMock;

    @Mock
    private TokenManager tokenManagerMock;

    @InjectMocks
    private UserController userController;

    // mocked userCache for test
    private static Cache<Integer, User> mockedUserCache;

    private User testUser1;
    private Credentials testCredentials1;
    private List<User> testUsers;
    private APIUser testAPIUser1;
    private List<APIUser> testAPIUsers;

    private static final String USER_URL = "https://vopro7.ugent.be/api/user/";

    /**
     *
     */
    public UserControllerTest() {

    }

    /**
     *
     */
    @BeforeClass
    public static void setUpClass() {

    }

    /**
     *
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // mock user cache
        mockedUserCache = Mockito.mock(Cache.class);
        // replace static userCache with mocked userCache
        Field f = Controller.class.getDeclaredField("userCache");
        f.setAccessible(true);
        f.set(null, mockedUserCache);

        // Mock annotations
        MockitoAnnotations.initMocks(this);

        testAPIUsers = new ArrayList<>();
        testUsers = new ArrayList<>();

        createTestUser1();

        testAPIUsers.add(testAPIUser1);
        testUsers.add(testUser1);
    }

    private void createTestUser1() {
        testUser1 = UserFactory.build(1, "George.Vermalen@example.com", "", "", null, false, true, true);
        testUser1.setFirstName("George");
        testUser1.setLastName("Vermalen");
        testUser1.setEmailValidation(false);
        testUser1.setCellPhoneValidation(false);
        testAPIUser1 = new APIUser();
        testAPIUser1.setPassword("password123");
        testAPIUser1.setEmail("George.Vermalen@example.com");
        testAPIUser1.setFirstName("George");
        testAPIUser1.setLastName("Vermalen");
        testAPIUser1.setCellNumber(null);
        testAPIUser1.setMuteNotifications(false);
        APIValidation v = new APIValidation();
        v.setCellNumberValidated(false);
        v.setEmailValidated(false);
        testAPIUser1.setValidated(v);

        testCredentials1 = UserCredentialsFactory.build(new HexBinaryAdapter().unmarshal("482C811DA5D5B4BC6D497FFA98491E38"), 1, "ROLE_USER");
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    private void prepareCache() {
        when(mockedUserCache.getIfPresent(1)).thenReturn(testUser1);
    }

    @Test
    public void getUser_returnUserFromCache() {
        prepareCache();
        User res = null;
        try {
            res = userController.getUser(1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertTrue(TestUtils.compareUsers(res, testUser1));
        verify(mockedUserCache, times(0)).put(Matchers.eq(1), Matchers.<User>any());
    }

    @Test
    public void getUser_returnUserFromDAO() {
        try {
            // prepare dao
            when(userDAOMock.getUser(1)).thenReturn(testUser1);
        } catch (DataAccessException ex) {
            fail();
        }

        User res = null;
        try {
            res = userController.getUser(1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertTrue(TestUtils.compareUsers(res, testUser1));
        verify(mockedUserCache, times(1)).put(Matchers.eq(1), Matchers.<User>any());
    }

    @Test
    public void getUser_throwNotExisting() {
        try {
            // prepare dao
            when(userDAOMock.getUser(1)).thenReturn(null);
        } catch (DataAccessException ex) {
            fail();
        }

        User res = null;
        boolean thrown = false;
        try {
            res = userController.getUser(1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        } catch (NotExistingResponseException ex) {
            thrown = true;
        }
        assertTrue("User does not exist and exception is not thrown!", thrown);
        verify(mockedUserCache, times(0)).put(Matchers.eq(1), Matchers.<User>any());
    }

    @Test
    public void getAPIUser_shouldReturnAPIUser() {
        testAPIUser1.setId("1");
        prepareCache();
        APIUser res = null;
        try {
            res = userController.getAPIUser(1);
        } catch (DataAccessException ex) {
            fail("Exception should not be thrown!");
        }
        assertNotNull(res);
        assertEquals(testUser1.getUserIdentifier() + "", res.getId());
        assertEquals(testUser1.getEmail(), res.getEmail());
        assertEquals(testUser1.getName().getFirstName(), res.getFirstName());
        assertEquals(testUser1.getName().getLastName(), res.getLastName());
        assertEquals(testUser1.getCellPhone(), res.getCellNumber());
        assertEquals(testUser1.isMute(), res.isMuteNotifications());
        assertEquals(testUser1.isCellPhoneValidated(), res.getValidated().isCellNumberValidated());
        assertEquals(testUser1.isEmailValidated(), res.getValidated().isEmailValidated());
        assertEquals("self", res.getLinks()[0].getRel());
        assertEquals(USER_URL + res.getId(), res.getLinks()[0].getHref());
        assertNull(res.getPassword());
    }

    @Test
    public void getAPIUsers_shouldReturnCorrectUsers() {
        try {
            when(userDAOMock.getUsers()).thenReturn(testUsers);
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
        List<APIUser> res = null;
        try {
            res = userController.getAPIUsers();
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
        assertNotNull(res);
        assertEquals(testUsers.size(), res.size());
        // test if only user is correct
        assertNotNull(res.get(0));
        assertEquals(testUser1.getUserIdentifier() + "", res.get(0).getId());
        assertEquals(testUser1.getEmail(), res.get(0).getEmail());
        assertEquals(testUser1.getName().getFirstName(), res.get(0).getFirstName());
        assertEquals(testUser1.getName().getLastName(), res.get(0).getLastName());
        assertEquals(testUser1.getCellPhone(), res.get(0).getCellNumber());
        assertEquals(testUser1.isMute(), res.get(0).isMuteNotifications());
        assertEquals(testUser1.isCellPhoneValidated(), res.get(0).getValidated().isCellNumberValidated());
        assertEquals(testUser1.isEmailValidated(), res.get(0).getValidated().isEmailValidated());
        assertEquals("self", res.get(0).getLinks()[0].getRel());
        assertEquals(USER_URL + testUser1.getUserIdentifier(), res.get(0).getLinks()[0].getHref());
        assertNull(res.get(0).getPassword());
    }

    @Test
    public void createAPIUser_shouldCreateCorrect() {
        try {
            when(userDAOMock.userExists(testAPIUser1.getEmail())).thenReturn(false);
            when(userDAOMock.userIdExists(Matchers.anyInt())).thenReturn(true, false);

            // call the to be tested method
            APIUser res = userController.createAPIUser(testAPIUser1);

            verify(userDAOMock, times(1)).userExists(testAPIUser1.getEmail());
            verify(userDAOMock, times(1)).createUser(Matchers.<User>any());
            verify(userDAOMock, times(1)).createCredentials(Matchers.<Credentials>any());
            verify(mockedUserCache, times(1)).put(Matchers.anyInt(), Matchers.<User>any());
            verify(validatorMock, times(1)).askForValidation(Matchers.<User>any());

            //verifyNoMoreInteractions(userDAOMock, validatorMock, mockedUserCache);
            // check if returned APIUser is as expected
            assertNotNull(res);
            assertFalse(res.getId().equals("-1"));
            assertEquals(testUser1.getEmail(), res.getEmail());
            assertEquals(testUser1.getName().getFirstName(), res.getFirstName());
            assertEquals(testUser1.getName().getLastName(), res.getLastName());
            assertEquals(testUser1.getCellPhone(), res.getCellNumber());
            assertEquals(testUser1.isMute(), res.isMuteNotifications());
            assertEquals(testUser1.isCellPhoneValidated(), res.getValidated().isCellNumberValidated());
            assertEquals(testUser1.isEmailValidated(), res.getValidated().isEmailValidated());
            assertEquals("self", res.getLinks()[0].getRel());
            assertEquals(USER_URL + res.getId(), res.getLinks()[0].getHref());
            assertNull(res.getPassword());

        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }

    @Test
    public void createAPIUser_shouldReturnNull() {
        try {
            when(userDAOMock.userExists(testAPIUser1.getEmail())).thenReturn(true);
            // call the to be tested method
            APIUser res = userController.createAPIUser(testAPIUser1);

            assertNull(res);

            verify(userDAOMock, times(1)).userExists(testAPIUser1.getEmail());
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }

    @Test
    public void deleteAPIUser_shouldDeleteCorrect() {
        try {
            when(mockedUserCache.getIfPresent(Matchers.eq(1))).thenReturn(testUser1);
            when(userDAOMock.deleteCredentials(Matchers.eq(1))).thenReturn(true);
            when(userDAOMock.deleteUser(Matchers.eq(1))).thenReturn(true);

            boolean res = userController.deleteAPIUser(1);
            assertTrue(res);

            verify(mockedUserCache, times(1)).getIfPresent(Matchers.eq(1));
            verify(mockedUserCache, times(1)).invalidate(Matchers.eq(1));
            verify(userDAOMock, times(1)).deleteCredentials(Matchers.eq(1));
            verify(userDAOMock, times(1)).deleteUser(Matchers.eq(1));
            verify(userDAOMock, times(1)).deleteMatchedEvents(Matchers.eq(1));
            verify(userDAOMock, times(1)).deleteUserPins(Matchers.eq(1));
            verify(tokenManagerMock, times(1)).deleteRefreshToken(Matchers.eq(1));
            verifyNoMoreInteractions(userDAOMock, validatorMock, mockedUserCache, tokenManagerMock);
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }

    @Test
    public void deleteAPIUser_shouldDeleteNot() {
        try {
            when(mockedUserCache.getIfPresent(Matchers.eq(1))).thenReturn(null);
            when(userDAOMock.deleteCredentials(Matchers.eq(1))).thenReturn(false);
            when(userDAOMock.deleteUser(Matchers.eq(1))).thenReturn(false);

            boolean res = userController.deleteAPIUser(1);
            assertFalse(res);

            verify(mockedUserCache, times(1)).getIfPresent(Matchers.eq(1));
            verify(mockedUserCache, times(0)).invalidate(Matchers.eq(1));
            verify(userDAOMock, times(1)).deleteCredentials(Matchers.eq(1));
            verify(userDAOMock, times(1)).deleteUser(Matchers.eq(1));
            verify(userDAOMock, times(1)).deleteMatchedEvents(Matchers.eq(1));
            verify(userDAOMock, times(1)).deleteUserPins(Matchers.eq(1));
            verify(tokenManagerMock, times(1)).deleteRefreshToken(Matchers.eq(1));
            verifyNoMoreInteractions(userDAOMock, validatorMock, mockedUserCache, tokenManagerMock);
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }

    @Test
    public void modifyAPIUser_shouldModifyCorrect() {
        testAPIUser1.setId("1");
        try {
            when(mockedUserCache.getIfPresent(Matchers.eq(1))).thenReturn(testUser1);

            APIUser res = userController.modifyAPIUser(1, testAPIUser1);

            verify(mockedUserCache, times(1)).getIfPresent(Matchers.eq(1));
            verify(mockedUserCache, times(1)).invalidate(Matchers.eq(1));
            verify(mockedUserCache, times(1)).put(Matchers.eq(1), Matchers.<User>any());
            verify(userDAOMock, times(1)).modifyUser(Matchers.eq(1), Matchers.<User>any());

            assertNotNull(res);
            assertEquals(testUser1.getUserIdentifier() + "", res.getId());
            assertEquals(testUser1.getEmail(), res.getEmail());
            assertEquals(testUser1.getName().getFirstName(), res.getFirstName());
            assertEquals(testUser1.getName().getLastName(), res.getLastName());
            assertEquals(testUser1.getCellPhone(), res.getCellNumber());
            assertEquals(testUser1.isMute(), res.isMuteNotifications());
            assertEquals(testUser1.isCellPhoneValidated(), res.getValidated().isCellNumberValidated());
            assertEquals(testUser1.isEmailValidated(), res.getValidated().isEmailValidated());
            assertEquals("self", res.getLinks()[0].getRel());
            assertEquals(USER_URL + testUser1.getUserIdentifier(), res.getLinks()[0].getHref());
            assertNull(res.getPassword());

        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }
    
    @Test
    public void modifyPassword_shouldModifyCorrect() {
        testAPIUser1.setId("1");
        try {
            when(userDAOMock.getCredentials(Matchers.eq(1))).thenReturn(UserCredentialsFactory.create("password123", UserFactory.toDomainModel(testAPIUser1)));
            
            userController.modifyPassword(1, "password123", "test124");

            verify(userDAOMock, times(1)).getCredentials(Matchers.eq(1));
            verify(userDAOMock, times(1)).modifyCredentials(Matchers.<Credentials>any());

        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }

    @Test
    public void forgotPasswordRequest_ShouldBeCorrect() {
        testAPIUser1.setId("1");
        try {
            when(userDAOMock.getCredentials(Matchers.eq("George.Vermalen@example.com"))).thenReturn(UserCredentialsFactory.create("password123", UserFactory.toDomainModel(testAPIUser1)));
            
            APICredentials c = new APICredentials();
            c.setEmail("George.Vermalen@example.com");
            
            userController.forgotPasswordRequest(c);

            verify(userDAOMock, times(1)).getCredentials(Matchers.eq("George.Vermalen@example.com"));
            verify(userDAOMock, times(1)).createPasswordResetPin(Matchers.eq(1), Matchers.<String>any());

        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }
    
    @Test
    public void forgotPasswordResponse_ShouldBeCorrect() {
        testAPIUser1.setId("1");
        try {
            when(userDAOMock.getUserIdFromPasswordPin(Matchers.<String>any())).thenReturn(1);
            when(userDAOMock.getCredentials(1)).thenReturn(UserCredentialsFactory.create("password123", UserFactory.toDomainModel(testAPIUser1)));
            when(userDAOMock.getPasswordResetPin(Matchers.eq(1))).thenReturn(Matchers.eq("token"));
            
            APICredentials c = new APICredentials();
            c.setPassword("password123");
            
            userController.forgotPasswordResponse("token", c);

            verify(userDAOMock, times(1)).getCredentials(Matchers.eq(1));
            verify(userDAOMock, times(1)).getUserIdFromPasswordPin(Matchers.<String>any());
            verify(userDAOMock, times(1)).getPasswordResetPin(Matchers.eq(1));

        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }

    @Test
    public void verify_ShouldReturnTrue() {
        APIVerification v = new APIVerification();
        v.setEmail("test@example.com");
        v.setEmailVerificationPin("0284935");
        try {
            when(userDAOMock.getCredentials(Matchers.eq("test@example.com"))).thenReturn(testCredentials1);
            when(userDAOMock.getEmailValidationPin(Matchers.eq(1))).thenReturn("0284935");
            when(userDAOMock.deleteEmailValidationPin(Matchers.eq(1))).thenReturn(true);
            when(mockedUserCache.getIfPresent(1)).thenReturn(testUser1);

            boolean res = userController.verify(v);
            assertTrue(res);
            verify(userDAOMock, times(1)).getCredentials(Matchers.eq("test@example.com"));
            verify(userDAOMock, times(1)).getEmailValidationPin(Matchers.eq(1));
            verify(userDAOMock, times(1)).deleteEmailValidationPin(Matchers.eq(1));
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }

    @Test
    public void verify_ShouldReturnFalse() {
        APIVerification v = new APIVerification();
        v.setEmail("test@example.com");
        v.setEmailVerificationPin(null);
        try {
            when(userDAOMock.getCredentials(Matchers.eq("test@example.com"))).thenReturn(testCredentials1);
            when(userDAOMock.getEmailValidationPin(Matchers.eq(1))).thenReturn("0284935");

            boolean res = userController.verify(v);
            assertFalse(res);
            verify(userDAOMock, times(1)).getCredentials(Matchers.eq("test@example.com"));
            verify(userDAOMock, times(1)).getEmailValidationPin(Matchers.eq(1));
            verify(userDAOMock, times(0)).deleteEmailValidationPin(Matchers.eq(1));
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }

    @Test
    public void verify_ShouldReturnFalse2() {
        APIVerification v = new APIVerification();
        v.setEmail("test@example.com");
        v.setEmailVerificationPin("5869325");
        try {
            when(userDAOMock.getCredentials(Matchers.eq("test@example.com"))).thenReturn(testCredentials1);
            when(userDAOMock.getEmailValidationPin(Matchers.eq(1))).thenReturn("0284935");

            boolean res = userController.verify(v);
            assertFalse(res);
            verify(userDAOMock, times(1)).getCredentials(Matchers.eq("test@example.com"));
            verify(userDAOMock, times(1)).getEmailValidationPin(Matchers.eq(1));
            verify(userDAOMock, times(0)).deleteEmailValidationPin(Matchers.eq(1));
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }

    @Test
    public void verify_ShouldReturnFalse3() {
        APIVerification v = new APIVerification();
         v.setEmail("test@example.com");
        v.setEmailVerificationPin("5869325");
        try {
             when(userDAOMock.getCredentials(Matchers.eq("test@example.com"))).thenReturn(testCredentials1);
            when(userDAOMock.getEmailValidationPin(Matchers.eq(1))).thenThrow(new DataAccessException(""));

            boolean res = userController.verify(v);
            assertFalse(res);
            verify(userDAOMock, times(1)).getCredentials(Matchers.eq("test@example.com"));
            verify(userDAOMock, times(1)).getEmailValidationPin(Matchers.eq(1));
            verify(userDAOMock, times(0)).deleteEmailValidationPin(Matchers.eq(1));
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }
    
    @Test
    public void verify_ShouldReturnFalse4() {
        APIVerification v = new APIVerification();
         v.setEmail("test@example.com");
        v.setEmailVerificationPin("5869325");
        try {
             when(userDAOMock.getCredentials(Matchers.eq("test@example.com"))).thenReturn(null);

            boolean res = userController.verify(v);
            assertFalse(res);
            verify(userDAOMock, times(1)).getCredentials(Matchers.eq("test@example.com"));
            verify(userDAOMock, times(0)).getEmailValidationPin(Matchers.eq(1));
            verify(userDAOMock, times(0)).deleteEmailValidationPin(Matchers.eq(1));
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
    }
    
    @Test
    public void getAPIUsersAdmin_shouldReturnCorrectUsers() {
        Map<User, String> usersWithRole = new HashMap<>();
        usersWithRole.put(testUser1, "ROLE_USER");
        try {
            when(userDAOMock.getUsersWithRole()).thenReturn(usersWithRole);
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
        List<APIUserAdmin> res = null;
        try {
            res = userController.getAPIUsersAdmin();
        } catch (DataAccessException ex) {
            fail("This DataAccessException should not be thrown!");
        }
        assertNotNull(res);
        assertEquals(usersWithRole.size(), res.size());
        // test if only user is correct
        assertNotNull(res.get(0));
        assertEquals("ROLE_USER", res.get(0).getRole().getRole());
        assertEquals(testUser1.getUserIdentifier() + "", res.get(0).getId());
        assertEquals(testUser1.getEmail(), res.get(0).getEmail());
        assertEquals(testUser1.getName().getFirstName(), res.get(0).getFirstName());
        assertEquals(testUser1.getName().getLastName(), res.get(0).getLastName());
        assertEquals(testUser1.getCellPhone(), res.get(0).getCellNumber());
        assertEquals(testUser1.isMute(), res.get(0).isMuteNotifications());
        assertEquals(testUser1.isCellPhoneValidated(), res.get(0).getValidated().isCellNumberValidated());
        assertEquals(testUser1.isEmailValidated(), res.get(0).getValidated().isEmailValidated());
        assertEquals("self", res.get(0).getLinks()[0].getRel());
        assertEquals(USER_URL + testUser1.getUserIdentifier(), res.get(0).getLinks()[0].getHref());
    }

}
