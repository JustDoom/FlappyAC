package com.imjustdoom.flappyanticheat.checks.player.inventory;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Inventory", checkType = "C", experimental = false, description = "Attacking while clicking in an inventory")
public class InventoryC extends Check {

    public InventoryC(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(!packet.isLook() && !packet.isPositionLook()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)) return;

        System.out.println(data.getRotationProcessor().getYaw());
        System.out.println(data.getRotationProcessor().getLastYaw());
        System.out.println(data.getRotationProcessor().getPitch());
        System.out.println(data.getRotationProcessor().getLastPitch());
        System.out.println(data.getPlayer().getAllowFlight());

        if(data.getActionProcessor().isOpen() && (data.getRotationProcessor().getYaw() != data.getRotationProcessor().getLastYaw()
                || data.getRotationProcessor().getPitch() != data.getRotationProcessor().getLastPitch())
                && data.getPlayer().getAllowFlight()) {
            fail("No Info", false);
        }
    }
}