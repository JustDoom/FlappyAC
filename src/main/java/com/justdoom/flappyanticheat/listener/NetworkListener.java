package com.justdoom.flappyanticheat.listener;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;
import com.justdoom.flappyanticheat.util.ClientBrandUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Bukkit;

public class NetworkListener extends PacketListenerAbstract {

    @Override
    public void onPostPlayerInject(final PostPlayerInjectEvent event) {

        FlappyAnticheat.INSTANCE.getDataManager().addPlayer(event.getPlayer());
        FlappyAnticheat.INSTANCE.getAlertManager().toggleAlerts(event.getPlayer());

        Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () ->
                ClientBrandUtil.addChannel(event.getPlayer(), "minecraft:brand"));
    }

    @Override
    public void onPacketPlayReceive(final PacketPlayReceiveEvent event){
        final FlappyPlayer player = FlappyAnticheat.INSTANCE.getDataManager().getData(event.getPlayer());

        if (player == null) return;

        // Check if someone is trying to choke the packet
        if (PacketType.Play.Client.Util.isInstanceOfFlying(event.getPacketId())) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(event.getNMSPacket());

            if (Math.abs(wrapper.getX()) > 1.0E+7
                    || Math.abs(wrapper.getY()) > 1.0E+7
                    || Math.abs(wrapper.getZ()) > 1.0E+7
                    || Math.abs(wrapper.getPitch()) > 1.0E+7
                    || Math.abs(wrapper.getYaw()) > 1.0E+7) {
                Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () -> event.getPlayer().kickPlayer("No"));
                return;
            }
        }

        // Handle the packet
        FlappyAnticheat.INSTANCE.getPacketExecutor().execute(() -> FlappyAnticheat.INSTANCE.getReceivingPacketProcessor()
                .handle(player, new Packet(Packet.Direction.RECEIVE, event.getNMSPacket(), event.getPacketId())));
    }

    // TODO: onReceivePackets
}