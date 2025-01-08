package dev.purdze.simplefly.listeners;

import dev.purdze.simplefly.SimpleFly;
import dev.purdze.simplefly.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateNotifier implements Listener {
    
    private final SimpleFly plugin;
    
    public UpdateNotifier(SimpleFly plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("simplefly.updates")) {
            String latestVersion = plugin.getUpdateChecker().getLatestVersion();
            String currentVersion = plugin.getDescription().getVersion();
            
            if (latestVersion != null && !currentVersion.equals(latestVersion)) {
                player.sendMessage(Messages.getUpdateAvailable(currentVersion, latestVersion));
            }
        }
    }
} 