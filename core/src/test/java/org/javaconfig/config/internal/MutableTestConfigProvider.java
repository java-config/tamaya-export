package org.javaconfig.config.internal;

import org.javaconfig.Configuration;
import org.javaconfig.MetaInfoBuilder;
import org.javaconfig.PropertyProvider;
import org.javaconfig.config.Configurations;
import org.javaconfig.properties.PropertyProviders;
import org.javaconfig.spi.ConfigurationProviderSpi;

import java.beans.PropertyChangeEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Anatole on 29.09.2014.
 */
public class MutableTestConfigProvider implements ConfigurationProviderSpi{

    private static final String CONFIG_NAME = "mutableTestConfig";
    private Configuration testConfig;
    private final Map<String, String> dataMap = new ConcurrentHashMap<>();

    public MutableTestConfigProvider(){
        dataMap.put("dad", "Anatole");
        dataMap.put("mom", "Sabine");
        dataMap.put("sons.1", "Robin");
        dataMap.put("sons.2", "Luke");
        dataMap.put("sons.3", "Benjamin");
        PropertyProvider provider = PropertyProviders.from(MetaInfoBuilder.of().setName(CONFIG_NAME).build(),
                dataMap);
        testConfig = Configurations.getConfiguration(provider);
    }

    @Override
    public String getConfigName(){
        return CONFIG_NAME;
    }

    @Override
    public Configuration getConfiguration(){
        return testConfig;
    }
}
