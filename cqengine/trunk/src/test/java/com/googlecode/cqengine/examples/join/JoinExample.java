package com.googlecode.cqengine.examples.join;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.predicate.JoinPredicate;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.LinkedList;
import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * Demonstrates a "join" between two indexed collections. Given a collection of Persons, and a collections of Accidents,
 * find people who had accidents in the last 3 years, along with details of those accidents.
 *
 * @author Niall Gallagher
 */
public class JoinExample {

    public static void main(String[] args) {
        // Collection of people (e.g. from a table called People)...
        final IndexedCollection<Person> people = CQEngine.newInstance();
        people.add(new Person(1, "John Doe", "Male", 26));
        people.add(new Person(2, "Jane Doe", "Female", 27));

        // Collection of accidents (e.g. from a table called Accidents)...
        final IndexedCollection<Accident> accidents = CQEngine.newInstance();
        accidents.add(new Accident(2011, "Injury", 2));

        // Query: Accidents in last 3 years which resulted in injuries (we will AND this with additional queries later)...
        final Query<Accident> accidentsTemplateQuery = and(
                equal(Accident.TYPE, "Injury"),
                greaterThan(Accident.YEAR, 2013 - 3)
        );

        // Define a "virtual attribute" which performs a query on accidents for a given person...
        Attribute<Person, Boolean> HAD_INJURY_ACCIDENT = new SimpleAttribute<Person, Boolean>("join") {
            public Boolean getValue(Person person) {
                return accidents.retrieve(
                        and(accidentsTemplateQuery, equal(Accident.PERSON_ID, person.getPersonId()))
                ).size() > 0;
            }
        };

        // Query: People who are female aged 25-30 who had an injury accident in last 3 years...
        Query<Person> personsQuery = and(
                equal(Person.GENDER, "Female"),
                between(Person.AGE, 25, 30),
                equal(HAD_INJURY_ACCIDENT, true)
        );
        ResultSet<Person> matchingPeople = people.retrieve(personsQuery);

        for (Person person : matchingPeople) {
            System.out.println(person.getName() + " had injury accidents in last 3 years:- " );
            Query<Accident> injuryAccidentsForThisPerson = and(
                    equal(Accident.PERSON_ID, person.getPersonId()), accidentsTemplateQuery
            );
            for (Accident accident : accidents.retrieve(injuryAccidentsForThisPerson)) {
                System.out.println("---> " + accident);
            }
        }
        // ..prints:
        // Jane Doe had injury accidents in last 3 years:-
        // ---> Accident{year=2011, type='Injury', personId=2}
    }
}