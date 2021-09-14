package com.imjustdoom.flappyanticheat.util;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.config.Config;
import net.md_5.bungee.api.ChatColor;

public class MessageUtil {

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void toConsole(String message) {
        if(Config.Settings.OUTPUT_TO_CONSOLE)
            FlappyAnticheat.INSTANCE.getPlugin().getLogger().info(message);
    }
}
