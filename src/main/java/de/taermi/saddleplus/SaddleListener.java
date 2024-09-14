package de.taermi.saddleplus;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class SaddleListener implements Listener {

    private final NamespacedKey ownerKey;

    public SaddleListener(JavaPlugin plugin) {
        this.ownerKey = new NamespacedKey(plugin, "saddleOwner");
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
                    player.sendMessage(ChatColor.RED + "You aren't the owner of this animal.");
                    // cancel the event to prevent mounting
                    event.setCancelled(true);
                }
            }
        }
    }
}
