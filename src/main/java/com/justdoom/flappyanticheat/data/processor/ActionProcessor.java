package com.justdoom.flappyanticheat.data.processor;

import com.justdoom.flappyanticheat.data.FlappyPlayer;
import lombok.Getter;
import org.bukkit.block.Block;

@Getter
public class ActionProcessor {

    private FlappyPlayer player;

    private Block lastBlockPlaced;

    public ActionProcessor(FlappyPlayer player){
        this.player = player;
    }

    public void handleBlockPlace(Block blockPlaced){
        lastBlockPlaced = blockPlaced;
    }
}