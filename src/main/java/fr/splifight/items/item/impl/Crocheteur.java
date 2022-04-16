package fr.splifight.items.item.impl;

import fr.splifight.items.config.Messages;
import fr.splifight.items.item.AbstractItem;
import fr.splifight.items.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Crocheteur extends AbstractItem implements Listener {

    @Override
    public Items getItem() {
        return Items.CROCHETEUR;
    }

    @Override
    public int getAmount() {
        return 10;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(!isItem(event.getPlayer().getItemInHand())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!isItem(event.getItem())) return;

        ItemStack itemStack = event.getItem();
        Player player = event.getPlayer();

        Block block = event.getClickedBlock();

        if(block == null) return;

        boolean blocked = false;

        for (String s : CONTAINER) {
            if(block.getType().name().contains(s)) blocked = true;
        }

        if(!blocked) return;

        if(itemStack.getAmount() <= 1) {
            player.getInventory().removeItem(itemStack);
        }
        else {
            itemStack.setAmount(itemStack.getAmount() - 1);
            player.getInventory().setItemInHand(itemStack);
        }

        Messages.CROCHETER.send(player);
        event.setCancelled(false);
    }

    public static final List<String> CONTAINER = Arrays.asList(
            "CHEST",
            "DOOR",
            "FENCE",
            "HOPPER"
    );

}
