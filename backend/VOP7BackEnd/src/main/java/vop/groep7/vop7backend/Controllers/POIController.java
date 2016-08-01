package vop.groep7.vop7backend.Controllers;

import java.util.ArrayList;
import java.util.List;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APIPOI;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.factories.AddressFactory;
import vop.groep7.vop7backend.factories.EventTypeFactory;
import vop.groep7.vop7backend.factories.POIFactory;

/**
 *
 * @author Backend Team
 */
public class POIController extends Controller {

    /**
     * Get a APIPOI object based on it's Id and the Id of the User
     *
     * @param userId Id of a user
     * @param poiId Id of the poi
     * @see APIPOI
     * @return The api poi object
     * @throws DataAccessException This is thrown when no poi can be found.
     */
    public APIPOI getAPIPOI(int userId, int poiId) throws DataAccessException {
        // get poi
        POI p = getPOI(userId, poiId);
        if (p == null) {
            return null;
        } else {
            return POIFactory.toAPIModel(p, userId);
        }
    }

    /**
     * Get all APIPOI objects of a User
     *
     * @param userId Id of a user
     * @see APIPOI
     * @return A list of all api pois
     * @throws DataAccessException This is thrown when no pois can be found.
     */
    public List<APIPOI> getAPIPOIs(int userId) throws DataAccessException {
        List<POI> pois = getPOIs(userId);

        // D2A pois
        List<APIPOI> res = new ArrayList<>();
        pois.stream().forEach((p) -> {
            res.add(POIFactory.toAPIModel(p, userId));
        });
        return res;
    }

    /**
     * Insert a APIPOI object in the database
     *
     * @param userId Id of a user
     * @param poi A api poi object that has to be created
     * @see APIPOI
     * @return The created api poi object
     * @throws DataAccessException This is thrown when no poi can be created.
     */
    public APIPOI createAPIPOI(int userId, APIPOI poi) throws DataAccessException {
        User user = getUser(userId);

        Address address = AddressFactory.toDomainModel(poi.getAddress());
        ArrayList<EventType> types = new ArrayList<>();
        for (APIEventType type : poi.getNotifyForEventTypes()) {
            types.add(EventTypeFactory.toDomainModel(type));
        }
        POI newPOI = POIFactory.create(address, poi.getName(), poi.getRadius(), poi.isActive(), poi.getNotify().isEmailNotify(), poi.getNotify().isCellNumberNotify(), types);

        // create poi in db
        int generatedId = getPOIDAO().createPOI(userId, newPOI);
        // use generatedId as poi identifier
        newPOI.setPoiIdentifier(generatedId);
        // load newPOI into user
        user.addPOI(newPOI);

        return POIFactory.toAPIModel(newPOI, userId);
    }

    /**
     * Delete a api poi object from the database
     *
     * @param userId Id of a user
     * @param poiId Id of the poi
     * @return true if the deleting has succeeded
     * @throws DataAccessException This is thrown when the poi can't be deleted.
     */
    public boolean deleteAPIPOI(int userId, int poiId) throws DataAccessException {
        User user = getUser(userId);

        // delete poi in cache
        user.removePOI(poiId);

        // clear already matched events of user
        getUserDAO().deleteMatchedEvents(userId);

        // delete poi in db
        return getPOIDAO().deletePOI(userId, poiId);
    }

    /**
     * Modify a api poi object in the database
     *
     * @param userId Id of a user
     * @param poiId Id of the poi
     * @param poi A poi api object that has to be modified
     * @see APIPOI
     * @return The modified api poi object
     * @throws DataAccessException This is thrown when the poi can't be
     * modified.
     */
    public APIPOI modifyAPIPOI(int userId, int poiId, APIPOI poi) throws DataAccessException {
        User user = getUser(userId);

        POI modifiedPOI = POIFactory.toDomainModel(poi);
        // load modified poi into user
        user.removePOI(poiId);
        user.addPOI(modifiedPOI);

        // modify poi in db
        getPOIDAO().modifyPOI(userId, poiId, modifiedPOI);

        // clear already matched events of user
        getUserDAO().deleteMatchedEvents(userId);

        return poi;
    }

    /**
     * Check if the api poi already exists
     *
     * @param userId Id of a user
     * @param poi A api poi object
     * @see APIPOI
     * @return If the api poi already exists
     * @throws DataAccessException This is thrown when the poi can't be checked.
     */
    public boolean existsAPIPOI(int userId, APIPOI poi) throws DataAccessException {
        return getPOIDAO().existsPOI(userId, POIFactory.toDomainModel(poi));
    }
}
