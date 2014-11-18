/*
 * Copyright (c) 2014.
 * Anatole Tresch IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE.
 *
 * Specification: Java Configuration ("Specification")
 *
 * Copyright (c) 2012-2014, Anatole Tresch All rights reserved.
 */

package org.apache.tamaya.core.internal.resources;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

@Singleton
public class AntPathClasspathsResolver implements PathResolver{

    @Override
    public String getResolverId(){
        return "classpath*";
    }

    @Override
    public Collection<URI> resolve(ClassLoader classLoader, Stream<String> expressions){
        List<URI> result = new ArrayList<>();
        Objects.requireNonNull(classLoader);
        expressions.forEach((expression) -> {
            if(expression.startsWith("classpath*:")){
                String exp = expression.substring("classpath*:".length());
                Enumeration<URL> urls;
                try{
                    urls = classLoader.getResources(exp);
                    while(urls.hasMoreElements()){
                        URL url = (URL) urls.nextElement();
                        try{
                            result.add(url.toURI());
                        }
                        catch(URISyntaxException e){
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                catch(IOException e1){
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        return result;
    }
}