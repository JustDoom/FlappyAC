package com.imjustdoom.flappyanticheat;

import net.minestom.server.extensions.Extension;

public class FlappyAnticheatPlugin extends Extension {

    @Override
    public void initialize() {
        FlappyAnticheat.INSTANCE.start(this);
    }

    @Override
    public void terminate() {
        FlappyAnticheat.INSTANCE.stop(this);
    }
}
