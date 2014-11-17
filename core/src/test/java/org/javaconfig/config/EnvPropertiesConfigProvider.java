package org.javaconfig.config;

import org.javaconfig.properties.AggregationPolicy;
import org.javaconfig.properties.PropertyProviders;
import org.javaconfig.spi.ConfigurationProviderSpi;

import org.javaconfig.Configuration;

/**
 * Provides a {@link org.javaconfig.Configuration} named 'environment.properties'
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
