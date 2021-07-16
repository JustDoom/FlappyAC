package com.justdoom.flappyanticheat.listener;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

public class NetworkListener extends PacketListenerAbstract {

    @Override
    public void onPacketPlayReceive(final PacketPlayReceiveEvent event){
        FlappyPlayer player = FlappyAnticheat.getInstance().getDataManager().getData(event.getPlayer());

        if(player == null) return;

        FlappyAnticheat.getInstance().getPacketExecutor().execute(() -> FlappyAnticheat.getInstance().getReceivingPacketProcessor()
                .handle(player, new Packet(event.getNMSPacket(), event.getPacketId())));
    }

    // TODO: onReceivePackets
}