package vop.groep7.vop7backend.database.mongodb;

import com.mongodb.MongoClient;

/**
 *
 * @author Backend Team
 */
public class EventDataAccessContext {

    private final MongoClient client;
    private EventDAO eventDAO;

    /**
     * The constructor of a EventDataAccessContext needs a connection to a
     * database.
     *
     * @param client A client to the mongoDB database
     */
    public EventDataAccessContext(MongoClient client) {
        this.client = client;
    }

    /**
     * Get the EventDAO object
     *
     * @return The EventDAO object to execute mongoDB queries on the database
     */
    public EventDAO getEventDAO() {
        if (eventDAO == null) {
            eventDAO = new EventDAO(client);
        }
        return eventDAO;
    }
    
    /**
     * Called when Spring wants to close the server. Will make sure all
     * connections are closed.
     */
    public void destroy() {
        if (client != null) {
            client.close();
        }
    }
}
