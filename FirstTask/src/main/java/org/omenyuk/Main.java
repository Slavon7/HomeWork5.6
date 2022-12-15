package org.omenyuk;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import org.omenyuk.model.Fines;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The program parses several JSON files,
 * sorts fine_amont and writes them to the output file
 * @author Omenyuk Vyacheslav .
 */

public class Main {

    public static void main(String[] args) throws IOException {

        long threadTime;
        ExecutorService executor;

        /* Two thread */
        threadTime = System.nanoTime();
        executor = Executors.newFixedThreadPool(2);
        executor.submit(new ThreadFolderReading());
        executor.submit(new ThreadFolderReading());
        executor.shutdown();
        threadTime = System.nanoTime() - threadTime;
        System.out.printf("Execution time in two threads %,9.3f ms\n", threadTime/1_000_000.0);

        /* Four thread */
        threadTime = System.nanoTime();
        executor = Executors.newFixedThreadPool(4);
        executor.submit(new ThreadFolderReading());
        executor.submit(new ThreadFolderReading());
        executor.submit(new ThreadFolderReading());
        executor.submit(new ThreadFolderReading());
        executor.shutdown();
        threadTime = System.nanoTime() - threadTime;
        System.out.printf("Execution time in four threads %,9.3f ms\n", threadTime/1_000_000.0);

        /* Eight thread */
        threadTime = System.nanoTime();
        executor = Executors.newFixedThreadPool(8);
        executor.submit(new ThreadFolderReading());
        executor.submit(new ThreadFolderReading());
        executor.submit(new ThreadFolderReading());
        executor.submit(new ThreadFolderReading());
        executor.submit(new ThreadFolderReading());
        executor.submit(new ThreadFolderReading());
        executor.submit(new ThreadFolderReading());
        executor.submit(new ThreadFolderReading());
        executor.shutdown();
        threadTime = System.nanoTime() - threadTime;
        System.out.printf("Execution time in eight threads %,9.3f ms\n", threadTime/1_000_000.0);
    }

    /**
    * Sorting method LinkedHashMap<>()
     */
    static Map<String, Double> getStatsFines(File FinesAllYears, Map<String, Double> Fines) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Double> sortedFines = new LinkedHashMap<>();
        Fines[] fine;
        String key;
        double value;
        try {
            fine = objectMapper.readValue(FinesAllYears, Fines[].class);
            for (Fines fines : fine) {
                key = fines.getType();
                value = fines.getFine_amount();
                if (Fines.containsKey(key)) {
                    Fines.put(key, Fines.get(key) + value);
                } else {
                    Fines.put(key, value);
                }
                //System.out.println(key + " : " + value);
            }

            Fines.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .forEach(entry -> sortedFines.put(entry.getKey(), entry.getValue()));

            return sortedFines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writing the sorted map to the output file
     */
    public static void writeStatsFines(Map<String, Double> fines, BufferedWriter bufferedWriter) throws IOException {

        bufferedWriter.write("<fines>\n");
            fines.forEach((key, value) -> {
            try {
                bufferedWriter.write("\t<fines type=\"" + key + "\" fine_amount=\"" + value + "\" />\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        bufferedWriter.write("</fines>\n");
    }
}

