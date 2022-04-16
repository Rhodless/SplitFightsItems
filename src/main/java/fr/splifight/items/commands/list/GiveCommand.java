package fr.splifight.items.commands.list;

import fr.splifight.items.item.Items;
import fr.splifight.items.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand {
    public GiveCommand(CommandSender sender, String[] args) {
        if(args.length != 3) {
            sender.sendMessage(CC.translate("&cUsage: /sftools give <item> <player/all>"));
            return;
        }

        Items item = Items.getItem(args[1]);

        if(item == null && !args[1].equals("all")) {
            sender.sendMessage(CC.translate("&cCet item n'existe pas."));
            return;
        }


        if(args[2].equals("all") && args[1].equals("all")) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                for (Items value : Items.values()) {
                    value.give(player);
                }
            });
            return;
        } else if(args[2].equals("all")) {
            Bukkit.getOnlinePlayers().forEach(item::give);
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);

        if(target == null) {
            sender.sendMessage(CC.translate("&cCe joueur n'est pas connecté."));
            return;
        }

        if(args[1].equals("all")) {
            for (Items value : Items.values()) {
                value.give(target);
            }
            return;
        }

        item.give(target);
        sender.sendMessage(CC.translate("&aVous avez donné l'item " + item.name() + " a " + target.getName()));
    }
}
