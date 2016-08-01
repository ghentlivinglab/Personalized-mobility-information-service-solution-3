package vop.groep7.vop7backend.Models.APIModels.Authentication;

import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APICredentials extends APIModel {

    private String email;
    private String password;

    /**
     * Compare this APICredentials object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APICredentials)) {
            return false;
        }
        APICredentials other = (APICredentials) o;

        return Objects.equals(getEmail(), other.getEmail())
                && Objects.equals(getPassword(), other.getPassword());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.email);
        hash = 11 * hash + Objects.hashCode(this.password);
        return hash;
    }

    /**
     * Get the email address
     *
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email address
     *
     * @param email The email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the password
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password
     *
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
