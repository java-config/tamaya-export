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
package org.apache.tamaya.core.properties;

import org.apache.tamaya.ConfigChangeSet;
import org.apache.tamaya.MetaInfo;
import org.apache.tamaya.MetaInfoBuilder;
import org.apache.tamaya.PropertyProvider;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Created by Anatole on 12.04.2014.
 */
class ContextualPropertyProvider implements PropertyProvider{

    private volatile Map<String,PropertyProvider> cachedMaps = new ConcurrentHashMap<>();

    private Supplier<PropertyProvider> mapSupplier;
    private Supplier<String> isolationKeySupplier;
    private MetaInfo metaInfo;

    /**
     * Creates a new contextual PropertyMap. Contextual maps delegate to different instances of PropertyMap depending
     * on the keys returned from the isolationP
     *
     * @param mapSupplier
     * @param isolationKeySupplier
     */
    public ContextualPropertyProvider(Supplier<PropertyProvider> mapSupplier, Supplier<String> isolationKeySupplier){
        this.metaInfo = MetaInfoBuilder.of().setType("contextual").set("mapSupplier", mapSupplier.toString())
                      .set("isolationKeySupplier", isolationKeySupplier.toString()).build();
        Objects.requireNonNull(mapSupplier);
        Objects.requireNonNull(isolationKeySupplier);
        this.mapSupplier = mapSupplier;
        this.isolationKeySupplier = isolationKeySupplier;
    }

    /**
     * This method provides the contextual Map for the current environment. Hereby, ba default, for each different
     * key returned by the #isolationKeySupplier a separate PropertyMap instance is acquired from the #mapSupplier.
     * If the map supplier returns an instance it is cached in the local #cachedMaps.
     *
     * @return the current contextual PropertyMap.
     */
    protected PropertyProvider getContextualMap(){
        String environmentKey = this.isolationKeySupplier.get();
        if(environmentKey == null){
            return PropertyProviders.empty();
        }
        PropertyProvider map = this.cachedMaps.get(environmentKey);
        if(map == null){
            synchronized(cachedMaps){
                map = this.cachedMaps.get(environmentKey);
                if(map == null){
                    map = this.mapSupplier.get();
                    if(map == null){
                        return PropertyProviders
                                .empty(MetaInfoBuilder.of().setInfo(
                                        "No map provided by supplier " + mapSupplier + " for key " + environmentKey)
                                                  .build());
                    }
                    this.cachedMaps.put(environmentKey, map);
                }
            }
        }
        return map;
    }

    @Override
    public void load(){
        getContextualMap().load();
    }

    @Override
    public boolean containsKey(String key){
        return getContextualMap().containsKey(key);
    }

    @Override
    public Map<String,String> toMap(){
        return getContextualMap().toMap();
    }

    @Override
    public MetaInfo getMetaInfo(){
        return this.metaInfo;
    }

    @Override
    public Optional<String> get(String key){
        return getContextualMap().get(key);
    }

    @Override
    public Set<String> keySet(){
        return getContextualMap().keySet();
    }

    /**
     * Apply a config change to this item. Hereby the change must be related to the same instance.
     * @param change the config change
     * @throws org.apache.tamaya.ConfigException if an unrelated change was passed.
     * @throws UnsupportedOperationException when the configuration is not writable.
     */
    @Override
    public void apply(ConfigChangeSet change){
        getContextualMap().apply(change);
    }

    /**
     * Access a cached PropertyMap.
     *
     * @param key the target environment key as returned by the environment key supplier, not null.
     * @return the corresponding PropertyMap, or null.
     */
    public PropertyProvider getCachedMap(String key){
        return this.cachedMaps.get(key);
    }

    /**
     * Access the set of currently loaded/cached maps.
     *
     * @return the set of cached map keys, never null.
     */
    public Set<String> getCachedMapKeys(){
        return this.cachedMaps.keySet();
    }

    /**
     * Access the supplier for environment key, determining map isolation.
     *
     * @return the environment key supplier instance, not null.
     */
    public Supplier<String> getIsolationKeySupplier(){
        return this.isolationKeySupplier;
    }

    /**
     * Access the supplier for new PropertyMap instances.
     *
     * @return the PropertyMap supplier instance, not null.
     */
    public Supplier<PropertyProvider> getMapSupplier(){
        return this.mapSupplier;
    }

    @Override
    public String toString(){
        return "ContextualMap{" +
                "cachedMaps(key)=" + cachedMaps.keySet() +
                ", mapSupplier=" + mapSupplier +
                ", isolationKeySupplier=" + isolationKeySupplier +
                '}';
    }
}
