/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tamaya.core.internal.format;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tamaya.core.spi.ConfigurationFormat;


import org.apache.tamaya.ConfigException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class IniFormat implements ConfigurationFormat{

    private static final Logger LOG = LogManager.getLogger(IniFormat.class);


    @Override
    public String getFormatName(){
        return "ini";
    }

    @Override
    public boolean isAccepted(URI resource){
        String path = resource.getPath();
        return path != null && path.endsWith(".ini");
    }

    @Override
    public Map<String,String> readConfiguration(URI resource){
        Map<String,String> result = new HashMap<>();
        if(isAccepted(resource)){
            try(InputStream is = resource.toURL().openStream()){
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                int lineNum = 0;
                String section = null;
                while(line != null){
                    lineNum++;
                    line = line.trim();
                    if(line.isEmpty()){
                        continue;
                    }
                    if(line.startsWith("[")){
                        int end = line.indexOf(']');
                        if(end < 0){
                            throw new ConfigException(
                                    "Invalid INI-Format, ']' expected, at " + lineNum + " in " + resource);
                        }
                        section = line.substring(1, end);
                    }
                    else{
                        int sep = line.indexOf('=');
                        String key = line.substring(0,sep);
                        String value = line.substring(sep+1);
                        if(section!=null){
                            result.put(section + '.' + key, value);
                        }
                        else{
                            result.put(key, value);
                        }
                    }
                    line = reader.readLine();
                }
            }
            catch(Exception e){
                LOG.error("Error reading configuration: " + resource, e);
            }
        }
        return result;
    }

}
