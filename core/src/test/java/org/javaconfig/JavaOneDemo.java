package org.javaconfig;

import org.javaconfig.config.ConfigurationBuilder;
import org.javaconfig.config.ConfigurationFormats;
import org.javaconfig.properties.AggregationPolicy;
import org.javaconfig.properties.PropertyProviders;
import org.javaconfig.samples.annotations.ConfiguredClass;
import org.javaconfig.spi.ConfigurationFormat;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Anatole on 30.09.2014.
 */
public class JavaOneDemo{
    @Test
    public void testInjection(){
        Weld weld = new Weld();
        try {
            WeldContainer container = weld.initialize();
            ConfiguredClass item = container.instance().select(ConfiguredClass.class).get();
            System.out.println("********************************************");
            System.out.println(item);
            System.out.println("********************************************");
            assertNotNull(item.getValue1());
            assertNotNull(item.getRuntimeVersion());
        }finally{
            weld.shutdown();
        }
    }

    @Test
    public void testFromSystemProperties(){
        PropertyProvider prov = PropertyProviders.fromSystemProperties();
        assertNotNull(prov);
        for(Map.Entry<Object,Object> en:System.getProperties().entrySet()){
            assertEquals(en.getValue(), prov.get(en.getKey().toString()).get());
        }
    }

    @Test
    public void testProgrammatixPropertySet(){
        System.out.println(PropertyProviders.fromPaths("test", "classpath:test.properties"));
    }

    @Test
    public void testProgrammaticConfig(){
        ConfigurationFormat format = ConfigurationFormats.getPropertiesFormat();
        Map<String,String> cfgMap = new HashMap<>();
        cfgMap.put("param1", "value1");
        cfgMap.put("a", "Adrian"); // overrides Anatole
        Configuration config = ConfigurationBuilder.of("myTestConfig").addResources(
                "classpath:test.properties").addConfigMaps(AggregationPolicy.OVERRIDE,
                                                           PropertyProviders
                                                                   .fromPaths("classpath:cfg/test.xml"),
                                                           PropertyProviders.fromArgs(new String[]{"-arg1", "--fullarg", "fullValue", "-myflag"}),
                                                           PropertyProviders.from(cfgMap)).build();
        System.out.println(config.getAreas());
        System.out.println("---");
        System.out.println(config.getAreas(s -> s.startsWith("another")));
        System.out.println("---");
        System.out.println(config.getTransitiveAreas());
        System.out.println("---");
        System.out.println(config.getTransitiveAreas(s -> s.startsWith("another")));
        System.out.println("---");
        System.out.println(config);
        System.out.print("--- b=");
        System.out.println(config.get("b"));
        System.out.println("--- only a,b,c)");
        System.out.println(PropertyProviders.filtered((f) -> f.equals("a") || f.equals("b") || f.equals("c"), config));
    }
}
