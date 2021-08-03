package com.justdoom.flappyanticheat.checks.combat.criticals;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import com.justdoom.flappyanticheat.util.PlayerUtil;

@CheckInfo(check = "Criticals", checkType = "A", experimental = false, description = "Checks if the player says it's on the ground but isn't")
public class CrititcalsA extends Check {

    private int buffer = 0;

    public CrititcalsA(FlappyPlayer player) {
        super(player);
    }

    @Override
    public void handle(Packet packet) {

        if(!packet.isPosition()
                && !packet.isLook()
                && !packet.isPositionLook()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)) return;

        double groundY = 0.015625;
        boolean client = player.getPositionProcessor().isOnGround(), server = player.getPositionProcessor().getY() % groundY < 0.0001;;

        if (!client && server && !PlayerUtil.isOnClimbable(player.getPlayer())) {
            if (++buffer > 1) {

                fail("");
            }
        } else if (buffer > 0) buffer-=0.5;
    }
}