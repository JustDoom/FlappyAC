package com.imjustdoom.flappyanticheat;

import com.github.retrooper.packetevents.PacketEvents;
import com.imjustdoom.flappyanticheat.listener.NetworkListener;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class FlappyAnticheatPlugin extends JavaPlugin {

    @Override
    public void onLoad(){
    }

    @Override
    public void onEnable() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().bStats(true);
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().getEventManager().registerListener(new NetworkListener());
        PacketEvents.getAPI().init();
        FlappyAnticheat.INSTANCE.start(this);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
        FlappyAnticheat.INSTANCE.stop(this);
    }
}
