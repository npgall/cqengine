package com.googlecode.cqengine.codegen;

import com.googlecode.cqengine.testutil.Car;

/**
 * @author Niall Gallagher
 */
public class AttributeGeneratorTest {

    public static void main(String[] args) {
        System.out.println(AttributeGenerator.generateAttributesForClass(Car.class, false));
    }


}
