package com.justdoom.flappyanticheat.checks.movement.nofall;

import com.justdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import com.justdoom.flappyanticheat.util.PlayerUtil;

@CheckInfo(check = "NoFall", checkType = "A", experimental = false, description = "Checks if the player says it's on the ground but isn't")
public class NoFallA extends Check {

    public NoFallA(FlappyPlayer player) {
        super(player);
    }

    @Override
    public void handle(Packet packet) {

        if(!packet.isPosition()
                && !packet.isLook()
                && !packet.isPositionLook()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS, ExemptType.JOINED,
                ExemptType.PISTON, ExemptType.SHULKER, ExemptType.VEHICLE)) return;

        double groundY = 0.015625;
        boolean client = data.getPositionProcessor().isOnGround(), server = data.getPositionProcessor().getY() % groundY < 0.0001;;

        // Check if the client says its on the ground but the server says it isn't
        if (client && !server && !PlayerUtil.isOnClimbable(data.getPlayer())) {
            if (++buffer > 1) {
                fail("No Info", false);
            }
        } else if (buffer > 0) buffer-=0.5;
    }
}