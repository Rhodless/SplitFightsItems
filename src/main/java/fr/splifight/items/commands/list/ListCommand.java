package fr.splifight.items.commands.list;

import fr.splifight.items.item.Items;
import fr.splifight.items.utils.CC;
import org.bukkit.command.CommandSender;

public class ListCommand {
    public ListCommand(CommandSender sender, String[] args) {
        sender.sendMessage(CC.translate("&8&m---------------------"));
        sender.sendMessage(CC.translate("&6Liste des items:"));
        for (Items value : Items.values()) {
            sender.sendMessage(CC.translate("&8&m-&r " + value.name()));
        }
        sender.sendMessage(CC.translate("&8&m---------------------"));
    }
}
