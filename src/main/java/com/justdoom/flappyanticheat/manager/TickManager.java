package com.justdoom.flappyanticheat.manager;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

//Taken from https://github.com/ElevatedDev/Frequency/blob/master/src/main/java/xyz/elevated/frequency/tick/TickManager.java
//Removed stuff I didnt need though

@Getter
public class TickManager implements Runnable {

    private int ticks;
    private static BukkitTask task;

    public void start() {
        assert task == null : "TickProcessor has already been started";

        task = Bukkit.getScheduler().runTaskTimer(FlappyAnticheat.INSTANCE.getPlugin(), this, 0L, 1L);
    }

    public void stop() {
        if(task == null) return;

        task.cancel();
        task = null;
    }

    @Override
    public void run() {
        ticks++;
    }
}