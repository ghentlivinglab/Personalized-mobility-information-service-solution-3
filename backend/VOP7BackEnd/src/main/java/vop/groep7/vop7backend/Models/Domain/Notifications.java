package vop.groep7.vop7backend.Models.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import vop.groep7.vop7backend.Models.Model;

/**
 *
 * @author Backend Team
 */
public class Notifications implements Model, Serializable {

    private final ArrayList<EventType> notifyForEventTypes;
    private final boolean notifyEmail;
    private final boolean notifyCellPhone;

    /**
     * The public constructor with all fields required to create a Notifications object
     * 
     * @param notifyEmail boolean that indicates if the user wants to be
     * notified by email
     * @param notifyCellPhone boolean that indicates if the user wants to be
     * notified by SMS
     * @param types list of the event types for which the user wishes to be kept
     * informed
     */
    public Notifications(boolean notifyEmail, boolean notifyCellPhone, ArrayList<EventType> types) {
        if (types != null) {
            this.notifyForEventTypes = types;
        } else {
            this.notifyForEventTypes = new ArrayList<>();
        }
        this.notifyEmail = notifyEmail;
        this.notifyCellPhone = notifyCellPhone;
    }

    /**
     * Get the list of event types for which the user wishes to be kept
     * informed
     *
     * @return list of event types
     * @see EventType
     */
    public ArrayList<EventType> getNotifyForEventTypes() {
        return new ArrayList(notifyForEventTypes);
    }

    /**
     * Add an event type to the list of event types for which the user wishes
     * to be kept informed
     *
     * @param type the event type to be added
     * @see EventType
     */
    public void addType(EventType type) {
        if (type != null) {
            notifyForEventTypes.add(type);
        }
    }

    /**
     * Remove an event type to the list of event types for which the user
     * wishes to be kept informed
     *
     * @param type the event type to be removed
     * @see EventType
     */
    public void removeType(EventType type) {
        if (type != null) {
            notifyForEventTypes.remove(type);
        }
    }

    /**
     * Get if the user wants to be notified by email
     *
     * @return boolean
     */
    public boolean isNotifyEmail() {
        return notifyEmail;
    }

    /**
     * Get if the user wants to be notified by SMS
     *
     * @return boolean
     */
    public boolean isNotifyCellPhone() {
        return notifyCellPhone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Notifications other = (Notifications) obj;
        return this.notifyForEventTypes.equals(other.notifyForEventTypes)
                && Objects.equals(this.notifyEmail, other.notifyEmail)
                && Objects.equals(this.notifyCellPhone, other.notifyCellPhone);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.notifyForEventTypes);
        hash = 13 * hash + (this.notifyEmail ? 1 : 0);
        hash = 13 * hash + (this.notifyCellPhone ? 1 : 0);
        return hash;
    }
}
