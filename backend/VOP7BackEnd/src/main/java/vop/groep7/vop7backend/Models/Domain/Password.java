package vop.groep7.vop7backend.Models.Domain;

import java.util.Arrays;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import vop.groep7.vop7backend.Security.Hasher;

/**
 *
 * @author Backend Team
 */
public class Password {

    private final byte[] bytes;
    private final String hexString;

    /**
     * Create new Password object of a plain text password
     *
     * @param password plain text
     */
    public Password(String password) {
        this.bytes = Hasher.getInstance().hash(password);
        if (this.bytes.length == 0) {
            this.hexString = null;
        } else {
            this.hexString = new HexBinaryAdapter().marshal(this.bytes);
        }
    }

    /**
     * Create new Password object of a hashed password
     *
     * @param bytes byte array of hashed password
     */
    public Password(byte[] bytes) {
        this.bytes = bytes;
        this.hexString = new HexBinaryAdapter().marshal(this.bytes);
    }

    /**
     * Get hashed password as byte array
     *
     * @return byte array of hashed password
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Get hashed password as hex string
     *
     * @return hex string of hashed password
     */
    public String getHexString() {
        return hexString;
    }

    /**
     * Check if two passwords match
     *
     * @param other password object
     * @return true if passwords match, else false
     */
    public boolean comparePasswords(Password other) {
        return Arrays.equals(bytes, other.getBytes());
    }
}
