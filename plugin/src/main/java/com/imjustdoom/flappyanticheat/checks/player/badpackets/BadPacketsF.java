package com.imjustdoom.flappyanticheat.checks.player.badpackets;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "BadPackets", checkType = "F", experimental = false, description = "Checks for sprinting while not moving", type = CheckType.PLAYER)
public class BadPacketsF extends Check {

    public BadPacketsF(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(!packet.isPositionLook() && !packet.isPosition()) return;

        if(data.getPlayer().isSprinting() && data.getFlyingProcessor().getDeltaX() == 0 && data.getFlyingProcessor().getDeltaZ() == 0) {
            fail("No Info", false);
        }
    }
}