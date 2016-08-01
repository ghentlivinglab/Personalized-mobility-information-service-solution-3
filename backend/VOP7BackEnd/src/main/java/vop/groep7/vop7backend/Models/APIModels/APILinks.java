package vop.groep7.vop7backend.Models.APIModels;

import java.io.Serializable;
import java.util.Objects;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public class APILinks implements Serializable, Model {

    private String rel;
    private String href;

    /**
     * Compare this APILinks object with another object
     *
     * @param o another object
     * @return true if the objects are identical
     */
    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof APILinks)) {
            return false;
        }
        APILinks other = (APILinks) o;
        return Objects.equals(getRel(), other.getRel())
                && Objects.equals(getHref(), other.getHref());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.rel);
        hash = 29 * hash + Objects.hashCode(this.href);
        return hash;
    }

    /**
     * Get the relation of the linked object (ex. self)
     *
     * @return The relation of the linked object
     */
    public String getRel() {
        return rel;
    }

    /**
     * Set the relation of the linked object (ex. self)
     *
     * @param rel The relation of the linked object
     */
    public void setRel(String rel) {
        this.rel = rel;
    }

    /**
     * Get the actual link in full URL
     *
     * @return The actual link in full URL
     */
    public String getHref() {
        return href;
    }

    /**
     * Set the actual link in full URL
     *
     * @param href The actual link in full URL
     */
    public void setHref(String href) {
        this.href = href;
    }
}
