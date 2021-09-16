package com.imjustdoom.flappyanticheat.commands;

import com.imjustdoom.flappyanticheat.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlappyACHoverClick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("flappyachoverclick")) {

            // Check if command doesnt have a player set
            if (args.length == 0) {
                sender.sendMessage("FlappyAC {version}");
                return true;
            }

            // Check if the set player is valid, if not return
            final Player p = Bukkit.getPlayer(args[0]);
            if(p == null) {
                sender.sendMessage("Invalid player");
                return true;
            }

            // Run alert click commands
            for(String commands : Config.Alerts.CLICK_COMMANDS){
                Bukkit.dispatchCommand(sender, commands.replace("{player}", args[0]));
            }
        }

        return false;
    }
}