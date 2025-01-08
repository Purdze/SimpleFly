package dev.purdze.simplefly.commands;

import dev.purdze.simplefly.SimpleFly;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyOtherCommand implements CommandExecutor {
    
    private final SimpleFly plugin;
    
    public FlyOtherCommand() {
        this.plugin = SimpleFly.getInstance();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("simplefly.others")) {
            sender.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.no-permission"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.getConfig().getString("messages.prefix") + "§cUsage: /simplefly <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.player-not-found"));
            return true;
        }

        toggleFlight(target);
        sender.sendMessage(plugin.getConfig().getString("messages.prefix") + "§aToggled flight for " + target.getName());
        return true;
    }

    private void toggleFlight(Player player) {
        player.setAllowFlight(!player.getAllowFlight());
        String message = plugin.getConfig().getString("messages.prefix") + 
                        (player.getAllowFlight() 
                            ? plugin.getConfig().getString("messages.flight-enabled") 
                            : plugin.getConfig().getString("messages.flight-disabled"));
        player.sendMessage(message);
    }
} 