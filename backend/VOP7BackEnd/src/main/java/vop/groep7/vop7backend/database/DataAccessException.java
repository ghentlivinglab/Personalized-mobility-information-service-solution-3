package vop.groep7.vop7backend.database;

/**
 *
 * @author Backend Team
 */
public class DataAccessException extends Exception {

    /**
     * A specific exception that is created by giving it a message
     *
     * @param message A specification of the exception
     */
    public DataAccessException(String message) {
        super(message);
    }

    /**
     * A specific exception that is created by giving it a message and another
     * exception
     *
     * @param message A specification of the exception
     * @param ex Another exception that is thrown by another method
     */
    public DataAccessException(String message, Exception ex) {
        super(message, ex);
    }

}
