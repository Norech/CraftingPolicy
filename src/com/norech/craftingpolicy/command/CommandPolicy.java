package com.norech.craftingpolicy.command;

import com.norech.craftingpolicy.Main;
import com.norech.craftingpolicy.PolicyManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPolicy implements CommandExecutor {
    private Main plugin;

    public CommandPolicy(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;

            if(args.length == 0) {
                return false;
            }

            if(args.length == 2) {
                if(!player.hasPermission("craftingpolicy.command.others")) {
                    player.sendMessage(
                            Color.RED + plugin.getCommand("policy").getPermissionMessage()
                    );
                }
                Player foundPlayer = Bukkit.getPlayer(args[1]);
                if(foundPlayer == null) {
                    return false;
                }
                player = foundPlayer;
            }

            String subcommand = args[0];
            switch(subcommand.toLowerCase()) {
                case "show":
                    PolicyManager.displayPolicy(player);
                    break;

                case "accept":
                    PolicyManager.acceptPolicy(player);
                    break;

                case "deny":
                    PolicyManager.denyPolicy(player);
                    break;

                default:
                    return false;
            }
        }

        return true;
    }

}
