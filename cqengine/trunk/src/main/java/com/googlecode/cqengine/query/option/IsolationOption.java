package com.googlecode.cqengine.query.option;

/**
 * @author Niall Gallagher
 */
public class IsolationOption {

    private final IsolationLevel isolationLevel;

    public IsolationOption(IsolationLevel isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    public IsolationLevel getIsolationLevel() {
        return isolationLevel;
    }

    @Override
    public String toString() {
        return "isolationLevel(" + isolationLevel + ")";
    }

    /**
     * Utility method to extract an {@link IsolationOption} object from the query options provided, and to check
     * if its level is the same as the one supplied.
     *
     * @param queryOptions The query options to check
     * @param level The isolation level to compare with
     * @return True if {@link IsolationOption} object is contained in those provided and its level matches that given
     */
    public static boolean isIsolationLevel(QueryOptions queryOptions, IsolationLevel level) {
        IsolationOption option = queryOptions.get(IsolationOption.class);
        return option != null && level.equals(option.getIsolationLevel());
    }
}