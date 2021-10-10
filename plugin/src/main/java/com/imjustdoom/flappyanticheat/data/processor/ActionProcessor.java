package com.imjustdoom.flappyanticheat.data.processor;

import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import lombok.Getter;
import org.bukkit.block.Block;

@Getter
public class ActionProcessor {

    private FlappyPlayer player;

    private Block lastBlockPlaced, blockPlacedAgainst;
    private boolean open;
    private int slot, lastSlot;

    public ActionProcessor(FlappyPlayer player){
        this.player = player;
    }

    public void handleBlockPlace(Block blockPlaced, Block blockAgainst){
        this.lastBlockPlaced = blockPlaced;
        this.blockPlacedAgainst = blockAgainst;
    }

    public void handleInventory(boolean isOpen) {
        this.open = isOpen;
    }

    public void handleSlots(int slot) {
        this.lastSlot = this.slot;
        this.slot = slot;
    }
}