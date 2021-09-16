package com.imjustdoom.flappyanticheat.checks.movement.nofall;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "NoFall", checkType = "B", experimental = false, description = "Checks if the players fall distance is smaller than the last fall distance", type = CheckType.MOVEMENT)
public class NoFallB extends Check {

    public NoFallB(FlappyPlayer player) {
        super(player);
    }

    @Override
    public void handle(Packet packet) {

        if (!packet.isPosition()
                && !packet.isLook()
                && !packet.isPositionLook()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS, ExemptType.JOINED,
                ExemptType.PISTON, ExemptType.SHULKER, ExemptType.VEHICLE)) return;

        int airTicks = data.getPositionProcessor().getAirTicks();
        double fallDistance = data.getPositionProcessor().getFallDistance();

        if (data.getPositionProcessor().getLastFallDistance() < data.getPositionProcessor().getLastLastFallDistance() && airTicks > 10 && !data.getPositionProcessor().isOnGround()) {
            fail("fallDistance: " + fallDistance + " lastFallDistance: " + data.getPositionProcessor().getLastFallDistance()
                    + " blocks: " + !data.getPositionProcessor().isOnGround(), false);
        }
    }
}
