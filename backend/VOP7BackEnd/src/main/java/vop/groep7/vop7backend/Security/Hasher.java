package vop.groep7.vop7backend.Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Backend Team
 */
public class Hasher {

    public final static Hasher INSTANCE = new Hasher();

    /**
     * Get the only Hasher object
     *
     * @return the Hasher object
     */
    public static synchronized Hasher getInstance() {
        return INSTANCE;
    }

    /**
     * Create hash to secure an input value
     *
     * @param input The string we want secured
     * @return A byte array
     */
    public byte[] hash(String input) {
        if (input != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(input.getBytes());
                return hash;
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Hasher.class.getName()).log(Level.SEVERE, "Somethig went wrong while hashing.", ex);
            }
        }
        return new byte[0];
    }
}
