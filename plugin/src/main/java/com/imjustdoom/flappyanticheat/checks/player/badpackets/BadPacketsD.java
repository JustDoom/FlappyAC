package com.imjustdoom.flappyanticheat.checks.player.badpackets;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "BadPackets", checkType = "D", experimental = false, description = "Checks for spoofing the abilities packet", type = CheckType.PLAYER)
public class BadPacketsD extends Check {

    public BadPacketsD(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {

        if(!packet.isAbilities()) return;

        final WrapperPlayClientPlayerAbilities wrapper = new WrapperPlayClientPlayerAbilities(packet.getEvent());
        if (wrapper.isFlightAllowed().orElse(false) && !data.getPlayer().getAllowFlight()) {
            fail("No Info", false);
        }
    }
}
