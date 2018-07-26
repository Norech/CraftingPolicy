package com.norech.craftingpolicy;

import com.norech.craftingpolicy.command.CommandPolicy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;

public class Main extends JavaPlugin {
    private FileConfiguration config;
    private HashMap<Player, PlayerData> playersData;

    public Main() {
        this.config = getConfig();
        this.playersData = new HashMap<>();
        PolicyManager.setPlugin(this);
    }

    @Override
    public void onEnable() {
        initializeConfig();
        registerCommands();

        getServer().getPluginManager().registerEvents(
                new EventListener(this), this
        );

        registerScheduler();
    }

    private void initializeConfig() {
        config.addDefault("storage.autoSave", true);
        config.addDefault("storage.autoSaveDelay", 240);

        config.addDefault("translations.acceptedPolicyMessage", "You accepted our policy.");
        config.addDefault("translations.deniedPolicyMessage", "You denied our policy.");
        config.addDefault("translations.acceptButton", "Accept");
        config.addDefault("translations.denyButton", "Deny");

        config.addDefault("book.pages",
                Arrays.asList( "§6§lPolicy\n\n§rThis is a simple\n§rpolicy example.\n\n§lEdit it in plugin\n§lconfiguration file.", "§7You can have multiple\n§7pages, like a regular\n§7book." )
        );

        config.addDefault("book.allowManualClose", false);
        config.addDefault("book.kickOnPolicyDeny", true);

        config.options().copyDefaults(true);
        saveConfig();
    }

    private void registerCommands() {
        getCommand("policy")
                .setExecutor(new CommandPolicy( this ));
    }

    private void registerScheduler() {
        boolean autoSave = config.getBoolean("storage.autoSave");
        long autoSaveDelay = config.getLong("storage.autoSaveDelay");

        if(!autoSave) return;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(PlayerData playerData : playersData.values()) {
                playerData.save();
            }
        }, 0L, autoSaveDelay * 20L);
    }

    @Override
    public void onDisable() {
        for(PlayerData playerData : playersData.values()) {
            playerData.save();
        }
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData playerData = playersData.get(player);

        if(playerData == null) {
            playerData = new PlayerData(this, player);
            playersData.put(player, playerData);
        }

        return playerData;
    }
}
