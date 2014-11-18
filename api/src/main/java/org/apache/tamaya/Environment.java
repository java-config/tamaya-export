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
package org.apache.tamaya;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Models a runtime environment. Instances of this class are used to
 * evaluate the correct configuration artifacts.<br/>
 * <h3>Implementation Requirements</h3>
 * <p>
 * Implementations of this interface must be
 * <ul>
 * <li>Thread safe.
 * <li>Immutable
 * <li>serializable
 * </ul>
 * 
 * @author Anatole Tresch
 */
public interface Environment extends StageSupplier, Iterable<Environment>{

    /**
     * Get a unique type (within this VM) for this environment.
     * Types represent the environment level within the hierarchy
     * of possible environments, e.g. {@code system, ear, webapp, tenant}.
     */
    String getEnvironmentType();

    /**
     * Get a unique name (in combination with the environment type within this VM)
     * for this environment instance.
     * Where a human readable name is available this would be preferable
     * over a technical key/UUID.
     * @return a unique id for this environment, when comined with the environment type.
     */
    String getEnvironmentId();

    /**
     * Access a property.
     * @param key the property's key, not null.
     * @return the property's value.
     */
    Optional<String> get(String key);

    /**
     * Checks if a property is defined.
     * @param key the property's key, not null.
     * @return true, if the property is existing.
     */
    boolean containsKey(String key);

    /**
     * Access a property.
     * @param key the property's key, not null.
     * @return the property's value.
     */
    default String getOrDefault(String key, String defaultValue){
        return get(key).orElse(defaultValue);
    }

    /**
     * Access the set of property keys, defined by this provider.
     * @return the key set, never null.
     */
    Set<String> keySet();

    /**
     * Get an qualified path to this environment instance, by appending the
     * current environment id and type (in backets) with the ones of its parent and so on, e.g.
     * <code>root[system].HumanOne[ear].rest[webapp].atsticks[user]</code>
     * @return the qualified path of this environment instance
     */
    String getEnvironmentPath();

    /**
     * Get the parent context.
     * @return the parent context, or null.
     */
    Environment getParentEnvironment();

    /**
     * Get the current {@link Environment}. The environment is used to determine the current runtime state, which
     * is important for returning the correct configuration.
     * @return the current Environment, never null.
     */
    public static Environment of(){
        return EnvironmentManager.getEnvironment();
    }

    /**
     * Get the current root (startup/machine/VM) {@link Environment}.
     * @return the current root Environment, never null.
     */
    public static Environment getRootEnvironment(){
        return EnvironmentManager.getRootEnvironment();
    }

    /**
     * Evaluate the overall chain of possible environments.
     * @return the hierarchy chain of possible Environments.
     */
    public static List<String> getEnvironmentTypeOrder(){
        return EnvironmentManager.getEnvironmentTypeOrder();
    }

    /**
     * Evaluate the current type chain of environments.
     * @return the current type chain of Environments.
     */
    public static List<String> getEnvironmentHierarchy(){
        return EnvironmentManager.getEnvironmentHierarchy();
    }

    /**
     * Get the current environment of the given environment type.
     * @param environmentType the target type.
     * @return the corresponding environment
     */
    public static Optional<Environment> getEnvironment(String environmentType){
        return EnvironmentManager.getEnvironment(environmentType);
    }

    /**
     * Allows to check, if the czurrent environment type is one of the current active environment types.
     * @param environmentType the environment type to be queried.
     * @return true, if the czurrent environment type is one of the current active environment types.
     */
    public static boolean isEnvironmentActive(String environmentType){
        return EnvironmentManager.isEnvironmentActive(environmentType);
    }


}
