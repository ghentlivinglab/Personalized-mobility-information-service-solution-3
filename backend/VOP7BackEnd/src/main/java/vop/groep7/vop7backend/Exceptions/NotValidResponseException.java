package vop.groep7.vop7backend.Exceptions;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Backend Team
 */
public class NotValidResponseException extends ResponseException {

    /**
     * Constructor for a NotValidResponseException
     *
     * @param message The custom message of this error
     */
    public NotValidResponseException(String message) {
        super(HttpStatus.NOT_ACCEPTABLE, message, new ArrayList<String>());
    }
}
