package vop.groep7.vop7backend.Models.Domain.Events;

import java.sql.Timestamp;
import java.util.ArrayList;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Jam;
import vop.groep7.vop7backend.Models.Domain.Source;
import vop.groep7.vop7backend.Models.Domain.Transport;

/**
 *
 * @author Backend Team
 */
public class JamEvent extends Event {

    private final ArrayList<Jam> jams;

    /**
     * The public constructor with all fields required to create a JamEvent object
     * 
     * @param eventIdentifier the unique identifier of the event
     * @param location the address of where the event is located
     * @param active boolean that indicates if the event is active
     * @param source the source of the event
     * @param description the description of the event
     * @param jams the list of jams of this event
     * @param type the type of this event
     * @param publicationTime the publication time and date of this event
     * @param lastEditTime the time and date of when the event is last edited
     * @param transportTypes a list of transport types for which this event 
     * is relevant
     */
    public JamEvent(int eventIdentifier, Address location, boolean active, Source source, String description, Timestamp publicationTime, Timestamp lastEditTime, EventType type, ArrayList<Transport> transportTypes, ArrayList<Jam> jams) {
        super(eventIdentifier, location, active, source, description, publicationTime, lastEditTime, type, transportTypes);
        this.jams = jams;
    }

    /**
     * Get the list of jams of this event
     * 
     * @return the list of jams
     * @see Jam
     */
    public ArrayList<Jam> getJams() {
        return (ArrayList<Jam>) jams.clone();
    }

    /**
     * Add a jam to the list of jams
     * 
     * @param jam the jam to be added
     * @see Jam
     */
    public void addJam(Jam jam) {
        jams.add(jam);
        update();
    }

    /**
     * Remove a jam to the list of jams
     * 
     * @param jam the jam to removed added
     * @see Jam
     */
    public void removeJam(Jam jam) {
        jams.remove(jam);
        update();
    } 
}
