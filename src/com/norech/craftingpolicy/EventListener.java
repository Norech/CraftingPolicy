package com.norech.craftingpolicy;

import com.norech.craftingpolicy.command.CommandPolicy;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventListener implements Listener
{
    private Main plugin;
    private FileConfiguration config;

    private CommandPolicy privacyPolicyCommand;

    EventListener(Main plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.privacyPolicyCommand = (CommandPolicy)( plugin.getCommand("privacy-policy").getExecutor() );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData(player);

        if(!playerData.isPolicyAccepted()) {
            PolicyManager.displayPolicy(player);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(config.getBoolean("book.allowManualClose")) return;
        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getPitch() != to.getPitch() || from.getYaw() != to.getYaw()) {
            Player player = event.getPlayer();
            PlayerData playerData = plugin.getPlayerData(player);

            if(!playerData.isPolicyAccepted()) {
                PolicyManager.displayPolicy(player);
            }
        }
    }
}
