package fr.splifight.items.item.impl;

import fr.splifight.items.item.AbstractItem;
import fr.splifight.items.item.Items;
import fr.splifight.items.utils.CC;
import fr.splifight.items.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ArcDieux extends AbstractItem implements Listener {

    public static final HashMap<UUID, List<ItemStack>> MAP = new HashMap<>();

    @Override
    public Items getItem() {
        return Items.ARC_DIEUX;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        for (ItemStack content : event.getEntity().getInventory().getContents()) {
            if(!isItem(content)) continue;

            event.getDrops().remove(content);
            MAP.putIfAbsent(player.getUniqueId(), new ArrayList<>());

            List<ItemStack> list = MAP.get(player.getUniqueId());
            list.add(content);

            MAP.put(player.getUniqueId(), list);
        }

        if(event.getEntity().getKiller() != null && isItem(event.getEntity().getKiller().getItemInHand())) {
            ItemStack it = event.getEntity().getKiller().getItemInHand();

            List<String> lore = it.getItemMeta().getLore();
            int kills = Integer.parseInt(lore.get(lore.size() - 1).replace(CC.translate(getItem().getKillsLine()), ""));
            lore.remove(lore.size() - 1);
            lore.add(CC.translate(getItem().getKillsLine() + (kills + 1)));

            ItemBuilder itb = new ItemBuilder(player.getKiller().getItemInHand()).setLore(lore);

            player.getKiller().setItemInHand(itb.toItemStack());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if(MAP.get(event.getPlayer().getUniqueId()) != null) {
            for (ItemStack itemStack : MAP.get(event.getPlayer().getUniqueId())) {
                event.getPlayer().getInventory().addItem(itemStack);
            }

            MAP.remove(event.getPlayer().getUniqueId());
        }
    }
}
