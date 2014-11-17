package org.javaconfig.internal.resources;

import javax.inject.Singleton;
import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

@Singleton
public class AntPathFileResolver implements PathResolver{

    @Override
    public String getResolverId(){
        return "file";
    }

    @Override
    public Collection<URI> resolve(ClassLoader classLoader, Stream<String> expressions){
        List<URI> result = new ArrayList<>();
        expressions.forEach((expression) -> {
            if(expression.startsWith("file:")){
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
