package com.justdoom.flappyanticheat.checks.movement.fly;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Fly", checkType = "A", experimental = false, description = "Fly")
public class FlyA extends Check {

    private double stableY;

    public FlyA(FlappyPlayer player) {
        super(player);
    }

    public void handle(final Packet packet) {

        if (!packet.isPosition() || isExempt(ExemptType.TPS, ExemptType.GAMEMODE, ExemptType.VEHICLE)) return;

        if (data.getPositionProcessor().getY() == data.getPositionProcessor().getLastY() && data.getPositionProcessor().isInAir()) {
            this.stableY++;
        } else {
            this.stableY = 0.0D;
        }
        if (this.stableY > 2.0D)
            fail("No Info", true);
    }
}