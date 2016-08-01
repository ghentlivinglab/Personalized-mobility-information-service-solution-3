package vop.groep7.vop7backend.Models.APIModels.Authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APIVerification extends APIModel {

    private String email;
    @JsonProperty("email_verification_pin")
    private String emailVerificationPin;
    @JsonProperty("cell_number_verification_pin")
    private String cellNumberVerificationPin;

    /**
     * Compare this APIVerification object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final APIVerification other = (APIVerification) o;

        return Objects.equals(this.email, other.email)
                && Objects.equals(this.emailVerificationPin, other.emailVerificationPin)
                && Objects.equals(this.cellNumberVerificationPin, other.cellNumberVerificationPin);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.email);
        hash = 97 * hash + Objects.hashCode(this.emailVerificationPin);
        hash = 97 * hash + Objects.hashCode(this.cellNumberVerificationPin);
        return hash;
    }

    /**
     * Get the email address to be verified
     *
     * @return the address email
     */
    @JsonSerialize
    public String getEmail() {
        return email;
    }

    /**
     * Set the email address to be verified
     *
     * @param email the email address
     */
    @JsonSerialize
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the email verification pin
     *
     * @return The email verification pin
     */
    @JsonSerialize
    @JsonProperty("email_verification_pin")
    public String getEmailVerificationPin() {
        return emailVerificationPin;
    }

    /**
     * Set the email verification pin
     *
     * @param emailVerificationPin The email verification pin
     */
    @JsonSerialize
    @JsonProperty("email_verification_pin")
    public void setEmailVerificationPin(String emailVerificationPin) {
        this.emailVerificationPin = emailVerificationPin;
    }

    /**
     * Get the cell phone verification pin
     *
     * @return The cell phone verification pin
     */
    @JsonSerialize
    @JsonProperty("cell_number_verification_pin")
    public String getCellNumberVerificationPin() {
        return cellNumberVerificationPin;
    }

    /**
     * Set the cell phone verification pin
     *
     * @param cellNumberVerificationPin The cell phone verification pin
     */
    @JsonSerialize
    @JsonProperty("cell_number_verification_pin")
    public void setCellNumberVerificationPin(String cellNumberVerificationPin) {
        this.cellNumberVerificationPin = cellNumberVerificationPin;
    }
}
