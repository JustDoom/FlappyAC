package com.imjustdoom.flappyanticheat.config;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.util.FileUtil;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Paths;

public class Config {

    public static CommentedConfigurationNode configFile;

    public static String PREFIX;
    public static int CONFIG_VERSION;

    public static class Settings {
        public static boolean OUTPUT_TO_CONSOLE;
        public static int JOIN_EXEMPTION;
    }

    public static class Alerts {
        public static String FAILED_CHECK;
        public static String PLAYER_JOIN;
        public static String HOVER;
        public static String TOGGLE_ALERTS_ON;
        public static String TOGGLE_ALERTS_OFF;
    }

    public static class Logs {
        public static class PunishmentLog {
            public static boolean ENABLED;
            public static String MESSAGE;
        }

        public static class ViolationLog {
            public static boolean ENABLED;
            public static String MESSAGE;
        }
    }

    public static class Messages {
        public static String RELOAD;
        public static String RESET_ALL_VIOLATIONS;
    }

    public static void load() {

        // Create the config file
        try {
            if(!FileUtil.doesFileExist(FlappyAnticheat.INSTANCE.getPlugin().getDataFolder().getPath()))
                FileUtil.createDirectory(FlappyAnticheat.INSTANCE.getPlugin().getDataFolder().getPath());

            if(!FileUtil.doesFileExist(FlappyAnticheat.INSTANCE.getPlugin().getDataFolder() + "/config.yml"))
                FileUtil.addConfig(FlappyAnticheat.INSTANCE.getPlugin().getDataFolder() + "/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set where we want to load and save the config from
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Paths.get(FlappyAnticheat.INSTANCE.getPlugin().getDataFolder() + "/config.yml"))
                .build();

        // Load the config
        try {
            configFile = loader.load();
            FlappyAnticheat.INSTANCE.getPlugin().getLogger().info("Config has been loaded");
        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
            return;
        }

        PREFIX = configFile.node("prefix").getString();
        CONFIG_VERSION = configFile.node("config-version").getInt();

        Settings.OUTPUT_TO_CONSOLE = configFile.node("settings", "output-to-console").getBoolean();
        Settings.JOIN_EXEMPTION = configFile.node("join-exemption").getInt();

        Alerts.FAILED_CHECK = configFile.node("alerts", "failed-check").getString();
        Alerts.PLAYER_JOIN = configFile.node("alerts", "player-join").getString();
        Alerts.HOVER = configFile.node("alerts", "hover").getString();
        Alerts.TOGGLE_ALERTS_ON = configFile.node("alerts", "toggle-alerts-on").getString();
        Alerts.TOGGLE_ALERTS_OFF = configFile.node("alerts", "toggle-alerts-off").getString();

        Logs.PunishmentLog.ENABLED = configFile.node("logs", "punishment-log", "enabled").getBoolean();
        Logs.PunishmentLog.MESSAGE = configFile.node("logs", "punishment-log", "message").getString();

        Logs.ViolationLog.ENABLED = configFile.node("logs", "violation-log", "enabled").getBoolean();
        Logs.ViolationLog.MESSAGE = configFile.node("logs", "violation-log", "message").getString();

        Messages.RELOAD = configFile.node("messages", "reload").getString();
        Messages.RESET_ALL_VIOLATIONS = configFile.node("messages", "reset-all-violations").getString();

        if(CONFIG_VERSION != FlappyAnticheat.INSTANCE.getConfigVersion()) {
            FlappyAnticheat.INSTANCE.getPlugin().getLogger().info("Config is out of date, please update it");
        }
    }

    public static ConfigurationNode getConfigurationSection(String check, String type) {
        return configFile.node("checks", check, type);
    }
}