package de.taermi.saddleplus;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Mule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class SaddleDamageEvent implements Listener {
    private final NamespacedKey ownerKey;

    public SaddleDamageEvent(JavaPlugin plugin) {
        this.ownerKey = new NamespacedKey(plugin, "saddleOwner");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity attacker = event.getDamager();

        // check if entity has a saddle
        ItemStack saddle = getSaddle(entity);

        // check if attack comes from player
        if (saddle != null && attacker instanceof Player) {
            Player player = (Player) attacker;
            ItemMeta meta = saddle.getItemMeta();

            if (meta != null && meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);

                // check if attacker isn't the owner
                if (!owner.equals(player.getName())) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cannot damage animals from other players!");
                }
            }
        }
    }

    private ItemStack getSaddle(Entity entity) {
        if (entity instanceof Horse) {
            Horse horse = (Horse) entity;
            return horse.getInventory().getSaddle();
        } else if (entity instanceof Donkey) {
            Donkey donkey = (Donkey) entity;
            return donkey.getInventory().getSaddle();
        } else if (entity instanceof Mule) {
            Mule mule = (Mule) entity;
            return mule.getInventory().getSaddle();
        }
        return null;
    }
}
