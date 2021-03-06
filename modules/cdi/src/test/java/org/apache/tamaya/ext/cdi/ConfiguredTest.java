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
package org.apache.tamaya.ext.cdi;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Test;

/**
 * Created by Anatole on 08.09.2014.
 */
public class ConfiguredTest{

    @Test
    public void testInjection(){
        Weld weld = new Weld();
        WeldContainer container = weld.initialize();
        ConfiguredClass item = container.instance().select(ConfiguredClass.class).get();
        System.out.println("********************************************");
        System.out.println(item);
        System.out.println("********************************************");
        weld.shutdown();
    }

}
