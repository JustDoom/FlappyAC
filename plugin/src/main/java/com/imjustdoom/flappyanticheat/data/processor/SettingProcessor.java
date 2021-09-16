package com.imjustdoom.flappyanticheat.data.processor;

import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
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

        // Set last and current skin
        lastSkin = skin;
        skin = wrapper.getDisplaySkinPartsMask();
    }
}