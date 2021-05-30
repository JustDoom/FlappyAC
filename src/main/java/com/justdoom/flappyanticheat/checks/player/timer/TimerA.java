package com.justdoom.flappyanticheat.checks.player.timer;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.data.PlayerData;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.entity.Player;

public class TimerA extends Check {

    private long lastTime;
    private double balance;

    public TimerA() {
        super("Timer", "A", true);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        Player player = e.getPlayer();
        PlayerData data = FlappyAnticheat.getInstance().dataManager.getData(player.getUniqueId());

        if (e.getPacketId() == PacketType.Play.Client.POSITION || e.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());

            String path = ("checks." + check + "." + checkType).toLowerCase();
            if (PacketEvents.get().getServerUtils().getTPS() <= FlappyAnticheat.getInstance().getConfig().getDouble(path + ".min-tps")) {
                return;
            }

            long time = System.currentTimeMillis();
            long lastTime = this.lastTime != 0 ? this.lastTime : time - 50;
            this.lastTime = time;

            long rate = time - lastTime;

            balance += 50.0;
            balance -= rate;

            if(balance >= 10.0){
                fail("balance=" + balance, player);
                balance = 0.0;
            }
        } else if (e.getPacketId() == PacketType.Play.Server.POSITION){
            balance -= 50.0;
        }
    }
}
