package com.justdoom.flappyanticheat.checks.player.inventory;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
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
