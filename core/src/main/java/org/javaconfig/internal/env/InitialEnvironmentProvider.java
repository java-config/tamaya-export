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
package org.javaconfig.internal.env;

import org.javaconfig.env.ConfiguredSystemProperties;
import org.javaconfig.env.EnvironmentBuilder;

import java.net.InetAddress;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TimeZone;

import org.javaconfig.Environment;
import org.javaconfig.Stage;
import org.javaconfig.spi.EnvironmentProvider;

/**
 * Default {@link org.javaconfig.Environment}.
 * 
 * @author Anatole Tresch.
 */
public final class InitialEnvironmentProvider implements EnvironmentProvider{

	public static final String STAGE_PROP = "org.javaconfig.stage";
    public static final Stage DEFAULT_STAGE = Stage.development();

    private Environment rootEnvironment;

	public InitialEnvironmentProvider() {
        EnvironmentBuilder builder = EnvironmentBuilder.of(getEnvironmentType(), getEnvironmentType());
        Properties props = System.getProperties();
        if(props instanceof ConfiguredSystemProperties){
            props = ((ConfiguredSystemProperties)props).getInitialProperties();
        }
        String stageValue =  props.getProperty(STAGE_PROP);
        Stage stage = DEFAULT_STAGE;
        if (stageValue != null) {
            stage = Stage.of(stageValue);
        }
        builder.setStage(stage);
        // Copy system properties....
        // TODO filter properties
        for (Entry<Object, Object> en : props.entrySet()) {
            builder.set(en.getKey().toString(), en.getValue().toString());
        }
        builder.set("timezone", TimeZone.getDefault().getID());
        builder.set("locale", Locale.getDefault().toString());
        try {
            builder.set("host", InetAddress.getLocalHost().toString());
        } catch (Exception e) {
// log warning
        }
        // Copy env properties....
        for (Entry<String, String> en : System.getenv().entrySet()) {
            builder.set(en.getKey(), en.getValue());
        }
        this.rootEnvironment = builder.build();
	}

    @Override
	public Environment getEnvironment(Environment env) {
        return rootEnvironment;
	}

    @Override
    public String getEnvironmentType() {
        return "root";
    }

    @Override
    public boolean isEnvironmentActive() {
        return true;
    }

}
