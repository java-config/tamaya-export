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
package org.javaconfig;

import org.javaconfig.annot.WithPropertyAdapter;
import org.javaconfig.spi.Bootstrap;
import org.javaconfig.spi.PropertyAdaptersSingletonSpi;
import java.util.Optional;

/**
 * Singleton accessor that provides {@link PropertyAdapter} instance, usable for converting String
 * based configuration entries into any other target types.
 */
public final class PropertyAdapters{
    /** The SPI finally registered backing this singleton. */
    private static final PropertyAdaptersSingletonSpi propertyAdaptersSingletonSpi = loadConfigAdapterProviderSpi();

    /**
     * Method that loads the singleton backing bean from the {@link org.javaconfig.spi.Bootstrap} component.
     * @return the PropertyAdaptersSingletonSpi, never null.
     */
    private static PropertyAdaptersSingletonSpi loadConfigAdapterProviderSpi(){
        return Bootstrap.getService(PropertyAdaptersSingletonSpi.class);
    }

    /**
     * Orivate singleton constructor.
     */
    private PropertyAdapters(){}

    /**
     * Registers a new PropertyAdapter for the given target type, hereby replacing any existing adapter for
     * this type.
     * @param targetType The target class, not null.
     * @param adapter The adapter, not null.
     * @param <T> The target type
     * @return any adapter replaced with the new adapter, or null.
     */
    public static <T> PropertyAdapter<T> register(Class<T> targetType, PropertyAdapter<T> adapter){
        return Optional.of(propertyAdaptersSingletonSpi).get().register(targetType, adapter);
    }

    /**
     * Get an adapter converting to the given target type.
     * @param targetType the target type class
     * @return true, if the given target type is supported.
     */
    public static boolean isTargetTypeSupported(Class<?> targetType){
        return Optional.of(propertyAdaptersSingletonSpi).get().isTargetTypeSupported(targetType);
    }

    /**
     * Get an adapter converting to the given target type.
     * @param targetType the target type class
     * @param <T> the target type
     * @return the corresponding adapter, never null.
     * @throws ConfigException if the target type is not supported.
     */
    public static  <T> PropertyAdapter<T> getAdapter(Class<T> targetType){
        return getAdapter(targetType, null);
    }

    /**
     * Get an adapter converting to the given target type.
     * @param targetType the target type class
     * @param annotation the {@link org.javaconfig.annot.WithPropertyAdapter} annotation, or null. If the annotation is not null and
     *                   defines an overriding adapter, this instance is created and returned.
     * @param <T> the target type
     * @return the corresponding adapter, never null.
     * @throws ConfigException if the target type is not supported, or the overriding adapter cannot be
     * instantiated.
     */
    public static  <T> PropertyAdapter<T> getAdapter(Class<T> targetType, WithPropertyAdapter annotation){
        return Optional.of(propertyAdaptersSingletonSpi).get().getAdapter(targetType, annotation);
    }

}
