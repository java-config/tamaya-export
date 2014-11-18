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
package org.apache.tamaya.core.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.tamaya.core.properties.ClasspathModulePropertyProvider;
import org.junit.Test;

public class ClasspathModulePropertyMapTest{

	@Test
	public void testGetMetaInfoString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMetaInfo() {
		fail("Not yet implemented");
	}

	@Test
	public void testClasspathModuleConfigUnit() {
		ClasspathModulePropertyProvider u = new ClasspathModulePropertyProvider(
				ClasspathModulePropertyProvider.class.getClassLoader(),
				"cfg/test.xml");

		System.out.println(u);
		assertEquals("testValue", u.get("testFromXml"));
	}

	@Test
	public void testGetClassLoader() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProperty() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProperties() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsActive() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
