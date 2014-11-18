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


/**
 * Interface for an filter/operator that converts a configured String into another String. One typical
 * use case would the the decryption of an encrypted configuration value.
 */
@FunctionalInterface
public interface ConfigOperator{

    /**
     * Method that creates a Configuration from another Configuration. This can be used for implementing
     * views, security constraints or for overriding/inheriting of configuration.
     * @param config The target configuration to be operated, never nnull.
     * @return the operated configuration, never null.
     */
    Configuration operate(Configuration config);

}
