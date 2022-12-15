package org.omenyuk;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


/**
 * @author Omenyuk Vyacheslav
 */
@Setter
@Getter
public class Person {
    private String stringProperty;
    @Property(name = "numberProperty")
    private int myNumber;
    @Property(format = "yyyy.MM.dd HH:mm")
    private Instant timeProperty;

    @Override
    public String toString() {
        return "Person " +
                "\nstringProperty = '" + stringProperty + '\'' +
                "\nmyNumber = " + myNumber +
                "\ntimeProperty = " + timeProperty;
    }
}
