package com.googlecode.cqengine.codegen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to exclude a Getter from being used by
 * {@link AttributeBytecodeGenerator.createAttributesForGetters.}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AttributeGenerationExclude {
}
