package com.imjustdoom.flappyanticheat.checks.movement.fly;

import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.data.processor.PositionProcessor;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Fly", checkType = "B", experimental = false, description = "checks for mid air jump", type = CheckType.MOVEMENT)
public class FlyB extends Check {

    private boolean wentDown;

    public FlyB(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isPosition() || packet.isPositionLook()) {

            //both things here can false from velocity. just return on velocity
            if (isExempt(ExemptType.VELOCITY, ExemptType.PISTON, ExemptType.INSIDE_VEHICLE, ExemptType.FLYING) || !isEnabled()) return;

            boolean isExempt1 = isExempt(/**ExemptType.PLACING, ExemptType.STEPPED,**/
                    ExemptType.LIQUID, ExemptType.CLIMBABLE /**ExemptType.SLIME, ExemptType.WEB, ExemptType.TELEPORT**/);

            //check if their deltaY is less than previous, and set our wentDown boolean to true if so.
            if (!data.getPositionProcessor().isOnGround() && !data.getPlayer().isFlying() && !isExempt1) {
                if (data.getPositionProcessor().getDeltaY() < data.getPositionProcessor().getLastDeltaY())
                    wentDown = true;
            } else wentDown = false;

            //check if theyre heading back up after previous going down. dont run if theyre placing blocks (can false)
            if (data.getPositionProcessor().getDeltaY() > data.getPositionProcessor().getLastDeltaY() &&
                    wentDown && data.getPositionProcessor().getDeltaY() > data.getPositionProcessor().getLastLastDeltaY()) {
                fail("", false);
            }
        }
    }
}
