package com.justdoom.flappyanticheat.util;

import com.justdoom.flappyanticheat.FlappyAnticheat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static boolean doesFileExist(String filename) {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            return false;
        }
        return true;
    }

    public static void createFile(String filename) throws IOException {
        Path path = Paths.get(filename);
        File file = new File(String.valueOf(path));
        file.createNewFile();
    }

    public static void createDirectory(String path) throws IOException {
        Files.createDirectory(Paths.get(path));
    }

    public static void addConfig(String filename) throws IOException {
        Path path = Paths.get(filename);
        InputStream stream = FlappyAnticheat.class.getResourceAsStream("/config.yml");
        Files.copy(stream, path);
    }
}