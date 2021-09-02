package com.justdoom.flappyanticheat.checks.player.timer;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Timer", checkType = "A", experimental = false, description = "Timer")
public class TimerA extends Check {

    public TimerA(FlappyPlayer data) {
        super(data);
    }

    private long lastTime ;
    private double balance;

    @Override
    public void handle(Packet packet) {
        if(isExempt(ExemptType.JOINED, ExemptType.GAMEMODE, ExemptType.TPS)) return;
        if (packet.isPosition() || packet.isPositionLook()) {

            long time = System.currentTimeMillis();
            long lastTime = this.lastTime != 0 ? this.lastTime : time - 50;
            this.lastTime = time;

            long rate = time - lastTime;

            balance += 50.0;
            balance -= rate;

            if(balance >= 10.0){
                fail("balance=" + balance, false);
                balance = 0.0;
            }
        } else if (packet.isServerPosition()){
            balance -= 50.0;
        }
    }
}
