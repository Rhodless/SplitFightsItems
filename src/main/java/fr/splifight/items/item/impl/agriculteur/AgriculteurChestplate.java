package fr.splifight.items.item.impl.agriculteur;

import fr.splifight.items.item.AbstractItem;
import fr.splifight.items.item.Items;
import org.bukkit.event.Listener;

public class AgriculteurChestplate extends AbstractItem implements Listener {
    @Override
    public Items getItem() {
        return Items.AGRICULTEUR_CHESTPLATE;
    }
}
