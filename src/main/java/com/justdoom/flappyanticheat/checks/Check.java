package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.util.concurrent.atomic.AtomicBoolean;

public class Check <E extends Event>{

    public Check(String check, String checkType, boolean experimental){

    }

    public void check(E e){

    }

    public void fail(String msg){
        System.out.println(msg);
    }

    public void sync(Runnable runnable) {
        AtomicBoolean waiting = new AtomicBoolean(true);
            Bukkit.getScheduler().runTask(FlappyAnticheat.getInstance(), () -> {
                waiting.set(false);
            });
    }
}