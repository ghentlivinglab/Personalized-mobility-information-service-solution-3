package vop.groep7.vop7backend.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import vop.groep7.vop7backend.Models.Domain.User;
import vop.groep7.vop7backend.Models.Domain.Events.Event;
import vop.groep7.vop7backend.factories.EventFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import vop.groep7.vop7backend.AppConfig;

/**
 *
 * @author Backend Team
 */
public class RabbitmqEventsSender {

    private static final String BROKER_URL_PROP = "rabbitmq.broker.url";
    private static final String VHOST_PROP = "rabbitmq.vhost";
    private static final String USER_PROP = "rabbitmq.username";
    private static final String PWD_PROP = "rabbitmq.password";
    private static final String RESOURCE_PROP = "rabbitmq.resource";

    private String xchgName;

    private static final String ERROR = "An event could not send event to users!";

    private Channel channel;

    /**
     * Create the RabbitMQ sender to send notifications to users
     *
     */
    public RabbitmqEventsSender() {
    }

    /**
     * Get the channel (and create first if not existing)
     *
     */
    private synchronized Channel getChannel() {
        if (channel == null) {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(AppConfig.getApplicationProperty(BROKER_URL_PROP));
                factory.setVirtualHost(AppConfig.getApplicationProperty(VHOST_PROP));
                factory.setPassword(AppConfig.getApplicationProperty(PWD_PROP));
                factory.setUsername(AppConfig.getApplicationProperty(USER_PROP));

                xchgName = AppConfig.getApplicationProperty(RESOURCE_PROP);
                channel = factory.newConnection().createChannel();
                channel.exchangeDeclare(xchgName, "topic");
            } catch (IOException | TimeoutException ex) {
                Logger.getLogger(RabbitmqEventsSender.class.getName()).log(Level.SEVERE, "Something went wrong while creating channel", ex);
            }
        }
        return channel;
    }

    private String toJsonString(Event event) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(EventFactory.toAPIModel(event));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RabbitmqEventsSender.class.getName()).log(Level.SEVERE, "Event " + event.toString() + " could not be converted to String!", ex);
            return null;
        }
    }

    /**
     * Send an event to a list of userIds
     *
     * @param userIds A list of userIds
     * @param event An event to send
     */
    public void sendWithUserIds(Collection<Integer> userIds, Event event) {
        String eJSON = toJsonString(event);
        if (eJSON != null) {
            userIds.stream().forEach((id) -> {
                send(id, eJSON);
            });
        } else {
            Logger.getLogger(RabbitmqEventsSender.class.getName()).log(Level.SEVERE, ERROR);
        }
    }

    /**
     * Send an event to one user
     *
     * @param userid An Id of a user
     * @param msg A message to send
     */
    public void send(int userid, String msg) {
        String rkey = "user" + userid;
        try {
            getChannel().basicPublish(xchgName, rkey, null, msg.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(RabbitmqEventsSender.class.getName()).log(Level.SEVERE, "Could not send message to user " + userid + " (msg: '" + msg + "')", ex);
        }
    }
}