package com.justdoom.flappyanticheat.commands;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FlagClickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("flappyacflagclick")){
            if(args.length == 0){
                return true;
            } else {
                for(String commands:FlappyAnticheat.getInstance().getConfig().getStringList("messages.commands")){
                    Bukkit.dispatchCommand(sender, commands.replace("{player}", args[0]));

                }
            }
        }
        return true;
    }
}
