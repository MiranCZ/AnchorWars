package me.miran.anchorwars.commands;

import me.miran.anchorwars.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Leave implements CommandExecutor {

    Main main;

    public Leave (Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            main.customMe.tpToLobby((Player) sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
        }

        return false;
    }
}
