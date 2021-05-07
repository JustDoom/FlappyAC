package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.alert.AlertManager;
import com.justdoom.flappyanticheat.checks.CheckManager;
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
    private CheckManager checkManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadModules();
    }

    public void loadModules(){
        checkManager = new CheckManager(this);
        checkManager.loadChecks();
    }
}