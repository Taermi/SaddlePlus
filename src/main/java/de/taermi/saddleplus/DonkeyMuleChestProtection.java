package de.taermi.saddleplus;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataType;

public class DonkeyMuleChestProtection implements Listener {

    private final NamespacedKey ownerKey;

    public DonkeyMuleChestProtection(JavaPlugin plugin) {
        this.ownerKey = new NamespacedKey(plugin, "saddleOwner");
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerPlaceChestOnChestedHorse(PlayerInteractEntityEvent event) {
        // check if the entity is a ChestedHorse (includes donkey and mule)
        if (event.getRightClicked() instanceof ChestedHorse) {
            // prevent double error message
            if (event.isCancelled()) return;

            Player player = event.getPlayer();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            // check if the player is holding a chest
            if (itemInHand.getType() == Material.CHEST) {
                ChestedHorse chestedHorse = (ChestedHorse) event.getRightClicked();
                handleChestPlacement(player, chestedHorse, event);
            }
        }
    }

    private void handleChestPlacement(Player player, ChestedHorse chestedHorse, Cancellable event) {
        if (chestedHorse.getInventory().getSaddle() != null && chestedHorse.getInventory().getSaddle().getItemMeta() != null) {
            String owner = chestedHorse.getInventory().getSaddle().getItemMeta()
                    .getPersistentDataContainer()
                    .get(ownerKey, PersistentDataType.STRING);

            // if the horse has an owner, and the owner is not the player, cancel event
            if (owner != null && !owner.isEmpty() && !owner.equals(player.getName())) {
                Utils.sendActionbarMSG(player, "You cannot place a chest on someone else's animal!", ChatColor.RED);
                event.setCancelled(true);
            }
        }
    }
}
