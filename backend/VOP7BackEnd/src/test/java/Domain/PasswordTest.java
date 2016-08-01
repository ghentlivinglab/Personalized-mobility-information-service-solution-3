package Domain;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import vop.groep7.vop7backend.Models.Domain.Password;

/**
 *
 * @author Backend Team
 */
public class PasswordTest {

    public PasswordTest() {
    }

    @Test
    public void getHexString_shouldReturnCorrectHash() {
        Password password = new Password("password123");
        assertEquals("EF92B778BAFE771E89245B89ECBC08A44A4E166C06659911881F383D4473E94F", password.getHexString());
    }

    @Test
    public void getBytes_shouldReturnCorrectBytes() {
        String correctHexString = "EF92B778BAFE771E89245B89ECBC08A44A4E166C06659911881F383D4473E94F";
        Password password = new Password("password123");
        assertArrayEquals(new HexBinaryAdapter().unmarshal(correctHexString), password.getBytes());
    }

    @Test
    public void comparePasswords_shouldReturnTrue() {
        Password pass1 = new Password("password");
        Password pass2 = new Password("password");
        assertTrue(pass1.comparePasswords(pass2));
    }

    @Test
    public void comparePasswords_shouldReturnFalse() {
        Password pass1 = new Password("password");
        Password pass2 = new Password("otherpassword");
        assertFalse(pass1.comparePasswords(pass2));
    }
}
