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

/**
 * SPI for components that define a concrete type of {@link org.javaconfig.Environment}.
 * The chain of environment providers determine the current {@link Environment} active
 * and its parent instances.
 * Created by Anatole on 14.10.2014.
 */
public interface EnvironmentProvider {

    String getEnvironmentType();
    boolean isEnvironmentActive();
    Environment getEnvironment(Environment parentEnvironment);

}
