package com.imjustdoom.flappyanticheat.checks.player.inventory;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Inventory", checkType = "B", experimental = false, description = "Attacking while in an inventory", type = CheckType.PLAYER)
public class InventoryB extends Check {

    public InventoryB(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {

        // Check if the packet is not a use entity packet and if exempts are true, if true return
        if(!packet.isUseEntity()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)) return;

        // Check if an inventory is open while attacking
        if(data.getActionProcessor().isOpen()) {
            fail("No Info", false);
        }
    }
}