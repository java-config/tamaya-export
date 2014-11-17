package org.javaconfig.internal;

import org.javaconfig.ConfigException;
import org.javaconfig.Configuration;
import org.javaconfig.EnvironmentManager;
import org.javaconfig.spi.Bootstrap;
import org.javaconfig.spi.ExpressionEvaluator;
import org.javaconfig.spi.ExpressionResolver;

import javax.el.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Anatole on 28.09.2014.
 */
final class DefaultExpressionEvaluator implements ExpressionEvaluator{

    private Map<String, ExpressionResolver> resolvers = new ConcurrentHashMap<>();

    private ExpressionResolver defaultResolver;

    public DefaultExpressionEvaluator() {
        for(ExpressionResolver resolver: Bootstrap.getServices(ExpressionResolver.class)){
            resolvers.put(resolver.getResolverId(), resolver);
        }
        defaultResolver = Bootstrap.getService(ExpressionResolver.class);
    }

    /**
     * Resolves an expression in the form of <code>${resolverId:expression}</code>. The expression can be
     * part of any type of literal text. Also multiple expression, with different resolver ids are supported.
     * All control characters (${}\) can be escaped.<br>
     * So all the following are valid expressions:
     * <ul>
     * <li><code>${resolverId:expression}</code></li>
     * <li><code>bla bla ${resolverId:expression}</code></li>
     * <li><code>${resolverId:expression} bla bla</code></li>
     * <li><code>bla bla ${resolverId:expression} bla bla</code></li>
     * <li><code>${resolverId:expression}${resolverId2:expression2}</code></li>
     * <li><code>foo ${resolverId:expression}${resolverId2:expression2}</code></li>
     * <li><code>foo ${resolverId:expression} bar ${resolverId2:expression2}</code></li>
     * <li><code>${resolverId:expression}foo${resolverId2:expression2}bar</code></li>
     * <li><code>foor${resolverId:expression}bar${resolverId2:expression2}more</code></li>
     * <li><code>\${resolverId:expression}foo${resolverId2:expression2}bar</code> (first expression is escaped).</li>
     * </ul>
     *
     * @param expression the expression to be evaluated, not null
     * @return the evaluated expression.
     * @throws org.javaconfig.ConfigException if resolution fails.
     */
    @Override
    public String evaluate(String expression) {
        StringTokenizer tokenizer = new StringTokenizer(expression, "${}\\", true);
        boolean escaped = false;
        StringBuilder resolvedValue = new StringBuilder();
        StringBuilder current = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (escaped) {
                switch (token) {
                    case "n":
                        current.append("\n");
                        break;
                    case "r":
                        current.append("\r");
                        break;
                    case "t":
                        current.append("\t");
                        break;
                    default:
                        current.append(token);
                        break;
                }
                escaped = false;
                continue;
            }
            switch (token) {
                case "\\":
                    escaped = true;
                    break;
                case "$":
                    if (current.length() > 0) {
                        resolvedValue.append(current);
                        current.setLength(0);
                    }
                    if (!"{".equals(tokenizer.nextToken())) {
                        throw new ConfigException("Invalid expression encountered: " + expression);
                    }
                    String subExpression = tokenizer.nextToken();
                    if (!"}".equals(tokenizer.nextToken())) {
                        throw new ConfigException("Invalid expression encountered: " + expression);
                    }
                    // evalute subexpression
                    current.append(evaluteInternal(subExpression));
                    break;
                default:
                    current.append(token);
            }
        }
        if (current.length() > 0) {
            resolvedValue.append(current);
        }
        return resolvedValue.toString();
    }

    private String evaluteInternal(String subExpression) {
        int sepPos = subExpression.indexOf(':');
        if (sepPos > 0) {
            String refID = subExpression.substring(0, sepPos);
            String expression = subExpression.substring(sepPos + 1);
            return Optional.ofNullable(this.resolvers.get(refID)).orElseThrow(
                    () -> new ConfigException("Resolver not found: " + refID + " in " + subExpression)
            ).resolve(expression);
        } else {
            return Optional.ofNullable(this.defaultResolver).orElseThrow(
                    () -> new ConfigException("No default Resolver set, but required by " + subExpression)
            ).resolve(subExpression);
        }
    }


}
