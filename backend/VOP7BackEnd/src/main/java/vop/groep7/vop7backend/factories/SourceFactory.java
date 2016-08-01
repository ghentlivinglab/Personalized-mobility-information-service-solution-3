package vop.groep7.vop7backend.factories;

import vop.groep7.vop7backend.Models.APIModels.APISource;
import vop.groep7.vop7backend.Models.Domain.Source;

/**
 *
 * @author Backend Team
 */
public class SourceFactory {

    /**
     * Create a new Source object
     * 
     * @param name The name of the source
     * @param icon The icon of the source
     * @return A new Source object
     */
    public static Source create(String name, String icon) {
        Source source = new Source(name, icon);
        return source;
    }

    /**
     * Build a valid Source object
     * @param name The name of the source
     * @param icon The icon of the source
     * @return A valid Source object
     */
    public static Source build(String name, String icon) {
        Source source = new Source(name, icon);
        return source;
    }

    /**
     * Create an APISource object starting from a Source
     * 
     * @param source A Source object
     * @return A APISource object
     */
    public static APISource toAPIModel(Source source) {
        APISource result = new APISource();
        result.setName(source.getName());
        if(source.getIconUrl() != null){
            result.setIconUrl(source.getIconUrl().toString());
        }
        return result;
    }

    /**
     * Create a Source object starting from an APISource
     * 
     * @param source An APISource object
     * @return A Source object
     */
    public static Source toDomainModel(APISource source) {
        Source result = new Source(source.getName(), source.getIconUrl());
        return result;
    }
}
