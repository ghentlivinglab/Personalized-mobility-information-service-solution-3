package Security;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Controllers.UserController;
import vop.groep7.vop7backend.Exceptions.NotExistingResponseException;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.Security.TokenManager;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.UserDAO;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Backend Team
 */
public class TokenManagerTest {

    @Mock
    private UserController userControllerMock;

    @Mock
    private UserDAO userDAOMock;

    @InjectMocks
    private TokenManager tokenManager;

    private SecureRandom randomGeneratorMock;

    private User testUser;

    public TokenManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        tokenManager = new TokenManager();
        randomGeneratorMock = new SecureRandom();
        // Mock annotations
        MockitoAnnotations.initMocks(this);
        
        // mock final randomGenerator
        Field f = tokenManager.getClass().getDeclaredField("randomGenerator");
        f.setAccessible(true);
        f.set(tokenManager, randomGeneratorMock);

        createTestUser();
    }

    @After
    public void tearDown() {
    }

    private void createTestUser() {
        testUser = UserFactory.build(1, "George.Vermalen@example.com", "George", "Vermalen", null, false, false, false);
    }

    @Test
    public void createRefreshToken_shouldReturnToken() throws DataAccessException {
        String token = tokenManager.createRefreshToken(1);

        assertNotNull(token);

        verify(userDAOMock, times(1)).createRefreshToken(Matchers.<String>any(), Matchers.eq(1), Matchers.<Timestamp>any());
        verify(userDAOMock, times(0)).deleteRefreshToken(Matchers.<String>any());

    }

    @Test
    public void createRefreshToken_shouldReturnTokenAndDeleteOldest() throws DataAccessException {
        Map<String, Timestamp> testTokens = new HashMap<String, Timestamp>() {{
            put("test1", new Timestamp(669519519));
            put("test2", new Timestamp(669519520));
            put("test3", new Timestamp(669519521));
            put("test4", new Timestamp(669519522));
            put("test5", new Timestamp(669519522));
        }};
        when(userDAOMock.getAllTokens(1)).thenReturn(testTokens);
        tokenManager.createRefreshToken(1);

        verify(userDAOMock, times(1)).createRefreshToken(Matchers.<String>any(), Matchers.eq(1), Matchers.<Timestamp>any());
        verify(userDAOMock, times(1)).deleteRefreshToken(Matchers.<String>any());

    }

    @Test
    public void deleteRefreshTokenId_shouldDelete() throws DataAccessException {
        tokenManager.deleteRefreshToken(1);
        verify(userDAOMock, times(1)).deleteRefreshToken(1);
    }

    @Test
    public void deleteRefreshTokenString_shouldDelete() throws DataAccessException {
        tokenManager.deleteRefreshToken("TOKEN");
        verify(userDAOMock, times(1)).deleteRefreshToken(Matchers.<String>any());
    }

    @Test
    public void validateRefreshToken_shouldValidate() throws DataAccessException {
        when(userDAOMock.getIdFromRefreshToken(Matchers.<String>any())).thenReturn(1);
        when(userControllerMock.getUser(Matchers.eq(1))).thenReturn(testUser);
        
        User res = tokenManager.validateRefreshToken("TOKEN");

        assertNotNull(res);
        assertEquals(1, res.getUserIdentifier());
        
        verify(userDAOMock, times(1)).updateRefreshToken(Matchers.<String>any(), Matchers.eq(1), Matchers.<Timestamp>any());
        verify(userControllerMock, times(1)).getUser(Matchers.eq(1));
    }

    @Test
    public void validateRefreshToken_shouldReturnNull() throws DataAccessException {
        when(userDAOMock.getIdFromRefreshToken(Matchers.<String>any())).thenReturn(-1);
        User res = tokenManager.validateRefreshToken("TOKEN");
        assertNull(res);
    }

    @Test
    public void validateRefreshToken_shouldReturnNull2() throws DataAccessException {
        when(userControllerMock.getUser(1)).thenThrow(new DataAccessException(""));
        String token = tokenManager.createRefreshToken(1);

        User res = tokenManager.validateRefreshToken(token);

        assertNull(res);
    }
    
    @Test
    public void validateRefreshToken_shouldReturnNull3() throws DataAccessException {
        when(userDAOMock.getIdFromRefreshToken(Matchers.<String>any())).thenReturn(1);
        Mockito.doThrow(new DataAccessException("")).when(userDAOMock).updateRefreshToken(Matchers.<String>any(), Matchers.eq(1), Matchers.<Timestamp>any());
        
        User res = tokenManager.validateRefreshToken("TOKEN");

        assertNull(res);
    }

    @Test
    public void createAccessToken_shouldCreateToken() {
        String res = tokenManager.createAccessToken(testUser);

        assertNotNull(res);
        String[] parts = res.split(":");
        assertEquals(4, parts.length);
        assertEquals("token", parts[0]);
        assertEquals(testUser.getUserIdentifier() + "", parts[1]);
        assertNotNull(parts[2]);
        assertNotNull(parts[3]);
        // check if expiration time is in the future
        assertTrue(Long.valueOf(parts[3]) > System.currentTimeMillis());
    }

    @Test
    public void validateAccessToken_shouldReturnTrue() throws DataAccessException {
        tokenManager.createRefreshToken(1);
        String accessToken = tokenManager.createAccessToken(testUser);
        when(userControllerMock.getUser(1)).thenReturn(testUser);

        boolean res = tokenManager.validateAccessToken(accessToken);

        assertTrue(res);
    }

    /**
     * Test if null results in invalid access token
     * 
     * @throws DataAccessException 
     */
    @Test
    public void validateAccessToken_shouldReturnFalse() throws DataAccessException {
        String accessToken = null;

        boolean res = tokenManager.validateAccessToken(accessToken);

        assertFalse(res);
    }

    /**
     * Test if access token without 4 parts results in invalid accesstoken
     * 
     * @throws DataAccessException 
     */
    @Test
    public void validateAccessToken_shouldReturnFalse2() throws DataAccessException {
        String accessToken = "token:1:12315143186";

        boolean res = tokenManager.validateAccessToken(accessToken);

        assertFalse(res);
    }

     /**
     * Test if access token with incorrect hash (other email) results in
     * invalid accesstoken
     * 
     * @throws DataAccessException 
     */
    @Test
    public void validateAccessToken_shouldReturnFalse3() throws DataAccessException {
        User other = UserFactory.create(1, "test@test.test", null, null, null, true);
        when(userControllerMock.getUser(1)).thenReturn(other);

        String accessToken = tokenManager.createAccessToken(testUser);

        boolean res = tokenManager.validateAccessToken(accessToken);

        assertFalse(res);
    }

    /**
     * Test if access token with incorrect userid format results in
     * invalid accesstoken
     * 
     * @throws DataAccessException 
     */
    @Test
    public void validateAccessToken_shouldReturnFalse4() throws DataAccessException {
        String accessToken = "token:een:UHVYGVLHBLMBUVYK:3513513513";

        boolean res = tokenManager.validateAccessToken(accessToken);

        assertFalse(res);
    }

    /**
     * Test if DataAccessException results in invalid access token
     * 
     * @throws DataAccessException 
     */
    @Test
    public void validateAccessToken_shouldReturnFalse5() throws DataAccessException {
        String accessToken = tokenManager.createAccessToken(testUser);
        when(userControllerMock.getUser(1)).thenThrow(new DataAccessException(""));

        boolean res = tokenManager.validateAccessToken(accessToken);

        assertFalse(res);
    }

    /**
     * Test if NotExistingResponseException results in invalid access token
     * 
     * @throws DataAccessException 
     */
    @Test
    public void validateAccessToken_shouldReturnFalse6() throws DataAccessException {
        String accessToken = tokenManager.createAccessToken(testUser);
        when(userControllerMock.getUser(1)).thenThrow(new NotExistingResponseException(""));

        boolean res = tokenManager.validateAccessToken(accessToken);

        assertFalse(res);
    }

}
