package com.imjustdoom.flappyanticheat.checks.movement.speed;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Speed", checkType = "B", experimental = false, description = "Checks players speed in the air", type = CheckType.MOVEMENT)
public class SpeedB extends Check {

    public SpeedB(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        //TODO: Falses when landing with an elytra and turning

        if (!packet.isLook() && !packet.isPositionLook()
                || isExempt(ExemptType.JOINED)) return;

        final double deltaYaw = data.getRotationProcessor().getDeltaYaw();

        final double deltaXZ = data.getPositionProcessor().getDeltaXZ();
        final double lastDeltaXZ = data.getPositionProcessor().getLastDeltaXZ();

        final double accel = Math.abs(deltaXZ - lastDeltaXZ);
        final double squaredAccel = accel * 100;

        if(squaredAccel < 1.0E-5 && deltaXZ > 0.15 && deltaYaw > 1.5) {
            fail("deltaXZ=" + deltaXZ + " squaredAccel=" + squaredAccel, false);
        }
    }
}