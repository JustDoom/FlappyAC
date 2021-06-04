package com.justdoom.flappyanticheat.utils;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import io.github.retrooper.packetevents.PacketEvents;

public class ServerUtil {

    public static boolean lowTPS(String path){
        if(PacketEvents.get().getServerUtils().getTPS() <= FlappyAnticheat.getInstance().getConfig().getDouble(path + ".min-tps")){
            return true;
        }
        return false;
    }
}
