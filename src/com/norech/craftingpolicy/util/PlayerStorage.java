package com.norech.craftingpolicy.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class PlayerStorage {
    private JavaPlugin plugin;
    private Player player;
    private FileConfiguration data;
    private File dataFile;

    public PlayerStorage(JavaPlugin plugin, Player player) {
        String playerUUID = player.getUniqueId().toString();
        this.dataFile = new File(new File(plugin.getDataFolder(), "PlayerData"), playerUUID + ".yml");
        this.data = YamlConfiguration.loadConfiguration(this.dataFile);
    }

    public void put(String path, Object value) {
        data.createSection(path);
        data.set(path, value);
    }

    public Object get(String path) {
        return data.get(path);
    }

    public FileConfiguration getData() {
        return data;
    }

    public void save() {
        if (!dataFile.exists()) {
            try {
                data.save(dataFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }
}
