package vopro7.vop.mobiliteitgent.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author App Team
 */
public class Link extends Model{

    private String rel;
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    @Override
    public void loadFromJson(JSONObject json) throws JSONException {
        setRel(json.optString("rel", null));
        setHref(json.optString("href", null));
    }
}
