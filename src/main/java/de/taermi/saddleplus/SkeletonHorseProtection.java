package de.taermi.saddleplus;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataType;

public class SkeletonHorseProtection implements Listener {

    private final NamespacedKey ownerKey;
    private Player skeletonTargetPlayer = null; // track target player from skeleton

    public SkeletonHorseProtection(JavaPlugin plugin) {
        this.ownerKey = new NamespacedKey(plugin, "saddleOwner");
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSkeletonTargetPlayer(EntityTargetEvent event) {
        // check if a skeleton targets a player
        if (event.getEntity() instanceof Skeleton && event.getTarget() instanceof Player) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            Player player = (Player) event.getTarget();

            // saves target player
            this.skeletonTargetPlayer = player;
        }
    }

    @EventHandler
    public void onHorseDamageBySkeletonArrow(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();

        // check if the damaged entity is a horse
        if (damagedEntity instanceof Horse) {
            Horse horse = (Horse) damagedEntity;

            // check if the damage is caused by an arrow
            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();

                // check if the arrow is shot by a skeleton
                if (arrow.getShooter() instanceof Skeleton) {
                    Skeleton skeleton = (Skeleton) arrow.getShooter();

                    // check for the skeletons target player
                    if (this.skeletonTargetPlayer != null) {
                        // get horse owner (if the horse has a saddle with an owner)
                        if (horse.getInventory().getSaddle() != null && horse.getInventory().getSaddle().getItemMeta() != null) {
                            String owner = horse.getInventory().getSaddle().getItemMeta()
                                    .getPersistentDataContainer()
                                    .get(ownerKey, PersistentDataType.STRING);

                            // if the horse has an owner and its not the skeletons target, prevent damage
                            if (owner != null && !owner.isEmpty() && !owner.equals(skeletonTargetPlayer.getName())) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
