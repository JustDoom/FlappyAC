package com.justdoom.flappyanticheat.checks.player.timer;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.data.PlayerData;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimerA extends Check {

    private Map<UUID, Long> lastTime = new HashMap<>();
    private Map<UUID, Double> balance = new HashMap<>();

    public TimerA() {
        super("Timer", "A", true);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (e.getPacketId() == PacketType.Play.Client.POSITION || e.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            long time = System.currentTimeMillis();
            long lastTime = this.lastTime.getOrDefault(uuid, 0L) != 0 ? this.lastTime.getOrDefault(uuid, 0L) : time - 50;
            this.lastTime.put(uuid, time);

            long rate = time - lastTime;

            double balanceOrDefault = this.balance.getOrDefault(uuid, 0.0);
            this.balance.put(uuid, balanceOrDefault += 50.0);
            balanceOrDefault = this.balance.getOrDefault(uuid, 0.0);
            this.balance.put(uuid, balanceOrDefault -= rate);

            if(this.balance.getOrDefault(uuid, 0.0) >= 10.0){
                fail("balance=" + balance, player);
                this.balance.put(uuid, 0.0);
            }
        } else if (e.getPacketId() == PacketType.Play.Server.POSITION){
            double balanceOrDefault = this.balance.getOrDefault(uuid, 0.0);
            this.balance.put(uuid, balanceOrDefault -= 50.0);
        }
    }
}
