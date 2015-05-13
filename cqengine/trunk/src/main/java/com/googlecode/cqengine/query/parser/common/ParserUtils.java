package com.googlecode.cqengine.query.parser.common;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author niall.gallagher
 */
public class ParserUtils {

    /**
     * Examines the parent rule contexts of the given context, and returns the first parent context which is assignable
     * from (i.e. is a, or is a subclass of) one of the given context types.
     * @param currentContext The starting context whose parent contexts should be examined
     * @param parentContextTypes The types of parent context sought
     * @return The first parent context which is assignable from one of the given context types,
     * or null if there is no such parent in the tree
     */
    public static ParserRuleContext getParentContextOfType(ParserRuleContext currentContext, Class<?>... parentContextTypes) {
        while (currentContext != null) {
            currentContext = currentContext.getParent();
            if (currentContext != null) {
                for (Class<?> parentContextType : parentContextTypes) {
                    if (parentContextType.isAssignableFrom(currentContext.getClass())) {
                        return currentContext;
                    }
                }
            }
        }
        return null;
    }

    public static void validateObjectTypeParameter(Class<?> expectedType, String actualType) {
        if (!expectedType.getSimpleName().equals(actualType)) {
            throw new IllegalStateException("Unexpected object type parameter, expected: " + expectedType.getSimpleName() + ", found: " + actualType);
        }
    }

    public static void validateExpectedNumberOfChildQueries(int expected, int actual) {
        if (actual != expected) {
            throw new IllegalStateException("Unexpected number of child queries, expected: " + expected + ", actual: " + actual);
        }
    }

    public static void validateAllQueriesParsed(int numQueriesEncountered, int numQueriesParsed) {
        if (numQueriesEncountered != numQueriesParsed) {
            throw new IllegalStateException("A query declared in the antlr grammar, was not parsed by the listener. If a new query is added in the grammar, a corresponding handler must also be added in the listener.");
        }
    }

}
