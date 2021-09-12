package com.imjustdoom.flappyanticheat.checks.player.inventory;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import org.bukkit.entity.Player;

@CheckInfo(check = "Inventory", checkType = "B", experimental = false, description = "Attacking while in an inventory")
public class InventoryB extends Check {

    public InventoryB(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(!packet.isUseEntity()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)) return;

        //WrappedPacketInUseEntity wrappedPacketInUseEntity = new WrappedPacketInUseEntity(packet.getRawPacket());
        //WrappedPacketInUseEntity.EntityUseAction action = wrappedPacketInUseEntity.getAction();

        if(data.getActionProcessor().isOpen()) {
            fail("No Info", false);
        }
    }
}