package com.justdoom.flappyanticheat.data;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckManager;
import com.justdoom.flappyanticheat.data.processor.PositionProcessor;
import com.justdoom.flappyanticheat.exempt.ExemptManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@Setter
public class FlappyPlayer {

    private Player player;

    private List<Check> checks = CheckManager.loadChecks(this);

    private PositionProcessor positionProcessor;
    private ExemptManager exemptManager;

    public FlappyPlayer(Player player){

        this.player = player;

        this.positionProcessor = new PositionProcessor(this);
        this.exemptManager = new ExemptManager(this);
    }
}