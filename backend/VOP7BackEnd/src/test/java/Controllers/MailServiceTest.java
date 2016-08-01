package Controllers;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import vop.groep7.vop7backend.Controllers.MailService;

/**
 *
 * @author Backend Team
 */
public class MailServiceTest {

    @Mock
    private MailSender mailSenderMock;

    @InjectMocks
    private MailService mailService;

    public MailServiceTest() {
    }

    @Before
    public void setUp() {
        // Mock annotations
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendMailToOne_shouldSend() {
        mailService.sendMailToOne("test@test.test", "test", "test test test");

        verify(mailSenderMock, times(1)).send(Matchers.<SimpleMailMessage>any());
    }

    @Test
    public void sendMailToMany_shouldSend() {
        List<String> testMailList = new ArrayList<>();
        testMailList.add("test1@test.test");
        testMailList.add("test2@test.test");
        testMailList.add("test3@test.test");
        mailService.sendMailToMany(testMailList, "test", "test test test");

        verify(mailSenderMock, times(testMailList.size())).send(Matchers.<SimpleMailMessage>any());
    }

}
