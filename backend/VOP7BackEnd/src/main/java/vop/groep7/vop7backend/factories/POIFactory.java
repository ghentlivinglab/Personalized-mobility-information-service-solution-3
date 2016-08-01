package vop.groep7.vop7backend.factories;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APINotify;
import vop.groep7.vop7backend.Models.APIModels.APIPOI;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.POI;

/**
 *
 * @author Backend Team
 */
public class POIFactory extends Factory {

    /**
     * Create a new POI object
     * 
     * @param address The address of this POI
     * @param name The name of this POI
     * @param radius The radius of this POI
     * @param active Whether or not the route is active
     * @param notifyEmail Whether or not email notifications are enabled for this route
     * @param notifyCellPhone Whether or not cell phone notifications are enabled for this route
     * @param types The event types for which notifications are enabled for this route
     * @return A new POI object
     */
    public static POI create(Address address, String name, int radius, boolean active, boolean notifyEmail, boolean notifyCellPhone, ArrayList<EventType> types) {
        POI poi = new POI(address, name, radius, active, notifyEmail, notifyCellPhone, types);
        return poi;
    }

    /**
     * Build a valid POI object
     * 
     * @param poiIdentifier The identifier of the POI
     * @param address The address of this POI
     * @param name The name of this POI
     * @param radius The radius of this POI
     * @param active Whether or not the route is active
     * @param notifyEmail Whether or not email notifications are enabled for this route
     * @param notifyCellPhone Whether or not cell phone notifications are enabled for this route
     * @param types The event types for which notifications are enabled for this route
     * @return A valid POI object
     */
    public static POI build(int poiIdentifier, Address address, String name, int radius, boolean active, boolean notifyEmail, boolean notifyCellPhone, ArrayList<EventType> types) {
        POI poi = new POI(address, name, radius, active, notifyEmail, notifyCellPhone, types);
        poi.setPoiIdentifier(poiIdentifier);
        return poi;
    }

    /**
     * Create an APIPOI object starting from a POI
     * 
     * @param poi A POI object
     * @param userId The identifier of the owner of the POI
     * @return A APIPOI object
     */
    public static APIPOI toAPIModel(POI poi, int userId) {
        APIPOI result = new APIPOI();
        result.setAddress(AddressFactory.toAPIModel(poi.getAddress()));
        result.setName(poi.getName());
        result.setRadius(poi.getRadius());
        result.setActive(poi.isActive());
        if (poi.getNotifications() != null) {
            APINotify notify = new APINotify();
            notify.setEmailNotify(poi.getNotifications().isNotifyEmail());
            notify.setCellNumberNotify(poi.getNotifications().isNotifyCellPhone());
            result.setNotify(notify);

            ArrayList<EventType> list = poi.getNotifications().getNotifyForEventTypes();
            APIEventType[] types = null;
            if (list != null) {
                types = new APIEventType[list.size()];
                for (int i = 0; i < types.length; i++) {
                    if (list.get(i) != null) {
                        types[i] = EventTypeFactory.toAPIModel(list.get(i));
                    }
                }
            }
            result.setNotifyForEventTypes(types);
        }
        try {
            result.setId(String.valueOf(poi.getPoiIdentifier()));
            result.setLinks(getLinks(USER + userId + POI + poi.getPoiIdentifier()));
        } catch (IllegalAccessException ex) {
            Logger.getLogger(POIFactory.class.getName()).log(Level.SEVERE, "POI id cannot be null!", ex);
            return null;
        }

        return result;
    }

    /**
     * Create a POI object starting from an APIPOI
     * 
     * @param poi An APIPOI object
     * @return A POI object
     */
    public static POI toDomainModel(APIPOI poi) {
        ArrayList<EventType> types = new ArrayList<>();
        if (poi.getNotifyForEventTypes() != null) {
            for (APIEventType type : poi.getNotifyForEventTypes()) {
                if (type.getType() != null) {
                    types.add(EventTypeFactory.toDomainModel(type));
                }
            }
        }
        POI result = new POI(AddressFactory.toDomainModel(poi.getAddress()), poi.getName(), poi.getRadius(), poi.isActive(), poi.getNotify().isEmailNotify(), poi.getNotify().isCellNumberNotify(), types);
        String id = poi.getId();
        if (id != null) {
            result.setPoiIdentifier(Integer.valueOf(id));
        }
        return result;
    }
}
