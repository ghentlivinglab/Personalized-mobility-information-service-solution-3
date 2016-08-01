package vop.groep7.vop7backend.Controllers;

import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import vop.groep7.vop7backend.Models.Domain.User;

/**
 *
 * @author Backend Team
 */
public class Validator {


    private final String numbers = "0123456789";
    private final int validationCodeLength = 14;

    private final SecureRandom randomGenerator;

    @Autowired
    private MailService mailService;

    /**
     * Create a validator object
     */
    public Validator() {
        randomGenerator = new SecureRandom();
    }

    /**
     * Ask the user for validation by sending him an email and/or sms with
     * instructions
     *
     * @param user the user whose email address has to be validated
     * @see User
     */
    public void askForValidation(User user) {
        // validate email
        EmailValidator ev = new EmailValidator(mailService, user, getNewValidationCode());
        ev.start();

    }

    /**
     * Generate a random validation code
     * 
     * @return A random code
     */
    public String getNewValidationCode() {
        String newCode = "";
        for (int i = 0; i < validationCodeLength; i++) {
            newCode += numbers.charAt(randomGenerator.nextInt(numbers.length()));
        }
        return newCode;
    }

}
