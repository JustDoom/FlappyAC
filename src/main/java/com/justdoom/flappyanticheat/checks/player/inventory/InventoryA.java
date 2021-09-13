package com.justdoom.flappyanticheat.checks.player.inventory;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class InventoryA extends Check {

    public InventoryA(){
        super("Inventory", "A", true);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if(event.getPacketId() != PacketType.Play.Client.WINDOW_CLICK) return;
        if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
            return;

        final Player player = event.getPlayer();
        if(player.isSprinting() || player.isSneaking()) {
            fail("No Info", player);
        }
    }
}
