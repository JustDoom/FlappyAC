package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.alert.AlertManager;
import com.justdoom.flappyanticheat.checks.CheckManager;
import com.justdoom.flappyanticheat.data.PlayerDataManager;
import com.justdoom.flappyanticheat.listener.PlayerConnectionListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlappyAnticheat extends JavaPlugin {

    private static FlappyAnticheat instance;

    public static FlappyAnticheat getInstance() {
        return instance;
    }

    public FlappyAnticheat(){
        instance = this;
    }

    public AlertManager msgutil = new AlertManager(this);
    public PlayerDataManager dataManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.dataManager = new PlayerDataManager();
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
    }
}