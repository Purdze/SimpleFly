package dev.purdze.simplefly.commands;

import dev.purdze.simplefly.SimpleFly;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlyDurationCommand implements CommandExecutor {
    
    private final SimpleFly plugin;
    private final Map<UUID, BukkitTask> flyTasks;
    private final Pattern durationPattern = Pattern.compile("(\\d+)([smhd])");
    
    public FlyDurationCommand() {
        this.plugin = SimpleFly.getInstance();
        this.flyTasks = new HashMap<>();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfig().getString("messages.prefix") + "§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("simplefly.duration")) {
            player.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.no-permission"));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.duration-format"));
            return true;
        }

        Matcher matcher = durationPattern.matcher(args[0]);
        if (!matcher.matches()) {
            player.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.duration-format"));
            return true;
        }

        int amount = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2);
        long ticks = convertToTicks(amount, unit);

        // Cancel existing task if there is one
        if (flyTasks.containsKey(player.getUniqueId())) {
            flyTasks.get(player.getUniqueId()).cancel();
        }

        // Enable flight
        player.setAllowFlight(true);
        player.sendMessage(plugin.getConfig().getString("messages.prefix") + "§aFlight enabled for " + amount + unit);

        // Schedule disable
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.setAllowFlight(false);
            player.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.flight-disabled"));
            flyTasks.remove(player.getUniqueId());
        }, ticks);

        flyTasks.put(player.getUniqueId(), task);
        return true;
    }

    private long convertToTicks(int amount, String unit) {
        switch (unit.toLowerCase()) {
            case "s": return amount * 20L;
            case "m": return amount * 20L * 60L;
            case "h": return amount * 20L * 60L * 60L;
            case "d": return amount * 20L * 60L * 60L * 24L;
            default: return amount * 20L;
        }
    }
} 