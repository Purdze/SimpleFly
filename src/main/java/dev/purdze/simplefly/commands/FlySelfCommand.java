package dev.purdze.simplefly.commands;

import dev.purdze.simplefly.SimpleFly;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlySelfCommand implements CommandExecutor {
    
    private final SimpleFly plugin;
    
    public FlySelfCommand() {
        this.plugin = SimpleFly.getInstance();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfig().getString("messages.prefix") + "§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("simplefly.use")) {
            player.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.no-permission"));
            return true;
        }

        toggleFlight(player);
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