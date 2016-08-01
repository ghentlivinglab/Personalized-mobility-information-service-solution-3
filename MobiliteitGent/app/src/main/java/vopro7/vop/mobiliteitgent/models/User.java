package vopro7.vop.mobiliteitgent.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author App Team
 */
public class User extends Model {

    private String email;
    private String firstName;
    private String lastName;
    private String cellNumber;
    private boolean emailValidated;
    private boolean mute;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    @Override
    public void loadFromJson(JSONObject json) throws JSONException {
        setEmail(json.optString("email", null));
        setCellNumber(json.optString("cell_number", null));
        JSONObject validation = json.optJSONObject("validated");
        setEmailValidated(validation.optBoolean("email"));
        setFirstName(json.optString("first_name", null));
        setLastName(json.optString("last_name", null));
        setMute(json.optBoolean("mute_notifications"));
    }
}
