package com.imjustdoom.flappyanticheat.listener;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.PlayerInventory;

public class BukkitEventListener implements Listener {

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final FlappyPlayer player = FlappyAnticheat.INSTANCE.getDataManager().getData(event.getPlayer());

        // Handle the event
        if (player != null) player.getActionProcessor().handleBlockPlace(event.getBlockPlaced(),
                event.getBlockAgainst());
    }

    @EventHandler
    public void onInventoryOpen(final InventoryOpenEvent event) {
        System.out.println(event.getPlayer().getUniqueId());
        final FlappyPlayer player = FlappyAnticheat.INSTANCE.getDataManager().getData(Bukkit.getPlayer(event.getPlayer().getUniqueId()));

        System.out.println(player.getPlayer().getDisplayName());

        // Handle the event
        if (player != null) {
            System.out.println("NOT EVEN NULLLLLLLL");
            player.getActionProcessor().handleInventory(true);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        final FlappyPlayer player = FlappyAnticheat.INSTANCE.getDataManager().getData(Bukkit.getPlayer(event.getPlayer().getUniqueId()));

        // Handle the event
        if (player != null) {
            player.getActionProcessor().handleInventory(false);
        }
    }
}