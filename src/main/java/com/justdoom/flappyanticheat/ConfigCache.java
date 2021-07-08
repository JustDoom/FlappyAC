package com.justdoom.flappyanticheat;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigCache {

    public FileConfiguration configuration;

    public ConfigCache(){
        configuration = FlappyAnticheat.getInstance().getConfig();
    }

    public void reloadConfig(){
        FlappyAnticheat.getInstance().reloadConfig();
        configuration = FlappyAnticheat.getInstance().getConfig();
    }
}