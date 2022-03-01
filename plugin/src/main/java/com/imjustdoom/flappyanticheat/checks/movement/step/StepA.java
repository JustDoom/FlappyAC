package com.imjustdoom.flappyanticheat.checks.movement.step;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Step", checkType = "A", experimental = false, description = "Checks for being on ground and stepping up", type = CheckType.MOVEMENT)
public class StepA extends Check {

    public StepA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if(!packet.isPosition() && !packet.isPositionLook() || isExempt(ExemptType.PISTON)) return;

        if(Math.abs(data.getFlyingProcessor().getDeltaY()) > 0.6 && data.getFlyingProcessor().isMathematicallyOnGround()
                && data.getFlyingProcessor().isLastMathematicallyOnGround()) {
            fail("deltaY: " + data.getFlyingProcessor().getDeltaY(), false);
        }
    }
}