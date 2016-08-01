package vop.groep7.vop7backend.Exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Backend team
 */
public class IncorrectValidationResponseException extends ResponseException {
    
    /**
     * Constructor for an IncorrectValidationResponseException
     *
     * @param message The custom message of this error
     * @param invalidFields The fields that are wrong
     */
    public IncorrectValidationResponseException(String message, List<String> invalidFields) {
        super(HttpStatus.UNAUTHORIZED, message, invalidFields);
    }
    
}
