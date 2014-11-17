package org.javaconfig.internal;

import org.javaconfig.config.ConfigurationBuilder;
import org.javaconfig.properties.AggregationPolicy;
import org.javaconfig.properties.PropertyProviders;
import org.javaconfig.spi.ConfigurationProviderSpi;
import org.jboss.weld.literal.NamedLiteral;

import org.javaconfig.Configuration;
import org.javaconfig.Environment;
import org.javaconfig.MetaInfoBuilder;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
