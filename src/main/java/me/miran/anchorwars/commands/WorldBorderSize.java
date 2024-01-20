package me.miran.anchorwars.commands;

import me.miran.anchorwars.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.awt.*;

public class WorldBorderSize implements CommandExecutor {

    Main main;

    public WorldBorderSize(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (sender.isOp()) {
            if (main.customMe.isDouble(args[0])) {

                main.worldBSize = Double.parseDouble(args[0]);
                sender.sendMessage(ChatColor.GREEN + "World border size was successfully set to " + net.md_5.bungee.api.ChatColor.of(Color.orange) + main.worldBSize);

            } else {
                sender.sendMessage(ChatColor.RED + "Incorrect use of command, please use: /.borderSize <size>");
            }

        }

        return false;
    }

}
