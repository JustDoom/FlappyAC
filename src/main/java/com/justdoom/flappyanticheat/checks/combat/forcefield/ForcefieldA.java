package com.justdoom.flappyanticheat.checks.combat.forcefield;

import com.justdoom.flappyanticheat.checks.Check;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

public class ForcefieldA extends Check {

    private long lastFlying; //when the last flying packet was sent
    private int buffer; //for prevent falses

    public ForcefieldA() {
        super("Forcefield", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if(event.getPacketId() == PacketType.Play.Client.FLYING ||
                event.getPacketId() == PacketType.Play.Client.POSITION_LOOK ||
                event.getPacketId() == PacketType.Play.Client.POSITION) { //packets sended every ticks

            this.lastFlying = System.currentTimeMillis();  //setting the value when a flying packet is sent

        }else if(event.getPacketId() == PacketType.Play.Client.USE_ENTITY)  {
            WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(event.getNMSPacket());
            if(wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                if((System.currentTimeMillis() - this.lastFlying) < 10L) /** this value should be around 40**/ {
                    if(++this.buffer > 5) {
                        fail("dF=" +(System.currentTimeMillis() - this.lastFlying),event.getPlayer());
                    }


                }else buffer -= buffer > 0 ? 0.25 : 0;
            }
        }
    }
}
