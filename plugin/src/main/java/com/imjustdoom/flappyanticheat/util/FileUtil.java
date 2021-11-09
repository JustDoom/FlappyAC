package com.imjustdoom.flappyanticheat.util;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import net.md_5.bungee.api.ChatColor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    /**
     * Checks if a file exists
     * @param filename - The file name to check if exists
     * @return - Returns if the file exists
     */
    public static boolean doesFileExist(String filename) {
        Path path = Paths.get(filename);
        return Files.exists(path);
    }

    /**
     * Creates a directory
     * @param path - The directory path to create
     * @throws IOException
     */
    public static void createDirectory(String path) throws IOException {
        Files.createDirectory(Paths.get(path));
    }

    /**
     * Creates the config file
     * @param filename - The file name/directory to create the config file
     * @throws IOException
     */
    public static void addConfig(String filename) throws IOException {
        Path path = Paths.get(filename);
        InputStream stream = FlappyAnticheat.class.getResourceAsStream("/config.yml");
        Files.copy(stream, path);
    }

    /**
     * Adds a message to a file
     * @param fileName - The file to add the text to
     * @param message - The message to add to the file
     */
    public static void addToFile(String fileName, String message) {
        File log = new File(FlappyAnticheat.INSTANCE.getPlugin().getDataFolder(), fileName);
        try {
            if(!log.exists()){
                createFiles(fileName);
            }
            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            out.append(ChatColor.stripColor(message));
            out.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Create some files
     * @param name - Name of the file to create
     */
    public static void createFiles(String name) {
        if (!new File(FlappyAnticheat.INSTANCE.getPlugin().getDataFolder(), name).exists()) {
            File todayFile = new File(FlappyAnticheat.INSTANCE.getPlugin().getDataFolder(), name);
            try {
                todayFile.createNewFile(); // Error
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}