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
package org.javaconfig.config;

import org.javaconfig.MetaInfo;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


class MapConfiguration extends AbstractConfiguration {

    private static final long serialVersionUID = 707575538680740130L;
    private Supplier<Map<String,String>> mapSupplier;

    public MapConfiguration(MetaInfo metaInfo, Supplier<Map<String,String>> mapSupplier) {
        super(metaInfo);
        Objects.requireNonNull(mapSupplier);
        this.mapSupplier = mapSupplier;
    }

    @Override
    public Map<String,String> toMap(){
        return mapSupplier.get();
    }

}
