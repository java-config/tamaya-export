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
package org.javaconfig.env;


import org.javaconfig.Environment;

/**
 * The selector is responsible for determining if a configuration should be
 * included into the current configuration aggregate for a given runtime
 * {@link org.javaconfig.Environment}.
 * 
 * @author Anatole Tresch
 */
@FunctionalInterface
public interface EnvironmentSelector {

	/**
	 * Selector INSTANCE that selects every environment.
	 */
	public static final EnvironmentSelector ANY = configurationContext -> true;

	/**
	 * Method that evaluates if a concrete environment is matching the
	 * constraints of this selector.
	 * 
	 * @param configurationContext
	 *            The environment, not {@code null}.
	 * @return {@code true} if the environment is selected.
	 */
	public boolean isMatching(Environment configurationContext);

}
