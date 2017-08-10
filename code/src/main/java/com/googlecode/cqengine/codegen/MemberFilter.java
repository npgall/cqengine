package com.googlecode.cqengine.codegen;

import java.lang.reflect.Member;

/**
 * A filter which determines the subset of the members of a class (fields and methods) for which
 * attributes should be generated.
 * <p/>
 * This can be supplied to {@link AttributeSourceGenerator} or {@link AttributeBytecodeGenerator}.
 *
 * @see MemberFilters
 */
public interface MemberFilter {

    boolean accept(Member member);
}
