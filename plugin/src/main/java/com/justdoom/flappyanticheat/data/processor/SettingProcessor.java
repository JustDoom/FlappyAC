package com.justdoom.flappyanticheat.data.processor;

import com.justdoom.flappyanticheat.data.FlappyPlayer;
import io.github.retrooper.packetevents.packetwrappers.play.in.settings.WrappedPacketInSettings;
import lombok.Getter;

@Getter
public class SettingProcessor {

    private final FlappyPlayer player;

    private int lastSkin = -1, skin = -1;

    public SettingProcessor(FlappyPlayer player){
        this.player = player;
    }

    public void handle(final WrappedPacketInSettings wrapper){
        lastSkin = skin;

        skin = wrapper.getDisplaySkinPartsMask();
    }
}