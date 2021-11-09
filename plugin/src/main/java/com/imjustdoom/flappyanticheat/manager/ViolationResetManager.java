package com.imjustdoom.flappyanticheat.manager;

import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.api.events.ViolationResetEvent;
import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ViolationResetManager {

    public ViolationResetManager() {
        int delay = Config.Settings.VIOLATION_RESET * 20;

        Bukkit.getScheduler().runTaskTimerAsynchronously(FlappyAnticheat.INSTANCE.getPlugin(), () -> {
            ViolationResetEvent violationResetEvent = new ViolationResetEvent();
            Bukkit.getPluginManager().callEvent(violationResetEvent);
            if(!violationResetEvent.isCancelled() && Bukkit.getOnlinePlayers().size() > 0) {

                for(final Object player : FlappyAnticheat.INSTANCE.getDataManager().getPlayers().values()) {
                    for(FlappyCheck check : ((FlappyPlayer) player).getChecks()) {
                        check.setVl(0);
                    }
                }

                // Loop through players with alerts enabled
                //TODO: Improve alert toggle
                String msg = MessageUtil.translate(Config.PREFIX + Config.Messages.RESET_ALL_VIOLATIONS);
                for (final UUID uuid : FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (!player.hasPermission("flappyac.alerts")) continue;
                    player.sendMessage(msg);
                }

                //TODO: send to console?
            }
        }, delay, delay);
    }
}
