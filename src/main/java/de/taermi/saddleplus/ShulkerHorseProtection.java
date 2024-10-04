package de.taermi.saddleplus;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataType;

public class ShulkerHorseProtection implements Listener {

    private final NamespacedKey ownerKey;
    private Player shulkerTargetPlayer = null;

    public ShulkerHorseProtection(JavaPlugin plugin) {
        this.ownerKey = new NamespacedKey(plugin, "saddleOwner");
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onShulkerTargetPlayer(EntityTargetEvent event) {
        if (event.getEntity() instanceof Shulker && event.getTarget() instanceof Player) {
            Shulker shulker = (Shulker) event.getEntity();
            Player player = (Player) event.getTarget();

            this.shulkerTargetPlayer = player;
        }
    }

    @EventHandler
    public void onHorseDamageByShulkerBullet(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();

        if (damagedEntity instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse) damagedEntity;

            // projectilename = ShulkerBullet
            if (event.getDamager() instanceof ShulkerBullet) {
                ShulkerBullet bullet = (ShulkerBullet) event.getDamager();

                if (bullet.getShooter() instanceof Shulker) {
                    Shulker shulker = (Shulker) bullet.getShooter();

                    if (this.shulkerTargetPlayer != null) {
                        if (horse.getInventory().getSaddle() != null && horse.getInventory().getSaddle().getItemMeta() != null) {
                            String owner = horse.getInventory().getSaddle().getItemMeta()
                                    .getPersistentDataContainer()
                                    .get(ownerKey, PersistentDataType.STRING);

                            if (owner != null && !owner.isEmpty() && !owner.equals(shulkerTargetPlayer.getName())) {
                                // shulkerTargetPlayer.sendMessage("debug: shulkerbullet blocked"); //debugmessage to test if it works
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
