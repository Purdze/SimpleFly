package dev.purdze.simplefly.commands;

import dev.purdze.simplefly.SimpleFly;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FlyReloadCommand implements CommandExecutor {
    
    private final SimpleFly plugin;
    
    public FlyReloadCommand() {
        this.plugin = SimpleFly.getInstance();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("simplefly.reload")) {
            sender.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.no-permission"));
            return true;
        }

        plugin.reloadConfig();
        sender.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.config-reloaded"));
        return true;
    }
} 