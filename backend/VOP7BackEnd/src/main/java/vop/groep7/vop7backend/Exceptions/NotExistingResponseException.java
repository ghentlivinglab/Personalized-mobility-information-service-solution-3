package vop.groep7.vop7backend.Exceptions;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Backend Team
 */
public class NotExistingResponseException extends ResponseException {

    /**
     * Constructor for a NotExistingResponseException
     *
     * @param message The custom message of this error
     */
    public NotExistingResponseException(String message) {
        super(HttpStatus.NOT_FOUND, message, new ArrayList<String>());
    }
}
