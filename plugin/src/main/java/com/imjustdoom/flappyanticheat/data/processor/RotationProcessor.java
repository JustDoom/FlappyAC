package com.imjustdoom.flappyanticheat.data.processor;

import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import lombok.Getter;

@Getter
public class RotationProcessor {

    private FlappyPlayer data;

    private float yaw, pitch;

    public RotationProcessor(FlappyPlayer data) {
        this.data = data;
    }

    public void handle(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
