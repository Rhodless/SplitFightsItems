package fr.splifight.items;

import fr.splifight.items.commands.SFToolsCommand;
import fr.splifight.items.config.Messages;
import fr.splifight.items.item.Items;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemPlugin extends JavaPlugin {

    @Getter
    private static ItemPlugin instance;

    @Override
    public void onEnable() {

        instance = this;
        saveDefaultConfig();
        Items.init();
        Messages.init();

        getCommand("sftools").setExecutor(new SFToolsCommand());
    }
}
