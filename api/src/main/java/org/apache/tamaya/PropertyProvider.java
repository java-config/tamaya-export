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
package org.apache.tamaya;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This interface models a provider that serves configuration properties. The contained
 * properties may be read from single or several sources (composite).<br/>
 * Property providers are the building blocks out of which complex
 * configuration is setup.
 * <p/>
 * <h3>Implementation Requirements</h3>
 * <p></p>Implementations of this interface must be
 * <ul>
 * <li>Thread safe.
 * </ul>
 * It is highly recommended that implementations also are
 * <ul>
 * <li>Immutable</li>
 * <li>serializable</li>
 * </ul>
 * </p>
 *
 * @author Anatole Tresch
 */
public interface PropertyProvider {

    /**
     * Access a property.
     * @param key the property's key, not null.
     * @return the property's value.
     */
    Optional<String> get(String key);

    /**
     * Checks if a property is defined.
     * @param key the property's key, not null.
     * @return true, if the property is existing.
     */
    boolean containsKey(String key);

    /**
     * Access the current properties as Map.
     * @return the a corresponding map, never null.
     */
    Map<String, String> toMap();

    /**
     * Get the meta-info of a configuration.
     * @return the configuration's/config map's metaInfo, or null.
     */
    MetaInfo getMetaInfo();

    /**
     * Compares the given property provider for same
     * contents, regardless of its current state or runtime implementation.
     * @param provider the provider to be compared, not null.
     * @return true, if both property sets equals.
     */
    default boolean hasSameProperties(PropertyProvider provider) {
        return this == provider || ConfigChangeSetBuilder.compare(this, provider).isEmpty();
    }

//    /**
//     * Access a property.
//     * @param key the property's key, not null.
//     * @return the property's value.
//     */
//    default String getOrDefault(String key, String defaultValue){
//        return get(key).orElse(defaultValue);
//    }

    /**
     * Access the set of property keys, defined by this provider.
     * @return the key set, never null.
     */
    default Set<String> keySet(){
        return toMap().keySet();
    }

    /**
     * Reloads the {@link org.apache.tamaya.PropertyProvider}.
     */
    default void load(){
        // by default do nothing
    }

    /**
     * Apply a config change to this item. Hereby the change must be related to the same instance.
     * @param change the config change
     * @throws ConfigException if an unrelated change was passed.
     * @throws UnsupportedOperationException when the configuration is not writable.
     */
    default void apply(ConfigChangeSet change){
        throw new UnsupportedOperationException("Config/properties not mutable: "+ this);
    }

}
