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
package org.javaconfig.spi;


import org.javaconfig.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for accessing {@link org.javaconfig.Environment}. Environments are used to
 * access/determine configurations.<br/>
 * <h3>Implementation PropertyMapSpec</h3> This class is
 * <ul>
 * <li>thread safe,
 * <li>and behaves contextual.
 * </ul>
 * 
 * @author Anatole Tresch
 */
public interface EnvironmentManagerSingletonSpi{

    /**
     * Get the current environment.
     *
     * @return the current environment, never null.
     */
    Environment getEnvironment();

    /**
     * Get the initial root environment, that typically contains any startup and initial parameters of an VM instance,
     * machine.
     *
     * @return the initial environment, never null.
     */
    Environment getRootEnvironment();

    /**
     * Access the chain of environment types that may produce an environment. Hereby it is possible
     * that chain elements can be ommitted in the final environment hierarchy, since the regarding
     * environment level is not defined or accessible.
     * @return the ordered list of environment type ids.
     */
    List<String> getEnvironmentTypeOrder();

    /**
     * Evaluate the current type chain of environments.
     * @return the current type chain of Environments.
     */
    default List<String> getEnvironmentHierarchy(){
        List<String> result = new ArrayList<>();
        for(Environment env:getEnvironment()){
            result.add(env.getEnvironmentId()+'('+env.getEnvironmentType()+')');
        }
        return result;
    }

    /**
     * Get the current environment of the given environment type.
     * @param environmentType the target type.
     * @return the corresponding environment
     * @throws java.lang.IllegalArgumentException if not such type is present or active.
     */
    default Optional<Environment> getEnvironment(String environmentType){
        for(Environment env:getEnvironment()){
            if(env.getEnvironmentType().equals(environmentType)){
                return Optional.of(env);
            }
        }
        return Optional.empty();
    }

    /**
     * Allows to check, if the czurrent environment type is one of the current active environment types.
     * @param environmentType the environment type to be queried.
     * @return true, if the czurrent environment type is one of the current active environment types.
     */
    default boolean isEnvironmentActive(String environmentType){
        return getEnvironmentHierarchy().contains(environmentType);
    }
}
