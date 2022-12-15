package org.omenyuk;

import com.fasterxml.jackson.core.exc.StreamWriteException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stream class that reads files
 * in the stream from the folder
 * specified in the File[] listOfFiles array
 * @author Omenyuk Vyacheslav .
 */

public class ThreadFolderReading implements Runnable {
    @Override
    public void run() {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get("output.xml"))) {
            Map<String, Double> FinesAllYears = new LinkedHashMap<>();

            File folder = new File("src/main/resources/");
            File[] listOfFiles = folder.listFiles();
            for(int i = 0; i <  listOfFiles.length; i++) {
                //System.out.println("Thread name is = " + Thread.currentThread().getName());

                FinesAllYears = Main.getStatsFines( listOfFiles[i], FinesAllYears);
            }
            Main.writeStatsFines(FinesAllYears, bufferedWriter);

        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
