package org.javaconfig.config;

import org.javaconfig.Configuration;
import org.javaconfig.MetaInfo;
import org.javaconfig.PropertyProvider;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Anatole on 21.10.2014.
 */
public final class Configurations {

    private Configurations(){}

    public static Configuration getConfiguration(MetaInfo metaInfo, Supplier<Map<String,String>> mapSupplier){
        return new MapConfiguration(metaInfo, mapSupplier);
    }

    public static Configuration getConfiguration(PropertyProvider provider){
        return new ConfigurationDecorator(provider);
    }

}
