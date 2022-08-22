package com.justdoom.flappyanticheat.command.subcommand;

import com.imjustdoom.cmdinstruction.SubCommand;
import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetViolationsCmd extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            if (Bukkit.getPlayerExact(args[1]) != null) {
                FlappyAnticheat.getInstance().violationHandler.clearViolations(Bukkit.getPlayerExact(args[1]));
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("flappyanticheat.alerts")) {
                        p.sendMessage(Color.translate(FlappyAnticheat.getInstance().config.configuration.getString("prefix") + FlappyAnticheat.getInstance().config.configuration.getString("messages.violation-reset.player").replace("{player}", args[1])));
                    }
                }
                if (FlappyAnticheat.getInstance().config.configuration.getBoolean("messages.flag-to-console")) {
                    Bukkit.getConsoleSender().sendMessage(Color.translate(FlappyAnticheat.getInstance().config.configuration.getString("prefix") + FlappyAnticheat.getInstance().config.configuration.getString("messages.violation-reset.player").replace("{player}", args[1])));
                }
            } else {
                sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().config.configuration.getString("prefix") + FlappyAnticheat.getInstance().config.configuration.getString("messages.violation-remove-invalid-player")));
            }
        } else {
            FlappyAnticheat.getInstance().violationHandler.clearAllViolations();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("flappyanticheat.alerts")) {
                    p.sendMessage(Color.translate(FlappyAnticheat.getInstance().config.configuration.getString("prefix") + FlappyAnticheat.getInstance().config.configuration.getString("messages.violation-reset.all")));
                }
            }
            if (FlappyAnticheat.getInstance().config.configuration.getBoolean("messages.flag-to-console")) {
                Bukkit.getConsoleSender().sendMessage(Color.translate(FlappyAnticheat.getInstance().config.configuration.getString("prefix") + FlappyAnticheat.getInstance().config.configuration.getString("messages.violation-reset.all")));
            }
        }
    }
}
