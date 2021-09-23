package com.imjustdoom.flappyanticheat.data.processor;

import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import lombok.Getter;
import net.minestom.server.instance.block.Block;

@Getter
public class ActionProcessor {

    private FlappyPlayer player;

    private Block blockPlaced, blockPlacedAgainst;
    private boolean open;

    public ActionProcessor(FlappyPlayer player){
        this.player = player;
    }

    public void handleBlockPlace(Block blockPlaced, Block blockAgainst){
        this.blockPlaced = blockPlaced;
        blockPlacedAgainst = blockAgainst;
    }

    public void handleInventory(boolean isOpen) {
        this.open = isOpen;
    }
}