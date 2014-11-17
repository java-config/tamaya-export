package org.javaconfig.properties;

import org.javaconfig.MetaInfo;
import org.javaconfig.MetaInfoBuilder;
import org.javaconfig.PropertyProvider;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Anatole on 22.10.2014.
 */
class IntersectingPropertyProvider extends AbstractPropertyProvider {

    private Collection<PropertyProvider> providers;
    private PropertyProvider union;

    public IntersectingPropertyProvider(AggregationPolicy policy, PropertyProvider... providers) {
        super(MetaInfoBuilder.of().setType("intersection").set("providers", Arrays.toString(providers)).build());
        this.providers = Arrays.asList(Objects.requireNonNull(providers));
        union = PropertyProviders.union(policy, providers);
    }

    public IntersectingPropertyProvider(MetaInfo metaInfo, AggregationPolicy policy, PropertyProvider... providers) {
        super(metaInfo);
        this.providers = Arrays.asList(Objects.requireNonNull(providers));
        union = PropertyProviders.union(policy, providers);
    }


    @Override
    public Optional<String> get(String key) {
        if (containsKey(key))
            return union.get(key);
        return Optional.empty();
    }

    private boolean filter(Map.Entry<String, String> entry) {
        return containsKey(entry.getKey());
    }

    @Override
    public boolean containsKey(String key) {
        for (PropertyProvider prov : this.providers) {
            if (!prov.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<String, String> toMap() {
        return union.toMap().entrySet().stream().filter(en -> containsKey(en.getKey())).collect(
                Collectors.toConcurrentMap(en -> en.getKey(), en -> en.getValue()));
    }

}
