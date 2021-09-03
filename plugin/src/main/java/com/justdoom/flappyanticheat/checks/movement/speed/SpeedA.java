package com.justdoom.flappyanticheat.checks.movement.speed;

import com.justdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Speed", checkType = "A", experimental = false, description = "Fly")
public class SpeedA extends Check{

    public SpeedA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        if (packet.isPosition()) {
            if (!data.getPositionProcessor().isOnGround() && !data.getPositionProcessor().isLastOnGround()) {

                //stole the if Y thing is same as last from hades. it seems to prevent some niche false positives.
                if (data.getPlayer().isFlying() || (data.getPositionProcessor().getY() == data.getPositionProcessor().getLastY())
                        /**|| isExempt(ExemptType.WEB, ExemptType.TELEPORT)**/) return;

                //0.029 is probably not entirely accurate, but thats the closest i got from debugging
                //while in the air the deltaXZ should reduce by 0.91x each run. the 0.0259 is whats leftover from debugging
                double prediction = data.getPositionProcessor().getLastDeltaXZ() * 0.91f + 0.026;
                double accuracy = (data.getPositionProcessor().getDeltaXZ() - prediction);

                //i could try to go for higher accuracy, but knowing me id mess something up and need a buffer. having no
                //buffer is better than accuracy for this type of check imo.
                if (accuracy > 0.001 && data.getPositionProcessor().getDeltaXZ() > 0.1) {
                    //if the player isnt taking knockback and doesnt meet our accuracy, then flag
                    if (!data.getVelocityProcessor().isTakingVelocity()) {
                        fail("exp=" + prediction + " got=" + data.getPositionProcessor().getDeltaXZ(), true);
                        //if the player is taking knockback, we have to account that into it. when in the air you only
                        //take the knockback, it resets all momentum. so compare our velocityXZ to our deltaXZ. from my
                        //testing it can be off by about 0.04, so check if the margin is greater than that.
                    } else if (Math.abs(data.getVelocityProcessor().getVelocityXZ() -
                            data.getPositionProcessor().getDeltaXZ()) > 0.04) {
                        fail("exp=" + prediction + " got=" + data.getPositionProcessor().getDeltaXZ() + " vel=" +
                                data.getVelocityProcessor().getVelocityXZ(), true);
                    }
                }
            }
        }
    }
}