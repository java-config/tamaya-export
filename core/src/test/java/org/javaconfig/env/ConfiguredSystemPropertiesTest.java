package org.javaconfig.env;

import org.javaconfig.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertTrue;

/**
 * Created by Anatole on 02.10.2014.
 */
public class ConfiguredSystemPropertiesTest {

    @Test
    public void testInstall(){
        Configuration config = Configuration.of();
        Properties props = System.getProperties();
        assertTrue(props.getClass().getName().equals(Properties.class.getName()));
        System.out.println("Props("+props.getClass().getName()+"): " + props);
        ConfiguredSystemProperties.install();
        props = System.getProperties();
        System.out.println("Props("+props.getClass().getName()+"): " + props);
        assertTrue(props.getClass().getName().equals(ConfiguredSystemProperties.class.getName()));
        ConfiguredSystemProperties.uninstall();
        props = System.getProperties();
        assertTrue(props.getClass().getName().equals(Properties.class.getName()));
    }
}
