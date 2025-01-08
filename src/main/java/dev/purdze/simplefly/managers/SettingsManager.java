package dev.purdze.simplefly.managers;

import dev.purdze.simplefly.SimpleFly;
import org.bukkit.World;

import java.util.List;

public class SettingsManager {
    private final SimpleFly plugin;
    
    public SettingsManager(SimpleFly plugin) {
        this.plugin = plugin;
    }
    
    public int getDefaultSpeed() {
        return plugin.getConfig().getInt("settings.default-speed", 1);
    }
    
    public boolean isDisableOnCombat() {
        return plugin.getConfig().getBoolean("settings.disable-on-combat", true);
    }
    
    public int getCombatTagDuration() {
        return plugin.getConfig().getInt("settings.combat-tag-duration", 10);
    }
    
    public boolean isPersistFlightState() {
        return plugin.getConfig().getBoolean("settings.persist-flight-state", true);
    }
    
    public int getMaxDuration() {
        return plugin.getConfig().getInt("settings.max-duration", -1);
    }
    
    public boolean isPreventFallDamage() {
        return plugin.getConfig().getBoolean("settings.prevent-fall-damage", true);
    }
    
    public boolean isFlightAllowedInWorld(World world) {
        List<String> disabledWorlds = plugin.getConfig().getStringList("settings.disabled-worlds");
        return !disabledWorlds.contains(world.getName());
    }
} 