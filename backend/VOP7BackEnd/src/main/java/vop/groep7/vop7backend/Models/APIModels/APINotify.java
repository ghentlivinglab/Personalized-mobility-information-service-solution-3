package vop.groep7.vop7backend.Models.APIModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.Objects;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public class APINotify implements Serializable, Model {

    @JsonProperty("email")
    private boolean emailNotify;
    @JsonProperty("cell_number")
    private boolean cellNumberNotify;

    /**
     * Compare this APINotify object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APINotify)) {
            return false;
        }
        APINotify other = (APINotify) o;

        return Objects.equals(isEmailNotify(), other.isEmailNotify())
                && Objects.equals(isCellNumberNotify(), other.isCellNumberNotify());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.emailNotify ? 1 : 0);
        hash = 89 * hash + (this.cellNumberNotify ? 1 : 0);
        return hash;
    }

    /**
     * Getter for email validated
     *
     * @return Email validated
     */
    @JsonSerialize
    @JsonProperty("email")
    public boolean isEmailNotify() {
        return emailNotify;
    }

    /**
     * Setter for email validated
     *
     * @param emailNotify Email validated
     */
    @JsonSerialize
    @JsonProperty("email")
    public void setEmailNotify(boolean emailNotify) {
        this.emailNotify = emailNotify;
    }

    /**
     * Getter for cell phone validated
     *
     * @return Cell phone validated
     */
    @JsonSerialize
    @JsonProperty("cell_number")
    public boolean isCellNumberNotify() {
        return cellNumberNotify;
    }

    /**
     * Setter for cell phone validated
     *
     * @param cellNumberNotify Cell phone validated
     */
    @JsonSerialize
    @JsonProperty("cell_number")
    public void setCellNumberNotify(boolean cellNumberNotify) {
        this.cellNumberNotify = cellNumberNotify;
    }
}
