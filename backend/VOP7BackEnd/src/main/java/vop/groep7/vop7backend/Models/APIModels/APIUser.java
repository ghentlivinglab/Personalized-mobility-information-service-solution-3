package vop.groep7.vop7backend.Models.APIModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Arrays;
import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APIUser extends APIModel {

    private String id;
    private APILinks[] links;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String password;
    private String email;
    @JsonProperty("cell_number")
    private String cellNumber;
    @JsonProperty("mute_notifications")
    private boolean muteNotifications;
    private APIValidation validated;

    /**
     * Compare this APIUser object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APIUser)) {
            return false;
        }
        APIUser other = (APIUser) o;

        return Objects.equals(getFirstName(), other.getFirstName())
                && Objects.equals(getLastName(), other.getLastName())
                && Objects.equals(getEmail(), other.getEmail())
                && Objects.equals(getCellNumber(), other.getCellNumber())
                && Objects.equals(getPassword(), other.getPassword())
                && Objects.equals(isMuteNotifications(), other.isMuteNotifications())
                && Arrays.deepEquals(getLinks(), other.getLinks())
                && Objects.equals(getValidated(), other.getValidated());

    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Arrays.deepHashCode(this.links);
        hash = 37 * hash + Objects.hashCode(this.firstName);
        hash = 37 * hash + Objects.hashCode(this.lastName);
        hash = 37 * hash + Objects.hashCode(this.password);
        hash = 37 * hash + Objects.hashCode(this.email);
        hash = 37 * hash + Objects.hashCode(this.cellNumber);
        hash = 37 * hash + (this.muteNotifications ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.validated);
        return hash;
    }

    /**
     * Getter for id
     *
     * @return The Id of the user
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id The Id of the user
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for firstName
     *
     * @return The first name of the user
     */
    @JsonSerialize
    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for firstName
     *
     * @param firstName The first name of the user
     */
    @JsonSerialize
    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for lastName
     *
     * @return The last name of the user
     */
    @JsonSerialize
    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for lastName
     *
     * @param lastName The last name of the user
     */
    @JsonSerialize
    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for cellNumber
     *
     * @return The cell number number of the user
     */
    @JsonSerialize
    @JsonProperty("cell_number")
    public String getCellNumber() {
        return cellNumber;
    }

    /**
     * Setter for cellNumber
     *
     * @param cellNumber The cell number of the user
     */
    @JsonSerialize
    @JsonProperty("cell_number")
    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    /**
     * Getter for email
     *
     * @return The email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email
     *
     * @param email The email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the links
     *
     * @return The links of this object
     */
    public APILinks[] getLinks() {
        return links;
    }

    /**
     * Setter for the links
     *
     * @param links The links of this object
     */
    public void setLinks(APILinks[] links) {
        this.links = links;
    }

    /**
     * Getter for the password
     *
     * @return The password of this user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password
     *
     * @param password The password of this user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for muteNotifications
     *
     * @return False if the user wants notifications
     */
    @JsonSerialize
    @JsonProperty("mute_notifications")
    public boolean isMuteNotifications() {
        return muteNotifications;
    }

    /**
     * Setter for muteNotifications
     *
     * @param muteNotifications False if the user wants notifications
     */
    @JsonSerialize
    @JsonProperty("mute_notifications")
    public void setMuteNotifications(boolean muteNotifications) {
        this.muteNotifications = muteNotifications;
    }

    /**
     * Getter for validated
     *
     * @return The validation of the user
     */
    public APIValidation getValidated() {
        return validated;
    }

    /**
     * Setter for validated
     *
     * @param validated The validation of the user
     */
    public void setValidated(APIValidation validated) {
        this.validated = validated;
    }
}
