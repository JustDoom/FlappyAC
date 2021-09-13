package com.justdoom.flappyanticheat.checks.player.badpackets;

import com.justdoom.flappyanticheat.checks.Check;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;

public class BadPacketsA extends Check {

    public BadPacketsA(){
        super("BadPackets", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if(PacketEvents.get().getPlayerUtils().isGeyserPlayer(event.getPlayer().getPlayer())) return;

        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            float pitch = event.getPlayer().getLocation().getPitch();
            if(Math.abs(pitch) > 90F || Math.abs(pitch) < -90F){
                String suspectedHack = "Old/Bad KillAura (This cannot false)";
                fail("&7pitch=&2" + pitch + " &7Suspected Hack: &2" + suspectedHack, event.getPlayer());
            }
        }
    }
}