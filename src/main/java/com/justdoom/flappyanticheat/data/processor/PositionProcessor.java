package com.justdoom.flappyanticheat.data.processor;

import com.justdoom.flappyanticheat.data.FlappyPlayer;
import lombok.Getter;

@Getter
public class PositionProcessor {

    private FlappyPlayer player;

    private boolean onGround, lastOnGround, inAir;

    private double x, y, z, lastX, lastY, lastZ;

    public PositionProcessor(FlappyPlayer player){
        this.player = player;
    }

    public void handle(double x, double y, double z, boolean onGround){
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;

        this.lastOnGround = this.onGround;

        this.x = x;
        this.y = y;
        this.z = z;

        this.onGround = onGround;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
