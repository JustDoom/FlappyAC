package com.justdoom.flappyanticheat.listener;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.PlayerData;
import com.justdoom.flappyanticheat.utils.BrandMessageUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class PlayerConnectionListener implements Listener {

    public final FlappyAnticheat flappyAnticheat;

    public PlayerConnectionListener(FlappyAnticheat flappyAnticheat) {
        this.flappyAnticheat = flappyAnticheat;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        flappyAnticheat.dataManager.addPlayer(player.getUniqueId());

        //ClientVersion clientVersion = PacketEvents.get().getPlayerUtils().getClientVersion(player);
        //WrappedGameProfile e = PacketEvents.get().getPlayerUtils().getGameProfile(player);
        //event.getPlayer().sendMessage(String.valueOf(e.getName()));

        BrandMessageUtil.addChannel(player, "minecraft:brand");

        /**for(Player p: Bukkit.getOnlinePlayers()){
            if(p.hasPermission("flappyanticheat.alerts")){
                if(!FlappyAnticheat.getInstance().dataManager.alertsDisabled.contains(p))
                    p.sendMessage(String.valueOf(clientVersion));
            }
        }

        if(FlappyAnticheat.getInstance().config.configuration.getBoolean("messages.flag-to-console")) {
            Bukkit.getConsoleSender().sendMessage(String.valueOf(clientVersion));
        }**/
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        flappyAnticheat.violationHandler.clearViolations(player);
        flappyAnticheat.dataManager.removePlayer(player.getUniqueId());
        flappyAnticheat.dataManager.disabledAlertsRemove(player);
    }
}