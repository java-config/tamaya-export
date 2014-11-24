/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tamaya;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.tamaya.core.internal.DefaultConfigurationManagerSingletonSpi;
import org.junit.Test;

/**
 * Test class for {@link org.apache.tamaya.core.internal.DefaultConfigurationManagerSingletonSpi}.
 */
public class DefaultConfigurationManagerSingletonSpiSingletonSpiTest{



	@Test
	public void testSEConfigurationService() {
		new DefaultConfigurationManagerSingletonSpi();
	}

	@Test
	public void testGetConfigurationString() {
		Configuration config = Configuration.of("default");
		assertNotNull(config);
        assertTrue(config.toString().contains("default"));
        assertNotNull(config.getMetaInfo());
        assertTrue(config.getMetaInfo().toString().contains("default"));
        System.out.println("CONFIG: " + config);
		assertEquals(System.getProperty("java.version"),
				config.get("java.version"));
		
		config = Configuration.of("system.properties");
		assertNotNull(config);
        assertNotNull(config.getMetaInfo());
        assertTrue(config.getMetaInfo().toString().contains("system.properties"));
		assertEquals(System.getProperty("java.version"),
				config.get("java.version"));
	}

	@Test
	public void testGetConfigurationStringEnvironment() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsConfigurationDefined() {
		assertTrue(Configuration.isDefined("test"));
		assertFalse(Configuration.isDefined("sdksajdsajdlkasj dlkjaslkd"));
	}

	@Test
	public void testGetCurrentEnvironment() {
		Environment env = Environment.of();
		assertNotNull(env);
		assertEquals(System.getProperty("java.version"),
				env.get("java.version").get());
	}

	@Test
	public void testGetRootEnvironment() {
		DefaultConfigurationManagerSingletonSpi s = new DefaultConfigurationManagerSingletonSpi();
		Environment env =  Environment.getRootEnvironment();
		assertNotNull(env);
		assertEquals(System.getProperty("java.version"),
				env.get("java.version").get());
	}

	@Test
	public void testQueryConfiguration() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateConfiguration() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateConfiguration() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddConfigChangeListener() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveConfigChangeListener() {
		fail("Not yet implemented");
	}

	@Test
	public void testConfigure() {
		fail("Not yet implemented");
	}

}
