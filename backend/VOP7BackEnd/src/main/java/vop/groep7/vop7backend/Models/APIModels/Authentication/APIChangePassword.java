package vop.groep7.vop7backend.Models.APIModels.Authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APIChangePassword extends APIModel {
    
    @JsonProperty("old_password")
    private String oldPassword;
    @JsonProperty("new_password")
    private String newPassword;

    /**
     * Compare this APIChangePassword object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIChangePassword)) {
            return false;
        }
        APIChangePassword other = (APIChangePassword) o;

        return Objects.equals(getNewPassword(), other.getNewPassword())
                && Objects.equals(getOldPassword(), other.getOldPassword());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.oldPassword);
        hash = 11 * hash + Objects.hashCode(this.newPassword);
        return hash;
    }

    /**
     * Get the old password
     *
     * @return The old password
     */
    @JsonSerialize
    @JsonProperty("old_password")
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Set the old password
     *
     * @param oldPassword The old password
     */
    @JsonSerialize
    @JsonProperty("old_password")
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * Get the new password
     *
     * @return The new password
     */
    @JsonSerialize
    @JsonProperty("new_password")
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Set the new password
     *
     * @param newPassword The new password
     */
    @JsonSerialize
    @JsonProperty("new_password")
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
