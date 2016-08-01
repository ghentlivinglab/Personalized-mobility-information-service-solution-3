package vop.groep7.vop7backend.Models.Domain;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.DomainModel;

/**
 *
 * @author Backend Team
 */
public class Travel extends DomainModel {

    private int travelIdentifier;

    private final String name;
    private final Address start;
    private final Address end;
    private final Interval interval;
    private final WeeklyRecurring recurring;

    private ArrayList<Route> routes;

    /**
     * The public constructor with all fields required to create a Travel object
     * 
     * @param name name of this travel, given by the user to recognize it
     * @param start the address of the start point of the travel
     * @param end the address of the end point of the travel
     * @param interval the interval of this travel in which the user wishes
     * to arrive or leave
     * @param recurring the weekly recurring object of this travel
     */
    public Travel(String name, Address start, Address end, Interval interval, WeeklyRecurring recurring) {
        this.travelIdentifier = -1;
        this.name = name;
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.recurring = recurring;
    }

    private boolean hasTravelIdentifier() {
        return travelIdentifier != -1;
    }

    /**
     * Set the unique identifier of this travel.
     * This can only be done once. 
     * From then the method will throw an IllegalArgumentException
     * 
     * @param travelIdentifier the unique identfier for this travel     
     */
    public void setTravelIdentifier(int travelIdentifier) {
        if (!hasTravelIdentifier()) {
            this.travelIdentifier = travelIdentifier;
        } else {
            throw new IllegalArgumentException("TravelIdentifier is already set and cannot be changed!");
        }
    }

    /**
     * Get the unique identifier of this travel
     * 
     * @return the identifier
     * @throws IllegalAccessException when the identifier is not set
     */
    public int getTravelIdentifier() throws IllegalAccessException {
        if (!hasTravelIdentifier()) {
            throw new IllegalAccessException("TravelIdentifier is not set!");
        }
        return travelIdentifier;
    }

    /**
     * Get the name of this travel given by the user to recognize it
     * 
     * @return the name of the travel
     */
    public String getName() {
        return name;
    }

   /**
     * Get the location of the start point
     * 
     * @return the location of the start point as an address object
     * @see Address
     */
    public Address getStart() {
        return start;
    }

     /**
     * Get the location of the end point
     * 
     * @return the location of the end point as an address object
     * @see Address
     */
    public Address getEnd() {
        return end;
    }

    /**
     * Get the interval of this travel
     * 
     * @return the interval object
     * @see Interval
     */
    public Interval getInterval() {
        return interval;
    }

    /**
     * Get the recurring of this travel
     * 
     * @return the WeeklyRecurring object of this travel
     * @see WeeklyRecurring
     * @see Recurring
     */
    public WeeklyRecurring getRecurring() {
        return recurring;
    }

    /**
     * Get the list of routes for this travel
     * 
     * @return list of routes
     * @see Route
     */
    public ArrayList<Route> getRoutes() {
        return (ArrayList<Route>) routes.clone();
    }

    /**
     * Add a route to this travel
     * 
     * @param route route to be added
     * @see Route
     */
    public void addRoute(Route route) {
        if (!isRoutesLoaded()) {
            routes = new ArrayList<>();
        }
        if (route != null) {
            routes.add(route);
        }
    }

    /**
     * Remove a route from this travel
     * 
     * @param routeIdentifier unique identifier of the route to be removed
     * @see Route
     */
    public void removeRoute(int routeIdentifier) {
        if (isRoutesLoaded()) {
            for (int i = 0; i < routes.size(); i++) {
                try {
                    Route r = routes.get(i);
                    if (r.getRouteIdentifier() == routeIdentifier) {
                        routes.remove(r);
                        break;
                    }
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Travel.class.getName()).log(Level.SEVERE, "Route should have an Id", ex);
                    // skip route without id (route should have id)
                }
            }
        }
    }

    /**
     * Check if the routes of this travel are loaded
     * 
     * @return true if loaded, else false
     */
    public boolean isRoutesLoaded() {
        return routes != null;
    }
}
