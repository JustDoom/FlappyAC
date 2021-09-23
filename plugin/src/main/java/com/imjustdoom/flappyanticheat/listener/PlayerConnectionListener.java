package com.imjustdoom.flappyanticheat.listener;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;

public class PlayerConnectionListener {

    public PlayerConnectionListener() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            // Check if the player is a bedrock player, if so don't add them to the anticheat

            //TODO: check if player is from geyser
            //if(PacketEvents.get().getPlayerUtils().isGeyserPlayer(event.getPlayer())) return;

            FlappyAnticheat.INSTANCE.getAlertManager().toggleAlerts(event.getPlayer());
            FlappyAnticheat.INSTANCE.getDataManager().addPlayer(event.getPlayer());

            //Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () -> ClientBrandListener.addChannel(event.getPlayer(), "minecraft:brand"));
        });

        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            FlappyAnticheat.INSTANCE.getDataManager().removePlayer(event.getPlayer());
            FlappyAnticheat.INSTANCE.getAlertManager().removeAlerts(event.getPlayer());
        });
    }
}