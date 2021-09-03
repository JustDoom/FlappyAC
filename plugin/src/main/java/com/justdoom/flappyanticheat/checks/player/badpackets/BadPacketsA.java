package com.justdoom.flappyanticheat.checks.player.badpackets;

import com.justdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "BadPackets", checkType = "A", experimental = false, description = "Checks for impossible pitch")
public class BadPacketsA extends Check {

    public BadPacketsA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(!packet.isPosition() || !packet.isPositionLook()) return;

        // Check if the pitch is above or below 90F which is impossible
        float pitch = data.getRotationProcessor().getPitch();
        if(Math.abs(pitch) > 90F){
            fail("&7pitch=&2" + pitch, false);
        }
    }
}
