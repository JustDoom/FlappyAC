package com.justdoom.flappyanticheat.manager;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManager {

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
