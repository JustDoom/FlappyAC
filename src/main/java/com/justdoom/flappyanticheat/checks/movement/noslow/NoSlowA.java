package com.justdoom.flappyanticheat.checks.movement.noslow;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckData;
import com.justdoom.flappyanticheat.data.PlayerData;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import io.github.retrooper.packetevents.packetwrappers.play.out.helditemslot.WrappedPacketOutHeldItemSlot;

@CheckData(name = "Fly", type = "A", experimental = false)
public class NoSlowA extends Check {

    private int buffer;

    public NoSlowA(){
        super("NoSlow", "A", false);
    }

    //doesnt work

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());
            WrappedPacketInHeldItemSlot packet2 = new WrappedPacketInHeldItemSlot(event.getNMSPacket());
            if(event.getPlayer().isBlocking() && event.getPlayer().isSprinting()){
                if (++buffer > 10) {
                    final int slot = packet2.getCurrentSelectedSlot() == 8 ? 1 : 8;
                    final WrappedPacketOutHeldItemSlot wrapper = new WrappedPacketOutHeldItemSlot(slot);
                    PacketEvents.get().getPlayerUtils().sendPacket(event.getPlayer(), wrapper);
                    buffer /= 2;
                }
            } else {
                buffer -= buffer > 0 ? 0.25 : 0;
            }
        }
    }
}
