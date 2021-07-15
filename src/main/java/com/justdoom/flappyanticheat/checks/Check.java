package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Check {

    public String check, checkType;
    public boolean experimental;

    public FlappyPlayer player;

    public CheckInfo checkData;

    public Check(FlappyPlayer player) {
        this.player = player;

        this.checkData = getClass().getAnnotation(CheckInfo.class);

        this.check = checkData.check();
        this.checkType = checkData.checkType();
        this.experimental = checkData.experimental();
    }

    public abstract void handle(final Packet packet);

    public void fail(){
        System.out.println("You are hacking " + check + checkType);
        player.getPlayer().sendMessage("You are hacking " + check + checkType);
    }
}