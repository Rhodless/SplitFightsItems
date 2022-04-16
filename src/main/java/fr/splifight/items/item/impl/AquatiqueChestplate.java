package fr.splifight.items.item.impl;

import fr.splifight.items.ItemPlugin;
import fr.splifight.items.config.Messages;
import fr.splifight.items.item.AbstractItem;
import fr.splifight.items.item.Items;
import fr.splifight.items.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AquatiqueChestplate extends AbstractItem implements Listener {

    public final static List<UUID> HAVE_ARMOR = new ArrayList<>();

    @Override
    public Items getItem() {
        return Items.AQUATIQUE_CHESTPLATE;
    }

    public void check(Player player) {
        PlayerInventory playerInventory = player.getInventory();

        if (Items.AQUATIQUE_CHESTPLATE.getItem().isItem(playerInventory.getChestplate())) {
            if(HAVE_ARMOR.contains(player.getUniqueId())) return;

            new BukkitRunnable() {
                final UUID uuid = player.getUniqueId();
                @Override
                public void run() {
                    Player player = Bukkit.getPlayer(uuid);

                    if(player == null || !HAVE_ARMOR.contains(player.getUniqueId())) {
                        cancel();
                        return;
                    }

                    Cuboid cuboid = new Cuboid(player.getLocation().clone().add(1, -2, 1), player.getLocation().clone().add(-1, -1, -1));

                    HashMap<Location, Material> changedBlocks = new HashMap<>();

                    for (Block block : cuboid.getBlockList()) {
                        if(block.getType() == Material.WATER) {
                            changedBlocks.put(block.getLocation(), block.getType());
                            block.setType(Material.ICE);
                        }
                    }

                    Bukkit.getScheduler().runTaskLater(ItemPlugin.getInstance(), () -> changedBlocks.forEach((location, block) -> location.getBlock().setType(block)), 40);
                }
            }.runTaskTimer(ItemPlugin.getInstance(), 0, 4);
            HAVE_ARMOR.add(player.getUniqueId());
        } else {
            if(!HAVE_ARMOR.contains(player.getUniqueId())) return;

            HAVE_ARMOR.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(ItemPlugin.getInstance(), () -> check(event.getPlayer()), 10);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Bukkit.getScheduler().runTaskLater(ItemPlugin.getInstance(), () -> check(event.getPlayer()), 2);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Bukkit.getScheduler().runTaskLater(ItemPlugin.getInstance(), () -> check((Player) event.getWhoClicked()), 2);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        HAVE_ARMOR.remove(event.getPlayer().getUniqueId());
    }

}
