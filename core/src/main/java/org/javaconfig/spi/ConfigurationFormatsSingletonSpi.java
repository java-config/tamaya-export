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

import java.net.URI;
import java.util.Collection;

/**
 * Created by Anatole on 17.09.2014.
 */
public interface ConfigurationFormatsSingletonSpi{
    /**
     * Access a {@link org.javaconfig.spi.ConfigurationFormat}.
     *
     * @param formatName the format name
     * @return the corresponding {@link org.javaconfig.spi.ConfigurationFormat}, or {@code null}, if
     * not available for the given environment.
     */
    ConfigurationFormat getFormat(String formatName);

    /**
     * Get a collection of the keys of the registered {@link ConfigurationFormat} instances.
     *
     * @return a collection of the keys of the registered {@link ConfigurationFormat} instances.
     */
    Collection<String> getFormatNames();

    /**
     * Evaluate the matching format for a given resource.
     *
     * @param resource The resource
     * @return a matching configuration format, or {@code null} if no matching format could be determined.
     */
    ConfigurationFormat getFormat(URI resource);

    default ConfigurationFormat getPropertiesFormat(){
        return getFormat("properties");
    }

    default ConfigurationFormat getXmlPropertiesFormat(){
        return getFormat("xml-properties");
    }
}
