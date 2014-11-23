/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
