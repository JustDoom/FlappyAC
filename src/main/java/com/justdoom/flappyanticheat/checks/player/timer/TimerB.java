package com.justdoom.flappyanticheat.checks.player.timer;

import com.justdoom.flappyanticheat.checks.Check;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;

public class TimerB extends Check {

    private long lastFlyingTime = 0L;
    private long balance = 0L;

    public TimerB() {
        super("Timer", "B", true);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Play.Client.POSITION) {
            if (lastFlyingTime != 0L) {
                final long now = System.currentTimeMillis();
                balance += 50L;
                balance -= now - lastFlyingTime;
                if (balance > 0) {
                    fail("balance=" + balance, event.getPlayer());
                    balance = -50;
                }
            }
            lastFlyingTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event){
        if (event.getPacketId() == PacketType.Play.Server.POSITION) {
            balance -= 50;
        }
    }
}
