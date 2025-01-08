package dev.purdze.simplefly.commands;

import dev.purdze.simplefly.SimpleFly;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {
    
    private final FlyCommand flyCommand;
    
    public CommandManager(SimpleFly plugin) {
        this.flyCommand = new FlyCommand(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return flyCommand.onCommand(sender, command, label, args);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return flyCommand.onTabComplete(sender, command, alias, args);
    }
} 