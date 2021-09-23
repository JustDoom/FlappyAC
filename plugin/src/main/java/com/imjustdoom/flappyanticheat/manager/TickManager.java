package com.imjustdoom.flappyanticheat.manager;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskBuilder;
import net.minestom.server.utils.time.TimeUnit;

//Taken from https://github.com/ElevatedDev/Frequency/blob/master/src/main/java/xyz/elevated/frequency/tick/TickManager.java
//Removed stuff I didnt need though

@Getter
public class TickManager implements Runnable {

    private int ticks;
    private static Task task;

    public void start() {
        assert task == null : "TickProcessor has already been started";



        task = MinecraftServer.getSchedulerManager().buildTask(this).delay( 0, TimeUnit.SERVER_TICK).repeat(1, TimeUnit.SERVER_TICK).schedule();
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