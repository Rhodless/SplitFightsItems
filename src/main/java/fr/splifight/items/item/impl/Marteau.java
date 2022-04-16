package fr.splifight.items.item.impl;

import fr.splifight.items.item.AbstractItem;
import fr.splifight.items.item.Items;
import fr.splifight.items.utils.Cuboid;
import fr.splifight.items.utils.LocationUtils;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Marteau extends AbstractItem implements Listener {
    @Override
    public Items getItem() {
        return Items.MARTEAU;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (!isItem(event.getPlayer().getItemInHand())) return;

        String charDir = LocationUtils.getCardinalDirection(event.getPlayer());

        Location loc = event.getBlock().getLocation();

        if (charDir.equals("N") || charDir.equals("S")) {
            Cuboid cuboid = new Cuboid(loc.clone().add(0, 1, 1), loc.clone().add(0, -1, -1));
            cuboid.getBlockList().forEach(block -> destroyBlock(event.getPlayer(), block));
        } else {
            Cuboid cuboid = new Cuboid(loc.clone().add(1, 1, 0), loc.clone().add(-1, -1, 0));
            cuboid.getBlockList().forEach(block -> destroyBlock(event.getPlayer(), block));
        }
    }

    public void destroyBlock(Player player, Block b) {
        PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(2001, new BlockPosition(b.getX(), b.getY(), b.getZ()), b.getTypeId(), false);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

        b.breakNaturally();
    }

}
