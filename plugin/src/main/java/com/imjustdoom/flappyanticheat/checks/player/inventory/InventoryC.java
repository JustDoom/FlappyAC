package com.imjustdoom.flappyanticheat.checks.player.inventory;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Inventory", checkType = "C", experimental = true, description = "Attacking while clicking in an inventory", type = CheckType.PLAYER)
public class InventoryC extends Check {

    public InventoryC(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {

        // Check if the packet is not a look, position look or position packet
        // and if exempts are true, if true return
        if(!packet.isLook() && !packet.isPositionLook() && !packet.isPosition()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)) return;

        // Check if the player is in an inventory and the players yaw or pitch changed while it was open
        if(data.getActionProcessor().isOpen() && (data.getRotationProcessor().getYaw() != data.getRotationProcessor().getLastYaw()
                || data.getRotationProcessor().getPitch() != data.getRotationProcessor().getLastPitch())
                && data.getPlayer().getAllowFlight()) {
            fail("No Info", false);
        }
    }
}