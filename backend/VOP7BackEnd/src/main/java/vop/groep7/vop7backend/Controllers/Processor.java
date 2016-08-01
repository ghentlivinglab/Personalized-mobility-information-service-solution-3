package vop.groep7.vop7backend.Controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import vop.groep7.vop7backend.AppConfig;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.database.DataAccessException;
import vop.groep7.vop7backend.database.sql.EventProcessingDAO;
import vop.groep7.vop7backend.rabbitmq.RabbitmqEventsSender;

/**
 *
 * @author Backend Team
 */
public class Processor {

    private volatile EventProcessingDAO eventProcessingDAO;

    private volatile RabbitmqEventsSender rabbitmqSender;

    @Autowired
    private MailService mailService;

    
    private synchronized EventProcessingDAO getEventProcessingDAO() {
        if (eventProcessingDAO == null) {
            try {
                eventProcessingDAO = AppConfig.getDataAccessProvider().getUserDataAccessContext().getEventProcessingDAO();
            } catch (DataAccessException ex) {
                Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, "Could not get processing DAO", ex);
            }
        }
        return eventProcessingDAO;
    }

    private synchronized RabbitmqEventsSender getRabbitmqEventsSender() {
        if (rabbitmqSender == null) {
            rabbitmqSender = new RabbitmqEventsSender();
        }
        return rabbitmqSender;
    }

    /**
     * Start the event processor on an other thread
     *
     * @param event the event to be processed
     */
    public void process(Event event) {
        EventProcessor ep = new EventProcessor(mailService, getEventProcessingDAO(), getRabbitmqEventsSender(), event);
        ep.start();
    }
}
