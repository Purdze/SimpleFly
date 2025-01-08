package dev.purdze.simplefly.listeners;

import dev.purdze.simplefly.SimpleFly;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CombatListener implements Listener {
    
    private final SimpleFly plugin;
    
    public CombatListener() {
        this.plugin = SimpleFly.getInstance();
    }
    
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event instanceof EntityDamageByEntityEvent) return; // Handle PvP separately
        
        Player victim = (Player) event.getEntity();
        
        // Ignore fall damage to prevent loop
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) return;
        
        plugin.getCombatManager().tagPlayer(victim);
    }
    
    @EventHandler
    public void onPlayerCombat(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        
        Player victim = (Player) event.getEntity();
        plugin.getCombatManager().tagPlayer(victim);
        
        // If attacker is also a player, tag them too
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            plugin.getCombatManager().tagPlayer(attacker);
        }
    }
} 