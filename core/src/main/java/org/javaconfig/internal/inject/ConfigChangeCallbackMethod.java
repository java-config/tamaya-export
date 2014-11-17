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
package org.javaconfig.internal.inject;

import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Anatole on 03.10.2014.
 */
public final class ConfigChangeCallbackMethod {

    private Method callbackMethod;

    public ConfigChangeCallbackMethod(Method callbackMethod) {
        Objects.requireNonNull(callbackMethod);
        this.callbackMethod = Optional.of(callbackMethod).filter(
                (m) -> void.class.equals(m.getReturnType()) &&
                        m.getParameterCount() == 1 &&
                        m.getParameterTypes()[0].equals(PropertyChangeEvent.class)).get();
    }

    public void call(Object instance, PropertyChangeEvent configChangeEvent) {
        try {
            callbackMethod.setAccessible(true);
            callbackMethod.invoke(instance, configChangeEvent);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("Error calling ConfigChange callback method " + callbackMethod.getDeclaringClass().getName() + '.' + callbackMethod.getName() + " on " + instance, e);
        }
    }
}
