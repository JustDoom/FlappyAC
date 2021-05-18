package com.justdoom.flappyanticheat.checks.player.inventory;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;

public class InventoryA extends Check {

    public InventoryA(){
        super("Inventory", "A", true);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {
            //WrappedPacketIn packet = new WrappedPacketInFlying(e.getNMSPacket());

            String path = ("checks." + check + "." + checkType).toLowerCase();
            if(PacketEvents.get().getServerUtils().getTPS() <= FlappyAnticheat.getInstance().getConfig().getDouble(path + ".min-tps")){
                return;
            }
        }
    }
}
