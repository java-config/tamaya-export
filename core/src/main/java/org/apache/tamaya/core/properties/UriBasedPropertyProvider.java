package org.apache.tamaya.core.properties;

import org.apache.tamaya.MetaInfo;
import org.apache.tamaya.MetaInfoBuilder;
import org.apache.tamaya.core.config.ConfigurationFormats;
import org.apache.tamaya.spi.Bootstrap;
import org.apache.tamaya.core.spi.ConfigurationFormat;

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
