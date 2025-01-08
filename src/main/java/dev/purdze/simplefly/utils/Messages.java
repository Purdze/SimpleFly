package dev.purdze.simplefly.utils;

import dev.purdze.simplefly.SimpleFly;

public class Messages {
    private static final String UPDATE_PREFIX = "§e[SimpleFly] ";
    private static final String COMBAT_DISABLED = "§cYour flying has been disabled due to combat. Flight will be re-enabled in %time% seconds if you stay out of combat.";
    private static final String COMBAT_ENDED = "§aYou are no longer in combat! You may now fly again.";
    private static final String SPEED_USAGE = "§cUsage: /sf speed <1-10>";
    private static final String INVALID_SPEED = "§cSpeed must be between 1-10!";
    private static final String SPEED_SET = "§aFlight speed set to %speed%";
    private static final String SPEED_SET_OTHER = "§aSet flight speed to %speed% for %player%";
    private static final String NO_PERMISSION = "§cYou don't have permission to use this command!";
    private static final String FLIGHT_DISABLED_IN_WORLD = "§cFlight is disabled in this world!";

    // Update messages
    public static String getUpdateAvailable(String currentVersion, String latestVersion) {
        return UPDATE_PREFIX + "A new update is available!\n" +
               UPDATE_PREFIX + "Current version: " + currentVersion + "\n" +
               UPDATE_PREFIX + "Latest version: " + latestVersion + "\n" +
               UPDATE_PREFIX + "Download: https://www.spigotmc.org/resources/121622";
    }

    // Get prefix from config
    private static String getPrefix() {
        return SimpleFly.getInstance().getConfig().getString("settings.prefix", "§7[§6SimpleFly§7] ")
                .replace("&", "§");
    }

    public static String getCombatDisabled(int time) {
        return getPrefix() + COMBAT_DISABLED.replace("%time%", String.valueOf(time));
    }

    public static String getCombatEnded() {
        return getPrefix() + COMBAT_ENDED;
    }

    public static String getSpeedUsage() {
        return getPrefix() + SPEED_USAGE;
    }

    public static String getInvalidSpeed() {
        return getPrefix() + INVALID_SPEED;
    }

    public static String getSpeedSet(int speed) {
        return getPrefix() + SPEED_SET.replace("%speed%", String.valueOf(speed));
    }

    public static String getSpeedSetOther(String playerName, int speed) {
        return getPrefix() + SPEED_SET_OTHER
                .replace("%speed%", String.valueOf(speed))
                .replace("%player%", playerName);
    }

    public static String getNoPermission() {
        return getPrefix() + NO_PERMISSION;
    }

    public static String getFlightDisabledInWorld() {
        return getPrefix() + FLIGHT_DISABLED_IN_WORLD;
    }
} 