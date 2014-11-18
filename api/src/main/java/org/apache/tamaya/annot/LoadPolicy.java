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
package org.apache.tamaya.annot;

/**
 * Available policies that describe how changes affecting configured values are published/reinjected.
 * Created by Anatole on 10.10.2014.
 */
public enum LoadPolicy {
    /**
     * The configuration value is evaluated once, when the owning component is loaded/configured, but never updated later.
     */
    INITIAL,
    /**
     * The configuration value is evaluated exactly once on its first use lazily, but never updated later.
     * This feature is not applicable on field injection, but only on configuration template methods.
     */
    LAZY,
    /**
     * The configuration value is evaluated once, when the owning component is loaded/configured.
     * Later changes on this configuration entry will be reinjected/updated and additionally triggered
     * as {@link java.beans.PropertyChangeEvent}.
     */
    MANAGED,
    /**
     * The configuration value is evaluated once, when the owning component is loaded/configured.
     * Later changes on this configuration entry will be reinjected/updated, but no {@link java.beans.PropertyChangeEvent}
     * will be triggered.
     */
    SILENT
}
