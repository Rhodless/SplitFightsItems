package fr.splifight.items.item.impl.guerrier;

import fr.splifight.items.item.AbstractItem;
import fr.splifight.items.item.Items;
import org.bukkit.event.Listener;

public class GuerrierChestplate extends AbstractItem implements Listener {
    @Override
    public Items getItem() {
        return Items.GUERRIER_CHESTPLATE;
    }
}
