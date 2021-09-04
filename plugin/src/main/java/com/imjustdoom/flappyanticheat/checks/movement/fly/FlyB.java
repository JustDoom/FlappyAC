package com.imjustdoom.flappyanticheat.checks.movement.fly;

import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.data.processor.PositionProcessor;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Fly",checkType = "B",experimental = false,description = "checks for mid air jump")
public class FlyB extends Check {

    private int verbose;

    public FlyB(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isFlying()) {

            final PositionProcessor positionProcessor = data.getPositionProcessor();

            final double deltaY = positionProcessor.getDeltaY();
            final double lastDeltaY = positionProcessor.getLastDeltaY();

            if (isExempt(ExemptType.TPS, ExemptType.GAMEMODE, ExemptType.VEHICLE,ExemptType.LIQUID)) return;

            final int airTicks = positionProcessor.getAirTicks();

            if(airTicks > 6 && deltaY > lastDeltaY) {
                if(++verbose > 3) {
                    fail("dY=" + deltaY +" lDY=" + lastDeltaY,true);
                }
            } else verbose *= 0.9;
        }
    }
}