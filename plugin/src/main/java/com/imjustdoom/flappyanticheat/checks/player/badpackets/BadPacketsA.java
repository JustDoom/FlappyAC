package com.imjustdoom.flappyanticheat.checks.player.badpackets;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "BadPackets", checkType = "A", experimental = false, description = "Checks for impossible pitch", type = CheckType.PLAYER)
public class BadPacketsA extends Check {

    public BadPacketsA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {

        // Check if the packet is not a position or position look packet
        // and if exempts are true, if true return
        if(!packet.isPosition() || !packet.isPositionLook()) return;

        // Check if the pitch is above or below 90F which is impossible
        float pitch = data.getFlyingProcessor().getPitch();
        if(Math.abs(pitch) > 90F){
            fail("&7pitch=&2" + pitch, false);
        }
    }
}
