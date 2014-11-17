package org.javaconfig.env;

import org.javaconfig.Environment;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for basic {@link org.javaconfig.EnvironmentManager} functionality.
 * Created by Anatole on 17.10.2014.
 */
public class EnvironmentManagerTest {

    @Test
    public void testGetEnvironment(){
        Environment env = Environment.of();
        assertNotNull(env);
        Environment env2 = Environment.of();
        assertNotNull(env2);
        assertTrue("Current Environments requested in same context are not the same!", env==env2);
    }

    @Test
    public void testGetRootEnvironment(){
        Environment env = Environment.getRootEnvironment();
        assertNotNull(env);
        Environment env2 = Environment.getRootEnvironment();
        assertNotNull(env2);
        assertTrue("Root Environments requested in same context are not the same!", env==env2);
    }

    @Test
    public void testRootIsNotCurrentEnvironment(){
        Environment env1 = Environment.getRootEnvironment();
        Environment env2 = Environment.of();
        assertNotNull(env1);
        assertNotNull(env2);
        assertNotSame(env1, env2);
    }

    @Test
    public void testEnvironmentTypeOrder(){
        List<String> envOrder = Environment.getEnvironmentTypeOrder();
        assertNotNull(envOrder);
        assertTrue(envOrder.size()>0);
        assertTrue(envOrder.contains("root"));
        assertTrue(envOrder.contains("system"));
        assertTrue(envOrder.contains("ear"));
        assertTrue(envOrder.contains("application"));
        assertTrue(envOrder.get(0).equals("root"));
    }

    @Test
    public void testEnvironmentHierarchy(){
        List<String> envOrder = Environment.getEnvironmentHierarchy();
        assertNotNull(envOrder);
        assertTrue(envOrder.size()>0);
        assertTrue(envOrder.contains("root(root)"));
        assertTrue(envOrder.contains("system(system)"));
        assertTrue(envOrder.contains("myEar1(ear)"));
        assertTrue(envOrder.contains("MyApp1(application)"));
        assertTrue(envOrder.get(0).equals("MyApp1(application)"));
    }

    @Test
    public void testEnvironmentPath(){
        String envPath = Environment.of().getEnvironmentPath();
        assertNotNull(envPath);
        assertTrue(envPath.contains("root(root)"));
        assertTrue(envPath.contains("system(system)"));
        assertTrue(envPath.contains("myEar1(ear)"));
        assertTrue(envPath.contains("MyApp1(application)"));
    }

    @Test
    public void testEnvironmentOverride(){
        assertEquals(Environment.getRootEnvironment().get("user.country").get(),System.getProperty("user.country"));
        assertEquals(Environment.of().get("user.country").get(), "DE_1234");
    }
}
