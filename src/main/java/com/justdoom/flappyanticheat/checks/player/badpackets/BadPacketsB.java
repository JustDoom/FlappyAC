package com.justdoom.flappyanticheat.checks.player.badpackets;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckData;
import com.justdoom.flappyanticheat.data.PlayerData;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;

public class BadPacketsB extends Check {

    private boolean wasLastArmAnimation = false;

    public BadPacketsB(){
        super("BadPackets", "B", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY){
            if(!wasLastArmAnimation){
                fail("&7ArmAnimation=&2false", event.getPlayer());
            }
        } else if (event.getPacketId() == PacketType.Play.Client.ARM_ANIMATION){
            wasLastArmAnimation = true;
        } else if (event.getPacketId() == PacketType.Play.Client.FLYING){
            wasLastArmAnimation = false;
        }
    }
}