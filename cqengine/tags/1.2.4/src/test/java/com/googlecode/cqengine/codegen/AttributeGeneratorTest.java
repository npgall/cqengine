/**
 * Copyright 2012 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.codegen;

import org.junit.Test;

import java.util.List;

import static com.googlecode.cqengine.codegen.AttributesGenerator.generateAttributesForPastingIntoTargetClass;
import static com.googlecode.cqengine.codegen.AttributesGenerator.generateSeparateAttributesClass;
import static org.junit.Assert.assertEquals;

/**
 * @author Niall Gallagher
 */
public class AttributeGeneratorTest {

    public static class CarParent {
        private int inaccessible;
        protected int inheritedCarId;
    }
    public static class Car extends CarParent {
        public String name;
        private String description;
        List<String> features;
        double[] prices;
        String[] extras;
    }

    @Test
    public void testGenerateAttributesForPastingIntoTargetClass() {
        String expected = "" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for accessing field {@code Car.name}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if this field cannot be null, replace this SimpleNullableAttribute with a SimpleAttribute\n" +
                "    public static final Attribute<Car, String> NAME = new SimpleNullableAttribute<Car, String>(\"NAME\") {\n" +
                "        public String getValue(Car car) { return car.name; }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for accessing field {@code Car.description}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if this field cannot be null, replace this SimpleNullableAttribute with a SimpleAttribute\n" +
                "    public static final Attribute<Car, String> DESCRIPTION = new SimpleNullableAttribute<Car, String>(\"DESCRIPTION\") {\n" +
                "        public String getValue(Car car) { return car.description; }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for accessing field {@code Car.features}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if the list cannot contain null elements change true to false in the following constructor, or\n" +
                "    // - if the list cannot contain null elements AND the field itself cannot be null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<Car, String> FEATURES = new MultiValueNullableAttribute<Car, String>(\"FEATURES\", true) {\n" +
                "        public List<String> getNullableValues(Car car) { return car.features; }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for accessing field {@code Car.prices}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if this field cannot be null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<Car, Double> PRICES = new MultiValueNullableAttribute<Car, Double>(\"PRICES\", false) {\n" +
                "        public List<Double> getNullableValues(final Car car) {\n" +
                "            return new AbstractList<Double>() {\n" +
                "                public Double get(int i) { return car.prices[i]; }\n" +
                "                public int size() { return car.prices.length; }\n" +
                "            };\n" +
                "        }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for accessing field {@code Car.extras}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if the array cannot contain null elements change true to false in the following constructor, or\n" +
                "    // - if the array cannot contain null elements AND the field itself cannot be null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<Car, String> EXTRAS = new MultiValueNullableAttribute<Car, String>(\"EXTRAS\", true) {\n" +
                "        public List<String> getNullableValues(Car car) { return Arrays.asList(car.extras); }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for accessing field {@code Car.inheritedCarId}.\n" +
                "     */\n" +
                "    public static final Attribute<Car, Integer> INHERITED_CAR_ID = new SimpleAttribute<Car, Integer>(\"INHERITED_CAR_ID\") {\n" +
                "        public Integer getValue(Car car) { return car.inheritedCarId; }\n" +
                "    };";
        assertEquals(expected, generateAttributesForPastingIntoTargetClass(Car.class));
    }

    @Test
    public void testGenerateSeparateAttributesClass() {
        String expected = "" +
                "package com.googlecode.cqengine.codegen;\n" +
                "\n" +
                "import com.googlecode.cqengine.attribute.*;\n" +
                "import java.util.*;\n" +
                "import com.googlecode.cqengine.codegen.AttributeGeneratorTest.Car;\n" +
                "\n" +
                "/**\n" +
                " * CQEngine attributes for accessing fields in class {@code com.googlecode.cqengine.codegen.AttributeGeneratorTest.Car}.\n" +
                " * <p/>.\n" +
                " * Auto-generated by CQEngine's {@code AttributesGenerator}.\n" +
                " */\n" +
                "public class CQCar {\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for accessing field {@code Car.name}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if this field cannot be null, replace this SimpleNullableAttribute with a SimpleAttribute\n" +
                "    public static final Attribute<Car, String> NAME = new SimpleNullableAttribute<Car, String>(\"NAME\") {\n" +
                "        public String getValue(Car car) { return car.name; }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for accessing field {@code Car.features}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if the list cannot contain null elements change true to false in the following constructor, or\n" +
                "    // - if the list cannot contain null elements AND the field itself cannot be null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<Car, String> FEATURES = new MultiValueNullableAttribute<Car, String>(\"FEATURES\", true) {\n" +
                "        public List<String> getNullableValues(Car car) { return car.features; }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for accessing field {@code Car.prices}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if this field cannot be null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<Car, Double> PRICES = new MultiValueNullableAttribute<Car, Double>(\"PRICES\", false) {\n" +
                "        public List<Double> getNullableValues(final Car car) {\n" +
                "            return new AbstractList<Double>() {\n" +
                "                public Double get(int i) { return car.prices[i]; }\n" +
                "                public int size() { return car.prices.length; }\n" +
                "            };\n" +
                "        }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for accessing field {@code Car.extras}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if the array cannot contain null elements change true to false in the following constructor, or\n" +
                "    // - if the array cannot contain null elements AND the field itself cannot be null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<Car, String> EXTRAS = new MultiValueNullableAttribute<Car, String>(\"EXTRAS\", true) {\n" +
                "        public List<String> getNullableValues(Car car) { return Arrays.asList(car.extras); }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for accessing field {@code Car.inheritedCarId}.\n" +
                "     */\n" +
                "    public static final Attribute<Car, Integer> INHERITED_CAR_ID = new SimpleAttribute<Car, Integer>(\"INHERITED_CAR_ID\") {\n" +
                "        public Integer getValue(Car car) { return car.inheritedCarId; }\n" +
                "    };\n" +
                "}\n";
        System.out.println(expected);
        assertEquals(expected, generateSeparateAttributesClass(Car.class, Car.class.getPackage()));
    }
}
