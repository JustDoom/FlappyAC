package com.imjustdoom.flappyanticheat.checks.movement.fly;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Fly", checkType = "A", experimental = false, description = "Floating and players Y staying the same", type = CheckType.MOVEMENT)
public class FlyA extends Check {

    private double stableY;

    public FlyA(FlappyPlayer player) {
        super(player);
    }

    public void handle(final Packet packet) {

        // Check if the packet is not a position packet
        // and if exempts are true, if true return
        if (!packet.isPosition() || isExempt(ExemptType.TPS, ExemptType.GAMEMODE, ExemptType.VEHICLE, ExemptType.FLYING, ExemptType.PISTON)) return;

        // Checks if the Y says the same while in the air
        if (data.getFlyingProcessor().getY() == data.getFlyingProcessor().getLastY()
                && data.getFlyingProcessor().isInAir()) {
            this.stableY++;
        } else {
            this.stableY = 0.0D;
        }

        if (this.stableY > 2.0D)
            fail("No Info", true);
    }
}