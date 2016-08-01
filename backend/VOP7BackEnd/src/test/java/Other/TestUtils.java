package Other;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.http.MediaType;
import vop.groep7.vop7backend.Models.Domain.Address;
import vop.groep7.vop7backend.Models.Domain.Credentials;
import vop.groep7.vop7backend.Models.Domain.EventType;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.Interval;
import vop.groep7.vop7backend.Models.Domain.Name;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.Models.Domain.Recurring;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.Models.Domain.User;

/**
 *
 * @author Jonas Van Wilder
 */
public class TestUtils {

    /**
     *
     */
    public static final MediaType APPLICATION_JSON = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    /**
     *
     * @param object
     * @return
     * @throws IOException
     */
    public static String convertToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public static boolean compareUsers(User u1, User u2) {
        if (u1 == null && u2 == null) {
            return true;
        }
        if (u1 == null || u2 == null) {
            return false;
        }
        Name n1 = u1.getName();
        Name n2 = u2.getName();
        boolean equalName = true;
        if (((n1 == null) && (n2 != null)) || ((n1 != null) && (n2 == null))) {
            return false;
        } else if ((n1 != null) && (n2 != null)) {
            equalName = Objects.equals(u1.getName().getFirstName(), u1.getName().getFirstName())
                    && Objects.equals(u1.getName().getLastName(), u1.getName().getLastName());
        }
        return equalName
                && Objects.equals(u1.getEmail(), u2.getEmail())
                && Objects.equals(u1.getCellPhone(), u2.getCellPhone())
                && Objects.equals(u1.isMute(), u2.isMute())
                && Objects.equals(u1.isCellPhoneValidated(), u2.isCellPhoneValidated())
                && Objects.equals(u1.isEmailValidated(), u2.isEmailValidated());
        // ignore travels and pois
    }

    public static boolean compareTravels(Travel t1, Travel t2) throws IllegalAccessException {
        if (t1 == null && t2 == null) {
            return true;
        }
        if (t1 == null || t2 == null) {
            return false;
        }
        Address a1 = t1.getStart();
        Address a2 = t2.getStart();
        boolean equalStart = true;
        if (((a1 == null) && (a2 != null)) || ((a1 != null) && (a2 == null))) {
            return false;
        } else if ((a1 != null) && (a2 != null)) {
            equalStart = a1.getCity().equals(a2.getCity())
                    && Objects.equals(a1.getStreet(), a2.getStreet())
                    && Objects.equals(a1.getHousenumber(), a2.getHousenumber())
                    && a1.getCoordinates().equals(a2.getCoordinates());
        }
        Address a3 = t1.getEnd();
        Address a4 = t2.getEnd();
        boolean equalEnd = true;
        if (((a3 == null) && (a4 != null)) || ((a3 != null) && (a4 == null))) {
            return false;
        } else if ((a3 != null) && (a4 != null)) {
            equalEnd = a3.getCity().equals(a4.getCity())
                    && Objects.equals(a3.getStreet(), a4.getStreet())
                    && Objects.equals(a3.getHousenumber(), a4.getHousenumber())
                    && a3.getCoordinates().equals(a4.getCoordinates());
        }
        Interval i1 = t1.getInterval();
        Interval i2 = t2.getInterval();
        Recurring w1 = t1.getRecurring();
        Recurring w2 = t2.getRecurring();

        //TODO compare travels
        return Objects.equals(t1.getName(), t2.getName())
                && equalStart
                && equalEnd
                && i1.equals(i2)
                && w1.equals(w2);
    }

    public static boolean compareRoutes(Route r1, Route r2) throws IllegalAccessException {
        if (r1 == null && r2 == null) {
            return true;
        }
        if (r1 == null || r2 == null) {
            return false;
        }

        //TODO compare routes
        return Objects.equals(r1.isActive(), r2.isActive())
                && r1.getWaypoints().equals(r2.getWaypoints())
                && r1.getTransportationType().equals(r2.getTransportationType())
                && r1.getNotifications().equals(r2.getNotifications());
    }

    public static boolean comparePOIs(POI p1, POI p2) throws IllegalAccessException {
        if (p1 == null && p2 == null) {
            return true;
        }
        if (p1 == null || p2 == null) {
            return false;
        }
        Address a1 = p1.getAddress();
        Address a2 = p2.getAddress();
        boolean equalAddress = true;
        if (((a1 == null) && (a2 != null)) || ((a1 != null) && (a2 == null))) {
            return false;
        } else if ((a1 != null) && (a2 != null)) {
            equalAddress = a1.getCity().equals(a2.getCity())
                    && Objects.equals(a1.getStreet(), a2.getStreet())
                    && Objects.equals(a1.getHousenumber(), a2.getHousenumber())
                    && a1.getCoordinates().equals(a2.getCoordinates());
        }
        return equalAddress
                && Objects.equals(p1.getName(), p2.getName())
                && Objects.equals(p1.getRadius(), p2.getRadius())
                && Objects.equals(p1.isActive(), p2.isActive())
                && p1.getNotifications().equals(p2.getNotifications());
    }

    public static boolean compareEvents(Event e1, Event e2) {
        if (e1 == null && e2 == null) {
            return true;
        }
        if (e1 == null || e2 == null) {
            return false;
        }

        return Objects.equals(e1.getDescription(), e2.getDescription())
                && Objects.equals(e1.getLastEditTime(), e2.getLastEditTime())
                && Objects.equals(e1.getLocation().getCity().getCity(), e2.getLocation().getCity().getCity())
                && Objects.equals(e1.getLocation().getCity().getCountry(), e2.getLocation().getCity().getCountry())
                && Objects.equals(e1.getLocation().getCity().getPostalCode(), e2.getLocation().getCity().getPostalCode())
                && Objects.equals(e1.getLocation().getHousenumber(), e2.getLocation().getHousenumber())
                && Objects.equals(e1.getLocation().getCoordinates().getLat(), e2.getLocation().getCoordinates().getLat())
                && Objects.equals(e1.getLocation().getCoordinates().getLon(), e2.getLocation().getCoordinates().getLon())
                && Objects.equals(e1.getLocation().getStreet(), e2.getLocation().getStreet())
                && Objects.equals(e1.getPublicationTime(), e2.getPublicationTime())
                && Objects.equals(e1.getSource().getName(), e2.getSource().getName())
                && Objects.equals(e1.getSource().getIconUrl(), e2.getSource().getIconUrl())
                && Objects.equals(e1.getType().getType(), e2.getType().getType())
                && Objects.equals(e1.isActive(), e2.isActive())
                && Arrays.deepEquals(e1.getRelevantTransportationTypes().toArray(), e2.getRelevantTransportationTypes().toArray());
    }
    
    public static boolean compareEventTypes(EventType e1, EventType e2) {
        if (e1 == null && e2 == null) {
            return true;
        }
        if (e1 == null || e2 == null) {
            return false;
        }
        
        return Objects.equals(e1.getType(), e2.getType());
    }
}
