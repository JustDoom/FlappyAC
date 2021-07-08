package com.justdoom.flappyanticheat.checks.player.inventory;

import com.justdoom.flappyanticheat.checks.Check;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class InventoryA extends Check implements Listener {

    public InventoryA(){
        super("Inventory", "A", true);
    }

    @EventHandler
    public void onBlockPlace(InventoryMoveItemEvent event) {
        //Player player = (Player) event.getDestination().get;
        //player.sendMessage("e");
    }
}
