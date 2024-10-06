package de.taermi.saddleplus;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import net.md_5.bungee.api.ChatColor;

public class SaddleListener implements Listener {

    private final NamespacedKey ownerKey;

    public SaddleListener(NamespacedKey ownerKey) {
        this.ownerKey = ownerKey;
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("SaddlePlus"));
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player) {
            Player player = (Player) event.getEntered();
            Entity vehicle = event.getVehicle();
            ItemStack saddle = getSaddle(vehicle);

            if (saddle != null) {
                checkSaddleOwnership(player, saddle, event);
            }
        }
    }

    private ItemStack getSaddle(Entity entity) {
        if (entity instanceof AbstractHorse horse)
            return horse.getInventory().getSaddle();
        return null;
    }

    private void checkSaddleOwnership(Player player, ItemStack saddle, VehicleEnterEvent event) {
        if (saddle.getItemMeta() != null) {
            ItemMeta meta = saddle.getItemMeta();
            if (meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
                if (!owner.equals(player.getName())) {
                    Utils.sendActionbarMSG(player, "You aren't the owner of this animal.", ChatColor.RED);
                    // cancel the event to prevent mounting
                    event.setCancelled(true);
                }
            }
        }
    }
}
