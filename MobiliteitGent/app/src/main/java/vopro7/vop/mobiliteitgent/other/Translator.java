package vopro7.vop.mobiliteitgent.other;

import java.util.HashMap;
import java.util.Map;

/**
 * @author App Team
 */
public class Translator {

    private static Map<String, String> transportTranslations = new HashMap<String, String>() {{
        put("car", "auto");
        put("streetcar", "tram");
        put("train", "trein");
        put("bike", "fiets");
        put("bus", "bus");
    }};

    private static Map<String, String> eventTypeTranslations = new HashMap<String, String>() {{
        put("Accident", "Ongeval");
        put("Road_closed", "Weg afgesloten");
        put("Weatherhazard", "Gevaar");
        put("Construction", "Werken");
        put("Hazard", "Gevaar");
        put("Jam", "File");
        put("Misc", "Andere");

    }};

    /**
     * Translate a transportation type
     *
     * @param transport the transport we want translated
     * @return The translation
     */
    public static String getTransportTranslation(String transport) {
        if (transport == null) {
            return "";
        }
        String translation = transportTranslations.get(transport);
        if (translation != null) {
            return translation;
        } else {
            return transport;
        }
    }

    /**
     * Translate an event type
     * @param eventType the event type we want translated
     * @return The translation
     */
    public static String getEventTypeTranslation(String eventType) {
        if (eventType == null) {
            return "";
        }
        String translation = eventTypeTranslations.get(eventType);
        if (translation != null) {
            return translation;
        } else {
            return eventType;
        }
    }

}
