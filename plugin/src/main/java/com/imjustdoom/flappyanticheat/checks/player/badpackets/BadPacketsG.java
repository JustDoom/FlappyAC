package com.imjustdoom.flappyanticheat.checks.player.badpackets;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "BadPackets", checkType = "G", experimental = false, description = "Checks for look packet without yaw/pitch changes", type = CheckType.PLAYER)
public class BadPacketsG extends Check {

    public BadPacketsG(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(!packet.isPositionLook() && !packet.isLook() || isExempt(ExemptType.JOINED, ExemptType.INSIDE_VEHICLE) || data.getActionProcessor().getUseItem() < 10) return;

        if(data.getFlyingProcessor().getDeltaPitch() == 0 && data.getFlyingProcessor().getDeltaYaw() == 0) {
            fail("No Info", false);
        }
    }
}