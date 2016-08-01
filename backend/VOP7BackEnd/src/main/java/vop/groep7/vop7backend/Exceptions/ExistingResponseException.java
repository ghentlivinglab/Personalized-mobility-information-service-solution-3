package vop.groep7.vop7backend.Exceptions;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Backend Team
 */
public class ExistingResponseException extends ResponseException {

    /**
     * Constructor for a ExistingResponseException
     *
     * @param message The custom message of this error
     */
    public ExistingResponseException(String message) {
        super(HttpStatus.CONFLICT, message, new ArrayList<String>());
    }

}
