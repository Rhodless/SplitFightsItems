package fr.splifight.items.config;

import fr.splifight.items.ItemPlugin;
import fr.splifight.items.item.Items;
import fr.splifight.items.utils.CC;
import fr.splifight.items.utils.Title;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

@Getter
@AllArgsConstructor
public enum Messages {

    PREFIX("&c&lSplifight &8&l» &f"),
    CROCHETER("<prefix>&fVous avez utilisé un &acrochet&f."),
    RECOLTEUR("<prefix>&fVous avez posé un &arécolteur&f."),
    RECEIVE("<prefix>&fVous avez &areçu &fl'item &c<item>"),
    USED("<prefix>&cUn hopper est déjà dans ce chunk."),

    GUERRIER_EQUIPE("<prefix>&eArmure du &6guerrier&e bien équipé."),
    GUERRIER_DESEQUIPE("<prefix>&eArmure du &6guerrier&e déséquipé."),

    AGRICULTEUR_EQUIPE("<prefix>&eArmure de &6l’agriculteur&e bien équipé."),
    AGRICULTEUR_DESEQUIPE("<prefix>&eArmure de &6l’agriculteur&e déséquipé."),

    UNCLAIM_FINDER("&fIl y a &c<nombre>% &fd'entités"),

    COOLDOWN("<prefix>&cVous êtes encore sur un cooldown de <cooldown> secondes.")
    ;

    @Setter
    private String display;

    public String placeHolders(String message) {
        return message.replace("<prefix>", PREFIX.getDisplay());
    }

    public void send(Player player) {
        if (display.equalsIgnoreCase("false")) return;

        player.sendMessage(CC.translate(placeHolders(getDisplay())));
    }

    public void sendActionBar(Player player, int amount) {
        if (display.equalsIgnoreCase("false")) return;

        Title.sendActionBar(player, CC.translate(placeHolders(getDisplay().replace("<nombre>", "" + amount))));
    }

    public void send(Player player, Items item) {
        if (display.equalsIgnoreCase("false")) return;

        player.sendMessage(CC.translate(placeHolders(getDisplay().replace("<item>", item.name()))));
    }

    public void send(Player player, int cooldown) {
        if (display.equalsIgnoreCase("false")) return;

        player.sendMessage(CC.translate(placeHolders(getDisplay().replace("<cooldown>", "" + cooldown))));
    }

    @SneakyThrows
    public static void init() {
        FileConfiguration config = ItemPlugin.getInstance().getConfig();
        config.load(new File(ItemPlugin.getInstance().getDataFolder() + "/config.yml"));

        for (Messages value : values()) {
            if (config.get("messages." + value.name()) == null) {
                config.set("messages." + value.name(), value.getDisplay());
                ItemPlugin.getInstance().saveConfig();
            }

            value.setDisplay(config.getString("messages." + value.name()));
        }
    }
}
