//Full credit to GladUrBad for the original check. This comes from Medusa.
//https://github.com/GladUrBad/Medusa/blob/f00848c2576e4812283e6dc2dc05e29e2ced866a/Impl/src/main/java/com/gladurbad/medusa/check/impl/player/protocol/ProtocolF.java

package com.justdoom.flappyanticheat.checks.player.badpackets;

import com.justdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;

@CheckInfo(check = "BadPackets", checkType = "B", experimental = false, description = "Checks for common exploit in disablers")
public class BadPacketsB extends Check {

    public BadPacketsB(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isSteerVehicle()) return;
            final WrappedPacketInSteerVehicle wrapper = new WrappedPacketInSteerVehicle(packet.getRawPacket());

            final boolean unmount = wrapper.isDismount();

            final boolean invalid = data.getPlayer().getVehicle() == null && !unmount;

            if (invalid) {
                if (++buffer > 8) {
                    fail("buffer=" + buffer, false);
                    buffer /= 2;
                }
            } else {
                buffer = 0;
            }
    }
}
