package com.imjustdoom.flappyanticheat.listener;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.util.MessageUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class ClientBrandListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] msg) {
        try {
            String message = Config.PREFIX + Config.Alerts.PLAYER_JOIN;
            String brand = new String(msg, "UTF-8").substring(1);

            FlappyPlayer data = FlappyAnticheat.INSTANCE.getDataManager().getData(p.getPlayer());

            message = MessageUtil.translate(message)
                    .replace("{player}", p.getName())
                    .replace("{brand}", brand)
                    .replace("{version}", MessageUtil.translateVersion(data.getClientVersion().name()));

            //TODO: Improve alert toggle
            for (final UUID uuid : FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts()) {
                Player player = Bukkit.getPlayer(uuid);
                if (!player.hasPermission("flappyac.alerts")) continue;
                player.sendMessage(message);
            }

            MessageUtil.toConsole(message);

            data.setClientBrand(brand);
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