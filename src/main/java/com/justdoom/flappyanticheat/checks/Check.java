package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public abstract class Check {

    public String check, checkType;
    public boolean experimental;

    public FlappyPlayer player;

    public CheckInfo checkData;

    public Check(FlappyPlayer player) {
        this.player = player;

        this.checkData = getClass().getAnnotation(CheckInfo.class);

        this.check = checkData.check();
        this.checkType = checkData.checkType();
        this.experimental = checkData.experimental();
    }

    public abstract void handle(final Packet packet);

    public void fail(String ... info){
        player.getPlayer().sendMessage("You are hacking " + check + checkType + " " + Arrays.toString(info));
    }

    public void sync(Runnable runnable) {
        AtomicBoolean waiting = new AtomicBoolean(true);
        if (FlappyAnticheat.getInstance().isEnabled()) {
            Bukkit.getScheduler().runTask(FlappyAnticheat.getInstance(), () -> {
                runnable.run();
                waiting.set(false);
            });
        }
    }
}