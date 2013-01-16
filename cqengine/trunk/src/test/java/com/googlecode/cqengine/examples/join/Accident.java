package com.googlecode.cqengine.examples.join;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

/**
 * @author ngallagher
 * @since 2013-01-16 10:39
 */
public class Accident {
    private final int year;
    private final String type;
    private final int personId;

    Accident(int year, String type, int personId) {
        this.year = year;
        this.type = type;
        this.personId = personId;
    }

    public int getYear() {
        return year;
    }

    public String getType() {
        return type;
    }

    public int getPersonId() {
        return personId;
    }

    @Override
    public String toString() {
        return "Accident{" +
                "year=" + year +
                ", type='" + type + '\'' +
                ", personId=" + personId +
                '}';
    }

    static Attribute<Accident, Integer> YEAR = new SimpleAttribute<Accident, Integer>("year") {
        public Integer getValue(Accident accident) { return accident.year; }
    };
    static Attribute<Accident, String> TYPE = new SimpleAttribute<Accident, String>("type") {
        public String getValue(Accident accident) { return accident.type; }
    };
    static Attribute<Accident, Integer> PERSON_ID = new SimpleAttribute<Accident, Integer>("personId") {
        public Integer getValue(Accident accident) { return accident.personId; }
    };

}
