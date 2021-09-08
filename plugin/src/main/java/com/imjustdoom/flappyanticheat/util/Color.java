package com.imjustdoom.flappyanticheat.util;

import net.md_5.bungee.api.ChatColor;

public class Color {

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
