package Database.sql;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.UserDAO;
import vop.groep7.vop7backend.factories.UserCredentialsFactory;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Backend Team
 */
public class UserDAOTest {

    @Mock
    private Connection connectionMock;

    @InjectMocks
    private UserDAO userDAO;

    public UserDAOTest() {
    }

    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUser_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getUser(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getCredentialsByEmail_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getCredentials("test@example.com");
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getCredentialsById_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getCredentials(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getUsers_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getUsers();
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getUserIds_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getUserIds();
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void userExists_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.userExists("test@example.com");
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void userIdExists_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.userIdExists(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void createCredentials_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.createCredentials(UserCredentialsFactory.create("test", UserFactory.create(1, "", "test", "test", "", true)));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void createUser_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.createUser(UserFactory.create(1, "", "test", "test", "", true));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void deleteUser_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.deleteUser(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void deleteCredentials_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.deleteCredentials(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void modifyUser_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.modifyUser(1, UserFactory.create(1, "", "test", "test", "", true));
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void modifyCredentials_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.modifyCredentials(null);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getEmailValidationPin_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getEmailValidationPin(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void createEmailValidationPin_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.createEmailValidationPin(1, "pin");
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void deleteEmailValidationPin_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.deleteEmailValidationPin(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getPasswordResetPin_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getPasswordResetPin(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getUserIdFromPasswordPin_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getUserIdFromPasswordPin("PIN");
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getMatchedEvents_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getMatchedEvents(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void createMatch_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.createMatch(1, 1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void deleteMatchedEvent_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.deleteMatchedEvent(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void deleteMatchedEvents_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.deleteMatchedEvents(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void createPasswordResetPin_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.createPasswordResetPin(1, "PIN");
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void deletePasswordResetPin_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.deletePasswordResetPin(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void createUserPins_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.createUserPins(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void deleteUserPins_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.deleteUserPins(1);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getMatchedUsers_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getMatchedUsers(0);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void deleteMatch_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.deleteMatch(0, 0);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getUsersWithRole_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getUsersWithRole();
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getIdFromRefreshToken_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getIdFromRefreshToken("token_test");
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void updateRefreshToken_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.updateRefreshToken("0", 0, null);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void createRefreshToken_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.createRefreshToken("0", 0, null);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void deleteRefreshTokenByToken_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.deleteRefreshToken("0");
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void deleteRefreshTokenById_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.deleteRefreshToken(0);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void getAllTokens_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.getAllTokens(0);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }

    @Test
    public void areMatched_shouldThrowException() {
        try {
            when(connectionMock.prepareStatement(Matchers.<String>any())).thenThrow(new SQLException());

            boolean thrown = false;
            try {
                userDAO.areMatched(0, 0);
            } catch (DataAccessException ex) {
                thrown = true;
            }
            assertTrue(thrown);
        } catch (SQLException ex) {
            fail("SQLException should not be thrown!");
        }
    }
}
