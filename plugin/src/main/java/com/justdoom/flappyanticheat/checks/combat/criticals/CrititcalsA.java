package com.justdoom.flappyanticheat.checks.combat.criticals;

import com.justdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import com.justdoom.flappyanticheat.util.PlayerUtil;

@CheckInfo(check = "Criticals", checkType = "A", experimental = false, description = "Checks if the player says it's falling but isn't")
public class CrititcalsA extends Check {

    private int buffer = 0;

    public CrititcalsA(FlappyPlayer data) {
        super(data);
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

        // Check if the client says it's not on the ground but the server says it is
        if (!client && server && !PlayerUtil.isOnClimbable(data.getPlayer())) {
            if (++buffer > 1) {
                fail("No Info", false);
            }
        } else if (buffer > 0) buffer-=0.5;
    }
}