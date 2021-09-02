package com.justdoom.flappyanticheat.util;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import net.md_5.bungee.api.ChatColor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    // Config file functions
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

    // Log file functions
    public void addToFile(String fileName, String message) {
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

    public void createFiles(String name) {
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