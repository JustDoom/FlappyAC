package com.imjustdoom.flappyanticheat.data.processor;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import lombok.Getter;

@Getter
public class SettingProcessor {

    private final FlappyPlayer player;

    private int lastSkin = -1, skin = -1;

    public SettingProcessor(FlappyPlayer player){
        this.player = player;
    }

    public void handle(final WrapperPlayClientSettings wrapper){

        // Set last and current skin
        lastSkin = skin;
        skin = wrapper.getVisibleSkinSectionMask();
    }
}