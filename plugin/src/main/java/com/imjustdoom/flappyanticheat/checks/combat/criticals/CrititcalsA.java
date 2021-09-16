package com.imjustdoom.flappyanticheat.checks.combat.criticals;

import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import com.imjustdoom.flappyanticheat.util.PlayerUtil;

@CheckInfo(check = "Criticals", checkType = "A", experimental = false, description = "Checks if the player says it's falling but isn't", type = CheckType.COMBAT)
public class CrititcalsA extends Check {

    private int buffer = 0;

    public CrititcalsA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {

        // Check if the packet is not a position, look or position look packet
        // and if exempts are true, if true return
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