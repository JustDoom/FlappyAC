package com.justdoom.flappyanticheat.commands;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FlappyACCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("flappyanticheat")){
            if(args.length == 0){
                sender.sendMessage("Please do /flappyac help got help");
            } else if (args[0].equalsIgnoreCase("reload")) {
                FlappyAnticheat.getInstance().reloadConfig();
                sender.sendMessage("FlappyAC config reloaded");
            }
        }
        return false;
    }
}
