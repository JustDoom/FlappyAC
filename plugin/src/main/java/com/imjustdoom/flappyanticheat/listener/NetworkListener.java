package com.imjustdoom.flappyanticheat.listener;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;
import com.imjustdoom.flappyanticheat.util.MessageUtil;
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

        // Check if the player is a bedrock player, if so don't add them to the anticheat
        if(PacketEvents.get().getPlayerUtils().isGeyserPlayer(event.getPlayer())) return;

        FlappyAnticheat.INSTANCE.getAlertManager().toggleAlerts(event.getPlayer());
        FlappyAnticheat.INSTANCE.getDataManager().addPlayer(event.getPlayer());

        Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () ->
                ClientBrandListener.addChannel(event.getPlayer(), "minecraft:brand"));
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
                Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () -> {
                    event.getPlayer().kickPlayer("No");
                    MessageUtil.toConsole("No");
                });
                return;
            }
        }

        // Handle the packet
        FlappyAnticheat.INSTANCE.getPacketExecutor().execute(() -> FlappyAnticheat.INSTANCE.getReceivingPacketProcessor()
                .handle(player, new Packet(Packet.Direction.RECEIVE, event.getNMSPacket(), event.getPacketId())));
    }

    // TODO: onReceivePackets
}