package com.googlecode.cqengine.performance;

/**
 * Created by kimptonc on 02/06/2016.
 */
public interface Worker<T,P> {
    T run(P param);
}
