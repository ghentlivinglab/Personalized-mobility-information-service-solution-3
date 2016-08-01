package vop.groep7.vop7backend;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import vop.groep7.vop7backend.Controllers.EventController;
import vop.groep7.vop7backend.Controllers.MailService;
import vop.groep7.vop7backend.Controllers.POIController;
import vop.groep7.vop7backend.Controllers.Processor;
import vop.groep7.vop7backend.Controllers.RouteController;
import vop.groep7.vop7backend.Controllers.TravelController;
import vop.groep7.vop7backend.Controllers.UserController;
import vop.groep7.vop7backend.Controllers.UtilController;
import vop.groep7.vop7backend.Controllers.Validator;
import vop.groep7.vop7backend.Datasources.WazeDataSource;
import vop.groep7.vop7backend.Security.TokenManager;
import vop.groep7.vop7backend.database.DataAccessProvider;

/**
 *
 * @author Backend Team
 */
@Configuration
public class AppConfig {

    private static AbstractApplicationContext context;

    private static final String PROPERTIES = "application.properties";
    private static Properties props;

    /**
     * Get the value of a property in the application properties file
     *
     * @param property name of the property
     * @return value of the property
     */
    public static String getApplicationProperty(String property) {
        if (props == null) {
            props = new Properties();
            try {
                try (InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream(PROPERTIES)) {
                    props.load(inputStream);
                }
            } catch (IOException ex) {
                Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, "Cannot load properties", ex);
            }
        }

        return props.getProperty(property);
    }
    
    /**
     * Get the list with blacklisted patterns for the email address of recipients
     * 
     * @return the list of patterns
     */
    public static List<String> getEmailBlacklistPatterns(){
        String patternList = getApplicationProperty("email_blacklist");
        return Arrays.asList(patternList);
    }

    /**
     * Get the Application Context
     *
     * @return The context of this application
     */
    public static ApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("/beans.xml");
            context.registerShutdownHook();
        }
        return context;
    }

    /**
     * Set the Application Context
     *
     * @param context A context for this application
     */
    public static void setContext(AbstractApplicationContext context) {
        AppConfig.context = context;
    }

    /**
     * Get the DataAccessProvider bean
     *
     * @see DataAccessProvider
     * @return The DataAccessProvider of this application
     */
    @Bean(name = "dataAccessProvider", autowire = Autowire.BY_NAME)
    public static DataAccessProvider getDataAccessProvider() {
        return getContext().getBean(DataAccessProvider.class);
    }

    /**
     * Get the UserController bean
     *
     * @see UserController
     * @return The UserController of this application
     */
    @Bean(name = "userController", autowire = Autowire.BY_NAME)
    public static UserController getUserController() {
        return getContext().getBean(UserController.class);
    }

    /**
     * Get the POIController bean
     *
     * @see POIController
     * @return The POIController of this application
     */
    @Bean(name = "poiController", autowire = Autowire.BY_NAME)
    public static POIController getPOIController() {
        return getContext().getBean(POIController.class);
    }

    /**
     * Get the TravelController bean
     *
     * @see TravelController
     * @return The TravelController of this application
     */
    @Bean(name = "travelController", autowire = Autowire.BY_NAME)
    public static TravelController getTravelController() {
        return getContext().getBean(TravelController.class);
    }

    /**
     * Get the RouteController bean
     *
     * @see RouteController
     * @return The RouteController of this application
     */
    @Bean(name = "routeController", autowire = Autowire.BY_NAME)
    public static RouteController getRouteController() {
        return getContext().getBean(RouteController.class);
    }

    /**
     * Get the UtilController bean
     *
     * @see UtilController
     * @return The UtilController of this application
     */
    @Bean(name = "utilController", autowire = Autowire.BY_NAME)
    public static UtilController getUtilController() {
        return getContext().getBean(UtilController.class);
    }

    /**
     * Get the EventController bean
     *
     * @see EventController
     * @return The EventController of this application
     */
    @Bean(name = "eventController", autowire = Autowire.BY_NAME)
    public static EventController getEventController() {
        return getContext().getBean(EventController.class);
    }

    /**
     * Get the MailSender bean
     *
     * @see MailSender
     * @return The MailSender of this application
     */
    @Bean(name = "mailSender", autowire = Autowire.BY_NAME)
    public static MailSender getMailSender() {
        return getContext().getBean(JavaMailSenderImpl.class);
    }
    
    /**
     * Get the MailService bean
     *
     * @see MailService
     * @return The MailService of this application
     */
    @Bean(name = "mailService", autowire = Autowire.BY_NAME)
    public static MailService getMailService() {
        return getContext().getBean(MailService.class);
    }
    
    /**
     * Get the Validator bean
     *
     * @see Validator
     * @return The Validator of this application
     */
    @Bean(name = "validator", autowire = Autowire.BY_NAME)
    public static Validator getValidator() {
        return getContext().getBean(Validator.class);
    }
    
    /**
     * Get the TokenManager bean
     *
     * @see TokenManager
     * @return The TokenManager of this application
     */
    @Bean(name = "tokenManager", autowire = Autowire.BY_NAME)
    public static TokenManager getTokenManager() {
        return getContext().getBean(TokenManager.class);
    }
    
    /**
     * Get the Processor bean
     *
     * @see Processor
     * @return The Processor of this application
     */
    @Bean(name = "processor", autowire = Autowire.BY_NAME)
    public static Processor getProcessor() {
        return getContext().getBean(Processor.class);
    }
    
    /**
     * Get the Waze datasource bean
     *
     * @see WazeDataSource
     * @return The Waze datasource of this application
     */
    @Bean(name = "wazeDataSource", autowire = Autowire.BY_NAME)
    public static WazeDataSource getWazeDataSource() {
        return getContext().getBean(WazeDataSource.class);
    }
}

