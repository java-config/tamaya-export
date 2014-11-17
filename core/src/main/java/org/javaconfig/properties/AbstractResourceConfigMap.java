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

import org.javaconfig.spi.Bootstrap;
import org.javaconfig.spi.ResourceLoader;

import org.javaconfig.MetaInfo;
import org.javaconfig.MetaInfoBuilder;
import javax.enterprise.inject.spi.CDI;
import java.net.URI;
import java.util.*;

public abstract class AbstractResourceConfigMap extends AbstractPropertyProvider{

    private static final long serialVersionUID = 5484306410557548246L;
    private ClassLoader classLoader;
    private AbstractResourceConfigMap parentConfig;
    private Set<String> sources;


    public AbstractResourceConfigMap(ClassLoader classLoader, AbstractResourceConfigMap parentConfig,
                                     Set<String> sourceExpressions, long configReadDT, Map<String,String> entries,
                                     MetaInfo metaInfo, Set<String> sources, List<Throwable> errors){
        super(metaInfo);
        Objects.requireNonNull(sources, "sources required.");
        Objects.requireNonNull(classLoader, "classLoader required.");
        this.sources = sources;
        this.classLoader = classLoader;
        this.parentConfig = parentConfig;
    }

    public AbstractResourceConfigMap(ClassLoader classLoader, AbstractResourceConfigMap parentConfig,
                                     String sourceExpression){
        super(MetaInfoBuilder.of().setSourceExpressions(sourceExpression).set("parentConfig", parentConfig.toString())
                      .build());
        Objects.requireNonNull(sources, "sources required.");
        Objects.requireNonNull(sourceExpression, "sourceExpression required.");
        List<URI> urls = Bootstrap.getService(ResourceLoader.class).getResources(classLoader, sourceExpression);
        for(URI url : urls){
            addSource(url.toString());
        }
        this.classLoader = classLoader;
        this.parentConfig = parentConfig;
    }

    protected abstract void readSource(Map<String,String> targetMap, String source);

    @Override
    public Map<String,String> toMap(){
        Map<String,String> result = new HashMap<>();
        for(String source : sources){
            //            if(!isSourceRead(source)){
            readSource(result, source);
            //            }
        }
        return result;
    }


    public ClassLoader getClassLoader(){
        return classLoader;
    }

    public AbstractResourceConfigMap getParentConfig(){
        return this.parentConfig;
    }


}
