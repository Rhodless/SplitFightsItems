package fr.splifight.items.item.impl;

import fr.splifight.items.ItemPlugin;
import fr.splifight.items.config.Messages;
import fr.splifight.items.item.AbstractItem;
import fr.splifight.items.item.Items;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ChunkDestroyer extends AbstractItem implements Listener {

    public static final HashMap<UUID, Integer> COOLDOWN = new HashMap<>();

    @Override
    public Items getItem() {
        return Items.CHUNK_DESTROYER;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!isItem(event.getPlayer().getItemInHand())) return;

        if (COOLDOWN.getOrDefault(event.getPlayer().getUniqueId(), 0) > 0) {
            Messages.COOLDOWN.send(event.getPlayer(), COOLDOWN.get(event.getPlayer().getUniqueId()));
            event.setCancelled(true);
            return;
        }

        Chunk chunk = event.getPlayer().getLocation().getChunk();

        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;

        World world = chunk.getWorld();


        for (int xx = x; xx < x + 16; xx++) {
            for (int zz = z; zz < z + 16; zz++) {
                for (int yy = 0; yy < 256; yy++) {
                    Block block = world.getBlockAt(xx, yy, zz);
                    if (block.getType() != Material.BEDROCK && block.getType() != Material.AIR) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }

        event.setCancelled(true);

        ItemStack it = event.getPlayer().getItemInHand();

        if (it.getAmount() > 1) {
            it.setAmount(it.getAmount() - 1);
            event.getPlayer().getInventory().setItemInHand(it);
        } else {
            event.getPlayer().getInventory().removeItem(it);
        }

        COOLDOWN.put(event.getPlayer().getUniqueId(), 10 * 60);

        new BukkitRunnable() {
            final UUID uuid = event.getPlayer().getUniqueId();

            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);

                if (player == null) {
                    COOLDOWN.remove(uuid);
                    cancel();
                    return;
                }
                if (COOLDOWN.get(player.getUniqueId()) == null) {
                    cancel();
                    return;
                }

                COOLDOWN.put(player.getUniqueId(), COOLDOWN.get(player.getUniqueId()) - 1);
            }
        }.runTaskTimer(ItemPlugin.getInstance(), 0, 10);
    }

}
