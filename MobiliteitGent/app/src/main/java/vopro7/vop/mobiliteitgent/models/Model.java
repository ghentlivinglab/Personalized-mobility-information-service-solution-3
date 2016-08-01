package vopro7.vop.mobiliteitgent.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author App Team
 */
public abstract class Model implements Serializable {

    public abstract void loadFromJson(JSONObject json) throws JSONException;
}
