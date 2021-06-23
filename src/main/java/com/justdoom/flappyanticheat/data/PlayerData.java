package com.justdoom.flappyanticheat.data;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.*;

public class PlayerData {

    public final UUID uuid;
    public Player player;
    public final List<BoundingBox> pastVictimBoxes;
    public LivingEntity vicitm;
    public int ping;

    public CheckManager checkManager;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.pastVictimBoxes = new ArrayList<>();

        this.checkManager = new CheckManager(FlappyAnticheat.getInstance());
        new BukkitRunnable() {

            @Override
            public void run() {
                if(vicitm != null) {
                    if(pastVictimBoxes.size() > 20) pastVictimBoxes.clear();
                    pastVictimBoxes.add(vicitm.getBoundingBox());
                }
            }
        }.runTaskTimerAsynchronously(FlappyAnticheat.getInstance(),0L,1L);
    }
}