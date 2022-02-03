package com.imjustdoom.flappyanticheat;

import com.github.retrooper.packetevents.PacketEvents;
import com.imjustdoom.flappyanticheat.listener.NetworkListener;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class FlappyAnticheatPlugin extends JavaPlugin {

    @Override
    public void onLoad(){
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
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
