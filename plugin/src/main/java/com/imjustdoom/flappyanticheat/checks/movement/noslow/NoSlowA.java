package com.imjustdoom.flappyanticheat.checks.movement.noslow;

import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import com.imjustdoom.flappyanticheat.util.PlayerUtil;

@CheckInfo(check = "NoSlow", checkType = "A", experimental = false, description = "Checks if the player is sprinting at an impossible time (1.8 only)", type = CheckType.MOVEMENT)
public class NoSlowA extends Check {

    public NoSlowA(FlappyPlayer player) {
        super(player);
    }

    @Override
    public void handle(Packet packet) {

        // TODO: proper version checking

        // Check if the packet is not a position or position look packet,
        // if exempts are true and if the client version is not 1.8.X, if true return
        if(!packet.isPosition() && !packet.isPositionLook()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS, ExemptType.JOINED)
                || !data.getClientVersion().equalsIgnoreCase("1.8") || !isEnabled()) return;

        // Check if the player is blocking with the sword or sneaking and sprinting at the same time
        // This check is disabled on 1.9+ servers because you can now sprint while blocking
        if(data.getPlayer().isSprinting() && (PlayerUtil.isBlocking(data.getPlayer()) || data.getPlayer().isSneaking())){
            fail("Sprinting: " + data.getPlayer().isSprinting() + "  Blocking: "
                    + PlayerUtil.isBlocking(data.getPlayer()) + "  Sneaking: " + data.getPlayer().isSneaking(), false);
        }
    }
}