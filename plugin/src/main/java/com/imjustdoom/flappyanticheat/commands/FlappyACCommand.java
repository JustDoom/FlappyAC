package com.imjustdoom.flappyanticheat.commands;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class FlappyACCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("flappyanticheat") || command.getName().equalsIgnoreCase("flappyac")){

            if(args.length == 0) sender.sendMessage("FlappyAC {version}");

            switch (args[0].toUpperCase()) {
                case "reload":
                    Config.load();
                    sender.sendMessage(Color.translate(Config.Messages.RELOAD));
                    break;

                case "violations":

                    break;

                case "alerts":
                    FlappyAnticheat.INSTANCE.getAlertManager().toggleAlerts((Player) sender);
                    if(FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts().contains((Player) sender))
                        sender.sendMessage(Color.translate(Config.Alerts.TOGGLE_ALERTS_OFF));
                    else
                        sender.sendMessage(Color.translate(Config.Alerts.TOGGLE_ALERTS_ON));

                    break;

                case "profile":

                    break;
            }







            // OLD --------------------------------------------------------------------------
            /**if (args[0].equalsIgnoreCase("resetviolations")) {
                if(args.length >= 2){
                    if(Bukkit.getPlayerExact(args[1]) != null) {
                        FlappyAnticheat.INSTANCE.violationHandler.clearViolations(Bukkit.getPlayerExact(args[1]));
                        for(Player p: Bukkit.getOnlinePlayers()){
                            if(p.hasPermission("flappyanticheat.alerts")){
                                p.sendMessage(Color.translate(FlappyAnticheat.INSTANCE.getConfigFile().node("prefix") + FlappyAnticheat.INSTANCE.getConfigFile().node("messages.violation-reset.player").replace("{player}", args[1])));
                            }
                        }
                        if(FlappyAnticheat.getInstance().config.configuration.getBoolean("messages.flag-to-console")) {
                            Bukkit.getConsoleSender().sendMessage(Color.translate(FlappyAnticheat.INSTANCE.getConfigFile().node("prefix") + FlappyAnticheat.INSTANCE.getConfigFile().node("messages.violation-reset.player").replace("{player}", args[1])));
                        }
                    } else {
                        sender.sendMessage(Color.translate(FlappyAnticheat.INSTANCE.getConfigFile().node("prefix") + FlappyAnticheat.INSTANCE.getConfigFile().node("messages.violation-remove-invalid-player")));
                    }
                } else {
                    FlappyAnticheat.getInstance().violationHandler.clearAllViolations();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.hasPermission("flappyanticheat.alerts")) {
                            p.sendMessage(Color.translate(FlappyAnticheat.INSTANCE.getConfigFile().node("prefix") + FlappyAnticheat.INSTANCE.getConfigFile().node("messages.violation-reset.all")));
                        }
                    }
                    if(FlappyAnticheat.INSTANCE.getConfigFile().node("messages.flag-to-console")) {
                        Bukkit.getConsoleSender().sendMessage(Color.translate(FlappyAnticheat.INSTANCE.getConfigFile().node("prefix") + FlappyAnticheat.INSTANCE.getConfigFile().node("messages.violation-reset.all")));
                    }
                }

                //Alerts toggle
            }**/
        }
        return false;
    }
}