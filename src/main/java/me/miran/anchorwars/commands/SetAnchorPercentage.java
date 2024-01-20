package me.miran.anchorwars.commands;

import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.gameManager.teamManager.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetAnchorPercentage implements CommandExecutor {

    Main main;

    public SetAnchorPercentage(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() && main.gameStart && DataManager.teams.contains(args[0])) {
            if (main.customMe.isInt(args[1])) {

                main.customMe.setAnchor(args[0], Integer.parseInt(args[1]), true);
            } else {
                sender.sendMessage(ChatColor.RED + args[1] + " is not valid number! Use: /.setAnchorPercentage <team> <percentage>");
            }
        }
        return false;
    }
}


