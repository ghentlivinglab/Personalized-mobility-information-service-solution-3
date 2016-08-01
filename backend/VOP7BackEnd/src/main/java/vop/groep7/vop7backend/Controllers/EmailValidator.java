package vop.groep7.vop7backend.Controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import vop.groep7.vop7backend.AppConfig;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Backend Team
 */
@Configurable
public class EmailValidator extends Thread {

    private final User user;
    private final String validationCode;
    
    private MailService mailService;

    /**
     * Create a UserValidator object
     *
     * @param mailService the mail service object
     * @param user the user whose email address has to be validated
     * @param validationCode A new validation code for the email
     */
    public EmailValidator(MailService mailService, User user, String validationCode) {
        super();
        this.user = user;
        this.validationCode=validationCode;
        this.mailService=mailService;
    }

    private void validateEmail(User u) {
        try {
            // save code in db
            AppConfig.getUserController().getUserDAO().createEmailValidationPin(u.getUserIdentifier(), validationCode);
        } catch (DataAccessException ex) {
            Logger.getLogger(Validator.class.getName()).log(Level.SEVERE, "The generated email pin for the user could not be saved in the db.", ex);
        }
        // create message
        String messageHead;
        // set correct salutation
        String firstName = u.getName().getFirstName();
        String lastName = u.getName().getLastName();
        if ((firstName == null) && (lastName == null)) {
            messageHead = "Beste nieuwe gebruiker,\n\n";
        } else {
            messageHead = "Beste";
            if (firstName != null) {
                messageHead += " " + firstName;
                if (lastName != null) {
                    messageHead += " " + lastName;
                }
            } else {
                messageHead += " Mr./Mevr. " + lastName;
            }
            messageHead += ",\n\n";
        }
        String message = messageHead + AppConfig.getApplicationProperty("message.validator.email.main") +
                AppConfig.getApplicationProperty("message.validator.email.link.base") +
                u.getEmail() + "/" +
                validationCode +
                AppConfig.getApplicationProperty("message.tail") + AppConfig.getApplicationProperty("message.footer");
        // send validation email with code
        mailService.sendMailToOne(u.getEmail(), AppConfig.getApplicationProperty("message.validator.email.subject"), message);
    }

    @Override
    public void run() {
        validateEmail(user);
    }
}

