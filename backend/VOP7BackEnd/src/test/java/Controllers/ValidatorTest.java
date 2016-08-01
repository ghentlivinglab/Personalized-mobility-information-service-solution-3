package Controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vop.groep7.vop7backend.Controllers.MailService;
import vop.groep7.vop7backend.Controllers.Validator;
import vop.groep7.vop7backend.factories.UserFactory;

/**
 *
 * @author Backend Team
 */
public class ValidatorTest {

    @Mock
    private MailService mailServiceMock;

    @InjectMocks
    private Validator validator;

    public ValidatorTest() {
    }

    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void askForValidationTest(){
        validator.askForValidation(UserFactory.build(1, "George.Vermalen@example.com", "George", "Vermalen", null, false, false, false));
    }

}
