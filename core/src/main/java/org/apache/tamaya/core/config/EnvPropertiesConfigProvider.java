package org.apache.tamaya.core.config;

import org.apache.tamaya.core.properties.AggregationPolicy;
import org.apache.tamaya.core.properties.PropertyProviders;
import org.apache.tamaya.core.spi.ConfigurationProviderSpi;

import org.apache.tamaya.Configuration;

/**
 * Provides a {@link org.apache.tamaya.Configuration} named 'environment.properties'
 * containing the current environment properties.
 *
 * Created by Anatole on 29.09.2014.
 */
public class EnvPropertiesConfigProvider implements ConfigurationProviderSpi{

    private Configuration envConfig;

    public EnvPropertiesConfigProvider(){
        envConfig = ConfigurationBuilder.of("environment.properties").addConfigMaps(AggregationPolicy.OVERRIDE, PropertyProviders.fromEnvironmentProperties()).build();
    }

    @Override
    public String getConfigName(){
        return "environment.properties";
    }

    @Override
    public Configuration getConfiguration(){
        return envConfig;
    }
}
