package org.javaconfig.properties;

import org.javaconfig.MetaInfo;
import org.javaconfig.MetaInfoBuilder;
import org.javaconfig.config.ConfigurationFormats;
import org.javaconfig.spi.Bootstrap;
import org.javaconfig.spi.ConfigurationFormat;
import org.javaconfig.spi.ResourceLoader;

import java.net.URI;
import java.util.*;

/**
 * Created by Anatole on 16.10.2014.
 */
final class URIBasedPropertyProvider extends AbstractPropertyProvider {

    private List<URI> uris = new ArrayList<>();
    private Map<String,String> properties = new HashMap<>();

    public URIBasedPropertyProvider(MetaInfo metaInfo, Collection<URI> uris) {
        super(metaInfo);
        Objects.requireNonNull(uris);
        this.uris.addAll(uris);
        init();
    }

    private void init(){
        List<String> sources = new ArrayList<>();
        for(URI uri : uris){
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
        MetaInfoBuilder metaInfoBuilder = MetaInfoBuilder.of(getMetaInfo());
        super.metaInfo = metaInfoBuilder
                .setSources(sources.toString()).build();
    }

    @Override
    public Map<String, String> toMap() {
        return properties;
    }
}
