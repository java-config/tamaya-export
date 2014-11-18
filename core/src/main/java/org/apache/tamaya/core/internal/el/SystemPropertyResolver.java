package org.apache.tamaya.core.internal.el;

import org.apache.tamaya.ConfigException;
import org.apache.tamaya.Configuration;
import org.apache.tamaya.core.spi.ExpressionResolver;

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
