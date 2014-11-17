package org.javaconfig;

import java.beans.PropertyChangeEvent;
import java.util.*;

/**
 * Event that contains a set of changes that were applied or could be applied.
 * This class is immutable and thread-safe. To create instances use
 * {@link ConfigChangeSetBuilder}.
 *
 * Created by Anatole on 22.10.2014.
 */
public final class ConfigChangeSet {
    /** The base property provider/configuration. */
    private PropertyProvider propertyProvider;
    /** The base version, usable for optimistic locking. */
    private String baseVersion;
    /** The recorded changes. */
    private Map<String,PropertyChangeEvent> changes = new HashMap<>();

    /**
     * Constructor used by {@link org.javaconfig.ConfigChangeSetBuilder}.
     * @param propertyProvider The base property provider/configuration, not null.
     * @param baseVersion The base version, usable for optimistic locking.
     * @param changes The recorded changes, not null.
     */
    ConfigChangeSet(PropertyProvider propertyProvider, String baseVersion, Collection<PropertyChangeEvent> changes) {
        this.propertyProvider = Objects.requireNonNull(propertyProvider);
        this.baseVersion = baseVersion;
        changes.forEach((c) -> this.changes.put(c.getPropertyName(), c));
    }

    /**
     * Get the underlying property provider/configuration.
     * @return the underlying property provider/configuration, never null.
     */
    public PropertyProvider getPropertyProvider(){
        return this.propertyProvider;
    }

    /**
     * Get the base version, usable for optimistic locking.
     * @return the base version.
     */
    public String getBaseVersion(){
        return baseVersion;
    }

    /**
     * Get the changes recorded.
     * @return the recorded changes, never null.
     */
    public Collection<PropertyChangeEvent> getEvents(){
        return Collections.unmodifiableCollection(this.changes.values());
    }

    /**
     * Access the number of removed entries.
     * @return the number of removed entries.
     */
    public int getRemovedSize() {
        return (int) this.changes.values().stream().filter((e) -> e.getNewValue() == null).count();
    }

    /**
     * Access the number of added entries.
     * @return the number of added entries.
     */
    public int getAddedSize() {
        return (int) this.changes.values().stream().filter((e) -> e.getOldValue() == null).count();
    }

    /**
     * Access the number of updated entries.
     * @return the number of updated entries.
     */
    public int getUpdatedSize() {
        return (int) this.changes.values().stream().filter((e) -> e.getOldValue()!=null && e.getNewValue()!=null).count();
    }


    /**
     * Checks if the given key was removed.
     * @param key the target key, not null.
     * @return true, if the given key was removed.
     */
    public boolean isRemoved(String key) {
        PropertyChangeEvent change = this.changes.get(key);
        return change != null && change.getNewValue() == null;
    }

    /**
     * Checks if the given key was added.
     * @param key the target key, not null.
     * @return true, if the given key was added.
     */
    public boolean isAdded(String key) {
        PropertyChangeEvent change = this.changes.get(key);
        return change != null && change.getOldValue() == null;
    }

    /**
     * Checks if the given key was updated.
     * @param key the target key, not null.
     * @return true, if the given key was updated.
     */
    public boolean isUpdated(String key) {
        PropertyChangeEvent change = this.changes.get(key);
        return change != null && change.getOldValue() != null && change.getNewValue() != null;
    }

    /**
     * Checks if the given key is added, or updated AND NOT removed.
     * @param key the target key, not null.
     * @return true, if the given key was added, or updated BUT NOT removed.
     */
    public boolean containsKey(String key) {
        PropertyChangeEvent change = this.changes.get(key);
        return change != null && change.getNewValue() != null;
    }

    /**
     * CHecks if the current change set does not contain any changes.
     * @return tru, if the change set is empty.
     */
    public boolean isEmpty(){
        return this.changes.isEmpty();
    }

    /**
     * Applies all changes to the given map instance.
     * @param map the target map.never null.
     */
    public void applyChangesTo(Map<String, String> map) {
        for(Map.Entry<String,PropertyChangeEvent> en: this.changes.entrySet()){
            if(en.getValue().getNewValue() == null){
                map.remove(en.getKey());
            }
            else{
                map.put(en.getKey(), (String)en.getValue().getNewValue());
            }
        }
    }


    @Override
    public String toString() {
        return "ConfigChangeSet{" +
                "properties=" + propertyProvider +
                ", baseVersion=" + baseVersion +
                ", changes=" + changes +
                '}';
    }
}
