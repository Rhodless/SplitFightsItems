package fr.splifight.items.item;

import fr.splifight.items.item.impl.ArcDieux;
import fr.splifight.items.item.impl.UnClaimFinder;
import fr.splifight.items.utils.CC;
import fr.splifight.items.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractItem {

    public abstract Items getItem();

    public int getAmount() {
        return 1;
    }

    public ItemStack getItemStack() {
        String display = getItem().getName();
        Material material = getItem().getMaterial();

        if(this instanceof ArcDieux) {
            return new ItemBuilder(material, getAmount())
                    .setLore(getItem().getLore())
                    .setName(CC.translate(display))
                    .addEnchant(Enchantment.ARROW_INFINITE, 1)
                    .toItemStack();
        }

        List<String> lore = new ArrayList<>(getItem().getLore());

        return new ItemBuilder(material, getAmount()).setLore(lore).setName(CC.translate(display)).toItemStack();
    }

    public boolean isItem(ItemStack itemStack) {
        if(itemStack == null) return false;
        if(itemStack.getType() == Material.AIR) return false;
        if(!itemStack.hasItemMeta()) return false;
        if(!itemStack.getItemMeta().hasDisplayName()) return false;

        ItemMeta meta = itemStack.getItemMeta();

        if(this instanceof UnClaimFinder) {
            return meta.getDisplayName().startsWith(CC.translate(getItem().getName())) && itemStack.getType() == getItem().getMaterial();
        }

        return meta.getDisplayName().equalsIgnoreCase(CC.translate(getItem().getName())) && itemStack.getType() == getItem().getMaterial();
    }

}
