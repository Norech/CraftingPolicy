package com.norech.craftingpolicy;

import com.norech.craftingpolicy.util.BookUtil;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class PolicyManager {
    private static Main plugin;
    private static Configuration config;

    static void setPlugin(Main plugin) {
        PolicyManager.plugin = plugin;
        PolicyManager.config = plugin.getConfig();
    }

    public static void displayPolicy(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta)book.getItemMeta();

        List<String> content = config.getStringList("book.pages");
        BookUtil.setPages(meta, BookRenderer.renderPages(config, content));

        book.setItemMeta(meta);
        BookUtil.openBook(book, player);
    }

    public static void acceptPolicy(Player player) {
        PlayerData playerData = plugin.getPlayerData(player);

        playerData.setPolicyAccepted(true);
        player.sendMessage(config.getString("translations.acceptedPolicyMessage"));
        BookUtil.closeBook(player);
    }

    public static void denyPolicy(Player player) {
        PlayerData playerData = plugin.getPlayerData(player);

        playerData.setPolicyAccepted(false);
        if(config.getBoolean("book.kickOnPolicyDeny")) {
            player.kickPlayer(config.getString("translations.deniedPolicyMessage"));
        } else {
            player.sendMessage(config.getString("translations.deniedPolicyMessage"));
        }
    }

}
