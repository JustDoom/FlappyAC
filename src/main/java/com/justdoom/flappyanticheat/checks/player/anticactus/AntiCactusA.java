package com.justdoom.flappyanticheat.checks.player.anticactus;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.settings.WrappedPacketInSettings;
import org.bukkit.entity.Player;

import java.util.List;

public class AntiCactusA extends Check {

    public AntiCactusA(){
        super("AntiCactus", "A", false);
    }

    private List<Boolean> damage;

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if (event.getPacketId() == PacketType.Play.Client.POSITION){
            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());
            Player player = event.getPlayer();

            String path = ("checks." + check + "." + checkType).toLowerCase();
            if(PacketEvents.get().getServerUtils().getTPS() <= FlappyAnticheat.getInstance().getConfig().getDouble(path + ".min-tps")){
                return;
            }


        }
    }
}