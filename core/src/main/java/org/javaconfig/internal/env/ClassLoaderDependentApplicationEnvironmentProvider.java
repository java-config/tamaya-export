package org.javaconfig.internal.env;

import org.javaconfig.Environment;
import org.javaconfig.config.ConfigurationFormats;
import org.javaconfig.env.EnvironmentBuilder;
import org.javaconfig.spi.Bootstrap;
import org.javaconfig.spi.ConfigurationFormat;
import org.javaconfig.spi.EnvironmentProvider;
import org.javaconfig.spi.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;

/**
 * Created by Anatole on 17.10.2014.
 */
public class ClassLoaderDependentApplicationEnvironmentProvider implements EnvironmentProvider {

    private static  final Logger LOG = LoggerFactory.getLogger(ClassLoaderDependentApplicationEnvironmentProvider.class);

    private static final String WARID_PROP = "org.javaconfig.env.applicationId";

    private Map<ClassLoader, Environment> environments = new WeakHashMap<>();

    @Override
    public String getEnvironmentType() {
        return "application";
    }

    @Override
    public boolean isEnvironmentActive() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if(cl==null){
            return false;
        }
        List<URI> propertyUris = Bootstrap.getService(ResourceLoader.class).getResources(cl,
                "classpath*:META-INF/env/application.properties", "classpath*:META-INF/env/application.xml", "classpath*:META-INF/env/application.ini");
        return !propertyUris.isEmpty();
    }

    @Override
    public Environment getEnvironment(Environment parentEnvironment) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if(cl==null){
            return null;
        }
        Environment environment = this.environments.get(cl);
        if(environment!=null){
            return environment;
        }
        List<URI> propertyUris = Bootstrap.getService(ResourceLoader.class).getResources(cl,
                "classpath*:META-INF/env/application.properties", "classpath*:META-INF/env/application.xml", "classpath*:META-INF/env/application.ini");
        Map<String,String> data = new HashMap<>();

        for(URI uri:propertyUris){
            try{
                ConfigurationFormat format = ConfigurationFormats.getFormat(uri);
                Map<String,String> read = format.readConfiguration(uri);
                data.putAll(read);
            }
            catch(Exception e){
                LOG.error("Error reading application environment data from " + uri, e);
            }
        }
        String applicationId = data.getOrDefault(WARID_PROP, cl.toString());
        EnvironmentBuilder builder = EnvironmentBuilder.of(applicationId, getEnvironmentType());
        builder.setParent(parentEnvironment);
        builder.set("classloader.type", cl.getClass().getName());
        builder.set("classloader.info", cl.toString());
        Set<URI> uris = new HashSet<>();
        uris.addAll(propertyUris);
        builder.set("environment.sources", uris.toString());
        builder.setAll(data);
        environment = builder.build();
        this.environments.put(cl, environment);
        return environment;
    }
}
