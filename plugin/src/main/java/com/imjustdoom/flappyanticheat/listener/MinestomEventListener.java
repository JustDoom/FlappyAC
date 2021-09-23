package com.imjustdoom.flappyanticheat.listener;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;

public class MinestomEventListener {

    public MinestomEventListener() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerBlockPlaceEvent.class, event -> {
            final FlappyPlayer player = FlappyAnticheat.INSTANCE.getDataManager().getData(event.getPlayer());

            // TODO: handle block place

            // Handle the event
            //if (player != null) player.getActionProcessor().handleBlockPlace(event.getBlockPlaced(),
                    //event.getBlockAgainst());
        });

        globalEventHandler.addListener(InventoryOpenEvent.class, event -> {
            final FlappyPlayer player = FlappyAnticheat.INSTANCE.getDataManager().getData(event.getPlayer());

            // Handle the event
            if (player != null) {
                player.getActionProcessor().handleInventory(true);
            }
        });

        globalEventHandler.addListener(InventoryCloseEvent.class, event -> {
            final FlappyPlayer player = FlappyAnticheat.INSTANCE.getDataManager().getData(event.getPlayer());

            // Handle the event
            if (player != null) {
                player.getActionProcessor().handleInventory(false);
            }
        });
    }
}