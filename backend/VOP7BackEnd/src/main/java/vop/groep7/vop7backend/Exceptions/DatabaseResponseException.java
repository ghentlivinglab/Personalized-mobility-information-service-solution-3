package vop.groep7.vop7backend.Exceptions;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Backend Team
 */
public class DatabaseResponseException extends ResponseException {

    /**
     * Constructor for a DatabaseResponseException
     *
     * @param message The custom message of this error
     */
    public DatabaseResponseException(String message) {
        super(HttpStatus.I_AM_A_TEAPOT, message, new ArrayList<String>());
    }
}
