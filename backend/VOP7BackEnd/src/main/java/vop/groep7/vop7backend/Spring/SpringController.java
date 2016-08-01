package vop.groep7.vop7backend.Spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vop.groep7.vop7backend.Exceptions.AuthenticationFailedResponseException;
import vop.groep7.vop7backend.Exceptions.DatabaseResponseException;
import vop.groep7.vop7backend.Exceptions.ExistingResponseException;
import vop.groep7.vop7backend.Exceptions.IncorrectValidationResponseException;
import vop.groep7.vop7backend.Exceptions.InvalidInputResponseException;
import vop.groep7.vop7backend.Exceptions.NonActiveResponseException;
import vop.groep7.vop7backend.Exceptions.NotExistingResponseException;
import vop.groep7.vop7backend.Models.APIModels.APIAddress;
import vop.groep7.vop7backend.Models.APIModels.APIError;

/**
 *
 * @author Backend Team
 */
@RestController
public abstract class SpringController {

    private static final Pattern VALID_COUNTRY_REGEX = Pattern.compile("^[A-Z]{2}$");

    /**
     * A Spring exception handler that will handle NotExistingResponseExceptions
     *
     * @param ex A NotExistingResponseException that is caught by Spring
     * @see APIError
     * @return A APIError object created from the exception
     */
    @ExceptionHandler(NotExistingResponseException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody
    APIError handleNotExistingResponseError(NotExistingResponseException ex) {
        return new APIError(ex);
    }

    /**
     * A Spring exception handler that will handle ExistingResponseExceptions
     *
     * @param ex A ExistingResponseException that is caught by Spring
     * @see APIError
     * @return A APIError object created from the exception
     */
    @ExceptionHandler(ExistingResponseException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public @ResponseBody
    APIError handleExistingResponseError(ExistingResponseException ex) {
        return new APIError(ex);
    }

    /**
     * A Spring exception handler that will handle DatabaseResponseExceptions
     *
     * @param ex A DatabaseResponseException that is caught by Spring
     * @see APIError
     * @return A APIError object created from the exception
     */
    @ExceptionHandler(DatabaseResponseException.class)
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    public @ResponseBody
    APIError handleDatabaseResponseException(DatabaseResponseException ex) {
        return new APIError(ex);
    }

    /**
     * A Spring exception handler that will handle NonActiveResponseExceptions
     *
     * @param ex A NonActiveResponseException that is caught by Spring
     * @see APIError
     * @return A APIError object created from the exception
     */
    @ExceptionHandler(NonActiveResponseException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public @ResponseBody
    APIError handleNonActiveResponseException(NonActiveResponseException ex) {
        return new APIError(ex);
    }

    /**
     * A Spring exception handler that will handle
     * AuthenticationFailedResponseException
     *
     * @param ex A AuthenticationFailedResponseException that is caught by
     * Spring
     * @return A APIError object created from the exception
     */
    @ExceptionHandler(AuthenticationFailedResponseException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public @ResponseBody
    APIError handleAuthenticationFailedResponseException(AuthenticationFailedResponseException ex) {
        return new APIError(ex);
    }

    /**
     * A Spring exception handler that will handle
     * IncorrectValidationResponseException
     *
     * @param ex An IncorrectValidationResponseException that is caught by
     * Spring
     * @see APIError
     * @return A APIError object created from the exception
     */
    @ExceptionHandler(IncorrectValidationResponseException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public @ResponseBody
    APIError handleIncorrectValidationResponseException(IncorrectValidationResponseException ex) {
        return new APIError(ex);
    }

    /**
     * A Spring exception handler that will handle InvalidInputResponseException
     *
     * @param ex An InvalidInputResponseException that is caught by Spring
     * @see APIError
     * @return A APIError object created from the exception
     */
    @ExceptionHandler(InvalidInputResponseException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    APIError handleInvalidInputResponseException(InvalidInputResponseException ex) {
        return new APIError(ex);
    }

    /**
     * Check the input of an address object
     *
     * @param a The address object
     * @param fieldName The field where the address can be found
     * @return A list of invalid fields
     */
    protected List<String> checkAddressInput(APIAddress a, String fieldName) {
        List<String> invalidFields = new ArrayList<>();
        if (a != null) {
            if (Objects.equals(a.getCity(), null)) {
                invalidFields.add(fieldName + ".city");
            }
            if (a.getPostalCode() <= 0) {
                invalidFields.add(fieldName + ".postal_code");
            }
            if (Objects.equals(a.getStreet(), null)) {
                invalidFields.add(fieldName + ".street");
            }
            if (Objects.equals(a.getHousenumber(), null)) {
                invalidFields.add(fieldName + ".housenumber");
            }
            if (Objects.equals(a.getCountry(), null) || !VALID_COUNTRY_REGEX.matcher(a.getCountry()).find()) {
                invalidFields.add(fieldName + ".country");
            }
            if (Objects.equals(a.getCoordinates(), null)) {
                invalidFields.add(fieldName + ".coordinates");
            } 
        } else {
            invalidFields.add(fieldName);
        }
        return invalidFields;
    }
}
