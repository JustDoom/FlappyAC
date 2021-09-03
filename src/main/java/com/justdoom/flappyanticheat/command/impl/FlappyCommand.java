package com.justdoom.flappyanticheat.command.impl;

import com.justdoom.flappyanticheat.FlappyAnticheat;

public class FlappyCommand {
    public FlappyAnticheat plugin = FlappyAnticheat.INSTANCE;

    public FlappyCommand() {
        this.plugin.getCommandFramework().registerCommands(this);
    }
}