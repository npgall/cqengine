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
package com.googlecode.cqengine.examples.nestedobjects;

import com.google.common.base.Function;
import com.googlecode.cqengine.*;
import com.googlecode.cqengine.attribute.*;
import com.googlecode.cqengine.query.option.QueryOptions;
import java.util.*;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.google.common.collect.Iterables.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * Demonstrates how to define attributes which read from nested objects,
 * and to search the collection for objects whose nested objects match a query.
 * <p/>
 * In this example there are User, Order and Product objects.
 * A User can have many Orders. An Order can have many Products. Each Product has a name.
 * This example shows how to search for Users who have ordered a particular product, by specifying the product's name.
 */
public class NestedObjectsExample {

    // For Java 8: A multi-value attribute which returns the names of products ordered by a user
//    static final Attribute<User, String> PRODUCT_NAMES_ORDERED = new MultiValueAttribute<User, String>() {
//        public Iterable<String> getValues(User user, QueryOptions queryOptions) {
//            return user.orders.stream()
//                    .map(order -> order.products).flatMap(Collection::stream)
//                    .map(product -> product.name)::iterator;
//        }
//    };

    // For Java 6: A multi-value attribute which returns the names of products ordered by a user
    static final Attribute<User, String> PRODUCT_NAMES_ORDERED = new MultiValueAttribute<User, String>() {
        public Iterable<String> getValues(User user, QueryOptions queryOptions) {
            return concat(transform(user.orders, new Function<Order, Iterable<String>>() {
                public Iterable<String> apply(Order order) {
                    return transform(order.products, new Function<Product, String>() {
                        public String apply(Product product) {
                            return product.name;
                        }
                    });
                }
            }));
        }
    };

    public static void main(String[] args) {
        Order order1 = new Order(asList(new Product("Diet Coke"), new Product("Snickers Bar")));
        Order order2 = new Order(singletonList(new Product("Sprite")));
        User user1 = new User(1, asList(order1, order2)); // userId 1

        Order order3 = new Order(asList(new Product("Sprite"), new Product("Popcorn")));
        User user2 = new User(2, singletonList(order3));  // userId 2

        Order order4 = new Order(singletonList(new Product("Snickers Bar")));
        User user3 = new User(3, singletonList(order4));  // userId 3


        IndexedCollection<User> users = new ConcurrentIndexedCollection<User>();
        users.addAll(asList(user1, user2, user3));

        for (User user : users.retrieve(equal(PRODUCT_NAMES_ORDERED, "Snickers Bar"))) {
            System.out.println(user.userId);
        } // ...prints 1, 3
    }


    // ***** Domain objects... *****

    static class User {
        final long userId;
        final List<Order> orders;

        User(long userId, List<Order> orders) {
            this.userId = userId;
            this.orders = orders;
        }
    }

    static class Order {
        final List<Product> products;

        Order(List<Product> products) {
            this.products = products;
        }
    }

    static class Product {
        final String name;

        Product(String name) {
            this.name = name;
        }
    }
}
