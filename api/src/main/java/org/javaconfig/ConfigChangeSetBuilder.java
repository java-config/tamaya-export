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
package org.javaconfig;

import java.beans.PropertyChangeEvent;
import java.util.*;

/**
 * Models a set of changes to be applied to a configuration/property provider.  Such a set can be applied
 * to any {@link org.javaconfig.PropertyProvider} instance. If the provider is mutable it may check the
 * version given and apply the changes to the provider/configuration, including triggering of regarding
 * change events.
 *
 * Created by Anatole on 06.09.2014.
 */
public final class ConfigChangeSetBuilder {
    /** The recorded changes. */
    final SortedMap<String, PropertyChangeEvent> delta = new TreeMap<>();
    /** The underlying configuration/provider. */
    PropertyProvider source;
    /** The base version, if any. Used for optimistic version checking. */
    String baseVersion;

    /**
     * Constructor.
     * @param source the underlying configuration/provider, not null.
     * @param baseVersion the base version, used for optimistic version checking.
     */
    private ConfigChangeSetBuilder(PropertyProvider source, String baseVersion) {
        Objects.requireNonNull(source);
        this.source = source;
        this.baseVersion= baseVersion;
    }

    /**
     * Creates a new instance of this builder.
     * @param source the underlying property provider/configuration, not null.
     * @param baseVersion the base version to be used.
     * @return the builder for chaining.
     */
    public static ConfigChangeSetBuilder of(PropertyProvider source, String baseVersion) {
        return new ConfigChangeSetBuilder(source, baseVersion);
    }

    /**
     * Creates a new instance of this builder.
     * @param configuration the base configuration, not null.
     * @return the builder for chaining.
     */
    public static ConfigChangeSetBuilder of(Configuration configuration) {
        return new ConfigChangeSetBuilder(configuration, configuration.getVersion());
    }

    /**
     * Add a change as a {@link java.beans.PropertyChangeEvent}.
     * @param changeEvent the change event.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder addChange(PropertyChangeEvent changeEvent) {
        Objects.requireNonNull(changeEvent);
        this.delta.put(changeEvent.getPropertyName(), changeEvent);
        return this;
    }

    /**
     * This method records all changes to be applied to the base property provider/configuration to
     * achieve the given target state.
     * @param newState the new target state, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder addChanges(PropertyProvider newState) {
        compare(newState, this.source).forEach((c) -> this.delta.put(c.getPropertyName(), c));
        return this;
    }

    /**
     * Get the current values, also considering any changes recorded within this change set.
     * @param key the key of the entry, not null.
     * @return the value, or null.
     */
    public String get(String key) {
        PropertyChangeEvent change = this.delta.get(key);
        if(change!=null && !(change.getNewValue()==null)){
            return (String)change.getNewValue();
        }
        return null;
    }

    /**
     * Marks the given key(s) from the configuration/properties to be removed.
     * @param key the key of the entry, not null.
     * @param otherKeys additional keys to be removed (convenience), not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder remove(String key, String... otherKeys) {
        String oldValue = this.source.get(key).orElse(null);
        if(oldValue==null){
            this.delta.remove(key);
        }
        this.delta.put(key, new PropertyChangeEvent(this.source, key, oldValue, null));
        for(String addKey:otherKeys){
            oldValue = this.source.get(addKey).orElse(null);
            if(oldValue==null){
                this.delta.remove(addKey);
            }
            this.delta.put(addKey, new PropertyChangeEvent(this.source, addKey, oldValue, null));
        }
        return this;
    }

    /**
     * Applies the given value.
     * @param key the key of the entry, not null.
     * @param value the value to be applied, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder put(String key, String value) {
        this.delta.put(key, new PropertyChangeEvent(this.source, key, this.source.get(key).orElse(null), Objects.requireNonNull(value)));
        return this;
    }

    /**
     * Applies the given value.
     * @param key the key of the entry, not null.
     * @param value the value to be applied, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder put(String key, Boolean value) {
        this.delta.put(key, new PropertyChangeEvent(this.source, key, this.source.get(key).orElse(null), Objects.requireNonNull(value).toString()));
        return this;
    }

    /**
     * Applies the given value.
     * @param key the key of the entry, not null.
     * @param value the value to be applied, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder put(String key, Byte value) {
        this.delta.put(key, new PropertyChangeEvent(this.source, key, this.source.get(key).orElse(null), Objects.requireNonNull(value).toString()));
        return this;
    }

    /**
     * Applies the given value.
     * @param key the key of the entry, not null.
     * @param value the value to be applied, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder put(String key, Character value) {
        this.delta.put(key, new PropertyChangeEvent(this.source, key, this.source.get(key).orElse(null), Objects.requireNonNull(value).toString()));
        return this;
    }

    /**
     * Applies the given value.
     * @param key the key of the entry, not null.
     * @param value the value to be applied, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder put(String key, Short value) {
        this.delta.put(key, new PropertyChangeEvent(this.source, key, this.source.get(key).orElse(null), Objects.requireNonNull(value).toString()));
        return this;
    }

    /**
     * Applies the given value.
     * @param key the key of the entry, not null.
     * @param value the value to be applied, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder put(String key, Integer value) {
        this.delta.put(key, new PropertyChangeEvent(this.source, key, this.source.get(key).orElse(null), Objects.requireNonNull(value).toString()));
        return this;
    }

    /**
     * Applies the given value.
     * @param key the key of the entry, not null.
     * @param value the value to be applied, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder put(String key, Long value) {
        this.delta.put(key, new PropertyChangeEvent(this.source, key, this.source.get(key).orElse(null), Objects.requireNonNull(value).toString()));
        return this;
    }

    /**
     * Applies the given value.
     * @param key the key of the entry, not null.
     * @param value the value to be applied, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder put(String key, Float value) {
        this.delta.put(key, new PropertyChangeEvent(this.source, key, this.source.get(key).orElse(null), Objects.requireNonNull(value).toString()));
        return this;
    }

    /**
     * Applies the given value.
     * @param key the key of the entry, not null.
     * @param value the value to be applied, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder put(String key, Double value) {
        this.delta.put(key, new PropertyChangeEvent(this.source, key, this.source.get(key).orElse(null), Objects.requireNonNull(value).toString()));
        return this;
    }

    /**
     * Applies the given value.
     * @param key the key of the entry, not null.
     * @param value the value to be applied, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder put(String key, Object value) {
        this.delta.put(key, new PropertyChangeEvent(this.source, key, this.source.get(key).orElse(null), Objects.requireNonNull(value).toString()));
        return this;
    }

    /**
     * Apply all the given values to the base configuration/properties.
     * @param changes the changes to be applied, not null.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder putAll(Map<String,String> changes) {
        changes.forEach((k,v) ->
                this.delta.put(k, new PropertyChangeEvent(this.source, k, this.source.get(k).orElse(null), v)));
        return this;
    }

    /**
     * This method will create a change set that clears all entries from the given base configuration/properties.
     * @return the builder for chaining.
     */
    public ConfigChangeSetBuilder clear() {
        this.delta.clear();
        this.source.toMap().forEach((k,v) ->
                this.delta.put(k, new PropertyChangeEvent(this.source, k, v, null)));
        return this;
    }

    /**
     * Checks if the change set is empty, i.e. does not contain any changes.
     * @return true, if the set is empty.
     */
    public boolean isEmpty(){
        return this.delta.isEmpty();
    }

    /**
     * Resets this change set instance. This will clear all changes done to this set, so the
     * set will be empty.
     */
    public void reset(){
        this.delta.clear();
    }

    /**
     * Builds the corresponding change set.
     * @return the new change set, never null.
     */
    public ConfigChangeSet build() {
        return new ConfigChangeSet(this.source, baseVersion, Collections.unmodifiableCollection(this.delta.values()));
    }

    /**
     * Compares the two property providers/configurations and creates a collection of all changes
     * that must be appied to render {@code map1} into {@code map2}.
     * @param map1 the source map, not null.
     * @param map2 the target map, not null.
     * @return a collection of change events, never null.
     */
    public static Collection<PropertyChangeEvent> compare(PropertyProvider map1, PropertyProvider map2) {
        List<PropertyChangeEvent> changes = new ArrayList<>();
        for (Map.Entry<String, String> en : map1.toMap().entrySet()) {
            Optional<String> val = map2.get(en.getKey());
            if(!val.isPresent()) {
                changes.add(new PropertyChangeEvent(map1, en.getKey(), null, en.getValue()));
            }
            else if(!val.get().equals(en.getValue())){
                changes.add(new PropertyChangeEvent(map1, en.getKey(), val.get(), en.getValue()));
            }
        }
        for (Map.Entry<String, String> en : map2.toMap().entrySet()) {
            Optional<String> val = map1.get(en.getKey());
            if(!val.isPresent()) {
                changes.add(new PropertyChangeEvent(map1, en.getKey(), null, en.getValue()));
            }
            else if(!val.equals(en.getValue())){
                changes.add(new PropertyChangeEvent(map1, en.getKey(), val.get(), en.getValue()));
            }
        }
        return changes;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PropertyChangeEventBuilder [source=" + source + ", " +
                ", delta=" + delta + "]";
    }

}
