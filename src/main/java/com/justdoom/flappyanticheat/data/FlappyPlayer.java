package com.justdoom.flappyanticheat.data;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.data.processor.*;
import com.justdoom.flappyanticheat.manager.CheckManager;
import com.justdoom.flappyanticheat.exempt.ExemptManager;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@Setter
public class FlappyPlayer {

    private final Player player;
    private final ClientVersion clientVersion;

    private final long joinTime = System.currentTimeMillis();
    private final List<Check> checks = CheckManager.loadChecks(this);

    private final PositionProcessor positionProcessor;
    private final RotationProcessor rotationProcessor;
    private final VelocityProcessor velocityProcessor;
    private final SettingProcessor settingProcessor;
    private final ActionProcessor actionProcessor;
    private final ExemptManager exemptManager;

    public FlappyPlayer(Player player){

        this.player = player;
        clientVersion = PacketEvents.get().getPlayerUtils().getClientVersion(player);

        this.positionProcessor = new PositionProcessor(this);
        this.rotationProcessor = new RotationProcessor(this);
        this.velocityProcessor = new VelocityProcessor(this);
        this.settingProcessor = new SettingProcessor(this);
        this.actionProcessor = new ActionProcessor(this);
        this.exemptManager = new ExemptManager(this);
    }
}