package com.imjustdoom.flappyanticheat.checks.movement.fly;

import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Fly", checkType = "A", experimental = false, description = "Fly", type = CheckType.MOVEMENT)
public class FlyA extends Check {

    private double stableY;

    public FlyA(FlappyPlayer player) {
        super(player);
    }

    public void handle(final Packet packet) {

        if (!packet.isPosition() || isExempt(ExemptType.TPS, ExemptType.GAMEMODE, ExemptType.VEHICLE)) return;

        // Checks if the Y says the same while in the air
        if (data.getPositionProcessor().getY() == data.getPositionProcessor().getLastY() && data.getPositionProcessor().isInAir()) {
            this.stableY++;
        } else {
            this.stableY = 0.0D;
        }
        if (this.stableY > 2.0D)
            fail("No Info", true);
    }
}