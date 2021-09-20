package com.imjustdoom.flappyanticheat.checks.movement.fastclimb;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "FastClimb", checkType = "A", experimental = false, description = "Checks if the player is climbing too fast", type = CheckType.MOVEMENT)
public class FastClimbA extends Check {

    public FastClimbA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isPositionLook() && !packet.isPosition()) return;

        if (data.getPositionProcessor().isOnLadder() && data.getPositionProcessor().isLastOnLadder()) {
            double aNumber = data.getPositionProcessor().getDeltaY() - data.getPositionProcessor().getLastDeltaY();
            if (data.getPositionProcessor().getDeltaY() > 0.1177 && aNumber == 0) {
                fail("deltaY: " + data.getPositionProcessor().getDeltaY(), false);
            }
        }
    }
}