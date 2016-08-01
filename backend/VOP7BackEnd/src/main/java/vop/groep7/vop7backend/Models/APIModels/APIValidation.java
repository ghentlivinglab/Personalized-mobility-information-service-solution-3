package vop.groep7.vop7backend.Models.APIModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public class APIValidation implements Serializable, Model {

    @JsonProperty("email")
    private boolean emailValidated;
    @JsonProperty("cell_number")
    private boolean cellNumberValidated;

    /**
     * Compare this APIValidation object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIValidation)) {
            return false;
        }
        APIValidation other = (APIValidation) o;

        return isEmailValidated() == other.isEmailValidated()
                && isCellNumberValidated() == other.isCellNumberValidated();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (this.emailValidated ? 1 : 0);
        hash = 19 * hash + (this.cellNumberValidated ? 1 : 0);
        return hash;
    }

    /**
     * Getter for email validated
     *
     * @return Email validated
     */
    @JsonSerialize
    @JsonProperty("email")
    public boolean isEmailValidated() {
        return emailValidated;
    }

    /**
     * Setter for email validated
     *
     * @param emailValidated Email validated
     */
    @JsonSerialize
    @JsonProperty("email")
    public void setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    /**
     * Getter for cell phone validated
     *
     * @return Cell phone validated
     */
    @JsonSerialize
    @JsonProperty("cell_number")
    public boolean isCellNumberValidated() {
        return cellNumberValidated;
    }

    /**
     * Setter for cell phone validated
     *
     * @param cellNumberValidated Cell phone validated
     */
    @JsonSerialize
    @JsonProperty("cell_number")
    public void setCellNumberValidated(boolean cellNumberValidated) {
        this.cellNumberValidated = cellNumberValidated;
    }
}
