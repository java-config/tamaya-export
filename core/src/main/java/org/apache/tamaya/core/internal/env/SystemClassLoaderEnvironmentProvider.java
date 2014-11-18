package org.apache.tamaya.core.internal.env;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tamaya.Environment;
import org.apache.tamaya.Stage;
import org.apache.tamaya.core.config.ConfigurationFormats;
import org.apache.tamaya.core.env.EnvironmentBuilder;
import org.apache.tamaya.spi.Bootstrap;
import org.apache.tamaya.core.spi.ConfigurationFormat;
import org.apache.tamaya.core.spi.EnvironmentProvider;
import org.apache.tamaya.core.spi.ResourceLoader;


import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Anatole on 17.10.2014.
 */
public class SystemClassLoaderEnvironmentProvider implements EnvironmentProvider {

    private static  final Logger LOG = LogManager.getLogger(SystemClassLoaderEnvironmentProvider.class);

    private Environment environment;

    @Override
    public String getEnvironmentType() {
        return "system";
    }

    @Override
    public boolean isEnvironmentActive() {
        return true;
    }

    @Override
    public Environment getEnvironment(Environment parentEnvironment) {
        if(this.environment!=null){
            return this.environment;
        }
        List<URI> propertyUris = Bootstrap.getService(ResourceLoader.class).getResources(ClassLoader.getSystemClassLoader(),
                "classpath*:META-INF/env/system.properties", "classpath*:META-INF/env/system.xml", "classpath*:META-INF/env/system.ini");
        EnvironmentBuilder builder = EnvironmentBuilder.of("system", getEnvironmentType());
        for(URI uri:propertyUris){
            try{
                ConfigurationFormat format = ConfigurationFormats.getFormat(uri);
                Map<String,String> data = format.readConfiguration(uri);
                builder.setAll(data);
            }
            catch(Exception e){
                LOG.error("Error readong environment data from " + uri, e);
            }
        }
        builder.setParent(parentEnvironment);
        String stageValue =  builder.getProperty(InitialEnvironmentProvider.STAGE_PROP);
        Stage stage = InitialEnvironmentProvider.DEFAULT_STAGE;
        if (stageValue != null) {
            stage = Stage.of(stageValue);
        }
        builder.setStage(stage);
        builder.set("classloader.type", ClassLoader.getSystemClassLoader().getClass().getName());
        builder.set("classloader.info", ClassLoader.getSystemClassLoader().toString());
        Set<URI> uris = new HashSet<>();
        uris.addAll(propertyUris);
        builder.set("environment.sources", uris.toString());
        this.environment = builder.build();
        return this.environment;
    }
}
