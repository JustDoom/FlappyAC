package com.imjustdoom.flappyanticheat.command.impl;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;

public class FlappyCommand {
    public FlappyAnticheat plugin = FlappyAnticheat.INSTANCE;

    public FlappyCommand() {
        this.plugin.getCommandFramework().registerCommands(this);
    }
}