package org.apache.tamaya.samples.annotations;

import org.apache.tamaya.annot.ConfigChanged;
import org.apache.tamaya.annot.ConfiguredProperty;
import org.apache.tamaya.annot.DefaultValue;

import java.math.BigDecimal;

/**
 * Created by Anatole on 08.09.2014.
 */
public interface ConfigTemplate {

    @ConfiguredProperty
    String testProperty();

    @ConfiguredProperty("Foo")
    @DefaultValue("The current \\${JAVA_HOME} env property is ${env:JAVA_HOME}.")
    String value1();

    @ConfiguredProperty("COMPUTERNAME")
    String computerName();

    @ConfiguredProperty()
    String APPDATA();

    @ConfiguredProperty
    @DefaultValue("N/A")
    String runtimeVersion();

    @ConfiguredProperty
    @DefaultValue("${sys:java.version}")
    String javaVersion2();

    @ConfiguredProperty
    @DefaultValue("5")
    Integer int1();

    @ConfiguredProperty
    @DefaultValue("2233")
    int int2();

    @ConfiguredProperty
    boolean booleanT();

    @ConfiguredProperty("BD")
    BigDecimal bigNumber();

}
