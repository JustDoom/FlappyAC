package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.checks.combat.reach.ReachA;
import com.justdoom.flappyanticheat.checks.movement.speed.SpeedA;
import com.justdoom.flappyanticheat.util.MessageUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.annotation.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketStatusReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlappyAnticheat extends JavaPlugin {

    public MessageUtil msgutil = new MessageUtil();

    @Override
    public void onLoad(){
        PacketEvents.create(this);
        PacketEventsSettings settings = PacketEvents.get().getSettings();
        settings.backupServerVersion(ServerVersion.v_1_8_8).checkForUpdates(true);
        PacketEvents.get().loadAsyncNewThread();
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new SpeedA(), this);
        PacketEvents.get().getEventManager().registerListener(new ReachA());

        PacketEvents.get().init(this);
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}