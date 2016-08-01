package vop.groep7.vop7backend.factories;

import vop.groep7.vop7backend.Models.APIModels.APILinks;

/**
 *
 * @author Backend Team
 */
public abstract class Factory {

    /**
     * The self field
     */
    protected static final String SELF = "self";
    
    /**
     * The basic url of the application
     */
    protected static final String URL = "https://vopro7.ugent.be/api/";
    
    /**
     * The path to a user
     */
    protected static final String USER = "user/";
    
    /**
     * The path to a travel
     */
    protected static final String TRAVEL = "/travel/";
    
    /**
     * The path to a route
     */
    protected static final String ROUTE = "/route/";
    
    /**
     * The path to a point of interest
     */
    protected static final String POI = "/point_of_interest/";
    
    /**
     * The path to an event
     */
    protected static final String EVENT = "event/";

    /**
     * Get an array link objects based on the last part of the url
     *
     * @param url The last part of the url
     * @return An array of links
     */
    public static APILinks[] getLinks(String url) {
        APILinks[] links = new APILinks[1];
        APILinks self = new APILinks();
        self.setRel(SELF);
        self.setHref(URL + url);
        links[0] = self;

        return links;
    }
}
