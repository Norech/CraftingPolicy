package com.norech.craftingpolicy;

import com.norech.craftingpolicy.util.PlayerStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerData {
    private JavaPlugin plugin;
    private PlayerStorage storage;
    private FileConfiguration data;

    public PlayerData(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.storage = new PlayerStorage(plugin, player);
        this.data = this.storage.getData();
    }

    public void setPolicyAccepted(boolean accepted) {
        storage.put("policy.accepted", accepted);
        storage.save();
    }

    public boolean isPolicyAccepted() {
        return data.getBoolean("policy.accepted", false);
    }

    public void save() {
        storage.save();
    }
}
