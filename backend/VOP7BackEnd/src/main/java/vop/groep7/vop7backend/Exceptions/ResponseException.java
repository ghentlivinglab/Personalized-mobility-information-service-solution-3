package vop.groep7.vop7backend.Exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Backend Team
 */
public abstract class ResponseException extends RuntimeException {

    /**
     * The HTTP status code of this response
     */
    protected final HttpStatus code;
    protected final List<String> fields;

    /**
     * Constructor for an abstract ResponseException
     *
     * @param code The HTTP status code of the error
     * @param message The custom message of this error
     * @param fields The fields that are wrong
     */
    public ResponseException(HttpStatus code, String message, List<String> fields) {
        super(message);
        this.code = code;
        this.fields=fields;
    }

    /**
     * Get the HTTP code of this error
     *
     * @return The code of this error
     */
    public HttpStatus getCode() {
        return code;
    }
    
    /**
     * Get the list of invalid fields
     * 
     * @return the list of invalid fields. (empty if not applicable to exception)
     */
    public List<String> getFields(){
        return fields;
    }
}
