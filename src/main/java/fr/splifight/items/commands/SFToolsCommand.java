package fr.splifight.items.commands;

import fr.splifight.items.commands.list.GiveCommand;
import fr.splifight.items.commands.list.ListCommand;
import fr.splifight.items.config.Messages;
import fr.splifight.items.item.Items;
import fr.splifight.items.item.impl.Recolteur;
import fr.splifight.items.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

import java.util.Arrays;
import java.util.List;

public class SFToolsCommand implements CommandExecutor {

    public static final List<String> HELP_MESSAGE = Arrays.asList(
            "&8&m--------------------",
            "&8- &a/sftools help&8: &fAfficher cette page",
            "&8- &a/sftools list&8: &fAfficher la liste des items",
            "&8- &a/sftools give <item/all> <joueur/all>&8: &fGive un item à un joueur",
            "&8- &a/sftools reload&8: &fReload la config (messages, etc)",
            "&8&m--------------------"
            );

    public static void sendHelpMessage(CommandSender sender) {
        HELP_MESSAGE.forEach(s -> sender.sendMessage(CC.translate(s)));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(sender);
            return false;
        }

        if(args[0].equalsIgnoreCase("list")) {
            new ListCommand(sender, args);
        } else if(args[0].equalsIgnoreCase("reload")) {
            Items.loadConfig();
            Messages.init();
            Recolteur.reload();
            sender.sendMessage(CC.translate("&aLe plugin a été reload avec succès !"));
        } else if(args[0].equalsIgnoreCase("give")) {
            new GiveCommand(sender, args);
        } else {
            sender.sendMessage(CC.translate("&cCette commande n'existe pas. Utilise /sftools help pour la liste des commandes."));
        }

        return false;
    }
}
