package de.taermi.saddleplus;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;

public class SaddleDamageEvent implements Listener {

    private final NamespacedKey ownerKey;

    public SaddleDamageEvent(JavaPlugin plugin) {
        this.ownerKey = new NamespacedKey(plugin, "saddleOwner");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        // check if animal has a saddle
        ItemStack saddle = getSaddle(entity);
        if (saddle != null) {
            ItemMeta meta = saddle.getItemMeta();
            if (meta != null && meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);

                // check if attacker is player
                if (event.getDamager() instanceof Player player) {
                    // if attacker isn't owner cancel event
                    if (!owner.equals(player.getName())) {
                        event.setCancelled(true);
                        Utils.sendActionbarMSG(player, "You can't harm other player animals.", ChatColor.RED);
                    }
                } else if (event.getDamager() instanceof Projectile projectile) {
                    // check if projectile from player
                    ProjectileSource shooter = projectile.getShooter();
                    if (shooter instanceof Player) {
                        Player player = (Player) shooter;
                        if (!owner.equals(player.getName())) {
                            event.setCancelled(true);
                            Utils.sendActionbarMSG(player, "You can't harm other player animals.", ChatColor.RED);
                        }
                    }
                } else if (event.getDamager() instanceof TNTPrimed tnt) {
                    // check if tnt from player
                    Entity source = tnt.getSource();
                    if (source instanceof Player) {
                        Player player = (Player) source;
                        if (!owner.equals(player.getName())) {
                            event.setCancelled(true);
                            Utils.sendActionbarMSG(player, "You can't harm other player animals.", ChatColor.RED);
                        }
                    }
                }
            }
        }
    }

    // event for splash-potions (poison etc.) --- Lingering Potions coming soon
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();
        ProjectileSource shooter = potion.getShooter();

        // check if potion is from player
        if (shooter instanceof Player player) {
            for (Entity entity : event.getAffectedEntities()) {
                // check if animal has protected saddle
                ItemStack saddle = getSaddle(entity);
                if (saddle != null) {
                    ItemMeta meta = saddle.getItemMeta();
                    if (meta != null && meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                        String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);

                        // if player isn't owner, block event
                        if (!owner.equals(player.getName())) {
                            for (PotionEffect effect : event.getPotion().getEffects()) {
                                    event.setCancelled(true);
                                    Utils.sendActionbarMSG(player, "You can't harm other player animals.", ChatColor.RED);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    // method to call the saddle of the entity
    private ItemStack getSaddle(Entity entity) {
        if (entity instanceof AbstractHorse horse)
            return horse.getInventory().getSaddle();
        return null;
    }
}