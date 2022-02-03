package com.imjustdoom.flappyaddon;

import com.imjustdoom.flappyaddon.events.FlappyPlayerLoadListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new FlappyPlayerLoadListener(), this);
    }
}