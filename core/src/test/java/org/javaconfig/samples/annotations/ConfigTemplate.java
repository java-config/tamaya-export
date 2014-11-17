package org.javaconfig.samples.annotations;

import org.javaconfig.annot.ConfigChanged;
import org.javaconfig.annot.ConfiguredProperty;
import org.javaconfig.annot.DefaultValue;

import javax.inject.Singleton;
import java.beans.PropertyChangeEvent;
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
