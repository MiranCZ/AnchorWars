package me.miran.anchorwars.gameManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetMinPlayersWaiting implements CommandExecutor {

    Main main;

    public SetMinPlayersWaiting (Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() &&main.customMe.isInt(args[0])) {
            main.minPlayerCount = Integer.parseInt( args[0]);
        } else {
            sender.sendMessage(ChatColor.RED + args[0] + " is number!");
        }
        return false;
    }
}
