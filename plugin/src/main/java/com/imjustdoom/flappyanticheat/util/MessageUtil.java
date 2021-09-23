package com.imjustdoom.flappyanticheat.util;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MessageUtil {

    /**
     * Translate a string with colour codes
     * @param message - The message to translate
     * @return - The message after being translated
     */
    public static String translate(String message) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(Component.text(message));
    }

    /**
     * Outputs the message to the console if the config option is enabled
     * @param message - Message to output to the console
     */
    public static void toConsole(String message) {
        if(Config.Settings.OUTPUT_TO_CONSOLE)
            FlappyAnticheat.INSTANCE.getPlugin().getLogger().info(message);
    }

    /**
     * Turns the versions string into an easier to read string
     * @param version - The version to translate
     * @return - Returns the version in an easier to read string
     */
    public static String translateVersion(String version) {
        return version.replace("v_", "").replace("_", ".");
    }
}
