package com.imjustdoom.flappyanticheat.checks.player.badpackets;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "BadPackets", checkType = "I", experimental = false, description = "Checks for invalid held item slot", type = CheckType.PLAYER)
public class BadPacketsI extends Check {

    public BadPacketsI(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(!packet.isSlotChange()) return;

        int slot = data.getActionProcessor().getSlot();

        if(slot > 8 || slot < 0) {
            fail("slot=" + slot, false);
        }
    }
}