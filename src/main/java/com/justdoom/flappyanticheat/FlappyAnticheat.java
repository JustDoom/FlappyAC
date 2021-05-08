package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.checks.CheckManager;
import com.justdoom.flappyanticheat.commands.FlappyACCommand;
import com.justdoom.flappyanticheat.data.PlayerData;
import com.justdoom.flappyanticheat.data.PlayerDataManager;
import com.justdoom.flappyanticheat.listener.PlayerConnectionListener;
import com.justdoom.flappyanticheat.violations.ViolationHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlappyAnticheat extends JavaPlugin {

    private static FlappyAnticheat instance;

    public static FlappyAnticheat getInstance() {
        return instance;
    }

    public ViolationHandler violationHandler = new ViolationHandler();
    public PlayerDataManager dataManager;
    private CheckManager checkManager;
    public PlayerData playerData;

    public FlappyAnticheat(){
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);

        this.getCommand("flappyanticheat").setExecutor(new FlappyACCommand());

        this.dataManager = new PlayerDataManager();
        this.violationHandler = new ViolationHandler();

        loadModules();
    }

    public void loadModules(){
        checkManager = new CheckManager(this);
        checkManager.loadChecks();
    }
}