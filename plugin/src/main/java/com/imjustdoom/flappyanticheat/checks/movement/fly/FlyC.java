package com.imjustdoom.flappyanticheat.checks.movement.fly;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Fly", checkType = "C", experimental = false, description = "Jumping while in the air", type = CheckType.MOVEMENT)
public class FlyC extends Check {

    private double lastAccel;

    public FlyC(FlappyPlayer player) {
        super(player);
    }

    public void handle(final Packet packet) {
        if (!packet.isPosition() && !packet.isPositionLook()
                || isExempt(ExemptType.TPS, ExemptType.GAMEMODE)) return;

        final double accel =  data.getPositionProcessor().getLastDeltaY() - data.getPositionProcessor().getDeltaY();
        final double deltaY = data.getPositionProcessor().getDeltaY();
        final int airTicks = data.getPositionProcessor().getAirTicks();

        if(airTicks > 10 && accel > 0 && deltaY > 0 && lastAccel <= 0)
            fail("Air: " + airTicks + " accel: " + accel, false);

        lastAccel = accel;
    }
}