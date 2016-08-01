package vop.groep7.vop7backend.Datasources;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import vop.groep7.vop7backend.AppConfig;
import vop.groep7.vop7backend.Controllers.EventController;
import vop.groep7.vop7backend.Models.APIModels.APICoordinate;
import vop.groep7.vop7backend.Models.APIModels.APIEvent;
import vop.groep7.vop7backend.Models.APIModels.APIEventType;
import vop.groep7.vop7backend.Models.APIModels.APIJam;
import vop.groep7.vop7backend.Models.APIModels.APISource;
import vop.groep7.vop7backend.Models.APIModels.APITransport;
import vop.groep7.vop7backend.database.DataAccessException;

/**
 *
 * @author Backend Team
 */
public class WazeDataSource implements DataSource {

    private final static String DEFAULT_TYPE = "Misc";
    private final static String DEFAULT_ID = "-1";
    private final static String START_TIME_DUMP = "startTime";
    private final static String END_TIME_DUMP = "endTime";
    private final static String ALERT_EVENTS = "alerts";
    private final static String JAM_EVENTS = "jams";
    private final static String TYPE = "type";
    private final static String JAM_TYPE = "Jam";
    private final static String JAM = "File";
    private final static String LOCATION = "location";
    private final static String COORDINATE_X = "x";
    private final static String COORDINATE_Y = "y";
    private final static String DESCRIPTION = "reportDescription";
    private final static String JAM_LINE = "line";
    private final static String JAM_DELAY = "delay";
    private final static String JAM_SPEED = "speed";
    private final static String CITY = "city";
    private final static String COUNTRY = "country";
    private final static String STREET = "street";
    private final static String JAM_ID = "blockingAlertUuid";
    private final static String ALERT_ID = "uuid";

    private final APISource waze;

    @Autowired
    private EventController eventController;

    private final List<APIEvent> events;
    private final Map<String, APIEvent> alertIds;

    /**
     * The constructor for the Waze data source
     */
    public WazeDataSource() {
        waze = new APISource();
        waze.setName(AppConfig.getApplicationProperty("datasource.waze.name"));
        waze.setIconUrl(AppConfig.getApplicationProperty("datasource.waze.icon"));

        events = new ArrayList<>();
        alertIds = new HashMap<>();
    }

    private void log(String message, Exception ex) {
        Level level = Level.INFO;
        if (ex != null) {
            level = Level.SEVERE;
        }
        Logger.getLogger(WazeDataSource.class.getName()).log(level, message, ex);
    }

    @Override
    public void checkDataSource() {
        try {
            String u = AppConfig.getApplicationProperty("datasource.waze.url");
            URL url = new URL(u);
            String jsonDump = IOUtils.toString(url);
            parseJsonDump(jsonDump);
        } catch (IOException ex) {
            log("Something went wrong while checking datasource Waze: " + ex.getMessage(), ex);
        }
    }

    private void parseJsonDump(String jsonDump) throws IOException {
        JSONObject jsonDumpObject = new org.json.JSONObject(jsonDump);

        String startTime = jsonDumpObject.getString(START_TIME_DUMP);
        String endTime = jsonDumpObject.getString(END_TIME_DUMP);

        events.clear();
        alertIds.clear();

        if(jsonDumpObject.has(JAM_EVENTS)) {
            extractJamEvents(jsonDumpObject.getJSONArray(JAM_EVENTS));
        }
        if(jsonDumpObject.has(ALERT_EVENTS)) {
            extractAlertEvents(jsonDumpObject.getJSONArray(ALERT_EVENTS));
        }

        processEvents();

        log("Version " + startTime + " till " + endTime + " " + "Parsed", null);
    }

    private void extractJamEvents(JSONArray jsonEvents) {
        for (int i = 0; i < jsonEvents.length(); i++) {
            JSONObject jsonEvent = jsonEvents.getJSONObject(i);
            APIEvent e = new APIEvent();
            // set default id
            e.setId(DEFAULT_ID);
            // set source
            e.setSource(waze);
            // set event type
            e.setType(formatEventType(JAM_TYPE));
            // mark event as active
            e.setActive(true);
            
            // set description
            String description;
            if (jsonEvent.has(DESCRIPTION)) {
                description = jsonEvent.getString(DESCRIPTION);
            } else if (jsonEvent.has(TYPE)) {
                description = formatEventType(jsonEvent.getString(TYPE)).getType();
            } else {
                description = JAM;
            }

            if (jsonEvent.has(STREET) && jsonEvent.has(CITY)) {
                description += " in " + jsonEvent.getString(STREET) + ", " + jsonEvent.getString(CITY);
            } else if (jsonEvent.has(STREET)) {
                description += " in " + jsonEvent.getString(STREET);
            }
            e.setDescription(description);

            if (jsonEvent.has(STREET)) {
                e.setStreet(jsonEvent.getString(STREET));
            }

            if (jsonEvent.has(CITY)) {
                e.setCity(jsonEvent.getString(CITY));
            }

            if (jsonEvent.has(COUNTRY)) {
                e.setCountry(jsonEvent.getString(COUNTRY));
            }

            if (jsonEvent.has(JAM_LINE)) {
                // get line array with jam coordinates
                JSONArray line = jsonEvent.getJSONArray(JAM_LINE);
                // get the begin and end object
                JSONObject lineBeginning = line.getJSONObject(0);
                JSONObject lineMiddle;
                if ((line.length() % 2) == 0) {
                    lineMiddle = line.getJSONObject((line.length() / 2) - 1);
                } else {
                    lineMiddle = line.getJSONObject(line.length() / 2);
                }
                JSONObject lineEnding = line.getJSONObject(line.length() - 1);

                // set the middle location fo the jam as location coordinates
                APICoordinate coordinates = new APICoordinate();
                coordinates.setLat(lineMiddle.getDouble(COORDINATE_Y));
                coordinates.setLon(lineMiddle.getDouble(COORDINATE_X));
                e.setCoordinates(coordinates);

                //set jams
                APIJam[] jams = new APIJam[1];
                jams[0] = new APIJam();
                // set delay and speed of jam
                jams[0].setDelay(jsonEvent.getInt(JAM_DELAY));
                jams[0].setSpeed(jsonEvent.getInt(JAM_SPEED));
                // set start point of jam
                APICoordinate start = new APICoordinate();
                start.setLat(lineBeginning.getDouble(COORDINATE_Y));
                start.setLon(lineBeginning.getDouble(COORDINATE_X));
                jams[0].setStartNode(start);
                // set end point of jam
                APICoordinate end = new APICoordinate();
                end.setLat(lineEnding.getDouble(COORDINATE_Y));
                end.setLon(lineEnding.getDouble(COORDINATE_X));
                jams[0].setEndNode(end);

                e.setJams(jams);
            }

            APITransport[] relevantForTransportationTypes = new APITransport[3];
            relevantForTransportationTypes[0] = APITransport.CAR;
            relevantForTransportationTypes[1] = APITransport.BUS;
            relevantForTransportationTypes[2] = APITransport.STREETCAR;
            e.setRelevantForTransportationTypes(relevantForTransportationTypes);

            if (jsonEvent.has(JAM_ID)) {
                alertIds.put(jsonEvent.getString(JAM_ID), e);
            } else {
                events.add(e);
            }
        }
    }

    private void extractAlertEvents(JSONArray jsonEvents) {
        for (int i = 0; i < jsonEvents.length(); i++) {
            JSONObject jsonEvent = jsonEvents.getJSONObject(i);
            if (jsonEvent.has(ALERT_ID) && alertIds.containsKey(jsonEvent.getString(ALERT_ID))) {
                //Alert is linked to Jam
                APIEvent e = alertIds.get(jsonEvent.getString(ALERT_ID));

                APICoordinate coordinates = new APICoordinate();
                JSONObject location = jsonEvent.getJSONObject(LOCATION);
                coordinates.setLat(location.getDouble(COORDINATE_Y));
                coordinates.setLon(location.getDouble(COORDINATE_X));
                e.setCoordinates(coordinates);

                if (jsonEvent.has(STREET)) {
                    e.setStreet(jsonEvent.getString(STREET));
                }

                if (jsonEvent.has(CITY)) {
                    e.setCity(jsonEvent.getString(CITY));
                }

                if (jsonEvent.has(COUNTRY)) {
                    e.setCountry(jsonEvent.getString(COUNTRY));
                }

                if (jsonEvent.has(TYPE)) {
                    e.setType(formatEventType(jsonEvent.getString(TYPE)));
                }

                // set description
                String description;
                if (jsonEvent.has(DESCRIPTION)) {
                    description = JAM + ": " + jsonEvent.getString(DESCRIPTION);
                } else if (jsonEvent.has(TYPE)) {
                    description = JAM + ": " + formatEventType(jsonEvent.getString(TYPE)).getType();
                } else {
                    description = JAM + ": " + DEFAULT_TYPE;
                }

                if (jsonEvent.has(STREET) && jsonEvent.has(CITY)) {
                    description += " in " + jsonEvent.getString(STREET) + ", " + jsonEvent.getString(CITY);
                } else if (jsonEvent.has(STREET)) {
                    description += " in " + jsonEvent.getString(STREET);
                }
                e.setDescription(description);

                events.add(e);
            } else {
                //New event
                APIEvent e = new APIEvent();
                // set default id
                e.setId(DEFAULT_ID);
                // set source
                e.setSource(waze);
                // mark event as active
                e.setActive(true);

                //set type
                if (jsonEvent.has(TYPE)) {
                    e.setType(formatEventType(jsonEvent.getString(TYPE)));
                } else {
                    e.setType(formatEventType(DEFAULT_TYPE));
                }

                if (jsonEvent.has(LOCATION)) {
                    // set location coordinates
                    APICoordinate coordinates = new APICoordinate();
                    coordinates.setLat(jsonEvent.getJSONObject(LOCATION).getDouble(COORDINATE_Y));
                    coordinates.setLon(jsonEvent.getJSONObject(LOCATION).getDouble(COORDINATE_X));
                    e.setCoordinates(coordinates);
                }

                if (jsonEvent.has(STREET)) {
                    e.setStreet(jsonEvent.getString(STREET));
                }

                if (jsonEvent.has(CITY)) {
                    e.setCity(jsonEvent.getString(CITY));
                }

                if (jsonEvent.has(COUNTRY)) {
                    e.setCountry(jsonEvent.getString(COUNTRY));
                }
                
                // set description
                String description;
                if (jsonEvent.has(DESCRIPTION)) {
                    description = jsonEvent.getString(DESCRIPTION);
                } else if (jsonEvent.has(TYPE)) {
                    description = formatEventType(jsonEvent.getString(TYPE)).getType();
                } else {
                    description = DEFAULT_TYPE;
                }

                if (jsonEvent.has(STREET) && jsonEvent.has(CITY)) {
                    description += " in " + jsonEvent.getString(STREET) + ", " + jsonEvent.getString(CITY);
                } else if (jsonEvent.has(STREET)) {
                    description += " in " + jsonEvent.getString(STREET);
                }
                e.setDescription(description);
                APITransport[] relevantForTransportationTypes = APITransport.values();
                e.setRelevantForTransportationTypes(relevantForTransportationTypes);

                events.add(e);
            }
        }
    }

    private void processEvents() {
        events.stream().filter((e) -> (e.getCoordinates() != null)).forEach((e) -> {
            processEvent(e);
        });
    }

    /**
     * Process the event by creating or updating it
     *
     * @param event the event to be created or modified
     */
    private void processEvent(APIEvent event) {
        int id = eventController.getIdByAPIEvent(event);
        if (id != -1) {
            try {
                event.setPublicationTime(eventController.getAPIEvent(id).getPublicationTime());
                eventController.modifyAPIEvent(id, event);
            } catch (DataAccessException ex) {
                Logger.getLogger(WazeDataSource.class.getName()).log(Level.SEVERE, "Can not process event", ex);
            }
        } else {
            eventController.createAPIEvent(event);
        }
    }

    private APIEventType formatEventType(String type) {
        if (type == null || type.equals("")) {
            return null;
        }
        APIEventType res = new APIEventType();
        // set the string as lowercase except for the first character
        res.setType(String.valueOf(type.charAt(0)).toUpperCase() + type.substring(1).toLowerCase());
        return res;
    }

    @Override
    public void updateInactive() {
        log("Updating events to inactive", null);
        List<APIEvent> activeEvents = eventController.getActiveAPIEvents();

        int inactiveTime = Integer.valueOf(AppConfig.getApplicationProperty("datasource.waze.inactive_time_hour"));
        long inactiveTimeMS = inactiveTime * 60 * 60 * 1000;

        for (APIEvent e : activeEvents) {
            if (e.isActive() && checkTimeWithOffset(e.getLastEditTime(), inactiveTimeMS)) {
                try {
                    e.setActive(false);
                    int id = Integer.valueOf(e.getId());
                    eventController.modifyAPIEvent(id, e);
                } catch (DataAccessException ex) {
                    Logger.getLogger(WazeDataSource.class.getName()).log(Level.SEVERE, "Can not make event inactive", ex);
                }
            }
        }
    }

    private boolean checkTimeWithOffset(String lastEditTime, long offset) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date editDate = sdf.parse(lastEditTime);
            Timestamp editTimestamp = new Timestamp(editDate.getTime() + offset);
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            return currentTime.after(editTimestamp);
        } catch (ParseException ex) {
            Logger.getLogger(WazeDataSource.class.getName()).log(Level.SEVERE, "Event will be modified because time: " + lastEditTime + " is not valid.", ex);
            return true;
        }
    }

    @Override
    public void deleteInactive() {
        log("Deleting inactive events", null);
        List<APIEvent> inactiveEvents = eventController.getInactiveAPIEvents();

        int deleteTime = Integer.valueOf(AppConfig.getApplicationProperty("datasource.waze.delete_time_day"));
        long deleteTimeMS = deleteTime * 24 * 60 * 60 * 1000;

        for (APIEvent e : inactiveEvents) {
            if (!e.isActive() && checkTimeWithOffset(e.getLastEditTime(), deleteTimeMS)) {
                int id = Integer.valueOf(e.getId());
                eventController.deleteAPIEvent(id);
            }
        }
    }
}
