package Security;

import Controllers.UserControllerTest;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.AssertTrue;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Models.Domain.Credentials;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.Security.AuthenticationFilter;
import vop.groep7.vop7backend.Security.TokenManager;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.UserDAO;
import vop.groep7.vop7backend.factories.UserCredentialsFactory;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Backend Team
 */
public class AuthenticationFilterTest {

    @Mock
    private TokenManager tokenManagerMock;

    @Mock
    private UserDAO userDAOMock;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private FilterChain filterChain;

    private User testUser;
    private Credentials testCredentials;

    public AuthenticationFilterTest() {
    }

    @Before
    public void setUp() throws Exception {
        authenticationFilter = new AuthenticationFilter();
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        // Mock annotations
        MockitoAnnotations.initMocks(this);

        createTestUser();

        testCredentials = UserCredentialsFactory.build("test123", 1, "ROLE_USER");

    }

    private void createTestUser() {
        testUser = UserFactory.build(1, "George.Vermalen@example.com", "George", "Vermalen", null, false, false, false);
    }

    @Test
    public void testDoFilter_shouldSendForbiddenError() throws IOException, ServletException {
        when(httpServletRequest.getRequestURI()).thenReturn("/eventtype/");
        when(httpServletRequest.getMethod()).thenReturn("GET");

        authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "You have no access to this url or you are not logged in.");
    }

    @Test
    public void testDoFilter_shouldDoNothing() throws IOException, ServletException {
        when(httpServletRequest.getRequestURI()).thenReturn("/monitoring");
        when(httpServletRequest.getMethod()).thenReturn("GET");

        authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    @Test
    public void testDoFilter_shouldDoNothing2() throws IOException, ServletException {
        when(httpServletRequest.getRequestURI()).thenReturn("/user/");
        when(httpServletRequest.getMethod()).thenReturn("POST");

        authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    @Test
    public void testDoFilter_3() throws IOException, ServletException, DataAccessException {
        when(httpServletRequest.getRequestURI()).thenReturn("/user/");
        when(httpServletRequest.getHeader("X-Token")).thenReturn("validtoken");
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(userDAOMock.getUser(1)).thenReturn(testUser);
        when(tokenManagerMock.validateAccessToken("")).thenReturn(false);

        authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    @Test
    public void testDoFilter_4() throws IOException, ServletException, DataAccessException {
        String token = "token:1:HASH:6516516516516";
        when(httpServletRequest.getRequestURI()).thenReturn("/event/");
        when(httpServletRequest.getHeader("X-Token")).thenReturn(token);
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getParameter(Matchers.eq("user_id"))).thenReturn("" + testUser.getUserIdentifier());
        when(userDAOMock.getCredentials(Matchers.eq(1))).thenReturn(testCredentials);
        when(tokenManagerMock.validateAccessToken(token)).thenReturn(true);

        authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    @Test
    public void testDoFilter_5() throws IOException, ServletException, DataAccessException {
        testCredentials = UserCredentialsFactory.build("test123", 1, "ROLE_ADMIN");
        String token = "token:1:HASH:6516516516516";
        when(httpServletRequest.getRequestURI()).thenReturn("/user/");
        when(httpServletRequest.getHeader("X-Token")).thenReturn(token);
        when(httpServletRequest.getMethod()).thenReturn("DELETE");
        when(userDAOMock.getCredentials(Matchers.eq(1))).thenReturn(testCredentials);
        when(tokenManagerMock.validateAccessToken(token)).thenReturn(true);

        authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    @Test
    public void testDoFilter_shouldThrowException() throws IOException, ServletException, DataAccessException {
        testCredentials = UserCredentialsFactory.build("test123", 1, "ROLE_ADMIN");
        String token = "token:1:HASH:6516516516516";
        when(httpServletRequest.getRequestURI()).thenReturn("/user/");
        when(httpServletRequest.getHeader("X-Token")).thenReturn(token);
        when(httpServletRequest.getMethod()).thenReturn("DELETE");
        when(userDAOMock.getCredentials(Matchers.eq(1))).thenThrow(new DataAccessException(""));
        when(tokenManagerMock.validateAccessToken(token)).thenReturn(true);

        authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
