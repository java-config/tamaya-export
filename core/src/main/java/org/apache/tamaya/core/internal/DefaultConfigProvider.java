package org.apache.tamaya.core.internal;

import org.apache.tamaya.core.config.ConfigurationBuilder;
import org.apache.tamaya.core.properties.AggregationPolicy;
import org.apache.tamaya.core.properties.PropertyProviders;
import org.apache.tamaya.core.spi.ConfigurationProviderSpi;
import org.jboss.weld.literal.NamedLiteral;

import org.apache.tamaya.Configuration;
import org.apache.tamaya.Environment;
import org.apache.tamaya.MetaInfoBuilder;

/**
 * Created by Anatole on 30.09.2014.
 */
public class DefaultConfigProvider implements ConfigurationProviderSpi{

    private Configuration config;

    @Override
    public String getConfigName(){
        return "default";
    }

    @Override
    public Configuration getConfiguration(){
        if(config == null){
            config = ConfigurationBuilder.of(NamedLiteral.DEFAULT.value())
                    .addResources("classpath*:META-INF/config/**/*.xml", "classpath*:META-INF/config/**/*.properties",
                                  "classpath*:META-INF/config/**/*.init").setMetainfo(
                            MetaInfoBuilder.of("Default Configuration")
                                    .setSourceExpressions("classpath*:META-INF/config/**/*.xml",
                                                          "classpath*:META-INF/config/**/*" + ".properties",
                                                          "classpath*:META-INF/config/**/*.ini").build())
                    .addConfigMaps(AggregationPolicy.OVERRIDE, PropertyProviders.fromEnvironmentProperties(),
                                   PropertyProviders.fromSystemProperties()).build();
        }
        return config;
    }
}
