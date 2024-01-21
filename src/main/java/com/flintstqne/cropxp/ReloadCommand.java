package com.flintstqne.cropxp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final CropXP plugin;

    public ReloadCommand(CropXP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("cropxp") && args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            // Check if the sender has the required permission
            if (!sender.hasPermission("cropxp.reload")) {
                sender.sendMessage("You don't have permission to use this command.");
                return true;
            }

            // Reload the configuration
            plugin.reloadConfig();
            sender.sendMessage("CropXP configuration reloaded.");
            return true;
        }

        return false;
    }
}