package vopro7.vop.mobiliteitgent.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author App Team
 */
public class Source extends Model {

    private String name;
    private String iconUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public void loadFromJson(JSONObject json) throws JSONException {
        setName(json.optString("name", null));
        setIconUrl(json.optString("icon_url", null));
    }
}
