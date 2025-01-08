package dev.purdze.simplefly.managers;

import dev.purdze.simplefly.SimpleFly;
import dev.purdze.simplefly.data.FlightData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {
    private final SimpleFly plugin;
    private final Map<UUID, FlightData> flightData;
    private final File dataFile;
    private FileConfiguration dataConfig;

    public DataManager(SimpleFly plugin) {
        this.plugin = plugin;
        this.flightData = new HashMap<>();
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        loadData();
    }

    public void savePlayerState(Player player) {
        flightData.put(player.getUniqueId(), 
            new FlightData(player.getAllowFlight(), player.getFlySpeed()));
    }

    public void loadPlayerState(Player player) {
        FlightData data = flightData.get(player.getUniqueId());
        if (data != null) {
            player.setAllowFlight(data.isFlying());
            player.setFlySpeed(data.getSpeed());
        }
    }

    public void saveData() {
        dataConfig = new YamlConfiguration();
        for (Map.Entry<UUID, FlightData> entry : flightData.entrySet()) {
            String path = entry.getKey().toString();
            FlightData data = entry.getValue();
            dataConfig.set(path + ".flying", data.isFlying());
            dataConfig.set(path + ".speed", data.getSpeed());
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save flight data: " + e.getMessage());
        }
    }

    public void loadData() {
        if (!dataFile.exists()) {
            return;
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        for (String uuidString : dataConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidString);
                boolean flying = dataConfig.getBoolean(uuidString + ".flying");
                float speed = (float) dataConfig.getDouble(uuidString + ".speed");
                flightData.put(uuid, new FlightData(flying, speed));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid UUID in data file: " + uuidString);
            }
        }
    }

    public void clearData() {
        flightData.clear();
    }
} 