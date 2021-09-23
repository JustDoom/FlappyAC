package com.imjustdoom.flappyanticheat.listener;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;
import com.imjustdoom.flappyanticheat.util.MessageUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;

public class NetworkListener {

    public NetworkListener() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerPacketEvent.class, event -> {
            final FlappyPlayer player = FlappyAnticheat.INSTANCE.getDataManager().getData(event.getPlayer());

            if (player == null) return;

            // Check if someone is trying to choke the packet
            if (event.getPacket() instanceof ClientPlayerPositionPacket
                    || event.getPacket() instanceof ClientPlayerPositionAndRotationPacket
                    || event.getPacket() instanceof ClientPlayerRotationPacket) {
                final ClientPlayerPositionPacket wrapper = (ClientPlayerPositionPacket) event.getPacket();
                final ClientPlayerRotationPacket rotWrapper = (ClientPlayerRotationPacket) event.getPacket();

                if (Math.abs(wrapper.x) > 1.0E+7
                        || Math.abs(wrapper.y) > 1.0E+7
                        || Math.abs(wrapper.z) > 1.0E+7
                        || Math.abs(rotWrapper.pitch) > 1.0E+7
                        || Math.abs(rotWrapper.yaw) > 1.0E+7) {
                        event.getPlayer().kick("No");
                        MessageUtil.toConsole("No");
                    return;
                }
            }

            // Handle the packet
            FlappyAnticheat.INSTANCE.getPacketExecutor().execute(() -> FlappyAnticheat.INSTANCE.getReceivingPacketProcessor()
                    .handle(player, new Packet(Packet.Direction.RECEIVE, event.getPacket())));
        });
    }

    // TODO: onReceivePackets
}