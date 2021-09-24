package com.imjustdoom.flappyanticheat.checks.combat.aim;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Aim", checkType = "A", experimental = true, description = "checks for a straight line when rotating", type = CheckType.COMBAT)
public class AimA extends Check {

    public AimA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(!packet.isLook() && !packet.isPositionLook()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS, ExemptType.JOINED)) return;


    }
}
