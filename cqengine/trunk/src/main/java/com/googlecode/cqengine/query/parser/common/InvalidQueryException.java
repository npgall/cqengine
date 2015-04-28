package com.googlecode.cqengine.query.parser.common;

/**
 * @author Niall Gallagher
 */
public class InvalidQueryException extends IllegalStateException {

    public InvalidQueryException(String s) {
        super(s);
    }

    public InvalidQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
