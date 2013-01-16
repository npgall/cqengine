package com.googlecode.cqengine.examples.join;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

/**
 * @author ngallagher
 * @since 2013-01-16 10:38
 */
public class Person {
    private final int personId;
    private final String name;
    private final String gender;
    private final int age;

    Person(int personId, String name, String gender, int age) {
        this.personId = personId;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public int getPersonId() {
        return personId;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                '}';
    }

    static Attribute<Person, Integer> PERSON_ID = new SimpleAttribute<Person, Integer>("personId") {
        public Integer getValue(Person person) { return person.personId; }
    };
    static Attribute<Person, String> NAME = new SimpleAttribute<Person, String>("name") {
        public String getValue(Person person) { return person.name; }
    };
    static Attribute<Person, String> GENDER = new SimpleAttribute<Person, String>("gender") {
        public String getValue(Person person) { return person.gender; }
    };
    static Attribute<Person, Integer> AGE = new SimpleAttribute<Person, Integer>("age") {
        public Integer getValue(Person person) { return person.age; }
    };
}
