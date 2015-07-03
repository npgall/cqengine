package com.googlecode.cqengine.query.option;


/**
 * If set as a query option causes CQEngine to log its internal decisions during query evaluation to the given sink.
 * The given sink could be System.out in the simplest case.
 * This can be used for debugging, or for testing CQEngine itself.
 *
 * @author niall.gallagher
 */
public class QueryLog {

    final Appendable sink;

    public QueryLog(Appendable sink) {
        this.sink = sink;
    }

    public Appendable getSink() {
        return sink;
    }

    public void log(String message) {
        try {
            sink.append(message);
        }
        catch (Exception e) {
            throw new IllegalStateException("Exception appending to query log", e);
        }
    }
}
