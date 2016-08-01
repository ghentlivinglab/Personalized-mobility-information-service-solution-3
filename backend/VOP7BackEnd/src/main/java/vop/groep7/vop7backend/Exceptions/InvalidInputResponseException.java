package vop.groep7.vop7backend.Exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Backend team
 */
public class InvalidInputResponseException extends ResponseException {
    
    /**
     * Constructor for an InvalidInputResponseException
     *
     * @param invalidFields The fields that are wrong
     */
    public InvalidInputResponseException(List<String> invalidFields) {
        super(HttpStatus.BAD_REQUEST, "The input of one or more fields was invalid or missing", invalidFields);
    }
    
}
