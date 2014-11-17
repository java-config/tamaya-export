package org.javaconfig.spi;

/**
 * Created by Anatole on 12.10.2014.
 */
@FunctionalInterface
public interface ExpressionEvaluator {
    String evaluate(String expression);
}
