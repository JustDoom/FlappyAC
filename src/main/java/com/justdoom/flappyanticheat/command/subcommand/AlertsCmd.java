package com.justdoom.flappyanticheat.command.subcommand;

import com.imjustdoom.cmdinstruction.SubCommand;
import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.utils.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertsCmd extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!FlappyAnticheat.getInstance().dataManager.alertsDisabled.contains(((Player) sender).getPlayer())){
            FlappyAnticheat.getInstance().dataManager.disabledAlertsAdd(((Player) sender).getPlayer());
            sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().config.configuration.getString("prefix") + FlappyAnticheat.getInstance().config.configuration.getString("messages.alert-toggle.disable")));
        } else {
            FlappyAnticheat.getInstance().dataManager.disabledAlertsRemove(((Player) sender).getPlayer());
            sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().config.configuration.getString("prefix") + FlappyAnticheat.getInstance().config.configuration.getString("messages.alert-toggle.enable")));
        }
    }
}
