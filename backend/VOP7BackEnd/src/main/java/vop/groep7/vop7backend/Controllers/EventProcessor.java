package vop.groep7.vop7backend.Controllers;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.AppConfig;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.Models.Domain.POI;
import vop.groep7.vop7backend.Models.Domain.Route;
import vop.groep7.vop7backend.Models.Domain.Travel;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.EventProcessingDAO;
import vop.groep7.vop7backend.database.sql.UserDAO;
import vop.groep7.vop7backend.rabbitmq.RabbitmqEventsSender;

/**
 *
 * @author Backend Team
 */
public class EventProcessor extends Thread {

    private static final String NOTIFICATION_MAIN_POI_1 = "message.notification.poi.main.pt1";
    private static final String NOTIFICATION_MAIN_ROUTE_1 = "message.notification.route.main.pt1";
    private static final String NOTIFICATION_MAIN_2 = "message.notification.main.pt2";
    private static final String MESSAGE_TAIL = "message.tail";
    private static final String MESSAGE_NOTIFICATION_SUBJECT = "message.notification.subject";
    private static final String LAT = " latitude ";
    private static final String LON = " longitude";
    private static final String NAME = "Naam: ";
    private static final String LOCATION = "Locatie: ";

    private final Event event;
    private MailService mailService;
    private EventProcessingDAO eventProcessingDAO;
    private RabbitmqEventsSender rabbitmqSender;

    private final Map<Integer, Map<Travel, Route>> emailForRoutes;
    private final Map<Integer, POI> emailForPois;
    private final Set<Integer> pushMessage;

    /**
     * Create the processor
     *
     * @param mailService the mailService object
     * @param eventProcessingDAO the event processing dao
     * @param rabbitmqSender The rabbitMQ send service
     * @param event the event to be processed
     */
    public EventProcessor(MailService mailService, EventProcessingDAO eventProcessingDAO, RabbitmqEventsSender rabbitmqSender, Event event) {
        this.event = event;
        this.mailService = mailService;
        this.eventProcessingDAO = eventProcessingDAO;
        this.rabbitmqSender = rabbitmqSender;

        emailForRoutes = new HashMap<>();
        emailForPois = new HashMap<>();
        pushMessage = new HashSet<>();
    }

    private void notifyUsersByEmailForPOI(Map<Integer, POI> emailList) {
        if (emailList != null && !emailList.isEmpty()) {
            emailList.keySet().stream().forEach((id) -> {
                try {
                    // send event in email message
                    String message = AppConfig.getApplicationProperty(NOTIFICATION_MAIN_POI_1)
                            + getPOIInformation(emailList.get(id))
                            + AppConfig.getApplicationProperty(NOTIFICATION_MAIN_2)
                            + getEventInformation(event)
                            + AppConfig.getApplicationProperty(MESSAGE_TAIL);
                    String email = AppConfig.getUserController().getUserDAO().getUser(id).getEmail();
                    mailService.sendMailToOne(email, AppConfig.getApplicationProperty(MESSAGE_NOTIFICATION_SUBJECT), message);
                } catch (DataAccessException ex) {
                    Logger.getLogger(EventProcessor.class.getName()).log(Level.SEVERE, "Cannot send poi mail to " + id, ex);
                }
            });
        }
    }

    private void notifyUsersByEmailForRoute(Map<Integer, Map<Travel, Route>> emailList) {
        if (emailList != null && !emailList.isEmpty()) {
            emailList.keySet().stream().forEach((id) -> {
                emailList.get(id).keySet().stream().map((t) -> AppConfig.getApplicationProperty(NOTIFICATION_MAIN_ROUTE_1)
                        + getRouteInformation(t, emailList.get(id).get(t))
                        + AppConfig.getApplicationProperty(NOTIFICATION_MAIN_2)
                        + getEventInformation(event)
                        + AppConfig.getApplicationProperty(MESSAGE_TAIL)).forEach((message) -> {
                            try {
                                String email = AppConfig.getUserController().getUserDAO().getUser(id).getEmail();
                                mailService.sendMailToOne(email, AppConfig.getApplicationProperty(MESSAGE_NOTIFICATION_SUBJECT), message);
                            } catch (DataAccessException ex) {
                                Logger.getLogger(EventProcessor.class.getName()).log(Level.SEVERE, "Cannot send route mail to " + id, ex);
                            }
                        });
            });
        }
    }

    private String getPOIInformation(POI p) {
        String location;
        if (p.getAddress().getStreet() != null && p.getAddress().getCity() != null) {
            location = p.getAddress().getStreet() + ", " + p.getAddress().getCity();
        } else {
            location = p.getAddress().getCoordinates().getLat() + LAT + p.getAddress().getCoordinates().getLon() + LON;
        }
        return NAME + p.getName() + "\n"
                + LOCATION + location + "\n"
                + "Straal: " + p.getRadius();
    }

    private String getRouteInformation(Travel t, Route r) {
        String locationStart;
        if (t.getStart().getStreet() != null && t.getStart().getCity() != null) {
            locationStart = t.getStart().getStreet() + ", " + t.getStart().getCity();
        } else {
            locationStart = t.getStart().getCoordinates().getLat() + LAT + t.getStart().getCoordinates().getLon() + LON;
        }
        String locationEnd;
        if (t.getEnd().getStreet() != null && t.getEnd().getCity() != null) {
            locationEnd = t.getEnd().getStreet() + ", " + t.getEnd().getCity();
        } else {
            locationEnd = t.getEnd().getCoordinates().getLat() + LAT + t.getEnd().getCoordinates().getLon() + LON;
        }
        return NAME + t.getName() + "\n"
                + "Van: " + locationStart + "\n"
                + "Naar: " + locationEnd + "\n"
                + "Vervoersmiddel: " + r.getTransportationType();
    }

    private String getEventInformation(Event e) {
        String types = "";
        for (int i = 0; i < e.getRelevantTransportationTypes().size() - 1; i++) {
            types += e.getRelevantTransportationTypes().get(i).getTransport() + ", ";
        }
        types += e.getRelevantTransportationTypes().get(e.getRelevantTransportationTypes().size() - 1).getTransport();
        String location;
        if (e.getLocation().getStreet() != null && e.getLocation().getCity() != null) {
            location = e.getLocation().getStreet() + ", " + e.getLocation().getCity();
        } else {
            location = e.getLocation().getCoordinates().getLat() + LAT + e.getLocation().getCoordinates().getLon() + LON;
        }
        return LOCATION + location + "\n"
                + "Publicatie tijd: " + e.getPublicationTime() + "\n"
                + "Laatste update: " + e.getLastEditTime() + "\n"
                + "Beschrijving: " + e.getDescription() + "\n"
                + "Type gebeurtenis: " + e.getType().getType() + "\n"
                + "Bron: " + e.getSource().getName() + "\n"
                + "Geldig voor vervoersmiddellen: " + types;
    }

    /**
     * Match the event to users based on their preferences and notify these
     * users for this event
     *
     * @param e Event to be processed
     * @see Event
     */
    private void processEvent(Event e) {
        try {
            emailForRoutes.clear();
            emailForPois.clear();
            pushMessage.clear();

            UserDAO userDAO = AppConfig.getUserController().getUserDAO();
            List<Integer> oldMatchedUserIds = userDAO.getMatchedUsers(e.getEventIdentifier());
            
            // match event to users (based on preferences)
            Set<Integer> newMatchedUserIds = matchEventToUsers(e);
            
            //Delete every match that is in the old, but not the new matched list
            List<Integer> deleteMatches = new ArrayList<>();
            deleteMatches.addAll(oldMatchedUserIds);
            deleteMatches.removeAll(newMatchedUserIds);
            
            //Create a match for every id that is in the new but not in the old matched list
            List<Integer> createMatches = new ArrayList<>();
            createMatches.addAll(newMatchedUserIds);
            createMatches.removeAll(oldMatchedUserIds);
            
            for (int userId : deleteMatches) {
                userDAO.deleteMatch(userId, e.getEventIdentifier());
            }

            for (int userId : createMatches) {
                userDAO.createMatch(userId, e.getEventIdentifier());
            }

            // send notifications for matched users
            //first remove users that already were matched
            oldMatchedUserIds.stream().map((userId) -> {
                emailForPois.remove(userId);
                return userId;
            }).map((userId) -> {
                emailForRoutes.remove(userId);
                return userId;
            }).forEach((userId) -> {
                pushMessage.remove(userId);
            });
            
            notifyUsersByEmailForPOI(emailForPois);
            notifyUsersByEmailForRoute(emailForRoutes);
            if (rabbitmqSender != null) {
                rabbitmqSender.sendWithUserIds(pushMessage, e);
            }
            
            emailForRoutes.clear();
            emailForPois.clear();
            pushMessage.clear();
        } catch (DataAccessException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, "Something went wrong while processing the event", ex);
        }
    }

    /**
     * Match an event to users
     *
     * @param e the event to be matched
     * @return A set with the ids of the users matched to the event
     */
    private Set<Integer> matchEventToUsers(Event e) {
        Set<Integer> matchedUserIds = new HashSet<>();
        try {
            int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            Time lastEditTime = new Time(e.getLastEditTime().getTime());
            // get ids of matching routes
            Map<Integer, Map<Integer, List<Integer>>> idsRoute = eventProcessingDAO.getTimeTravels(dayOfWeek, lastEditTime, e.getType().getType(), e.getLocation().getCoordinates());

            for (Integer userId : idsRoute.keySet()) {
                User u = AppConfig.getUserController().getUser(userId);
                matchedUserIds.add(userId);
                if (!u.isMute()) {
                    for (Integer travelId : idsRoute.get(userId).keySet()) {
                        for (Integer routeId : idsRoute.get(userId).get(travelId)) {
                            Route r = AppConfig.getRouteController().getRoute(userId, travelId, routeId);
                            pushMessage.add(userId);
                            if (u.isEmailValidated() && r.getNotifications().isNotifyEmail() && !AppConfig.getUserController().getUserDAO().areMatched(userId, e.getEventIdentifier())) {
                                Travel t = AppConfig.getTravelController().getTravel(userId, travelId);
                                Map<Travel, Route> map = new HashMap<>();
                                map.put(t, r);
                                emailForRoutes.put(u.getUserIdentifier(), map);
                            }
                        }
                    }
                }
            }
            // get ids of matching pois
            Map<Integer, List<Integer>> idsPOI = eventProcessingDAO.getMatchingPOIs(e.getType().getType(), e.getLocation().getCoordinates());

            for (Integer userId : idsPOI.keySet()) {
                User u = AppConfig.getUserController().getUser(userId);
                matchedUserIds.add(userId);
                if (!u.isMute()) {
                    for (Integer poiId : idsPOI.get(userId)) {
                        POI p = AppConfig.getPOIController().getPOI(userId, poiId);
                        pushMessage.add(userId);
                        if (u.isEmailValidated() && p.getNotifications().isNotifyEmail() && !AppConfig.getUserController().getUserDAO().areMatched(userId, e.getEventIdentifier())) {
                            emailForPois.put(u.getUserIdentifier(), p);
                        }
                    }
                }
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, "Something went wrong while matching the events", ex);
        }

        return matchedUserIds;
    }

    @Override
    public void run() {
        processEvent(event);
    }
}
