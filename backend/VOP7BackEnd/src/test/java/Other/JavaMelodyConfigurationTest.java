package Other;

import net.bull.javamelody.Parameter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import vop.groep7.vop7backend.JavaMelodyConfiguration;

/**
 * @author Nick De Smedt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JavaMelodyConfiguration.class)
public class JavaMelodyConfigurationTest {

    @Autowired
    FilterRegistrationBean javaMelody;
    
    String monitoringPath = "/monitoring";

    @Test
    public void context() {
        Assert.assertNotNull(javaMelody);
        Assert.assertEquals("/monitoring", monitoringPath);
        Assert.assertEquals(monitoringPath, javaMelody.getInitParameters().get(Parameter.MONITORING_PATH.getCode()));
    }
    
}