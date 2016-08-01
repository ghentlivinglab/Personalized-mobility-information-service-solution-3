package vop.groep7.vop7backend.Exceptions;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Backend Team
 */
public class AuthenticationFailedResponseException extends ResponseException {

    /**
     * Constructor for a AuthenticationFailedResponseException
     *
     * @param message the message of the exception
     * @param invalidFields the list with invalid fields
     */
    public AuthenticationFailedResponseException(String message, List<String> invalidFields) {
        super(HttpStatus.UNAUTHORIZED, message, invalidFields);
    }
    
}
