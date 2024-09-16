package de.taermi.saddleplus;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class SaddlePlus extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info(ChatColor.BLUE + "Saddle" + ChatColor.YELLOW + "Plus" + ChatColor.GREEN + " enabled!");

        this.getCommand("saddle").setExecutor(new SaddleCommand(this));

        // Event listener
        getServer().getPluginManager().registerEvents(new SaddleListener(this), this);
        getServer().getPluginManager().registerEvents(new SaddleDamageEvent(this), this);
        getServer().getPluginManager().registerEvents(new SaddleLeadEvent(this), this);
        getServer().getPluginManager().registerEvents(new SaddleGriefPlace(this), this);
        getServer().getPluginManager().registerEvents(new SaddleCommandTrigger(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(ChatColor.BLUE + "Saddle" + ChatColor.YELLOW + "Plus" + ChatColor.RED + " disabled!");

    }
}
