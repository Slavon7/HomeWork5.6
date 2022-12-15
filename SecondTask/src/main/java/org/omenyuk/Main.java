package org.omenyuk;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Omenyuk Vyacheslav
 */

public class Main {
    private static String stringPath = "..\\property";
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {

        Path path = Paths.get(stringPath);
        Person person = Parser.loadFromProperties(Person.class, path);
        System.out.println(person);
    }
}
