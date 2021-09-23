package com.imjustdoom.flappyanticheat.data.processor;

import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import lombok.Getter;
import net.minestom.server.network.packet.client.play.ClientSettingsPacket;

@Getter
public class SettingProcessor {

    private final FlappyPlayer player;

    private int lastSkin = -1, skin = -1;

    public SettingProcessor(FlappyPlayer player){
        this.player = player;
    }

    public void handle(final ClientSettingsPacket wrapper){

        // Set last and current skin
        lastSkin = skin;
        skin = wrapper.displayedSkinParts;
    }
}