//Full credit to GladUrBad for the original check. This comes from Medusa.
//https://github.com/GladUrBad/Medusa/blob/f00848c2576e4812283e6dc2dc05e29e2ced866a/Impl/src/main/java/com/gladurbad/medusa/check/impl/player/protocol/ProtocolD.java

package com.justdoom.flappyanticheat.checks.player.badpackets;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;

@CheckInfo(check = "BadPackets", checkType = "C", experimental = false, description = "Checks for impossible pitch")
public class BadPacketsC extends Check {

    public BadPacketsC(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isSteerVehicle()) return;
        WrappedPacketInSteerVehicle wrapper = new WrappedPacketInSteerVehicle(packet.getRawPacket());

        float forwardValue = Math.abs(wrapper.getForwardValue()), sideValue = Math.abs(wrapper.getSideValue());
        boolean invalid = forwardValue > .98F || sideValue > .98F;

        if (invalid) {
            fail("", false);
        }
    }
}