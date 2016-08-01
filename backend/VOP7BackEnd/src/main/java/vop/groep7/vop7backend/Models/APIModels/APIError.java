package vop.groep7.vop7backend.Models.APIModels;

import java.io.Serializable;
import java.util.List;
import vop.groep7.vop7backend.Exceptions.ResponseException;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public class APIError implements Serializable, Model {

    private final int code;
    private final String message;
    private final String[] fields;

    /**
     * The constructor of an error object that is send to the UI when a method
     * fails.
     *
     * @param responseError The exception that needs to be shown to the UI
     */
    public APIError(ResponseException responseError) {
        code = responseError.getCode().value();
        message = responseError.getMessage();
        List<String> invalidFields = responseError.getFields();
        fields = new String[invalidFields.size()];
        for (int i = 0; i < invalidFields.size(); i++) {
            fields[i] = invalidFields.get(i);
        }
    }

    /**
     * Get the status code of the error
     *
     * @return The status code of the error
     */
    public int getCode() {
        return code;
    }

    /**
     * Get the message of the error
     *
     * @return The message of the error
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the optional fields of the error
     *
     * @return The optional fields of the error
     */
    public String[] getFields() {
        return fields;
    }
}
