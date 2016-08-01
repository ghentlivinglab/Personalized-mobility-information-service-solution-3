package vop.groep7.vop7backend.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import vop.groep7.vop7backend.AppConfig;

/**
 *
 * @author Backend Team
 */
public class MailService {

    @Autowired
    private MailSender mailSender;

    private final List<String> blacklistedPatterns;

    /**
     * Create the mail service and add the testing email domain
     */
    public MailService() {
        blacklistedPatterns = AppConfig.getEmailBlacklistPatterns();
    }

    /**
     * Send an email to one recipient
     *
     * @param emailAddress the email address of the recipient
     * @param subject the subject of the email
     * @param message the message to be send
     */
    public void sendMailToOne(String emailAddress, String subject, String message) {
        if (!isBlacklisted(emailAddress)) {
            SimpleMailMessage mail = new SimpleMailMessage();
            // create mail
            mail.setFrom(AppConfig.getApplicationProperty("message.sender.address"));
            mail.setTo(emailAddress);
            mail.setSubject(subject);
            mail.setText(message);
            // send mail
            mailSender.send(mail);
        }
    }

    /**
     * Send an email to multiple recipients
     *
     * @param emailList the list of email addresses of the recipients
     * @param subject the subject of the email
     * @param message the message to be send
     */
    public void sendMailToMany(List<String> emailList, String subject, String message) {
        emailList.stream().forEach((emailAddress) -> {
            sendMailToOne(emailAddress, subject, message);
        });
    }

    private boolean isBlacklisted(String emailAddress) {
        for (int i = 0; i < blacklistedPatterns.size(); i++) {
            if (emailAddress.contains(blacklistedPatterns.get(i))) {
                return true;
            }
        }
        return false;
    }

}
