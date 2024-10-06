package de.taermi.saddleplus;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.WeakHashMap;

public class ShulkerHorseProtection implements Listener {

    private final NamespacedKey ownerKey;
    // map to store target player for each shulker
    private final Map<Shulker, Player> shulkerTargetMap = new WeakHashMap<>();

    public ShulkerHorseProtection(NamespacedKey ownerKey) {
        this.ownerKey = ownerKey;
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("SaddlePlus"));
    }

    @EventHandler
    public void onShulkerTargetPlayer(EntityTargetEvent event) {
        // check if a shulker targets a player
        if (event.getEntity() instanceof Shulker && event.getTarget() instanceof Player) {
            Shulker shulker = (Shulker) event.getEntity();
            Player player = (Player) event.getTarget();
            // store the shulkers target player in the map
            shulkerTargetMap.put(shulker, player);
        }
    }

    @EventHandler
    public void onHorseDamageByShulkerBullet(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();
        // check if the damaged entity is a horse/donkey/mule
        if (damagedEntity instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse) damagedEntity;
            if (event.getDamager() instanceof ShulkerBullet) {
                ShulkerBullet bullet = (ShulkerBullet) event.getDamager();
                if (bullet.getShooter() instanceof Shulker) {
                    Shulker shulker = (Shulker) bullet.getShooter();
                    Player targetPlayer = shulkerTargetMap.get(shulker);
                    if (targetPlayer != null) {
                        if (horse.getInventory().getSaddle() != null && horse.getInventory().getSaddle().getItemMeta() != null) {
                            String owner = horse.getInventory().getSaddle().getItemMeta()
                                    .getPersistentDataContainer()
                                    .get(ownerKey, PersistentDataType.STRING);
                            if (owner != null && !owner.isEmpty() && !owner.equals(targetPlayer.getName())) {
                                //targetPlayer.sendMessage("debug: shulkerbullet blocked"); // Debug message to test if it works
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
