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

        this.getCommand("saddle").setExecutor(new SaddleCommand(this)); //is working in 26.1.2 todo: add perfomance stat for op users

        // Event listeners
        getServer().getPluginManager().registerEvents(new SaddleListener(ownerKey), this); //is working in 26.1.2
        getServer().getPluginManager().registerEvents(new SaddleDamageEvent(ownerKey), this); //is working in 26.1.2
        getServer().getPluginManager().registerEvents(new SaddleLeadEvent(ownerKey), this); //is working in 26.1.2
        getServer().getPluginManager().registerEvents(new SaddleGriefPlace(ownerKey), this); //is working in 26.1.2 //todo: disable water place and magma block
        getServer().getPluginManager().registerEvents(new SaddleCommandTrigger(this), this); //is working in 26.1.2
        getServer().getPluginManager().registerEvents(new SkeletonHorseProtection(ownerKey), this); //is working in 26.1.2
        getServer().getPluginManager().registerEvents(new CreeperHorseProtection(ownerKey), this); //is working in 26.1.2 todo: make more efficient
        getServer().getPluginManager().registerEvents(new ShulkerHorseProtection(ownerKey), this); //is working in 26.1.2
        getServer().getPluginManager().registerEvents(new DonkeyMuleChestProtection(ownerKey), this); //todo: cant place chest but open
    }

    @Override
    public void onDisable() {

        getLogger().info(ChatColor.BLUE + "Saddle" + ChatColor.YELLOW + "Plus" + ChatColor.RED + " disabled!");
    }
}
