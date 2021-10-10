package com.imjustdoom.flappyanticheat.checks.player.badpackets;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "BadPackets", checkType = "E", experimental = false, description = "Checks for held item slot changing to itself", type = CheckType.PLAYER)
public class BadPacketsE extends Check {

    public BadPacketsE(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(!packet.isSlotChange()) return;

        System.out.println(data.getActionProcessor().getSlot());
        System.out.println(data.getActionProcessor().getLastSlot());

        if(data.getActionProcessor().getSlot() == data.getActionProcessor().getLastSlot()) {
            fail("No Info", false);
        }
    }
}