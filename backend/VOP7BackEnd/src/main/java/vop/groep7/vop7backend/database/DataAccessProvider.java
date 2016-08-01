package vop.groep7.vop7backend.database;

import vop.groep7.vop7backend.database.mongodb.EventDataAccessContext;
import vop.groep7.vop7backend.database.sql.UserDataAccessContext;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;

/**
 * DataAccessProvider for databases
 *
 * @author Backend Team
 */
public class DataAccessProvider {

    private final Properties databaseProperties;
    private UserDataAccessContext userDataAccessContext;
    private EventDataAccessContext eventDataAccessContext;

    private static final String DB_ERROR = "Database error at startup";
    private static final String USERURL = "userurl";

    /**
     * Constructor that sets the field databaseProperties and additionally
     * starts the driver
     *
     * @param properties A properties object that contains the necessary
     * information to connect to the database
     */
    public DataAccessProvider(Properties properties) {
        this.databaseProperties = properties;
        try {
            getUserConnection();
            getEventConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessProvider.class.getName()).log(Level.SEVERE, "Could not start up databases from properties file.", ex);
            throw new RuntimeException(DB_ERROR, ex);
        }
    }

    /**
     * Constructor that reads the correct file given a string additionally it
     * sets the properties and finally it starts the driver
     *
     * @param resourceName The pathname of a properties file that contains the
     * necessary information to connect to the database
     */
    public DataAccessProvider(String resourceName) {
        try (InputStream inp = DataAccessProvider.class.getResourceAsStream(resourceName)) {
            databaseProperties = new Properties();
            databaseProperties.load(inp);
        } catch (IOException ex) {
            Logger.getLogger(DataAccessProvider.class.getName()).log(Level.SEVERE, "Could not read properties.", ex);
            throw new RuntimeException("Could not read database properties", ex);
        }
        try {
            getUserConnection();
            getEventConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessProvider.class.getName()).log(Level.SEVERE, "Could not start up databases from properties path.", ex);
            throw new RuntimeException(DB_ERROR, ex);
        }
    }

    /**
     * Get a connection to the User database
     *
     * @return A connection to the user database
     * @throws SQLException This is thrown when no connection can be made
     */
    private Connection getUserConnection() throws SQLException {
        String user = databaseProperties.getProperty("user");
        try {
            if (user != null) {
                Class.forName("net.bull.javamelody.JdbcDriver");
                databaseProperties.put("driver", "org.postgresql.Driver");
                return DriverManager.getConnection(databaseProperties.getProperty(USERURL),
                        databaseProperties);
            } else {
                return DriverManager.getConnection(databaseProperties.getProperty(USERURL));
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Get a connection to the Event database
     *
     * @return A MongoClient of the event database
     */
    private MongoClient getEventConnection() {
        MongoClientURI uri = new MongoClientURI(databaseProperties.getProperty("eventurl"));
        MongoClient client = new MongoClient(uri);
        return client;
    }

    /**
     * Get the DAC to the user database
     *
     * @return DAC The context of a user database that can be used to create
     * DAO's
     * @throws DataAccessException This is thrown if the DAC can't be created
     */
    public UserDataAccessContext getUserDataAccessContext() throws DataAccessException {
        if (userDataAccessContext != null) {
            return userDataAccessContext;
        } else {
            try {
                userDataAccessContext = new UserDataAccessContext(getUserConnection());
                return userDataAccessContext;
            } catch (SQLException ex) {
                Logger.getLogger(DataAccessProvider.class.getName()).log(Level.SEVERE, "Could not create user data access context.", ex);
                throw new DataAccessException("Could not create data access context", ex);
            }
        }
    }

    /**
     * Get the DAC to the event database
     *
     * @return DAC The context of a event database that can be used to create
     * DAO's
     */
    public EventDataAccessContext getEventDataAccessContext() {
        if (eventDataAccessContext != null) {
            return eventDataAccessContext;
        } else {
            eventDataAccessContext = new EventDataAccessContext(getEventConnection());
            return eventDataAccessContext;
        }
    }

    /**
     * Called when Spring wants to close the server. Will make sure all
     * connections are closed.
     */
    @PreDestroy
    public void destroy() {
        try {
            if (userDataAccessContext != null) {
                userDataAccessContext.destroy();
            }
            if (eventDataAccessContext != null) {
                eventDataAccessContext.destroy();
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(DataAccessProvider.class.getName()).log(Level.SEVERE, "Could not close connections.", ex);
            throw new RuntimeException("Could not close database connections.", ex);
        }
    }
}
