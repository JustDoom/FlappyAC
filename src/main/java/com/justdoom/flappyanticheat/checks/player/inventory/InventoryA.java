package com.justdoom.flappyanticheat.checks.player.inventory;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckData;
import com.justdoom.flappyanticheat.data.PlayerData;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;

@CheckData(name = "Inventory", type = "A")
public class InventoryA extends Check {

    public InventoryA(){
        super("Inventory", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {
            //WrappedPacketIn packet = new WrappedPacketInFlying(e.getNMSPacket());
        }
    }
}
