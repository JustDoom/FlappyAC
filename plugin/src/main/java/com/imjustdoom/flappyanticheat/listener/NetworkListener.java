package com.imjustdoom.flappyanticheat.listener;

import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PostPlayerInjectEvent;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.util.MessageUtil;
import io.github.retrooper.packetevents.utils.GeyserUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NetworkListener extends SimplePacketListenerAbstract {

    public NetworkListener() {
        super(PacketListenerPriority.MONITOR);
    }

    @Override
    public void onPostPlayerInject(final PostPlayerInjectEvent event) {

        final Player player = (Player) event.getPlayer();

        // Check if the player is a bedrock player, if so don't add them to the anticheat
        if(GeyserUtil.isGeyserPlayer(player.getUniqueId())) return;

        FlappyAnticheat.INSTANCE.getAlertManager().toggleAlerts(player);
        FlappyAnticheat.INSTANCE.getDataManager().addPlayer(player);

        Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () ->
                ClientBrandListener.addChannel(player, "minecraft:brand"));
    }

    @Override
    public void onPacketPlayReceive(final PacketPlayReceiveEvent event) {
        final FlappyPlayer player = FlappyAnticheat.INSTANCE.getDataManager().getData((Player) event.getPlayer());

        if (player == null) return;

        // Check if someone is trying to choke the packet
        // TODO: make sure it works with isFlying check
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            final WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);

            if (Math.abs(wrapper.getLocation().getX()) > 1.0E+7
                    || Math.abs(wrapper.getLocation().getY()) > 1.0E+7
                    || Math.abs(wrapper.getLocation().getZ()) > 1.0E+7
                    || Math.abs(wrapper.getLocation().getPitch()) > 1.0E+7
                    || Math.abs(wrapper.getLocation().getYaw()) > 1.0E+7) {
                Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () -> {
                    player.getPlayer().kickPlayer("No");
                    MessageUtil.toConsole("No");
                });
                return;
            }
        }

        // Handle the packet
        FlappyAnticheat.INSTANCE.getPacketExecutor().execute(() -> FlappyAnticheat.INSTANCE.getReceivingPacketProcessor()
                .handle(player, event));
    }

    // TODO: onReceivePackets
}