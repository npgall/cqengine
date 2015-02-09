package com.googlecode.cqengine.query.option;

/**
 * Allows the app to disable argument validation in CQEngine via a QueryOption, to improve performance.
 * This should only be done when application developers are sure that their code has no bugs!
 *
 * @author niall.gallagher
 */
public enum ArgumentValidationStrategy {
    VALIDATE,
    SKIP
}
