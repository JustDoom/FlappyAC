package com.imjustdoom.flappyanticheat.checks.player.badpackets;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.abilities.WrappedPacketInAbilities;

@CheckInfo(check = "BadPackets", checkType = "D", experimental = false, description = "Checks for spoofing the abilities packet", type = CheckType.PLAYER)
public class BadPacketsD extends Check {

    public BadPacketsD(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {

        final WrappedPacketInAbilities wrapper = new WrappedPacketInAbilities(packet.getRawPacket());
        if (wrapper.isFlightAllowed().orElse(false) && !data.getPlayer().getAllowFlight()) {
            fail("Spoofed Abilities Packet", false);
        }
    }
}
