/*
 * Copyright 2014 Anatole Tresch and other (see authors).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tamaya.core.internal.env;

import org.apache.tamaya.Environment;

import org.apache.tamaya.core.internal.MetaConfig;
import org.apache.tamaya.spi.Bootstrap;
import org.apache.tamaya.spi.EnvironmentManagerSingletonSpi;
import org.apache.tamaya.core.spi.EnvironmentProvider;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for accessing {@link org.apache.tamaya.Environment}. Environments are used to
 * access/determine configurations.<br/>
 * <h3>Implementation PropertyMapSpec</h3> This class is
 * <ul>
 * <li>thread safe,
 * <li>and behaves contextual.
 * </ul>
 *
 * @author Anatole Tresch
 */
public class SingleEnvironmentManager implements EnvironmentManagerSingletonSpi{

    private static final String ENVIRONMENT_ORDER_KEY = "org.apache.tamaya.environmentOrder";
    private static final String DEFAULT_ENVIRONMENT_ORDER = "root,system,ear,application";

    private List<EnvironmentProvider> environmentProviders = loadEnvironmentProviders();

    private List<EnvironmentProvider> loadEnvironmentProviders() {
        Map<String,EnvironmentProvider> providers = new HashMap<>();
        for(EnvironmentProvider prov: Bootstrap.getServices(EnvironmentProvider.class)){
            providers.put(prov.getEnvironmentType(), prov);
        }
        String providerOrdering = MetaConfig.getOrDefault(ENVIRONMENT_ORDER_KEY, DEFAULT_ENVIRONMENT_ORDER);
        String[] ids = providerOrdering.split(",");
        List<EnvironmentProvider> providerList = new ArrayList<>();
        for(String id: ids) {
            providerList.add(Optional.of(providers.get(id.trim())).get());
        }
        return providerList;
    }

    @Override
    public Environment getEnvironment(){
        Environment env = null;
        for(EnvironmentProvider prov: environmentProviders){
            if(prov.isEnvironmentActive()){
                env = prov.getEnvironment(env);
            }
        }
        if(env==null){
            throw new IllegalStateException("No current environment present.");
        }
        return env;
    }

    @Override
    public Environment getRootEnvironment(){
        for(EnvironmentProvider prov: environmentProviders){
            if(prov.isEnvironmentActive()){
                return prov.getEnvironment(null);
            }
        }
        throw new IllegalStateException("No root environment present.");
    }

    @Override
    public List<String> getEnvironmentTypeOrder() {
        return environmentProviders.stream().map(EnvironmentProvider::getEnvironmentType).collect(Collectors.toList());
    }
}
