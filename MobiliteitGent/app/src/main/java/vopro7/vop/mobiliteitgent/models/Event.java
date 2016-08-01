package vopro7.vop.mobiliteitgent.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author App Team
 */
public class Event extends Model {

    private Link link;
    private boolean active;
    private Coordinate coordinates;
    private String street;
    private String country;
    private String city;
    private String publicationTime;
    private String lastEditTime;
    private String description;
    private List<Jam> jams;
    private Source source;
    private EventType type;
    private List<Transport> releventTransportationTypes;

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public String getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

    public String getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(String lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Jam> getJams() {
        return jams;
    }

    public void setJams(List<Jam> jams) {
        this.jams = jams;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public List<Transport> getReleventTransportationTypes() {
        return releventTransportationTypes;
    }

    public void setReleventTransportationTypes(List<Transport> releventTransportationTypes) {
        this.releventTransportationTypes = releventTransportationTypes;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public void loadFromJson(JSONObject json) throws JSONException {
        setActive(json.optBoolean("active", true));
        setDescription(json.optString("description", null));
        setStreet(json.optString("street", null));
        setCountry(json.optString("country", null));
        setCity(json.optString("city", null));
        JSONObject coordinates = json.optJSONObject("coordinates");
        if(coordinates != null) {
            Coordinate c = new Coordinate();
            c.loadFromJson(coordinates);
            setCoordinates(c);
        }

        JSONArray links = json.optJSONArray("links");
        if(links != null) {
            Link link = new Link();
            link.loadFromJson(links.getJSONObject(0));
            setLink(link);
        }

        setPublicationTime(json.optString("publication_time"));
        setLastEditTime(json.optString("last_edit_time"));
        JSONObject source = json.optJSONObject("source");
        if(source != null) {
            Source s = new Source();
            s.loadFromJson(source);
            setSource(s);
        }

        JSONObject type = json.optJSONObject("type");
        if(type != null) {
            EventType et = new EventType();
            et.loadFromJson(type);
            setType(et);
        }

        JSONArray jsonJams = json.optJSONArray("jams");
        if(jsonJams != null) {
            List<Jam> jams = new ArrayList<>();
            for (int i = 0; i < jsonJams.length(); i++) {
                JSONObject jam = jsonJams.optJSONObject(i);
                if(jam != null) {
                    Jam j = new Jam();
                    j.loadFromJson(jam);
                    jams.add(j);
                }
            }
            setJams(jams);
        }

        JSONArray jsonTransports = json.optJSONArray("relevant_for_transportation_types");
        if(jsonTransports != null) {
            List<Transport> transports = new ArrayList<>();
            for (int i = 0; i < jsonTransports.length(); i++) {
                String transport = jsonTransports.optString(i);
                if(transport != null) {
                    Transport t = new Transport();
                    JSONObject jsonTransport = new JSONObject();
                    jsonTransport.put("type", transport);
                    t.loadFromJson(jsonTransport);
                    transports.add(t);
                }
            }
            setReleventTransportationTypes(transports);
        }
    }

    @Override
    public String toString() {
        return getType().getType() + ": " + getDescription();
    }
}
