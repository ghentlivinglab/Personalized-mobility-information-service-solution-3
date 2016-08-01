/*package vop7backend;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import vop.groep7.vop7backend.AppConfig;
import vop.groep7.vop7backend.Controllers.EventController;
import vop.groep7.vop7backend.Controllers.MailService;
import vop.groep7.vop7backend.Controllers.POIController;
import vop.groep7.vop7backend.Controllers.Processor;
import vop.groep7.vop7backend.Controllers.RouteController;
import vop.groep7.vop7backend.Controllers.TravelController;
import vop.groep7.vop7backend.Controllers.UserController;
import vop.groep7.vop7backend.Controllers.UtilController;
import vop.groep7.vop7backend.Controllers.Validator;
import vop.groep7.vop7backend.Security.TokenManager;

/**
 *
 * @author Backend team
 */
/*public class AppConfigTest {

    private AbstractApplicationContext contextMock;

    public AppConfigTest() {
    }

    @Before
    public void setUp() {
        contextMock = mock(AbstractApplicationContext.class);

        AppConfig.setContext(contextMock);
    }

    
    
    @Test
    public void getUserController_shouldReturnUserController() {
        when(contextMock.getBean(Matchers.<Class>any())).thenReturn(new UserController());
        assertNotNull(AppConfig.getUserController());
    }

    @Test
    public void getPOIController_shouldReturnPOIController() {
        when(contextMock.getBean(Matchers.<Class>any())).thenReturn(new POIController());
        assertNotNull(AppConfig.getPOIController());
    }

    @Test
    public void getTravelController_shouldReturnTravelController() {
        when(contextMock.getBean(Matchers.<Class>any())).thenReturn(new TravelController());
        assertNotNull(AppConfig.getTravelController());
    }

    @Test
    public void getRouteController_shouldReturnRouteController() {
        when(contextMock.getBean(Matchers.<Class>any())).thenReturn(new RouteController());
        assertNotNull(AppConfig.getRouteController());
    }

    @Test
    public void getUtilController_shouldReturnUtilController() {
        when(contextMock.getBean(Matchers.<Class>any())).thenReturn(new UtilController());
        assertNotNull(AppConfig.getUtilController());
    }

    @Test
    public void getEventController_shouldReturnEventController() {
        when(contextMock.getBean(Matchers.<Class>any())).thenReturn(new EventController());
        assertNotNull(AppConfig.getEventController());
    }

    @Test
    public void getMailSender_shouldReturnMailSender() {
        when(contextMock.getBean(Matchers.<Class>any())).thenReturn(new JavaMailSenderImpl());
        assertNotNull(AppConfig.getMailSender());
    }

    @Test
    public void getMailService_shouldReturnMailService() {
        when(contextMock.getBean(Matchers.<Class>any())).thenReturn(new MailService());
        assertNotNull(AppConfig.getMailService());
    }

    @Test
    public void getValidator_shouldReturnValidator() {
        when(contextMock.getBean(Matchers.<Class>any())).thenReturn(new Validator());
        assertNotNull(AppConfig.getValidator());
    }

    @Test
    public void getTokenManager_shouldReturnTokenManager() {
        when(contextMock.getBean(Matchers.<Class>any())).thenReturn(new TokenManager());
        assertNotNull(AppConfig.getTokenManager());
    }

    @Test
    public void getProcessor_shouldReturnProcessor() {
        when(contextMock.getBean(Matchers.<Class>any())).thenReturn(new Processor());
        assertNotNull(AppConfig.getProcessor());
    }

}
*/