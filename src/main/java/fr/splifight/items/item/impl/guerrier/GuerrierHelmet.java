package fr.splifight.items.item.impl.guerrier;

import com.sun.corba.se.impl.naming.cosnaming.InternalBindingValue;
import fr.splifight.items.ItemPlugin;
import fr.splifight.items.config.Messages;
import fr.splifight.items.item.AbstractItem;
import fr.splifight.items.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuerrierHelmet extends AbstractItem implements Listener {

    public final static List<UUID> HAVE_ARMOR = new ArrayList<>();

    @Override
    public Items getItem() {
        return Items.GUERRIER_HELMET;
    }

    public void check(Player player) {
        PlayerInventory playerInventory = player.getInventory();

        if (Items.GUERRIER_HELMET.getItem().isItem(playerInventory.getHelmet()) && Items.GUERRIER_CHESTPLATE.getItem().isItem(playerInventory.getChestplate())
                && Items.GUERRIER_LEGGINGS.getItem().isItem(playerInventory.getLeggings()) && Items.GUERRIER_BOOTS.getItem().isItem(playerInventory.getBoots())) {
            if(HAVE_ARMOR.contains(player.getUniqueId())) return;

            Messages.GUERRIER_EQUIPE.send(player);
            HAVE_ARMOR.add(player.getUniqueId());

        } else {
            if(!HAVE_ARMOR.contains(player.getUniqueId())) return;

            Messages.GUERRIER_DESEQUIPE.send(player);
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


    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(HAVE_ARMOR.contains(event.getEntity().getUniqueId())) {
            event.setDamage(event.getFinalDamage() * 0.9F);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(HAVE_ARMOR.contains(event.getDamager().getUniqueId())) {
            event.setDamage(event.getFinalDamage() * 1.1F);
        }
    }

}
