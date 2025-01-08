package dev.purdze.simplefly.utils;

import dev.purdze.simplefly.SimpleFly;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {
    
    private final SimpleFly plugin;
    private final int resourceId = 121622;
    private String latestVersion;
    private static final String PREFIX = "§7[§6SimpleFly§7]";

    public UpdateChecker(SimpleFly plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                
                if (scanner.hasNext()) {
                    latestVersion = scanner.next();
                    String currentVersion = plugin.getDescription().getVersion().replace("${project.version}", "1.1.0");

                    if (compareVersions(currentVersion, latestVersion) < 0) {
                        plugin.getLogger().info(PREFIX + " §eA new update is available!");
                        plugin.getLogger().info(PREFIX + " §eCurrent version: " + currentVersion);
                        plugin.getLogger().info(PREFIX + " §eLatest version: " + latestVersion);
                        plugin.getLogger().info(PREFIX + " §eDownload: https://www.spigotmc.org/resources/" + resourceId);
                    }
                }
            } catch (IOException exception) {
                plugin.getLogger().warning(PREFIX + " §cUnable to check for updates: " + exception.getMessage());
            }
        });
    }

    private int compareVersions(String version1, String version2) {
        String[] v1Parts = version1.split("\\.");
        String[] v2Parts = version2.split("\\.");
        
        int length = Math.max(v1Parts.length, v2Parts.length);
        for (int i = 0; i < length; i++) {
            int v1 = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int v2 = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;
            
            if (v1 < v2) return -1;
            if (v1 > v2) return 1;
        }
        return 0;
    }

    public String getLatestVersion() {
        return latestVersion;
    }
} 