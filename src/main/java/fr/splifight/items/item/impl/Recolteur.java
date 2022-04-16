package fr.splifight.items.item.impl;

import fr.splifight.items.ItemPlugin;
import fr.splifight.items.config.Messages;
import fr.splifight.items.item.AbstractItem;
import fr.splifight.items.item.Items;
import lombok.SneakyThrows;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Recolteur extends AbstractItem implements Listener {

    public static List<Location> HOPPERS;
    public static List<Material> FARM_ITEMS;

    @Override
    public Items getItem() {
        return Items.RECOLTEUR;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (!isItem(event.getPlayer().getItemInHand())) return;

        boolean used = false;

        for (Location location : HOPPERS) {
            if (location.getChunk() == event.getBlock().getChunk()) {
                used = true;
                break;
            }
        }

        if(used) {
            Messages.USED.send(event.getPlayer());
            event.setCancelled(true);
            return;
        }

        HOPPERS.add(event.getBlock().getLocation());
        ItemPlugin.getInstance().getConfig().set("data.hoppers", HOPPERS);
        ItemPlugin.getInstance().saveConfig();

        Messages.RECOLTEUR.send(event.getPlayer());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(HOPPERS.contains(event.getBlock().getLocation())) {
            HOPPERS.remove(event.getBlock().getLocation());
            ItemPlugin.getInstance().getConfig().set("data.hoppers", HOPPERS);
            ItemPlugin.getInstance().saveConfig();
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof Item)) return;

        Item item = (Item) event.getEntity();
        ItemStack is = item.getItemStack();

        if (!FARM_ITEMS.contains(is.getType())) return;

        Chunk chunk = event.getEntity().getLocation().getChunk();
        HOPPERS.forEach(block -> {
            if (block.getChunk().equals(chunk)) {
                Hopper hopper = (Hopper) block.getBlock().getState();

                if (!canAdd(hopper, is)) return;
                item.remove();
                hopper.getInventory().addItem(is);
            }
        });
    }

    public boolean canAdd(Hopper hopper, ItemStack is) {
        int foundcount = is.getAmount();
        for (ItemStack stack : hopper.getInventory().getContents()) {
            if (stack == null) {
                foundcount -= is.getMaxStackSize();
                continue;
            }
            if (stack.getType() == is.getType()) {
                if (stack.getDurability() == is.getDurability()) {
                    foundcount -= is.getMaxStackSize() - stack.getAmount();
                }
            }
        }
        return foundcount <= 0;
    }

    static {
        HOPPERS = (List<Location>) ItemPlugin.getInstance().getConfig().getList("data.hoppers");
        List<String> items = (List<String>) ItemPlugin.getInstance().getConfig().getList("items.RECOLTEUR.farm_items");
        FARM_ITEMS = new ArrayList<>();
        items.forEach(s -> FARM_ITEMS.add(Material.valueOf(s)));
    }

    @SneakyThrows
    public static void reload() {
        FileConfiguration configuration = ItemPlugin.getInstance().getConfig();
        configuration.load(ItemPlugin.getInstance().getDataFolder() + "/config.yml");
        HOPPERS = (List<Location>) configuration.getList("data.hoppers");

        List<String> items = (List<String>) ItemPlugin.getInstance().getConfig().getList("items.RECOLTEUR.farm_items");
        FARM_ITEMS = new ArrayList<>();
        items.forEach(s -> FARM_ITEMS.add(Material.valueOf(s)));

    }

}
