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
package org.apache.tamaya.core.internal.env;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tamaya.Environment;
import org.apache.tamaya.core.config.ConfigurationFormats;
import org.apache.tamaya.core.env.EnvironmentBuilder;
import org.apache.tamaya.spi.Bootstrap;
import org.apache.tamaya.core.spi.ConfigurationFormat;
import org.apache.tamaya.core.spi.EnvironmentProvider;
import org.apache.tamaya.core.spi.ResourceLoader;


import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Anatole on 17.10.2014.
 */
public class ClassLoaderDependentApplicationEnvironmentProvider implements EnvironmentProvider {

    private static  final Logger LOG = LogManager.getLogger(ClassLoaderDependentApplicationEnvironmentProvider.class);

    private static final String WARID_PROP = "org.apache.tamaya.env.applicationId";

    private Map<ClassLoader, Environment> environments = new ConcurrentHashMap<>();
    private Map<String, Environment> environmentsByAppId = new ConcurrentHashMap<>();

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
        if(applicationId!=null) {
            this.environmentsByAppId.put(applicationId, environment);
        }
        return environment;
    }

    @Override
    public Set<String> getEnvironmentContexts() {
        return environmentsByAppId.keySet();
    }
}
