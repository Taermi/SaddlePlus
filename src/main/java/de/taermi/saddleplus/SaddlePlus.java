package de.taermi.saddleplus;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class SaddlePlus extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info(ChatColor.BLUE + "Saddle" + ChatColor.YELLOW + "Plus" + ChatColor.GREEN + " enabled!");

        this.getCommand("saddle").setExecutor(new de.taermi.saddleplus.SaddleCommand(this));

        // Event listener registrieren
        getServer().getPluginManager().registerEvents(new de.taermi.saddleplus.SaddleListener(this), this);
        getServer().getPluginManager().registerEvents(new de.taermi.saddleplus.SaddleDamageEvent(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(ChatColor.BLUE + "Saddle" + ChatColor.YELLOW + "Plus" + ChatColor.RED + " disabled!");

    }
}
