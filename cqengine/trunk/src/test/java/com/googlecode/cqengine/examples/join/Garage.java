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
package com.googlecode.cqengine.examples.join;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

import java.util.List;

/**
 * @author Niall Gallagher
 */
public class Garage {

    public final int garageId;
    public final String name;
    public final String location;
    public final List<String> brandsServiced;

    public Garage(int garageId, String name, String location, List<String> brandsServiced) {
        this.garageId = garageId;
        this.name = name;
        this.location = location;
        this.brandsServiced = brandsServiced;
    }

    @Override
    public String toString() {
        return "Garage{" +
                "garageId=" + garageId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", brandsServiced=" + brandsServiced +
                '}';
    }

    public static Attribute<Garage, Integer> GARAGE_ID = new SimpleAttribute<Garage, Integer>("garageId") {
        public Integer getValue(Garage garage) { return garage.garageId; }
    };
    public static Attribute<Garage, String> NAME = new SimpleAttribute<Garage, String>("name") {
        public String getValue(Garage garage) { return garage.name; }
    };
    public static Attribute<Garage, String> LOCATION = new SimpleAttribute<Garage, String>("location") {
        public String getValue(Garage garage) { return garage.location; }
    };
    public static Attribute<Garage, String> BRANDS_SERVICED = new MultiValueAttribute<Garage, String>("brandsServiced") {
        public List<String> getValues(Garage garage) { return garage.brandsServiced; }
    };
}
