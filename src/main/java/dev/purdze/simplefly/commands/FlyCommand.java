package dev.purdze.simplefly.commands;

import dev.purdze.simplefly.SimpleFly;
import dev.purdze.simplefly.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlyCommand implements CommandExecutor, TabCompleter {
    
    private final SimpleFly plugin;
    private final Map<UUID, BukkitTask> flyTasks;
    private final Pattern durationPattern = Pattern.compile("(\\d+)([smhd])");
    
    public FlyCommand(SimpleFly plugin) {
        this.plugin = plugin;
        this.flyTasks = new HashMap<>();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return handleSelfToggle(sender);
        }

        if (args[0].equalsIgnoreCase("help")) {
            return handleHelp(sender);
        }

        if (args[0].equalsIgnoreCase("reload")) {
            return handleReload(sender);
        }

        if (args[0].equalsIgnoreCase("speed")) {
            return handleSpeed(sender, args);
        }

        Matcher matcher = durationPattern.matcher(args[0]);
        if (matcher.matches()) {
            return handleDurationFlight(sender, args[0]);
        }

        return handleOtherPlayer(sender, args[0]);
    }

    private boolean handleSelfToggle(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("simplefly.use")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        // Check if player is in a disabled world
        if (!plugin.getSettingsManager().isFlightAllowedInWorld(player.getWorld())) {
            player.sendMessage("§cFlight is disabled in this world!");
            return true;
        }

        if (plugin.getCombatManager().isInCombat(player)) {
            player.sendMessage("§cYou cannot fly while in combat!");
            return true;
        }

        toggleFlight(player);
        return true;
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("simplefly.reload")) {
            sender.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.no-permission"));
            return true;
        }

        plugin.reloadConfig();
        sender.sendMessage(plugin.getConfig().getString("messages.prefix") + "§aConfiguration reloaded!");
        return true;
    }

    private boolean handleDurationFlight(CommandSender sender, String durationArg) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfig().getString("messages.prefix") + "§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("simplefly.duration")) {
            player.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.no-permission"));
            return true;
        }

        Matcher matcher = durationPattern.matcher(durationArg);
        if (matcher.matches()) {
            int amount = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            long ticks = convertToTicks(amount, unit);

            if (flyTasks.containsKey(player.getUniqueId())) {
                flyTasks.get(player.getUniqueId()).cancel();
            }

            player.setAllowFlight(true);
            player.sendMessage(plugin.getConfig().getString("messages.prefix") + "§aFlight enabled for " + amount + unit);

            BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.setAllowFlight(false);
                player.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.flight-disabled"));
                flyTasks.remove(player.getUniqueId());
            }, ticks);

            flyTasks.put(player.getUniqueId(), task);
        }

        // Check if player is in a disabled world
        if (!plugin.getSettingsManager().isFlightAllowedInWorld(player.getWorld())) {
            player.sendMessage("§cFlight is disabled in this world!");
            return true;
        }

        return true;
    }

    private boolean handleOtherPlayer(CommandSender sender, String targetName) {
        if (!sender.hasPermission("simplefly.others")) {
            sender.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.no-permission"));
            return true;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return true;
        }

        // Check if target is in a disabled world
        if (!plugin.getSettingsManager().isFlightAllowedInWorld(target.getWorld())) {
            sender.sendMessage("§cFlight is disabled in that world!");
            return true;
        }

        toggleFlight(target);
        sender.sendMessage("§aToggled flight for " + target.getName());
        return true;
    }

    private void toggleFlight(Player player) {
        player.setAllowFlight(!player.getAllowFlight());
        
        // Fix the message handling
        String prefix = plugin.getConfig().getString("messages.prefix", "§8[§bSimpleFly§8] ");
        String messageKey = player.getAllowFlight() ? "messages.flight-enabled" : "messages.flight-disabled";
        String statusMessage = plugin.getConfig().getString(messageKey, player.getAllowFlight() ? "§aFlight enabled!" : "§cFlight disabled!");
        
        player.sendMessage(prefix + statusMessage);
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

    private boolean handleHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== SimpleFly Help ===");
        sender.sendMessage("§e/simplefly §7- Toggle flight mode");
        if (sender.hasPermission("simplefly.others"))
            sender.sendMessage("§e/simplefly <player> §7- Toggle flight for another player");
        if (sender.hasPermission("simplefly.duration"))
            sender.sendMessage("§e/simplefly <time><s|m|h|d> §7- Enable flight for duration");
        if (sender.hasPermission("simplefly.speed"))
            sender.sendMessage("§e/simplefly speed <1-10> §7- Set your flight speed");
        if (sender.hasPermission("simplefly.speed.others"))
            sender.sendMessage("§e/simplefly speed <player> <1-10> §7- Set another player's flight speed");
        if (sender.hasPermission("simplefly.reload"))
            sender.sendMessage("§e/simplefly reload §7- Reload the plugin");
        sender.sendMessage("§e/simplefly help §7- Show this help message");
        return true;
    }

    private boolean handleSpeed(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Messages.getSpeedUsage());
            return true;
        }

        // Check if setting speed for another player
        Player target;
        String speedArg;
        
        if (args.length >= 3) {
            // Format: /sf speed <player> <speed>
            if (!sender.hasPermission("simplefly.speed.others")) {
                sender.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.no-permission"));
                return true;
            }
            
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.player-not-found"));
                return true;
            }
            speedArg = args[2];
        } else {
            // Format: /sf speed <speed>
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.players-only"));
                return true;
            }
            if (!sender.hasPermission("simplefly.speed")) {
                sender.sendMessage(Messages.getNoPermission());
                return true;
            }
            target = (Player) sender;
            speedArg = args[1];
        }

        try {
            int speed = Integer.parseInt(speedArg);
            if (speed < 1 || speed > 10) {
                sender.sendMessage(Messages.getInvalidSpeed());
                return true;
            }

            // Check if target is in a disabled world
            if (!plugin.getSettingsManager().isFlightAllowedInWorld(target.getWorld())) {
                sender.sendMessage(Messages.getFlightDisabledInWorld());
                return true;
            }

            target.setFlySpeed((float) speed / 10);
            
            // Message to the target
            target.sendMessage(Messages.getSpeedSet(speed));
            
            // Message to the sender if different from target
            if (sender != target) {
                sender.sendMessage(Messages.getSpeedSetOther(target.getName(), speed));
            }
            
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(Messages.getInvalidSpeed());
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // First argument completions
            if (sender.hasPermission("simplefly.help"))
                completions.add("help");
            if (sender.hasPermission("simplefly.reload"))
                completions.add("reload");
            if (sender.hasPermission("simplefly.speed"))
                completions.add("speed");
            if (sender.hasPermission("simplefly.others")) {
                // Add online player names
                Bukkit.getOnlinePlayers().forEach(player -> completions.add(player.getName()));
            }
            if (sender.hasPermission("simplefly.duration")) {
                completions.add("30s");
                completions.add("1m");
                completions.add("5m");
                completions.add("1h");
            }
            
            return filterCompletions(completions, args[0]);
        }
        
        if (args.length == 2 && args[0].equalsIgnoreCase("speed")) {
            // Return empty list to show no suggestions for speed numbers
            return new ArrayList<>();
        }
        
        return completions;
    }
    
    private List<String> filterCompletions(List<String> completions, String partial) {
        String lowercasePartial = partial.toLowerCase();
        List<String> filtered = new ArrayList<>();
        for (String str : completions) {
            if (str.toLowerCase().startsWith(lowercasePartial)) {
                filtered.add(str);
            }
        }
        return filtered;
    }
} 