package fr.splifight.items.item.impl.agriculteur;

import fr.splifight.items.ItemPlugin;
import fr.splifight.items.config.Messages;
import fr.splifight.items.item.AbstractItem;
import fr.splifight.items.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.net.ssl.SSLEngineResult;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AgriculteurHelmet extends AbstractItem implements Listener {

    public final static List<UUID> HAVE_ARMOR = new ArrayList<>();

    @Override
    public Items getItem() {
        return Items.AGRICULTEUR_HELMET;
    }

    public void check(Player player) {
        PlayerInventory playerInventory = player.getInventory();

        if (Items.AGRICULTEUR_HELMET.getItem().isItem(playerInventory.getHelmet()) && Items.AGRICULTEUR_CHESTPLATE.getItem().isItem(playerInventory.getChestplate())
                && Items.AGRICULTEUR_LEGGINGS.getItem().isItem(playerInventory.getLeggings()) && Items.AGRICULTEUR_BOOTS.getItem().isItem(playerInventory.getBoots())) {
            if(HAVE_ARMOR.contains(player.getUniqueId())) return;

            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
            Messages.AGRICULTEUR_EQUIPE.send(player);
            HAVE_ARMOR.add(player.getUniqueId());
        } else {
            if(!HAVE_ARMOR.contains(player.getUniqueId())) return;

            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            Messages.AGRICULTEUR_DESEQUIPE.send(player);
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
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL && HAVE_ARMOR.contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        if(HAVE_ARMOR.contains(event.getEntity().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(HAVE_ARMOR.contains(event.getPlayer().getUniqueId())) {
            if(event.getPlayer().isSneaking()) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
            } else {
                event.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        } else {
            event.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

}
