package org.javaconfig.spi;

/**
 * This interface defines a small plugin for resolving of expressions within configuration.
 * Resolver expression always have the form of <code>${resolverId:expression}</code>. The
 * {@code resolverId} hereby references the resolver to be used to replace the according
 * {@code expression}. Also it is well possible to mix different resolvers, e.g. using
 * an expression like <code>${ref1:expression1} bla bla ${ref2:expression2}</code>.
 * Finally when no resolver id is passed, the default resolver should be used.
 */
public interface ExpressionResolver {

    /**
     * Get a (unique) resolver id used as a prefix for qualifying the resolver to be used for
     * resolving an expression.
     *
     * @return the (unique) resolver id, never null, not empty.
     */
    String getResolverId();

    /**
     * Resolve the expression. The expression should be stripped from any surrounding parts.
     * E.g. <code>${myresolver:blabla to be interpreted AND executed.}</code> should be passed
     * as {@code blabla to be interpreted AND executed.} only.
     *
     * @param expression the stripped expression.
     * @return the resolved expression.
     * @throws org.javaconfig.ConfigException when the expression passed is not resolvable, e.g. due to syntax issues
     *                                        or data not present or valid.
     */
    String resolve(String expression);
}
