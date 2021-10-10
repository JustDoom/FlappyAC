package com.imjustdoom.flappyanticheat.data.processor;

import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import lombok.Getter;

@Getter
public class RotationProcessor {

    private FlappyPlayer data;

    private float yaw, pitch, lastYaw, lastPitch, deltaYaw, deltaPitch;

    public RotationProcessor(FlappyPlayer data) {
        this.data = data;
    }

    public void handle(final float yaw, final float pitch) {

        // Set last and new pitch/yaw
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;

        this.yaw = yaw;
        this.pitch = pitch;

        this.deltaPitch = this.pitch - this.lastPitch;
        this.deltaYaw = this.yaw - this.lastYaw;
    }
}
