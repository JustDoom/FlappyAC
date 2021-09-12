package com.imjustdoom.flappyanticheat.data.processor;

import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import lombok.Getter;
import org.bukkit.block.Block;

@Getter
public class ActionProcessor {

    private FlappyPlayer player;

    private Block lastBlockPlaced, blockPlacedAgainst;
    private boolean open;

    public ActionProcessor(FlappyPlayer player){
        this.player = player;
    }

    public void handleBlockPlace(Block blockPlaced, Block blockAgainst){
        lastBlockPlaced = blockPlaced;
        blockPlacedAgainst = blockAgainst;
    }

    public void handleInventory(boolean isOpen) {
        this.open = isOpen;
    }
}