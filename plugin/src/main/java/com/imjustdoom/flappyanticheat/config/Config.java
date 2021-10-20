package com.imjustdoom.flappyanticheat.config;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.util.FileUtil;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Config {

    public static CommentedConfigurationNode configFile;
    private static YamlConfigurationLoader loader;

    public static String PREFIX;
    public static int CONFIG_VERSION;

    public static class Settings {
        public static boolean OUTPUT_TO_CONSOLE;
        public static int JOIN_EXEMPTION;
        public static boolean SEND_BRAND_MESSAGE;
    }

    public static class Alerts {
        public static String FAILED_CHECK;
        public static String PLAYER_JOIN;
        public static String HOVER;
        public static String TOGGLE_ALERTS_ON;
        public static String TOGGLE_ALERTS_OFF;
        public static List<String> CLICK_COMMANDS;
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
        public static String PROFILE;
    }

    public static class Menu {
        public static String NAME;
    }

    public static class Discord {
        public static boolean ENABLED;
        public static String WEBHOOK;
        public static String PROFILE;
        public static String USERNAME;
        public static String STATUS;
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
        loader = YamlConfigurationLoader.builder()
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
        Settings.JOIN_EXEMPTION = configFile.node("settings", "join-exemption").getInt();
        Settings.SEND_BRAND_MESSAGE = configFile.node("settings", "send-brand-message").getBoolean();

        Alerts.FAILED_CHECK = configFile.node("alerts", "failed-check").getString();
        Alerts.PLAYER_JOIN = configFile.node("alerts", "player-join").getString();
        Alerts.HOVER = configFile.node("alerts", "hover").getString();
        Alerts.TOGGLE_ALERTS_ON = configFile.node("alerts", "toggle-alerts-on").getString();
        Alerts.TOGGLE_ALERTS_OFF = configFile.node("alerts", "toggle-alerts-off").getString();

        try {
            Alerts.CLICK_COMMANDS = configFile.node("alerts", "click-commands").getList(String.class);
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        Logs.PunishmentLog.ENABLED = configFile.node("logs", "punishment-log", "enabled").getBoolean();
        Logs.PunishmentLog.MESSAGE = configFile.node("logs", "punishment-log", "message").getString();

        Logs.ViolationLog.ENABLED = configFile.node("logs", "violation-log", "enabled").getBoolean();
        Logs.ViolationLog.MESSAGE = configFile.node("logs", "violation-log", "message").getString();

        Messages.RELOAD = configFile.node("messages", "reload").getString();
        Messages.RESET_ALL_VIOLATIONS = configFile.node("messages", "reset-all-violations").getString();
        Messages.PROFILE = configFile.node("messages", "profile").getString();

        Menu.NAME = configFile.node("menu", "name").getString();

        Discord.ENABLED = configFile.node("discord", "enabled").getBoolean();
        Discord.WEBHOOK = configFile.node("discord", "webhook").getString();
        Discord.PROFILE = configFile.node("discord", "profile-picture").getString();
        Discord.USERNAME = configFile.node("discord", "username").getString();
        Discord.STATUS = configFile.node("discord", "status-text").getString();

        if(CONFIG_VERSION != FlappyAnticheat.INSTANCE.getConfigVersion()) {
            FlappyAnticheat.INSTANCE.getPlugin().getLogger().info("Config is out of date, please update it");
        }
    }

    public static ConfigurationNode getConfigurationSection(String check, String type) {
        return configFile.node("checks", check, type);
    }

    public static void saveConfig(ConfigurationNode file) throws ConfigurateException {
        loader.save(file);
    }
}