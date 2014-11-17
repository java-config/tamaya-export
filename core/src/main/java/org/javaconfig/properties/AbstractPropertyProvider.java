/*
 * Copyright (c) 2013, Anatole Tresch. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.javaconfig.properties;

import org.javaconfig.MetaInfo;
import org.javaconfig.PropertyProvider;

import java.io.Serializable;
import java.util.*;

/**
 * Abstract base class for implementing a {@link org.javaconfig.PropertyProvider}. Also
 * consider using {@link MapBasedPropertyProvider} instead of.
 *
 * @author Anatole Tresch
 */
@SuppressWarnings("NullableProblems")
public abstract class AbstractPropertyProvider implements PropertyProvider, Serializable{
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -6553955893879292837L;

    protected MetaInfo metaInfo;

    /**
     * The underlying sources.
     */
    private volatile Set<String> sources = new HashSet<>();

    /**
     * Constructor.
     */
    protected AbstractPropertyProvider(MetaInfo metaInfo){
        Objects.requireNonNull(metaInfo);
        this.metaInfo = metaInfo;
    }

    @Override
    public MetaInfo getMetaInfo(){
        return metaInfo;
    }


    /**
     * Method that allows an additional source to be added, to be used by
     * subclasses.
     *
     * @param source the source, not {@code null}.
     */
    protected void addSource(String source){
        Objects.requireNonNull(source);
        this.sources.add(source);
    }


    protected void addSources(Collection<String> sources){
        Objects.requireNonNull(sources);
        this.sources.addAll(sources);
    }

    @Override
    public boolean containsKey(String key){
        return toMap().containsKey(key);
    }

    @Override
    public Optional<String> get(String key){
        return Optional.ofNullable(toMap().get(key));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Set<String> keySet(){
        return toMap().keySet();
    }

    @Override
    public String toString(){
        StringBuilder b = new StringBuilder(getClass().getSimpleName()).append("{\n");
        printContents(b);
        return b.append('}').toString();
    }

    protected String printContents(StringBuilder b){
        Map<String,String> sortMap = toMap();
        if(!(sortMap instanceof SortedMap)){
            sortMap = new TreeMap<>(sortMap);
        }
        for(Map.Entry<String,String> en : sortMap.entrySet()){
            b.append("  ").append(en.getKey()).append(" = \"").append(en.getValue().replaceAll("\\\"", "\\\\\"").replaceAll("=", "\\=")).append("\"\n");
        }
        return b.toString();
    }

}
