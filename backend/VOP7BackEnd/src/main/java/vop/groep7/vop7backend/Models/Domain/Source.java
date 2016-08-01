package vop.groep7.vop7backend.Models.Domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.DomainModel;

/**
 *
 * @author Backend Team
 */
public class Source extends DomainModel {

    private final String name;
    private URL iconUrl;

    /**
     * The public constructor with all fields required to create a Source object
     * 
     * @param name the name of the source
     * @param icon the full url of to get the icon of the source 
     */
    public Source(String name, String icon) {
        this.name = name;
        try {
            this.iconUrl = new URL(icon);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Source.class.getName()).log(Level.SEVERE, "Icon url could not be created!: " + ex.getMessage());
        }
    }

    /**
     * Get the name of the source
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the icon url of the source
     * 
     * @return the url of the icon
     * @see URL
     */
    public URL getIconUrl() {
        return iconUrl;
    }
}
