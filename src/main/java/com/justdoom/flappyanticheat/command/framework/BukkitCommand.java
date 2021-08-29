package com.justdoom.flappyanticheat.command.framework;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class BukkitCommand extends Command {
    private final Plugin owningPlugin;

    private CommandExecutor executor;

    protected BukkitCompleter completer;

    protected BukkitCommand(String label, CommandExecutor executor, Plugin owner) {
        super(label);
        this.executor = executor;
        this.owningPlugin = owner;
        this.usageMessage = "";
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean success = false;
        if (!this.owningPlugin.isEnabled())
            return false;
        if (!testPermission(sender))
            return true;
        try {
            success = this.executor.onCommand(sender, this, commandLabel, args);
        } catch (Throwable ex) {
            throw new CommandException("Error while executing '" + commandLabel + "' in plugin " + this.owningPlugin
                    .getDescription().getFullName(), ex);
        }
        if (!success && this.usageMessage.length() > 0)
            for (String line : this.usageMessage.replace("<command>", commandLabel).split("\n"))
                sender.sendMessage(line);
        return success;
    }

    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws CommandException, IllegalArgumentException {
        Validate.notNull(sender, "The player is not valid.");
        Validate.notNull(args, "Invalid arguments.");
        Validate.notNull(alias, "Sub-command is not valid.");
        List<String> completions = null;
        try {
            if (this.completer != null)
                completions = this.completer.onTabComplete(sender, this, alias, args);
            if (completions == null && this.executor instanceof TabCompleter)
                completions = ((TabCompleter)this.executor).onTabComplete(sender, this, alias, args);
        } catch (Throwable ex) {
            StringBuilder message = new StringBuilder();
            message.append("Error while executing '/").append(alias).append(' ');
            for (String arg : args)
                message.append(arg).append(' ');
            message.deleteCharAt(message.length() - 1).append("' in plugin ")
                    .append(this.owningPlugin.getDescription().getFullName());
            throw new CommandException(message.toString(), ex);
        }
        if (completions == null)
            return super.tabComplete(sender, alias, args);
        return completions;
    }
}
