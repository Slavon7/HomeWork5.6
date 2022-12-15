import org.omenyuk.*;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Omenyuk Vyacheslav
 */

public class unitTest {
    private static String stringPath = "..\\property";

    @Test
    public void JunitOfParser() throws IOException, InstantiationException, IllegalAccessException {
        Path path = Paths.get(stringPath);
        String result = Person.class.getName();
        assertEquals(result, Parser.loadFromProperties(Person.class, path).getClass().getName());
    }
}
