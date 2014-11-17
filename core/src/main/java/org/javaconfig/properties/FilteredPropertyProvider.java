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
package org.javaconfig.properties;

import org.javaconfig.ConfigChangeSet;
import org.javaconfig.MetaInfoBuilder;
import org.javaconfig.PropertyProvider;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

class FilteredPropertyProvider extends AbstractPropertyProvider{

    private static final long serialVersionUID = 4301042530074932562L;
    private PropertyProvider unit;
    private Predicate<String> filter;

    public FilteredPropertyProvider(PropertyProvider configuration, Predicate<String> filter){
        super(MetaInfoBuilder.of(configuration.getMetaInfo()).setType("filtered").set("filter", filter.toString()).build());
        Objects.requireNonNull(configuration);
        this.unit = configuration;
        this.filter = filter;
    }

    @Override
    public Map<String,String> toMap(){
        final Map<String,String> result = new HashMap<>();
        this.unit.toMap().entrySet().forEach(e -> {
            if(filter.test(e.getKey())){
                result.put(e.getKey(), e.getValue());
            }
        });
        return result;
    }

    @Override
    public void load(){
        unit.load();
        super.load();
    }

    /**
     * Apply a config change to this item. Hereby the change must be related to the same instance.
     * @param change the config change
     * @throws org.javaconfig.ConfigException if an unrelated change was passed.
     * @throws UnsupportedOperationException when the configuration is not writable.
     */
    @Override
    public void apply(ConfigChangeSet change){
        this.unit.apply(change);
    }

}
