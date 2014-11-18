package org.apache.tamaya.core.internal.el;

import org.apache.tamaya.ConfigException;
import org.apache.tamaya.core.spi.ExpressionResolver;

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
