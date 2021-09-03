package com.justdoom.flappyanticheat.util;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class ClientBrandUtil implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] msg) {
        try {
            String message = FlappyAnticheat.INSTANCE.getConfigFile().node("messages", "prefix").getString()
                    + FlappyAnticheat.INSTANCE.getConfigFile().node("messages", "player-join").getString();

            FlappyPlayer data = FlappyAnticheat.INSTANCE.getDataManager().getData(p.getPlayer());

            message = ChatColor.translateAlternateColorCodes('&', message)
                    .replace("{player}", p.getName())
                    .replace("{brand}", new String(msg, "UTF-8").substring(1))
                    .replace("{version}", data.getClientVersion().name()
                            .replace("v_", "")
                            .replace("_", "."));

            //TODO: Improve alert toggle
            for (final UUID uuid : FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts()) {
                Player player = Bukkit.getPlayer(uuid);
                if (!player.hasPermission("flappyac.alerts")) continue;
                player.sendMessage(message);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void addChannel(Player p, String channel) {
        try {
            p.getClass().getMethod("addChannel", String.class).invoke(p, channel);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            e.printStackTrace();
        }
    }
}
