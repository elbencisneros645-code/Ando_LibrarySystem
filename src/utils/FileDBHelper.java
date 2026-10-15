/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author USER
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * to change this license
 */


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileDBHelper {

    private static final Path DATA_DIR = Paths.get(
        System.getProperty("user.home"),
        ".library-management"
    );

    private FileDBHelper() {
    }

    public static void save(Object object, String fileName)
            throws IOException {

        Files.createDirectories(DATA_DIR);

        Path file = DATA_DIR.resolve(fileName);

        try (ObjectOutputStream out =
                new ObjectOutputStream(
                    new BufferedOutputStream(
                        Files.newOutputStream(file)))) {

            out.writeObject(object);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(
            String fileName, T defaultValue)
            throws IOException, ClassNotFoundException {

        Path file = DATA_DIR.resolve(fileName);

        if (!Files.exists(file)) {
            return defaultValue;
        }

        try (ObjectInputStream in =
                new ObjectInputStream(
                    new BufferedInputStream(
                        Files.newInputStream(file)))) {

            return (T) in.readObject();
        }
    }
}

