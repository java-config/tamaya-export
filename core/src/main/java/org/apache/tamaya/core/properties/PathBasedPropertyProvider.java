package org.apache.tamaya.core.properties;

import org.apache.tamaya.MetaInfo;
import org.apache.tamaya.MetaInfoBuilder;
import org.apache.tamaya.PropertyProvider;
import org.apache.tamaya.core.config.ConfigurationFormats;
import org.apache.tamaya.spi.Bootstrap;
import org.apache.tamaya.core.spi.ConfigurationFormat;
import org.apache.tamaya.core.spi.ResourceLoader;

import java.net.URI;
import java.util.*;

/**
 * Created by Anatole on 16.10.2014.
 */
final class PathBasedPropertyProvider extends AbstractPropertyProvider {

    private List<String> paths = new ArrayList<>();
    private Map<String,String> properties = new HashMap<>();

    public PathBasedPropertyProvider(MetaInfo metaInfo, Collection<String> paths) {
        super(metaInfo);
        Objects.requireNonNull(paths);
        this.paths.addAll(paths);
        init();
    }

    @Override
    public Map<String, String> toMap() {
        return this.properties;
    }

    private void init() {
        List<String> sources = new ArrayList<>();
        List<String> effectivePaths = new ArrayList<>();
        paths.forEach((path) -> {
            effectivePaths.add(path);
            for(URI uri : Bootstrap.getService(ResourceLoader.class).getResources(path)){
                ConfigurationFormat format = ConfigurationFormats.getFormat(uri);
                if(format != null){
                    try{
                        properties.putAll(format.readConfiguration(uri));
                        sources.add(uri.toString());
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        MetaInfoBuilder metaInfoBuilder = MetaInfoBuilder.of(getMetaInfo());
        super.metaInfo = metaInfoBuilder.setSourceExpressions(new String[effectivePaths.size()])
                .set("sources", sources.toString()).build();
    }
}