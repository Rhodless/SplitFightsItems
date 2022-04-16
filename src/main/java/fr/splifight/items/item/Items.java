package fr.splifight.items.item;

import fr.splifight.items.ItemPlugin;
import fr.splifight.items.config.Messages;
import fr.splifight.items.item.impl.*;
import fr.splifight.items.item.impl.agriculteur.AgriculteurBoots;
import fr.splifight.items.item.impl.agriculteur.AgriculteurChestplate;
import fr.splifight.items.item.impl.agriculteur.AgriculteurHelmet;
import fr.splifight.items.item.impl.agriculteur.AgriculteurLeggings;
import fr.splifight.items.item.impl.guerrier.GuerrierBoots;
import fr.splifight.items.item.impl.guerrier.GuerrierChestplate;
import fr.splifight.items.item.impl.guerrier.GuerrierHelmet;
import fr.splifight.items.item.impl.guerrier.GuerrierLeggings;
import fr.splifight.items.utils.CC;
import fr.splifight.items.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum Items {

    CROCHETEUR(Material.TRIPWIRE_HOOK, "&c&lCrocheteur", new Crocheteur()),
    MARTEAU(Material.DIAMOND_PICKAXE, "&c&lMarteau", new Marteau()),
    RECOLTEUR(Material.HOPPER, "&c&lRécolteur", new Recolteur()),
    EPEE_DIEUX(Material.DIAMOND_SWORD, "&c&lÉpée des dieux", new EpeeDieux(), true),
    ARC_DIEUX(Material.BOW, "&c&lÉpée des dieux", new ArcDieux(), true),

    GUERRIER_HELMET(Material.DIAMOND_HELMET, "&c&lCasque du Guerrier", new GuerrierHelmet()),
    GUERRIER_CHESTPLATE(Material.DIAMOND_CHESTPLATE, "&c&lPlastron du Guerrier", new GuerrierChestplate()),
    GUERRIER_LEGGINGS(Material.DIAMOND_LEGGINGS, "&c&lPantalon du Guerrier", new GuerrierLeggings()),
    GUERRIER_BOOTS(Material.DIAMOND_BOOTS, "&c&lBottes du Guerrier", new GuerrierBoots()),

    AGRICULTEUR_HELMET(Material.LEATHER_HELMET, "&c&lCasque de l'Agriculteur", new AgriculteurHelmet()),
    AGRICULTEUR_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&c&lPlastron de l'Agriculteur", new AgriculteurChestplate()),
    AGRICULTEUR_LEGGINGS(Material.LEATHER_LEGGINGS, "&c&lPantalon de l'Agriculteur", new AgriculteurLeggings()),
    AGRICULTEUR_BOOTS(Material.LEATHER_BOOTS, "&c&lBottes de l'Agriculteur", new AgriculteurBoots()),

    AQUATIQUE_CHESTPLATE(Material.DIAMOND_CHESTPLATE, "&c&lPlastron Aquatique", new AquatiqueChestplate()),
    MAGMA_CHESTPLATE(Material.DIAMOND_CHESTPLATE, "&c&lPlastron Magmatique", new MagmaChestplate()),

    UNCLAIM_FINDER(Material.COMPASS, "&c&lUnclaim finder &8(&e", new UnClaimFinder()),
    CHUNK_DESTROYER(Material.BEACON, "&c&lDestructeur de chunk", new ChunkDestroyer())
    ;

    @Setter
    private Material material;

    @Setter
    private String name;

    @Setter
    private List<String> lore;

    private final AbstractItem item;

    private final boolean weapon;

    @Setter
    private String ownerLine = "";

    @Setter
    private String killsLine = "";

    Items(Material material, String name, AbstractItem item) {
        this.material = material;
        this.name = name;
        this.lore = new ArrayList<>();
        this.item = item;
        this.weapon = false;
    }

    Items(Material material, String name, AbstractItem item, boolean weapon) {
        this.material = material;
        this.name = name;
        this.lore = new ArrayList<>();
        this.item = item;
        this.weapon = weapon;
    }

    public void give(Player player) {
        ItemBuilder is = new ItemBuilder(item.getItemStack());

        if(this == UNCLAIM_FINDER) {
            is.setName(is.toItemStack().getItemMeta().getDisplayName() + "100&8)");
        }

        if(isWeapon()) {
            List<String> lore = is.toItemStack().getItemMeta().getLore();

            lore.add(CC.translate(ownerLine.replace("<owner>", player.getName())));
            lore.add(CC.translate(killsLine + 0));

            is.setLore(lore);
        }

        player.getInventory().addItem(is.toItemStack());
        Messages.RECEIVE.send(player, this);
    }

    public static void init() {
        loadConfig();
        initListeners();
    }

    @SneakyThrows
    public static void loadConfig() {
        FileConfiguration config = ItemPlugin.getInstance().getConfig();
        config.load(new File(ItemPlugin.getInstance().getDataFolder() + "/config.yml"));

        for (Items value : values()) {
            if (config.get("items." + value.name() + ".name") == null) {
                config.set("items." + value.name() + ".name", value.getName());
                ItemPlugin.getInstance().saveConfig();
            }
            if (config.get("items." + value.name() + ".item") == null) {
                config.set("items." + value.name() + ".item", value.getMaterial().name());
                ItemPlugin.getInstance().saveConfig();
            }
            if (config.get("items." + value.name() + ".lore") == null) {
                config.set("items." + value.name() + ".lore", value.getLore());
                ItemPlugin.getInstance().saveConfig();
            }

            value.setName(config.getString("items." + value.name() + ".name"));
            value.setLore(config.getStringList("items." + value.name() + ".lore"));
            value.setMaterial(Material.valueOf(config.getString("items." + value.name() + ".item")));
            if (value.isWeapon()) {
                value.setOwnerLine(config.getString("items." + value.name() + ".owner_line"));
                value.setKillsLine(config.getString("items." + value.name() + ".kill_line"));
            }
        }
    }

    private static void initListeners() {
        Arrays.stream(values())
                .filter(item -> item.getItem() instanceof Listener)
                .map(item -> (Listener) item.getItem())
                .forEach(item -> ItemPlugin.getInstance().getServer().getPluginManager().registerEvents(item, ItemPlugin.getInstance()));
    }

    public static Items getItem(String item) {
        Items toReturn = null;

        for (Items value : values()) {
            if (value.name().equalsIgnoreCase(item)) {
                toReturn = value;
                break;
            }
        }

        return toReturn;
    }

}
