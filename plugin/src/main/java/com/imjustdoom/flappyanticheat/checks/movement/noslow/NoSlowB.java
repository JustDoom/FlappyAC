package com.imjustdoom.flappyanticheat.checks.movement.noslow;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.utils.player.ClientVersion;

@CheckInfo(check = "NoSlow", checkType = "B", experimental = false, description = "Checks if the player is sprinting while hunger is too low", type = CheckType.MOVEMENT)
public class NoSlowB extends Check {

    public NoSlowB(FlappyPlayer player) {
        super(player);
    }

    @Override
    public void handle(Packet packet) {

        // Check if the packet is not a position or position look packet,
        // if exempts are true, if true return
        if(!packet.isPosition() && !packet.isPositionLook()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS, ExemptType.JOINED) || !isEnabled()) return;

        // Checks if player is sprinting and food level is smaller than 6
        if(data.getPlayer().isSprinting() && data.getPlayer().getFoodLevel() < 6) {
            fail("Food Level: " + data.getPlayer().getFoodLevel(), false);
        }
    }
}