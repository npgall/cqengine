package com.googlecode.cqengine.codegen;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author Niall Gallagher
 */
public class AttributeGeneratorTest {

    static class CarParent {
        private int inaccessible;
        protected int inheritedCarId;
    }
    static class Car extends CarParent {
        String name;
        private String description;
        List<String> features;
        double[] prices;
    }

    @Test
    public void testGenerateAttributesForPastingIntoTargetClass() {
        String expected = "\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for field {@code Car.name}.\n" +
                "     */\n" +
                "    public static final SimpleAttribute<Car, String> NAME = new SimpleAttribute<Car, String>(\"NAME\") {\n" +
                "        public String getValue(Car car) { return car.name; }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for field {@code Car.description}.\n" +
                "     */\n" +
                "    public static final SimpleAttribute<Car, String> DESCRIPTION = new SimpleAttribute<Car, String>(\"DESCRIPTION\") {\n" +
                "        public String getValue(Car car) { return car.description; }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for field {@code Car.features}.\n" +
                "     */\n" +
                "    public static final MultiValueAttribute<Car, String> FEATURES = new MultiValueAttribute<Car, String>(\"FEATURES\") {\n" +
                "        public List<String> getValues(Car car) { return car.features; }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for field {@code Car.prices}.\n" +
                "     */\n" +
                "    public static final MultiValueAttribute<Car, Double> PRICES = new MultiValueAttribute<Car, Double>(\"PRICES\") {\n" +
                "        public List<Double> getValues(final Car car) {\n" +
                "            return new AbstractList<Double>() {\n" +
                "                public Double get(int i) { return car.prices[i]; }\n" +
                "                public int size() { return car.prices.length; }\n" +
                "            };\n" +
                "        }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for field {@code Car.inheritedCarId}.\n" +
                "     */\n" +
                "    public static final SimpleAttribute<Car, Integer> INHERITED_CAR_ID = new SimpleAttribute<Car, Integer>(\"INHERITED_CAR_ID\") {\n" +
                "        public Integer getValue(Car car) { return car.inheritedCarId; }\n" +
                "    };";
        Assert.assertEquals(expected, AttributeGenerator.generateAttributesForPastingIntoTargetClass(Car.class));
    }

    @Test
    public void testGenerateSeparateAttributesClass() {
        String expected = "public class CarAttributes {\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for field {@code Car.name}.\n" +
                "     */\n" +
                "    public static final SimpleAttribute<Car, String> NAME = new SimpleAttribute<Car, String>(\"NAME\") {\n" +
                "        public String getValue(Car car) { return car.name; }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for field {@code Car.features}.\n" +
                "     */\n" +
                "    public static final MultiValueAttribute<Car, String> FEATURES = new MultiValueAttribute<Car, String>(\"FEATURES\") {\n" +
                "        public List<String> getValues(Car car) { return car.features; }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for field {@code Car.prices}.\n" +
                "     */\n" +
                "    public static final MultiValueAttribute<Car, Double> PRICES = new MultiValueAttribute<Car, Double>(\"PRICES\") {\n" +
                "        public List<Double> getValues(final Car car) {\n" +
                "            return new AbstractList<Double>() {\n" +
                "                public Double get(int i) { return car.prices[i]; }\n" +
                "                public int size() { return car.prices.length; }\n" +
                "            };\n" +
                "        }\n" +
                "    };\n" +
                "\n" +
                "    /**\n" +
                "     * CQEngine attribute for field {@code Car.inheritedCarId}.\n" +
                "     */\n" +
                "    public static final SimpleAttribute<Car, Integer> INHERITED_CAR_ID = new SimpleAttribute<Car, Integer>(\"INHERITED_CAR_ID\") {\n" +
                "        public Integer getValue(Car car) { return car.inheritedCarId; }\n" +
                "    };\n" +
                "}\n";
        Assert.assertEquals(expected, AttributeGenerator.generateSeparateAttributesClass(Car.class, Car.class.getPackage().toString()));
    }
}
