package org.javaconfig.internal.el;

import org.javaconfig.ConfigException;
import org.javaconfig.spi.ExpressionResolver;

import java.util.Optional;

/**
 * Created by Anatole on 28.09.2014.
 */
public final class EnvironmentPropertyResolver implements ExpressionResolver{

    @Override
    public String getResolverId() {
        return "env";
    }

    @Override
    public String resolve(String expression){
        return Optional.ofNullable(System.getenv(expression)).orElseThrow(
                () -> new ConfigException("No such environment property: " + expression)
        );
    }

}
