package org.levelup.up.json.impl;

import org.levelup.up.json.JsonDeserializer;
import org.levelup.up.json.JsonSerializer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonStorage {

    static Map <String, Object> SerializedMap, DeserializedMap;

    static {
        try {
            loadClasses();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
            Thread.sleep(1000);
    }

    private static void loadClasses() throws IOException {
        Collection <Class<?>> classes = scanDirectory();
        SerializedMap = classes.stream()
                .filter(clazz -> {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    return Arrays.stream(interfaces)
                            .anyMatch(c->c== JsonSerializer.class);
                })
                .collect(Collectors.toMap (clazz -> clazz.getName(), clazz-> newInstanceS(clazz)));

        DeserializedMap = classes.stream()
                .filter(clazz -> {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    return Arrays.stream(interfaces)
                            .anyMatch( c -> c == JsonDeserializer.class);
                })
                .collect(Collectors.toMap(clazz -> clazz.getName(), clazz -> newInstanceD(clazz)));
    }

    private static Collection <Class<?>> scanDirectory() throws IOException {
        String dirline = null;
        try (FileInputStream fin = new FileInputStream("C:\\Users\\Sam\\IdeaProjects\\jsonup\\dirname\\directory.txt")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
            dirline = br.readLine();
        }

       return Files.walk(Paths.get("src\\main\\java\\org\\level\\up\\json"))
        .map(Path::toFile)
        .filter(File::isFile)
        .map(File::toString)
        .map(filename -> filename .replace("src\\main\\java\\", "").replace("/", "").replace("\\","").replace(".java",""))
        .map(JsonStorage::loadClass)
        .collect(Collectors.toList())
               ;
    }

    private static Class<?> loadClass (String fullName) {
        try {
            return Class.forName(fullName);
        } catch (ClassNotFoundException exc) {
            throw new RuntimeException(exc);
        }
    }

    private static JsonSerializer<?> newInstanceS (Class <?> clazz) {
        try {
            return (JsonSerializer) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonDeserializer<?> newInstanceD (Class<?> clazz) {
        try {
            return (JsonDeserializer) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}
