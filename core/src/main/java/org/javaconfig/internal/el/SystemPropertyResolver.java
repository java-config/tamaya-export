package org.javaconfig.internal.el;

import org.javaconfig.ConfigException;
import org.javaconfig.Configuration;
import org.javaconfig.ConfigurationManager;
import org.javaconfig.EnvironmentManager;
import org.javaconfig.spi.ExpressionResolver;

import javax.el.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Anatole on 28.09.2014.
 */
public final class SystemPropertyResolver implements ExpressionResolver{

    @Override
    public String getResolverId() {
        return "sys";
    }

    @Override
    public String resolve(String expression){
        return Optional.ofNullable(System.getProperty(expression)).orElseThrow(
                () -> new ConfigException("No such system property: " + expression)
        );
    }

}
