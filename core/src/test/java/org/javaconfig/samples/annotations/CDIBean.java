///*
// * Copyright 2014 Anatole Tresch and other (see authors).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *    http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.javaconfig.samples.annotations;
//
//import java.net.URL;
//
//import org.javaconfig.Configured;
//import org.javaconfig.spi.ConfigurationManagerSingletonSpi;
//import org.javaconfig.PropertyAdapter;
//import javax.inject.Inject;
//import javax.inject.Singleton;
//import javax.sql.DataSource;
//
///*
// * <pre>
// *
// * ConfigurationScope(location):
// *
// * SERVICE APP EAR DOMAIN SERVER TIER CLUSTER NETWORKZONE ORGANIZATIONUNIT
// * ENTERPRISE GLOBAL
// *
// * Selectors:
// *
// * stage=PROD,PTA,TEST,DEV server=abc.myinc.com tier=PT zone=DMZ
// * application=PortfolioManager
// *
// * Types:
// *
// * CODE DEPLOYMENT ANY
// *
// * Aggregates:
// *
// * APP, DOMAIN, CLUSTER, VM </pre>
// */
//@Singleton
//// @ConfigFilter(MyFilter.class)
//public class CDIBean {
//
//	@Inject
//	private ConfigurationManagerSingletonSpi configurationManager;
//
//	@Configured
//	private String gValue;
//
//	@Configured("testValue")
//	private String value;
//
//	@Configured("number")
//	// @ConfigFilter(MyFilter.class)
//	private int myNum;
//
//	@Configured("XML stream")
//	private URL myXmlConfig;
//
//	@Configured(value={"mySimpleDS"}, adapter=DataSourceAdapter.class)
//	private DataSource myTestDS;
//
//	/**
//	 * Example adapter (not implemented).
//	 *
//	 * @author Anatole Tresch.
//	 */
//	private static class DataSourceAdapter implements
//			PropertyAdapter<DataSource> {
//
//		@Override
//		public DataSource adapt(String property) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//	}
//}
