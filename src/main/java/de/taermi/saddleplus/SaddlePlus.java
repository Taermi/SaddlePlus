package de.taermi.saddleplus;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class SaddlePlus extends JavaPlugin {

    // declare NamespacedKey once
    private final NamespacedKey ownerKey = new NamespacedKey(this, "saddleOwner");

    @Override
    public void onEnable() {

        getLogger().info(ChatColor.BLUE + "Saddle" + ChatColor.YELLOW + "Plus" + ChatColor.GREEN + " enabled!");

        this.getCommand("saddle").setExecutor(new SaddleCommand(this));

        // Event listeners
        getServer().getPluginManager().registerEvents(new SaddleListener(ownerKey), this);
        getServer().getPluginManager().registerEvents(new SaddleDamageEvent(ownerKey), this);
        getServer().getPluginManager().registerEvents(new SaddleLeadEvent(ownerKey), this);
        getServer().getPluginManager().registerEvents(new SaddleGriefPlace(ownerKey), this);
        getServer().getPluginManager().registerEvents(new SaddleCommandTrigger(this), this);
        getServer().getPluginManager().registerEvents(new SkeletonHorseProtection(ownerKey), this);
        getServer().getPluginManager().registerEvents(new CreeperHorseProtection(ownerKey), this);
        getServer().getPluginManager().registerEvents(new ShulkerHorseProtection(ownerKey), this);
        getServer().getPluginManager().registerEvents(new DonkeyMuleChestProtection(ownerKey), this);
    }

    @Override
    public void onDisable() {

        getLogger().info(ChatColor.BLUE + "Saddle" + ChatColor.YELLOW + "Plus" + ChatColor.RED + " disabled!");
    }
}
