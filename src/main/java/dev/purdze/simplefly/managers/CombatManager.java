package dev.purdze.simplefly.managers;

import dev.purdze.simplefly.SimpleFly;
import dev.purdze.simplefly.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatManager {
    private final SimpleFly plugin;
    private final Map<UUID, BukkitTask> combatTags;

    public CombatManager(SimpleFly plugin) {
        this.plugin = plugin;
        this.combatTags = new HashMap<>();
    }

    public void tagPlayer(Player player) {
        if (!plugin.getSettingsManager().isDisableOnCombat()) return;
        if (player.hasPermission("simplefly.bypass.combat")) return;

        if (combatTags.containsKey(player.getUniqueId())) {
            combatTags.get(player.getUniqueId()).cancel();
        }

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            int duration = plugin.getSettingsManager().getCombatTagDuration();
            player.sendMessage(Messages.getCombatDisabled(duration));
        }

        int duration = plugin.getSettingsManager().getCombatTagDuration();
        BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            combatTags.remove(player.getUniqueId());
            player.sendMessage(Messages.getCombatEnded());
        }, duration * 20L);

        combatTags.put(player.getUniqueId(), task);
    }

    public boolean isInCombat(Player player) {
        return combatTags.containsKey(player.getUniqueId());
    }
} 