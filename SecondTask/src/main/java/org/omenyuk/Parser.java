package org.omenyuk;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Omenyuk Vyacheslav
 * We parse the file and assign the values of
 * the variables that are specified in the Person class
 */

public class Parser {
    public static <T> T loadFromProperties(Class<T> cls, Path propertiesPath) throws InstantiationException, IllegalAccessException, IOException {
        T c = cls.newInstance();
        Map<String, String> propertySet = new HashMap<>();

        try {
            String[] strings;
            Scanner scanner = new Scanner(propertiesPath.getFileName());
            while (scanner.hasNext()) {
                 strings = scanner.nextLine().split("=");
                propertySet.put(strings[0], strings[1]);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        Field[] allFields = c.getClass().getDeclaredFields();

        // Reading Map
        for (Field field : allFields) {
            for (Map.Entry<String, String> entry : propertySet.entrySet()) {
                if (field.isAnnotationPresent(Property.class)) {
                    if (entry.getKey().equals(field.getAnnotation(Property.class).name())) {
                        elementParser(c, entry, field);
                    }
                }
                if (entry.getKey().equals(field.getName())) {
                    elementParser(c, entry, field);
                }
            }
        }
        return c;
    }

    /**
     * Parse elements by their data types
     */
    private static <T> void elementParser(T c, Map.Entry<String, String> entry, Field field) {
        switch (field.getType().getName()) {
            case "java.lang.String":
                getSetter(c, field.getName(), entry.getValue());
                break;
            case "int":
            case "java.lang.Integer":
                try {
                    getSetter(c, field.getName(), Integer.parseInt(entry.getValue()));
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "java.time.Instant":
                try {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(field.getAnnotation(Property.class).format());
                    LocalDateTime localDateTime = LocalDateTime.parse(entry.getValue(), dateTimeFormatter);
                    getSetter(c, field.getName(), localDateTime.toInstant(ZoneOffset.UTC));
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }
    private static void getSetter(Object object, String name, Object valueElement) {
        PropertyDescriptor propertyDescriptor;
        Method methodSet;
        try {
            propertyDescriptor = new PropertyDescriptor(name, object.getClass());
            methodSet = propertyDescriptor.getWriteMethod();
            try {
                methodSet.invoke(object, valueElement);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }
}