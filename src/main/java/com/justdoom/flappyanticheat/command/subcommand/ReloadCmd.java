package com.justdoom.flappyanticheat.command.subcommand;

import com.imjustdoom.cmdinstruction.SubCommand;
import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.utils.Color;
import org.bukkit.command.CommandSender;

public class ReloadCmd extends SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        FlappyAnticheat.getInstance().config.reloadConfig();
        sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().config.configuration.getString("prefix") + FlappyAnticheat.getInstance().config.configuration.getString("messages.reload")));
    }
}
