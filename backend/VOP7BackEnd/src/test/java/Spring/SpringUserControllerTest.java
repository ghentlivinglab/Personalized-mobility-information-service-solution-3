package Spring;

import Other.TestUtils;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONObject;

import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vop.groep7.vop7backend.Controllers.UserController;
import vop.groep7.vop7backend.Models.APIModels.APILinks;
import vop.groep7.vop7backend.Models.APIModels.APIUser;
import vop.groep7.vop7backend.Models.APIModels.APIUserAdmin;
import vop.groep7.vop7backend.Models.APIModels.APIValidation;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIChangePassword;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APICredentials;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIRole;
import vop.groep7.vop7backend.Models.APIModels.Authentication.APIVerification;
import vop.groep7.vop7backend.Spring.SpringUserController;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Jonas Van Wilder
 * @author Simon Scheerlynck
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SpringUserControllerTest {

    @Mock
    private UserController userControllerMock;

    @InjectMocks
    private SpringUserController springUserController;

    private MockMvc mockMvc;

    private List<APIUser> testUsers;
    private JSONObject testUserJson;
    private APIUser modifiedTestUser1;
    private String modifiedTestUserJson;
    private APICredentials testAPICredentials;
    private APIChangePassword testAPIChange;
    private JSONObject testAPICredentialsJson;
    private JSONObject testAPIChangeJson;
    private JSONObject testAPIVerificationJson;
    private List<APIUserAdmin> testUsersAdmin;

    private static final String BASIC_ERROR = "The database is not available at the moment.";
    private static final String EXISTS_ERROR = "The user already exists.";
    private static final String NOT_EXISTS_ERROR = "The user doesn't exist.";
    private static final String INVALID_INPUT = "The input of one or more fields was invalid or missing";
    private static final String VALIDATION_FAILED = "A verification pin is not (longer) valid or the email does not exist.";

    /**
     *
     */
    public SpringUserControllerTest() {
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
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        // Mock annotations
        MockitoAnnotations.initMocks(this);

        // Setup as standalone
        this.mockMvc = MockMvcBuilders.standaloneSetup(springUserController).build();

        testUsers = new ArrayList<>();
        testUsersAdmin = new ArrayList<>();
        // create json object of a test user
        testUserJson = new JSONObject();
        testUserJson.put("id", "1");
        APILinks[] l = new APILinks[1];
        l[0] = new APILinks();
        l[0].setRel("self");
        l[0].setHref("https://vopro7.ugent.be/user/0");
        testUserJson.put("links", l);
        testUserJson.put("first_name", "Sandra");
        testUserJson.put("last_name", "Vermeulen");
        testUserJson.put("password", "password");
        testUserJson.put("email", "sandrav@example.com");
        testUserJson.put("cell_number", "+32463962564");
        testUserJson.put("mute_notifications", false);
        testUserJson.put("validated", new APIValidation());

        createTestUsers();
        createModifiedtestUser();
        createTestCredentials();
        createTestVerification();
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    private void createTestUsers() {
        // create test user 1
        APIUser testUser1 = new APIUser();
        testUser1.setId("0");
        testUser1.setFirstName("Liam");
        testUser1.setLastName("Vermaelen");
        testUser1.setEmail("liamv@example.com");
        testUser1.setCellNumber("+32487982341");
        testUser1.setMuteNotifications(false);
        testUser1.setPassword("password");
        APILinks[] links1 = new APILinks[1];
        links1[0] = new APILinks();
        links1[0].setRel("self");
        links1[0].setHref("https://vopro7.ugent.be/user/0");
        testUser1.setLinks(links1);
        testUser1.setValidated(new APIValidation());
        testUsers.add(testUser1);

        // create test user 2
        APIUser testUser2 = new APIUser();
        testUser2.setId("1");
        testUser2.setFirstName("Sandra");
        testUser2.setLastName("Vermeulen");
        testUser2.setEmail("sandrav@example.com");
        testUser2.setCellNumber("+32463962564");
        testUser2.setMuteNotifications(false);
        testUser2.setPassword("password");
        APILinks[] links2 = new APILinks[1];
        links2[0] = new APILinks();
        links2[0].setRel("self");
        links2[0].setHref("https://vopro7.ugent.be/user/1");
        testUser2.setLinks(links2);
        testUser2.setValidated(new APIValidation());
        testUsers.add(testUser2);

        // create test user admin 2
        APIUserAdmin testUserAdmin1 = new APIUserAdmin(testUser1);
        testUserAdmin1.setRole(APIRole.ROLE_USER);
        testUsersAdmin.add(testUserAdmin1);

        APIUserAdmin testUserAdmin2 = new APIUserAdmin(testUser2);
        testUserAdmin2.setRole(APIRole.ROLE_OPERATOR);
        testUsersAdmin.add(testUserAdmin2);
    }

    private void createModifiedtestUser() {
        modifiedTestUser1 = testUsers.get(0);
        modifiedTestUser1.setFirstName("Lianna");
        modifiedTestUser1.setLastName("Van Dingelen");
        modifiedTestUser1.setEmail("liannavd@example.com");
        modifiedTestUser1.setCellNumber("+32487986741");
        modifiedTestUser1.setMuteNotifications(true);
        modifiedTestUser1.setPassword("password2");
        APIValidation v = new APIValidation();
        v.setEmailValidated(true);
        v.setCellNumberValidated(true);
        modifiedTestUser1.setValidated(v);

        // create json string of a modified test user
        modifiedTestUserJson = "{"
                + "\"id\":\"0\","
                + "\"links\":"
                + "["
                + "{"
                + "\"rel\":\"self\","
                + "\"href\":\"https://vopro7.ugent.be/user/0\""
                + "}"
                + "],"
                + "\"first_name\":\"Lianna\","
                + "\"last_name\":\"Van Dingelen\","
                + "\"password\":\"password2\","
                + "\"email\":\"liannavd@example.com\","
                + "\"cell_number\":\"+32487986741\","
                + "\"mute_notifications\":true,"
                + "\"validated\":{"
                + "\"email\": false,"
                + "\"cell_number\": false"
                + "}"
                + "}";
    }

    private void createTestCredentials() {
        testAPICredentials = new APICredentials();
        testAPICredentials.setEmail("test@example.com");
        // do not set password

        testAPICredentialsJson = new JSONObject();
        testAPICredentialsJson.put("email", "test@example.com");
        // do not set password

        testAPIChange = new APIChangePassword();
        testAPIChangeJson = new JSONObject();
    }

    private void createTestVerification() {
        testAPIVerificationJson = new JSONObject();
        testAPIVerificationJson.put("email", "test@example.com");
        testAPIVerificationJson.put("email_verification_pin", "021585");
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        when(userControllerMock.getAPIUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(testUsers.size())))
                .andExpect(jsonPath("$[0].id", is(testUsers.get(0).getId())))
                .andExpect(jsonPath("$[0].links", hasSize(testUsers.get(0).getLinks().length)))
                .andExpect(jsonPath("$[0].links[0].rel", is(testUsers.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[0].links[0].href", is(testUsers.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[0].first_name", is(testUsers.get(0).getFirstName())))
                .andExpect(jsonPath("$[0].last_name", is(testUsers.get(0).getLastName())))
                .andExpect(jsonPath("$[0].email", is(testUsers.get(0).getEmail())))
                .andExpect(jsonPath("$[0].cell_number", is(testUsers.get(0).getCellNumber())))
                .andExpect(jsonPath("$[0].mute_notifications", is(testUsers.get(0).isMuteNotifications())))
                .andExpect(jsonPath("$[0].validated.email", is(testUsers.get(0).getValidated().isEmailValidated())))
                .andExpect(jsonPath("$[0].validated.cell_number", is(testUsers.get(0).getValidated().isCellNumberValidated())))
                .andExpect(jsonPath("$[1].id", is(testUsers.get(1).getId())))
                .andExpect(jsonPath("$[1].links", hasSize(testUsers.get(1).getLinks().length)))
                .andExpect(jsonPath("$[1].links[0].rel", is(testUsers.get(1).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[1].links[0].href", is(testUsers.get(1).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[1].first_name", is(testUsers.get(1).getFirstName())))
                .andExpect(jsonPath("$[1].last_name", is(testUsers.get(1).getLastName())))
                .andExpect(jsonPath("$[1].email", is(testUsers.get(1).getEmail())))
                .andExpect(jsonPath("$[1].cell_number", is(testUsers.get(1).getCellNumber())))
                .andExpect(jsonPath("$[1].mute_notifications", is(testUsers.get(1).isMuteNotifications())))
                .andExpect(jsonPath("$[1].validated.email", is(testUsers.get(1).getValidated().isEmailValidated())))
                .andExpect(jsonPath("$[1].validated.cell_number", is(testUsers.get(1).getValidated().isCellNumberValidated())));

        verify(userControllerMock, times(1)).getAPIUsers();
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getAllUsers_ShouldReturnEmptyList() throws Exception {
        when(userControllerMock.getAPIUsers()).thenReturn(new ArrayList());

        mockMvc.perform(get("/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(userControllerMock, times(1)).getAPIUsers();
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     *
     * @throws Exception
     */
    @Test
    public void getAllUsers_ShouldReturnWentWrong() throws Exception {
        when(userControllerMock.getAPIUsers()).thenThrow(new DataAccessException(""));

        mockMvc.perform(get("/user/"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(userControllerMock, times(1)).getAPIUsers();
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getUser_shouldReturnAPIUser() throws Exception {
        when(userControllerMock.getAPIUser(0)).thenReturn(testUsers.get(0));

        mockMvc.perform(get("/user/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testUsers.get(0).getId())))
                .andExpect(jsonPath("$.links", hasSize(testUsers.get(0).getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(testUsers.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(testUsers.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$.first_name", is(testUsers.get(0).getFirstName())))
                .andExpect(jsonPath("$.last_name", is(testUsers.get(0).getLastName())))
                .andExpect(jsonPath("$.email", is(testUsers.get(0).getEmail())))
                .andExpect(jsonPath("$.cell_number", is(testUsers.get(0).getCellNumber())))
                .andExpect(jsonPath("$.mute_notifications", is(testUsers.get(0).isMuteNotifications())))
                .andExpect(jsonPath("$.validated.email", is(testUsers.get(0).getValidated().isEmailValidated())))
                .andExpect(jsonPath("$.validated.cell_number", is(testUsers.get(0).getValidated().isCellNumberValidated())));

        verify(userControllerMock, times(1)).getAPIUser(0);
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getUser_shouldReturnAPIUserNotFound() throws Exception {
        when(userControllerMock.getAPIUser(0)).thenReturn(null);

        mockMvc.perform(get("/user/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(userControllerMock, times(1)).getAPIUser(0);
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getUser_shouldReturnWentWrong() throws Exception {
        when(userControllerMock.getAPIUser(0)).thenThrow(new DataAccessException(""));

        mockMvc.perform(get("/user/0"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(userControllerMock, times(1)).getAPIUser(0);
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createUser_shouldCreateCorrect() throws Exception {
        when(userControllerMock.createAPIUser(Matchers.<APIUser>any())).thenReturn(testUsers.get(1));

        mockMvc.perform(post("/user/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testUserJson.toString().getBytes()))
                .andExpect(status().is(201))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testUsers.get(1).getId())))
                .andExpect(jsonPath("$.links", hasSize(testUsers.get(1).getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(testUsers.get(1).getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(testUsers.get(1).getLinks()[0].getHref())))
                .andExpect(jsonPath("$.first_name", is(testUsers.get(1).getFirstName())))
                .andExpect(jsonPath("$.last_name", is(testUsers.get(1).getLastName())))
                .andExpect(jsonPath("$.email", is(testUsers.get(1).getEmail())))
                .andExpect(jsonPath("$.cell_number", is(testUsers.get(1).getCellNumber())))
                .andExpect(jsonPath("$.mute_notifications", is(testUsers.get(1).isMuteNotifications())))
                .andExpect(jsonPath("$.validated.email", is(testUsers.get(1).getValidated().isEmailValidated())))
                .andExpect(jsonPath("$.validated.cell_number", is(testUsers.get(1).getValidated().isCellNumberValidated())));

        verify(userControllerMock, times(1)).createAPIUser(Matchers.<APIUser>any());
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createUser_shouldReturnAlreadyExists() throws Exception {
        when(userControllerMock.createAPIUser(Matchers.<APIUser>any())).thenReturn(null);

        mockMvc.perform(post("/user/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testUserJson.toString().getBytes()))
                .andExpect(status().is(409))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(409)));

        verify(userControllerMock, times(1)).createAPIUser(Matchers.<APIUser>any());
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void createUser_shouldReturnWentWrong() throws Exception {
        when(userControllerMock.createAPIUser(Matchers.<APIUser>any())).thenThrow(new DataAccessException(""));

        mockMvc.perform(post("/user/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testUserJson.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(userControllerMock, times(1)).createAPIUser(Matchers.<APIUser>any());
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void createUser_shouldReturnInvalidInput() throws Exception {
        testUserJson.put("email", "testATexample.com");
        mockMvc.perform(post("/user/")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testUserJson.toString().getBytes()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(1)))
                .andExpect(jsonPath("$.fields[0]", is("email")));

        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyUser_shouldModifyCorrect() throws Exception {
        when(userControllerMock.modifyAPIUser(eq(0), Matchers.<APIUser>any())).thenReturn(modifiedTestUser1);

        mockMvc.perform(put("/user/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestUserJson.getBytes()))
                .andExpect(status().is(200))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(modifiedTestUser1.getId())))
                .andExpect(jsonPath("$.links", hasSize(modifiedTestUser1.getLinks().length)))
                .andExpect(jsonPath("$.links[0].rel", is(modifiedTestUser1.getLinks()[0].getRel())))
                .andExpect(jsonPath("$.links[0].href", is(modifiedTestUser1.getLinks()[0].getHref())))
                .andExpect(jsonPath("$.first_name", is(modifiedTestUser1.getFirstName())))
                .andExpect(jsonPath("$.last_name", is(modifiedTestUser1.getLastName())))
                .andExpect(jsonPath("$.email", is(modifiedTestUser1.getEmail())))
                .andExpect(jsonPath("$.cell_number", is(modifiedTestUser1.getCellNumber())))
                .andExpect(jsonPath("$.mute_notifications", is(modifiedTestUser1.isMuteNotifications())))
                .andExpect(jsonPath("$.validated.email", is(modifiedTestUser1.getValidated().isEmailValidated())))
                .andExpect(jsonPath("$.validated.cell_number", is(modifiedTestUser1.getValidated().isCellNumberValidated())));

        verify(userControllerMock, times(1)).modifyAPIUser(eq(0), Matchers.<APIUser>any());
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyUser_shouldReturnAlreadyExisting() throws Exception {
        when(userControllerMock.modifyAPIUser(eq(0), Matchers.<APIUser>any())).thenThrow(new DataAccessException(""));
        when(userControllerMock.getAPIUser(eq(0))).thenReturn(testUsers.get(0));

        mockMvc.perform(put("/user/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestUserJson.getBytes()))
                .andExpect(status().is(409))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(409)));

        verify(userControllerMock, times(1)).modifyAPIUser(eq(0), Matchers.<APIUser>any());
        verify(userControllerMock, times(1)).getAPIUser(eq(0));
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyUser_shouldReturnNotExisting() throws Exception {
        when(userControllerMock.modifyAPIUser(eq(0), Matchers.<APIUser>any())).thenThrow(new DataAccessException(""));
        when(userControllerMock.getAPIUser(eq(0))).thenReturn(null);

        mockMvc.perform(put("/user/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestUserJson.getBytes()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(userControllerMock, times(1)).modifyAPIUser(eq(0), Matchers.<APIUser>any());
        verify(userControllerMock, times(1)).getAPIUser(eq(0));
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void modifyUser_shouldReturnWentWrong() throws Exception {
        when(userControllerMock.modifyAPIUser(eq(0), Matchers.<APIUser>any())).thenThrow(new DataAccessException(""));
        when(userControllerMock.getAPIUser(eq(0))).thenThrow(new DataAccessException(""));

        mockMvc.perform(put("/user/0")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(modifiedTestUserJson.getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(userControllerMock, times(1)).modifyAPIUser(eq(0), Matchers.<APIUser>any());
        verify(userControllerMock, times(1)).getAPIUser(eq(0));
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deleteUser_shouldDeleteCorrect() throws Exception {
        when(userControllerMock.deleteAPIUser(1)).thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().is(204));

        verify(userControllerMock, times(1)).deleteAPIUser(1);
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deleteUser_shouldReturnNotExisting() throws Exception {
        when(userControllerMock.deleteAPIUser(1)).thenReturn(Boolean.FALSE);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(userControllerMock, times(1)).deleteAPIUser(1);
        verifyNoMoreInteractions(userControllerMock);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void deleteUser_shouldReturnWentWrong() throws Exception {
        when(userControllerMock.deleteAPIUser(1)).thenThrow(new DataAccessException(""));

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(userControllerMock, times(1)).deleteAPIUser(1);
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void forgotPasswordRequest_shouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/user/forgot_password_request")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPICredentialsJson.toString().getBytes()))
                .andExpect(status().is(200));
        verify(userControllerMock, times(1)).forgotPasswordRequest(Matchers.<APICredentials>any());
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void forgotPasswordRequest_shouldReturnInvalidEmail() throws Exception {
        testAPICredentialsJson.put("email", null);
        mockMvc.perform(post("/user/forgot_password_request")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPICredentialsJson.toString().getBytes()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(1)))
                .andExpect(jsonPath("$.fields[0]", is("email")));

        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void forgotPasswordRequest_shouldReturnWentWrong() throws Exception {
        testAPICredentialsJson.put("password", "test123");
        Mockito.doThrow(new DataAccessException("")).when(userControllerMock).forgotPasswordRequest(Matchers.<APICredentials>any());
        mockMvc.perform(post("/user/forgot_password_request")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPICredentialsJson.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(userControllerMock, times(1)).forgotPasswordRequest(Matchers.<APICredentials>any());
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void forgotPasswordResponse_shouldReturnSuccess() throws Exception {
        testAPICredentialsJson.put("password", "test123");
        when(userControllerMock.forgotPasswordResponse(Matchers.eq("TOKEN"), Matchers.<APICredentials>any())).thenReturn(true);

        mockMvc.perform(post("/user/forgot_password?password_token=TOKEN")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPICredentialsJson.toString().getBytes()))
                .andExpect(status().is(200));

        verify(userControllerMock, times(1)).forgotPasswordResponse(Matchers.eq("TOKEN"), Matchers.<APICredentials>any());
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void forgotPasswordResponse_shouldReturnInvalidPassword() throws Exception {
        mockMvc.perform(post("/user/forgot_password?password_token=TOKEN")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPICredentialsJson.toString().getBytes()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(1)))
                .andExpect(jsonPath("$.fields[0]", is("password")));

        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void forgotPasswordResponse_shouldReturnFailed() throws Exception {
        testAPICredentials.setPassword("test123");
        testAPICredentialsJson.put("password", "test123");
        when(userControllerMock.forgotPasswordResponse("TOKEN", testAPICredentials)).thenReturn(false);
        mockMvc.perform(post("/user/forgot_password?password_token=TOKEN")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPICredentialsJson.toString().getBytes()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(VALIDATION_FAILED)))
                .andExpect(jsonPath("$.code", is(401)))
                .andExpect(jsonPath("$.fields", hasSize(1)))
                .andExpect(jsonPath("$.fields[0]", is("password_token")));

        verify(userControllerMock, times(1)).forgotPasswordResponse(Matchers.eq("TOKEN"), Matchers.<APICredentials>any());
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void modifyPassword_shouldModifyCorrect() throws Exception {
        when(userControllerMock.modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"))).thenReturn(true);
        testAPIChange.setOldPassword("test123");
        testAPIChange.setNewPassword("test124");
        testAPIChangeJson.put("old_password", "test123");
        testAPIChangeJson.put("new_password", "test124");

        mockMvc.perform(post("/user/1/modify_password")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIChangeJson.toString().getBytes()))
                .andExpect(status().isOk());

        verify(userControllerMock, times(1)).modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"));
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void modifyPassword_shouldReturnNotExisting() throws Exception {
        testAPIChange.setOldPassword("test123");
        testAPIChange.setNewPassword("test124");
        testAPIChangeJson.put("old_password", "test123");
        testAPIChangeJson.put("new_password", "test124");
        Mockito.doThrow(new DataAccessException("")).when(userControllerMock).modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"));
        when(userControllerMock.getAPIUser(Matchers.eq(1))).thenReturn(null);

        mockMvc.perform(post("/user/1/modify_password")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIChangeJson.toString().getBytes()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(NOT_EXISTS_ERROR)))
                .andExpect(jsonPath("$.code", is(404)));

        verify(userControllerMock, times(1)).modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"));
        verify(userControllerMock, times(1)).getAPIUser(Matchers.eq(1));
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void modifyPassword_shouldReturnInvalidInput() throws Exception {
        testAPIChange.setOldPassword("test123");
        testAPIChange.setNewPassword("test124");
        testAPIChangeJson.put("old_password", "test123");
        testAPIChangeJson.put("new_password", null);
        Mockito.doThrow(new DataAccessException("")).when(userControllerMock).modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"));
        when(userControllerMock.getAPIUser(Matchers.eq(1))).thenReturn(new APIUser());

        mockMvc.perform(post("/user/1/modify_password")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIChangeJson.toString().getBytes()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(2)))
                .andExpect(jsonPath("$.fields[0]", is("old_password")))
                .andExpect(jsonPath("$.fields[1]", is("new_password")));

        verify(userControllerMock, times(0)).modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"));
        verify(userControllerMock, times(0)).getAPIUser(Matchers.eq(1));
        verifyNoMoreInteractions(userControllerMock);
    }
    @Test
    public void modifyPassword_shouldReturnInvalidInput2() throws Exception {
        testAPIChange.setOldPassword("test123");
        testAPIChange.setNewPassword("test124");
        testAPIChangeJson.put("old_password", null);
        testAPIChangeJson.put("new_password", "test124");
        Mockito.doThrow(new DataAccessException("")).when(userControllerMock).modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"));
        when(userControllerMock.getAPIUser(Matchers.eq(1))).thenReturn(new APIUser());

        mockMvc.perform(post("/user/1/modify_password")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIChangeJson.toString().getBytes()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(2)))
                .andExpect(jsonPath("$.fields[0]", is("old_password")))
                .andExpect(jsonPath("$.fields[1]", is("new_password")));

        verify(userControllerMock, times(0)).modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"));
        verify(userControllerMock, times(0)).getAPIUser(Matchers.eq(1));
        verifyNoMoreInteractions(userControllerMock);
    }
    
    @Test
    public void modifyPassword_shouldReturnInvalidInput3() throws Exception {
        testAPIChange.setOldPassword("test123");
        testAPIChange.setNewPassword("test124");
        testAPIChangeJson.put("old_password", "test123");
        testAPIChangeJson.put("new_password", "test124");
        Mockito.doThrow(new DataAccessException("")).when(userControllerMock).modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"));
        when(userControllerMock.getAPIUser(Matchers.eq(1))).thenReturn(new APIUser());

        mockMvc.perform(post("/user/1/modify_password")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIChangeJson.toString().getBytes()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(INVALID_INPUT)))
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.fields", hasSize(2)))
                .andExpect(jsonPath("$.fields[0]", is("old_password")))
                .andExpect(jsonPath("$.fields[1]", is("new_password")));

        verify(userControllerMock, times(1)).modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"));
        verify(userControllerMock, times(1)).getAPIUser(Matchers.eq(1));
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void modifyPassword_shouldReturnWentWrong() throws Exception {
        testAPIChange.setOldPassword("test123");
        testAPIChange.setNewPassword("test124");
        testAPIChangeJson.put("old_password", "test123");
        testAPIChangeJson.put("new_password", "test124");
        Mockito.doThrow(new DataAccessException("")).when(userControllerMock).modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"));
        when(userControllerMock.getAPIUser(Matchers.eq(1))).thenThrow(new DataAccessException(""));

        mockMvc.perform(post("/user/1/modify_password")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIChangeJson.toString().getBytes()))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(userControllerMock, times(1)).modifyPassword(Matchers.eq(1), Matchers.eq("test123"), Matchers.eq("test124"));
        verify(userControllerMock, times(1)).getAPIUser(Matchers.eq(1));
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void verifyUser_shouldVerifyCorrect() throws Exception {
        when(userControllerMock.verify(Matchers.<APIVerification>any())).thenReturn(true);

        mockMvc.perform(post("/user/verify")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIVerificationJson.toString().getBytes()))
                .andExpect(status().isOk());

        verify(userControllerMock, times(1)).verify(Matchers.<APIVerification>any());
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void verifyUser_shouldReturnInvalidInput() throws Exception {
        when(userControllerMock.verify(Matchers.<APIVerification>any())).thenReturn(false);

        mockMvc.perform(post("/user/verify")
                .contentType(TestUtils.APPLICATION_JSON)
                .content(testAPIVerificationJson.toString().getBytes()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(VALIDATION_FAILED)))
                .andExpect(jsonPath("$.code", is(401)))
                .andExpect(jsonPath("$.fields", hasSize(2)))
                .andExpect(jsonPath("$.fields[0]", is("email")))
                .andExpect(jsonPath("$.fields[1]", is("email_verification_pin")));

        verify(userControllerMock, times(1)).verify(Matchers.<APIVerification>any());
        verifyNoMoreInteractions(userControllerMock);
    }

    @Test
    public void getAllUsersAdmin_shouldReturnUsers() throws Exception {
        when(userControllerMock.getAPIUsersAdmin()).thenReturn(testUsersAdmin);

        mockMvc.perform(get("/admin/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(testUsersAdmin.size())))
                .andExpect(jsonPath("$[0].id", is(testUsersAdmin.get(0).getId())))
                .andExpect(jsonPath("$[0].links", hasSize(testUsersAdmin.get(0).getLinks().length)))
                .andExpect(jsonPath("$[0].links[0].rel", is(testUsersAdmin.get(0).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[0].links[0].href", is(testUsersAdmin.get(0).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[0].first_name", is(testUsersAdmin.get(0).getFirstName())))
                .andExpect(jsonPath("$[0].last_name", is(testUsersAdmin.get(0).getLastName())))
                .andExpect(jsonPath("$[0].email", is(testUsersAdmin.get(0).getEmail())))
                .andExpect(jsonPath("$[0].cell_number", is(testUsersAdmin.get(0).getCellNumber())))
                .andExpect(jsonPath("$[0].mute_notifications", is(testUsersAdmin.get(0).isMuteNotifications())))
                .andExpect(jsonPath("$[0].validated.email", is(testUsersAdmin.get(0).getValidated().isEmailValidated())))
                .andExpect(jsonPath("$[0].validated.cell_number", is(testUsersAdmin.get(0).getValidated().isCellNumberValidated())))
                .andExpect(jsonPath("$[0].role", is(testUsersAdmin.get(0).getRole().toString())))
                .andExpect(jsonPath("$[1].id", is(testUsersAdmin.get(1).getId())))
                .andExpect(jsonPath("$[1].links", hasSize(testUsersAdmin.get(1).getLinks().length)))
                .andExpect(jsonPath("$[1].links[0].rel", is(testUsersAdmin.get(1).getLinks()[0].getRel())))
                .andExpect(jsonPath("$[1].links[0].href", is(testUsersAdmin.get(1).getLinks()[0].getHref())))
                .andExpect(jsonPath("$[1].first_name", is(testUsersAdmin.get(1).getFirstName())))
                .andExpect(jsonPath("$[1].last_name", is(testUsersAdmin.get(1).getLastName())))
                .andExpect(jsonPath("$[1].email", is(testUsersAdmin.get(1).getEmail())))
                .andExpect(jsonPath("$[1].cell_number", is(testUsersAdmin.get(1).getCellNumber())))
                .andExpect(jsonPath("$[1].mute_notifications", is(testUsersAdmin.get(1).isMuteNotifications())))
                .andExpect(jsonPath("$[1].validated.email", is(testUsersAdmin.get(1).getValidated().isEmailValidated())))
                .andExpect(jsonPath("$[1].validated.cell_number", is(testUsersAdmin.get(1).getValidated().isCellNumberValidated())))
                .andExpect(jsonPath("$[1].role", is(testUsersAdmin.get(1).getRole().toString())));

        verify(userControllerMock, times(1)).getAPIUsersAdmin();
        verifyNoMoreInteractions(userControllerMock);
    }
    /**
     *
     *
     * @throws Exception
     */
    @Test
    public void getAllUsersAdmin_ShouldReturnWentWrong() throws Exception {
        when(userControllerMock.getAPIUsersAdmin()).thenThrow(new DataAccessException(""));

        mockMvc.perform(get("/admin/user/"))
                .andExpect(status().is(418))
                .andExpect(content().contentType(TestUtils.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(BASIC_ERROR)))
                .andExpect(jsonPath("$.code", is(418)));

        verify(userControllerMock, times(1)).getAPIUsersAdmin();
        verifyNoMoreInteractions(userControllerMock);
    }
    
}
