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
package org.apache.tamaya.ext.cdi;

import org.apache.tamaya.core.internal.inject.ConfiguredInstancesManager;
import org.apache.tamaya.core.internal.inject.ConfiguredType;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Vetoed;
import javax.enterprise.inject.spi.*;
import java.util.*;

/**
 * Created by Anatole on 08.09.2014.
 */
@Vetoed
public final class ConfigurationInjector implements Extension {

    public <T> void initializeConfiguredFields(final @Observes ProcessInjectionTarget<T> pit) {
        final AnnotatedType<T> at = pit.getAnnotatedType();
        if (!ConfiguredType.isConfigured(at.getJavaClass())) {
            return;
        }
        final ConfiguredType configuredType = new ConfiguredType(at.getJavaClass());

        final InjectionTarget<T> it = pit.getInjectionTarget();
        InjectionTarget<T> wrapped = new InjectionTarget<T>() {
            @Override
            public void inject(T instance, CreationalContext<T> ctx) {
                it.inject(instance, ctx);
                configuredType.configure(instance);
                ConfiguredInstancesManager.register(configuredType, instance);
            }

            @Override
            public void postConstruct(T instance) {
                it.postConstruct(instance);
            }

            @Override
            public void preDestroy(T instance) {
                it.dispose(instance);
            }

            @Override
            public void dispose(T instance) {
                it.dispose(instance);
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return it.getInjectionPoints();
            }

            @Override
            public T produce(CreationalContext<T> ctx) {
                return it.produce(ctx);
            }
        };
        pit.setInjectionTarget(wrapped);
    }


}
