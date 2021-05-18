package com.justdoom.flappyanticheat.checks.player.badpackets;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckData;
import com.justdoom.flappyanticheat.data.PlayerData;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;

public class BadPacketsA extends Check {

    public BadPacketsA(){
        super("BadPackets", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            float pitch = event.getPlayer().getLocation().getPitch();
            if(Math.abs(pitch) > 90F || Math.abs(pitch) < -90F){
                String suspectedHack = "Old/Bad KillAura (This cannot false)";
                fail("&7pitch=&2" + pitch + " &7Suspected Hack: &2" + suspectedHack, event.getPlayer());
            }
        }
    }
}