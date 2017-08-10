package com.googlecode.cqengine.codegen;

import java.lang.reflect.Member;

/**
 * A filter which determines the subset of the members of a class (fields and accessor methods) for which CQEngine
 * attributes should be generated.
 * This can be supplied to {@link AttributeSourceGenerator} or {@link AttributeBytecodeGenerator}.
 */
public interface MemberFilter {

    boolean accept(Member member);
}
