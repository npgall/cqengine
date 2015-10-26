/**
 * Copyright 2012-2015 Niall Gallagher
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
package com.googlecode.cqengine.query.parser.cqn;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.parser.cqn.support.DateMathParser;
import com.googlecode.cqengine.resultset.ResultSet;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;

/**
 * @author niall.gallagher
 */
public class CQNDateMathTest {

    @Test
    public void testDateMath() {
        // Create a collection of Order objects, with shipDates 2015-08-01, 2015-08-02 and 2015-08-03...
        IndexedCollection<Order> collection = new ConcurrentIndexedCollection<Order>();
        collection.add(createOrder("2015-08-01"));
        collection.add(createOrder("2015-08-02"));
        collection.add(createOrder("2015-08-03"));

        // Create a parser for CQN queries on Order objects...
        CQNParser<Order> parser = CQNParser.forPojoWithAttributes(Order.class, createAttributes(Order.class));

        // Register a DateMathParser which can parse date math expressions
        // relative to the given date value for "now" ("2015-08-04").
        // The custom value for "now" can be omitted to have it always calculate relative to the current time...
        parser.registerValueParser(Date.class, new DateMathParser(createDate("2015-08-04")));

        // Retrieve orders whose ship date is between 3 days ago and 2 days ago...
        ResultSet<Order> results = parser.retrieve(collection, "between(\"shipDate\", \"-3DAYS\", \"-2DAYS\")");

        // Assert that the following two orders are returned...
        Assert.assertTrue(results.contains(createOrder("2015-08-01")));
        Assert.assertTrue(results.contains(createOrder("2015-08-02")));
        Assert.assertEquals(2, results.size());
    }

    /* The Order POJO */
    static class Order {
        final Date shipDate;

        public Order(Date shipDate) {
            this.shipDate = shipDate;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "shipDate=" + shipDate +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Order)) {
                return false;
            }
            Order order = (Order) o;
            return shipDate.equals(order.shipDate);
        }

        @Override
        public int hashCode() {
            return shipDate.hashCode();
        }
    }

    static Order createOrder(String date) {
        return new Order(createDate(date));
    }

    static Date createDate(String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(date);
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to parse date: " + date);
        }
    }
}
