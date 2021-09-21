package com.imjustdoom.flappyanticheat.checks.player.timer;

import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Timer", checkType = "A", experimental = false, description = "Timer", type = CheckType.PLAYER)
public class TimerA extends Check {

    public TimerA(FlappyPlayer data) {
        super(data);
    }

    private long lastTime ;
    private double balance;

    @Override
    public void handle(Packet packet) {

        if(isExempt(ExemptType.JOINED, ExemptType.GAMEMODE, ExemptType.TPS) || !isEnabled()) return;

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
