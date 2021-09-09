package com.imjustdoom.flappyanticheat.commands;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
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

                    break;

                case "violations":

                    break;

                case "alerts":

                    break;

                case "profile":

                    break;
            }







            // OLD --------------------------------------------------------------------------
            if(args.length == 0){
                sender.sendMessage(Color.translate(FlappyAnticheat.INSTANCE.getConfigFile().node("prefix") + "&7Please do \"/flappyac help\" for the help command"));

                //Reload
            } else if (args[0].equalsIgnoreCase("reload")) {
                // TODO: reload config
                FlappyAnticheat.INSTANCE.getConfigFile();
                sender.sendMessage(Color.translate(FlappyAnticheat.INSTANCE.getConfigFile().node("prefix") + FlappyAnticheat.INSTANCE.getConfigFile().node("messages.reload")));

                //Reset Violations
            } else if (args[0].equalsIgnoreCase("resetviolations")) {
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
            } else if (args[0].equalsIgnoreCase("alerts")) {
                FlappyAnticheat.INSTANCE.getAlertManager().toggleAlerts(player);

                //Player profile
            } else if (args[0].equalsIgnoreCase("profile")){
                if(args.length > 1){
                    Player targetPlayer = Bukkit.getPlayer(args[1]);
                    //String clientBrand =
                }
            }
        }
        return false;
    }
}