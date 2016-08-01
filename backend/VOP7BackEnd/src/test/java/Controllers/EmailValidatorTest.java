package Controllers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Controllers.EmailValidator;
import vop.groep7.vop7backend.Controllers.MailService;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Backen Team
 */
public class EmailValidatorTest {
    
    @Mock
    private MailService mailServiceMock; 
    
    @InjectMocks
    private EmailValidator emailValidator;
    
    private User testUser;
    
    public EmailValidatorTest() {
    }
    
    @Before
    public void setUp() {
        emailValidator = new EmailValidator(null, testUser, "1234567");
        testUser = UserFactory.build(1, "George.Vermalen@example.com", "George", "Vermalen", null, false, false, false);
        
         // Mock annotations
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void start_(){
        emailValidator.start();
    }
    
}
