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

package org.javaconfig.internal.resources;

import javax.inject.Singleton;
import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

@Singleton
public class AntPathFilesResolver implements PathResolver{

    @Override
    public String getResolverId(){
        return "file*";
    }

    @Override
    public Collection<URI> resolve(ClassLoader classLoader, Stream<String> expressions){
        List<URI> result = new ArrayList<>();
        expressions.forEach((expression) -> {
            if(expression.startsWith("file*:")){
                String exp = expression.substring("file:".length());
                File f = new File(exp);
                if(f.exists() && f.isFile()){
                    result.add(f.toURI());
                }
            }
        });
        return result;
    }
}
