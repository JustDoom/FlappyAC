package com.justdoom.flappyanticheat.checks.movement.noslow;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "NoSlow", checkType = "A", experimental = false, description = "Checks if the player is sprinting at an impossible time")
public class NoSlowA extends Check {

    public NoSlowA(FlappyPlayer player) {
        super(player);
    }

    @Override
    public void handle(Packet packet) {

        if(!packet.isPosition() || isExempt(ExemptType.GAMEMODE, ExemptType.TPS, ExemptType.JOINED)) return;

        if(data.getPlayer().isSprinting() && data.getPlayer().isBlocking()){
            fail("Sprinting: " + data.getPlayer().isSprinting() + "  Blocking: " + data.getPlayer().isBlocking(), false);
        }
    }
}