package com.imjustdoom.flappyanticheat.checks.player.badpackets;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;
import net.minestom.server.network.packet.client.play.ClientPlayerAbilitiesPacket;

@CheckInfo(check = "BadPackets", checkType = "D", experimental = false, description = "Checks for spoofing the abilities packet", type = CheckType.PLAYER)
public class BadPacketsD extends Check {

    public BadPacketsD(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {

        if(!isEnabled()) return;

        // TODO: abilities

        final ClientPlayerAbilitiesPacket wrapper = (ClientPlayerAbilitiesPacket) packet.getRawPacket();
        //if (wrapper.isFlightAllowed().orElse(false) && !data.getPlayer().getAllowFlight()) {
            //fail("Spoofed Abilities Packet", false);
        //}
    }
}
