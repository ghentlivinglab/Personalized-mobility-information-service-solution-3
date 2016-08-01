package vop.groep7.vop7backend.database.mongodb;

import com.mongodb.MongoClient;

/**
 *
 * @author Backend Team
 */
public abstract class MongoDBDAO {

    /**
     * The id field
     */
    protected static final String ID = "id";

    /**
     * The description field
     */
    protected static final String DESCRIPTION = "description";

    /**
     * The type field
     */
    protected static final String TYPE = "type";

    /**
     * The active field
     */
    protected static final String ACTIVE = "active";

    /**
     * The coordinates field
     */
    protected static final String LOCATION_COORD = "coordinates";

    /**
     * The latitude field
     */
    protected static final String LATITUDE = "lat";

    /**
     * The longitude field
     */
    protected static final String LONGITUDE = "lon";

    /**
     * The publication time field
     */
    protected static final String PUBTIME = "publication_time";

    /**
     * The source field
     */
    protected static final String SRC = "source";

    /**
     * The name field
     */
    protected static final String SRC_NAME = "name";

    /**
     * The jams field
     */
    protected static final String JAMS = "jams";
    
    /**
     * The street field
     */
    protected static final String STREET = "street";
    
    /**
     * The city field
     */
    protected static final String CITY = "city";
    
    /**
     * The country field
     */
    protected static final String COUNTRY = "country";

    /**
     * A mongo client object that represents the connection to the MongoDB
     * database
     */
    protected MongoClient client;

    /**
     * The constructor every MongoDBDAO should have. Every MongoDBDAO needs a
     * client to a database.
     *
     * @param client A client object to a database
     */
    public MongoDBDAO(MongoClient client) {
        this.client = client;
    }
}
