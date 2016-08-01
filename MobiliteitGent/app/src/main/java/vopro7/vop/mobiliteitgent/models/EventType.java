package vopro7.vop.mobiliteitgent.models;

import org.json.JSONException;
import org.json.JSONObject;

import vopro7.vop.mobiliteitgent.other.Translator;

/**
 * @author App Team
 */
public class EventType extends Model {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void loadFromJson(JSONObject json) throws JSONException {
        setType(Translator.getEventTypeTranslation(json.optString("type", null)));
    }
}
