package fr.splifight.items.item.impl;

import fr.splifight.items.config.Messages;
import fr.splifight.items.item.AbstractItem;
import fr.splifight.items.item.Items;
import fr.splifight.items.utils.CC;
import fr.splifight.items.utils.ItemBuilder;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.CompletableFuture;

public class UnClaimFinder extends AbstractItem implements Listener {
    @Override
    public Items getItem() {
        return Items.UNCLAIM_FINDER;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isItem(event.getItem())) return;

        int uses = Integer.parseInt(event.getItem().getItemMeta().getDisplayName()
                .replace(CC.translate(getItem().getName()), "")
                .replace("x", "")
                .replace("§8)", "")
        );

        if (uses == 1) event.getPlayer().getInventory().remove(event.getItem());
        else {
            ItemBuilder itemBuilder = new ItemBuilder(event.getItem());
            itemBuilder.setName(CC.translate(getItem().getName() + (uses - 1) + "§8)"));
            event.getPlayer().setItemInHand(itemBuilder.toItemStack());
        }

        Chunk chunk = event.getPlayer().getLocation().getChunk();

        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;

        World world = chunk.getWorld();

        CompletableFuture.runAsync(() -> {
            int amount = 0;
            for (int xx = x; xx < x + 16; xx++) {
                for (int zz = z; zz < z + 16; zz++) {
                    for (int yy = 0; yy < 256; yy++) {
                        Block block = world.getBlockAt(xx, yy, zz);
                        if(block.getType() == Material.CHEST || block.getType() == Material.HOPPER || block.getType() == Material.ENDER_CHEST) {
                            amount++;
                        }
                    }
                }
            }

            Messages.UNCLAIM_FINDER.sendActionBar(event.getPlayer(), amount);
        });
    }

}
