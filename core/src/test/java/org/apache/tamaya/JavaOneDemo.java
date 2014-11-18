package org.apache.tamaya;

import org.apache.tamaya.core.config.ConfigurationBuilder;
import org.apache.tamaya.core.config.ConfigurationFormats;
import org.apache.tamaya.core.properties.AggregationPolicy;
import org.apache.tamaya.core.properties.PropertyProviders;
import org.apache.tamaya.samples.annotations.ConfiguredClass;
import org.apache.tamaya.core.spi.ConfigurationFormat;
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
