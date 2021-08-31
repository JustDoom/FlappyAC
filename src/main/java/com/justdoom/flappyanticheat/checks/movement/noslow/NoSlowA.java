package com.justdoom.flappyanticheat.checks.movement.noslow;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.utils.player.ClientVersion;

@CheckInfo(check = "NoSlow", checkType = "A", experimental = false, description = "Checks if the player is sprinting at an impossible time (1.8 only)")
public class NoSlowA extends Check {

    public NoSlowA(FlappyPlayer player) {
        super(player);
    }

    @Override
    public void handle(Packet packet) {

        if(!packet.isPosition() && !packet.isPositionLook()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS, ExemptType.JOINED)
                && data.getClientVersion().isNewerThan(ClientVersion.v_1_8)) return;

        if(data.getPlayer().isSprinting() && (data.getPlayer().isBlocking() || data.getPlayer().isSneaking())){
            fail("Sprinting: " + data.getPlayer().isSprinting() + "  Blocking: "
                    + data.getPlayer().isBlocking() + "  Sneaking: " + data.getPlayer().isSneaking(), false);
        }
    }
}