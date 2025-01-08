package dev.purdze.simplefly;

import dev.purdze.simplefly.commands.CommandManager;
import dev.purdze.simplefly.listeners.FlyListener;
import dev.purdze.simplefly.listeners.CombatListener;
import dev.purdze.simplefly.managers.DataManager;
import dev.purdze.simplefly.managers.SettingsManager;
import dev.purdze.simplefly.managers.CombatManager;
import dev.purdze.simplefly.utils.UpdateChecker;
import dev.purdze.simplefly.listeners.UpdateNotifier;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;

public class SimpleFly extends JavaPlugin {
    
    private static SimpleFly instance;
    private DataManager dataManager;
    private SettingsManager settingsManager;
    private CombatManager combatManager;
    private UpdateChecker updateChecker;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Check config version
        reloadConfig();
        // Create data folder if it doesn't exist
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        String configVersion = getConfig().getString("config-version", "1.0.0");
        if (!configVersion.equals("1.1.0")) {  // First major update
            File backupFile = new File(getDataFolder(), "config.old.yml");
            File configFile = new File(getDataFolder(), "config.yml");
            if (configFile.exists()) {
                try {
                    java.nio.file.Files.copy(
                        configFile.toPath(),
                        backupFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );
                    getLogger().info("Created backup of old configuration at config.old.yml");
                } catch (IOException e) {
                    getLogger().warning("Failed to backup old configuration: " + e.getMessage());
                }
            }
            saveDefaultConfig();  // Save new config
        }
        
        // Initialize update checker
        this.updateChecker = new UpdateChecker(this);
        this.updateChecker.checkForUpdates();
        
        // Register update notifier
        getServer().getPluginManager().registerEvents(new UpdateNotifier(this), this);
        
        // Initialize managers
        settingsManager = new SettingsManager(this);
        dataManager = new DataManager(this);
        combatManager = new CombatManager(this);
        
        // Register commands and listeners
        getCommand("simplefly").setExecutor(new CommandManager(this));
        getCommand("simplefly").setTabCompleter(new CommandManager(this));
        getServer().getPluginManager().registerEvents(new FlyListener(), this);
        getServer().getPluginManager().registerEvents(new CombatListener(), this);
        
        getLogger().info("SimpleFly has been enabled!");
    }
    
    @Override
    public void onDisable() {
        if (settingsManager.isPersistFlightState()) {
            dataManager.saveData();
        }
        getLogger().info("SimpleFly has been disabled!");
    }
    
    public static SimpleFly getInstance() {
        return instance;
    }
    
    public DataManager getDataManager() {
        return dataManager;
    }
    
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
    
    public CombatManager getCombatManager() {
        return combatManager;
    }
    
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    
    public String formatMessage(String message) {
        return message.replace("%prefix%", getConfig().getString("messages.prefix"))
                     .replace("&", "§");
    }
} 