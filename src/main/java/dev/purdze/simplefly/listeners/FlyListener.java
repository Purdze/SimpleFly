package dev.purdze.simplefly.listeners;

import dev.purdze.simplefly.SimpleFly;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class FlyListener implements Listener {
    
    private final SimpleFly plugin;
    
    public FlyListener() {
        this.plugin = SimpleFly.getInstance();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getDataManager().loadPlayerState(player);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getDataManager().savePlayerState(player);
    }
    
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getSettingsManager().isFlightAllowedInWorld(player.getWorld()) && player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage("§cFlight has been disabled in this world!");
        }
    }
} 