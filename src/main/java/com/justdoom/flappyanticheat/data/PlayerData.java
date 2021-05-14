package com.justdoom.flappyanticheat.data;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class PlayerData {

    public final UUID uuid;
    public Player player;

    public CheckManager checkManager;

    public boolean onGround,
            inLiquid, onStairSlab, onIce, onClimbable, underBlock, onSlime, nearGround;
    public int airTicks, groundTicks, iceTicks, liquidTicks, blockTicks, slimeTicks, velXTicks, velYTicks, velZTicks;
    public float flyThreshold;
    public float lastDeltaY, lastAccel;
    public long lastVelocityTaken;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);

        this.checkManager = new CheckManager(FlappyAnticheat.getInstance());
        //this.checkManager.loadChecks();
    }
}