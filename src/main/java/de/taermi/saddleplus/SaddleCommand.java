package de.taermi.saddleplus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class SaddleCommand implements CommandExecutor {

    private final NamespacedKey ownerKey;

    public SaddleCommand(JavaPlugin plugin) {
        this.ownerKey = new NamespacedKey(plugin, "saddleOwner");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("claim")) {
                claimSaddle(player);
                return true;
            } else if (args[0].equalsIgnoreCase("unclaim")) {
                unclaimSaddle(player);
                return true;
            }
        }

        player.sendMessage(ChatColor.RED + "Usage: /saddle <claim|unclaim>");
        return false;
    }

    private boolean claimSaddle(Player player) {
        // check if player has a saddle in hand
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() != Material.SADDLE) {
            player.sendMessage(ChatColor.RED + "You must hold a saddle to claim it.");
            return true;
        }

        ItemMeta meta = heldItem.getItemMeta();

        if (meta != null) {
            // check if saddle already has a owner
            if (meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
                // player can only claim if the saddle do not have an owner:
                if (!owner.isEmpty()) {
                    player.sendMessage(ChatColor.RED + "This saddle is already claimed by " + owner + ".");
                    return true;
                }
            }

            // else we can claim the saddle:
            meta.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, player.getName());
            meta.setDisplayName(ChatColor.GOLD + player.getName() + "'s Saddle"); // set saddle name
            heldItem.setItemMeta(meta);
            player.sendMessage(ChatColor.GREEN + "You have claimed the saddle.");
            return true;
        }
        return false;
    }

    private boolean unclaimSaddle(Player player) {
        // check if player has a saddle in hand
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (heldItem.getType() != Material.SADDLE) {
            player.sendMessage(ChatColor.RED + "You must hold a saddle to unclaim it.");
            return true;
        }

        if (heldItem.getItemMeta() != null) {
            ItemMeta meta = heldItem.getItemMeta();
            if (meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
                // check if player is saddle owner
                if (owner.equals(player.getName())) {
                    // unclaim saddle
                    meta.getPersistentDataContainer().remove(ownerKey);
                    meta.setDisplayName(ChatColor.RESET + "Saddle"); // changes name to default
                    heldItem.setItemMeta(meta);
                    player.sendMessage(ChatColor.GREEN + "You have unclaimed your saddle.");
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "You are not the owner of this saddle.");
                    return true;
                }
            }
        }

        player.sendMessage(ChatColor.RED + "You do not have a saddle to unclaim.");
        return true;
    }
}