package com.justdoom.flappyanticheat.data;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.utils.Color;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileData {

    public FileData(){

    }

    public void addToFile(String fileName, String message) {
        File log = new File(FlappyAnticheat.getInstance().getDataFolder(), fileName);
        try{
            if(!log.exists()){
                createFiles(fileName);
            }
            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            out.append(ChatColor.stripColor(message));
            out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void createFiles(String name) {
        if (!new File(FlappyAnticheat.getInstance().getDataFolder(), name).exists()) {
            File todayFile = new File(FlappyAnticheat.getInstance().getDataFolder(), name);
            try {
                todayFile.createNewFile(); // Error
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
