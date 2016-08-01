package vop.groep7.vop7backend.Models.APIModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import vop.groep7.vop7backend.Models.APIModel;

/**
 *
 * @author Backend Team
 */
public class APISource extends APIModel {

    private String name;
    @JsonProperty("icon_url")
    private String iconUrl;

    /**
     * Compare this APISource object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APISource)) {
            return false;
        }
        APISource other = (APISource) o;

        return Objects.equals(getName(), other.getName())
                && Objects.equals(getIconUrl(), other.getIconUrl());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.name);
        hash = 41 * hash + Objects.hashCode(this.iconUrl);
        return hash;
    }

    /**
     * Get the name of the source
     *
     * @return The name of the source
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the source
     *
     * @param name The name of the source
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get an url to the icon of this source
     *
     * @return An url to the icon of this source
     */
    @JsonSerialize
    @JsonProperty("icon_url")
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * Set an url to the icon of this source
     *
     * @param iconUrl An url to the icon of this source
     */
    @JsonSerialize
    @JsonProperty("icon_url")
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
