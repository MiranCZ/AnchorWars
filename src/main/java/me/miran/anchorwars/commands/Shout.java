package me.miran.anchorwars.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Shout implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

           String name = sender.getName();

           if (sender instanceof BlockCommandSender) {
               name = name + "[CB]";
           }


        if(args.length > 0) {
            String message = "";

            for (String s : args) {
                message = message + " " + s;
            }

            Bukkit.broadcastMessage(ChatColor.RED +  "[SHOUT] " +ChatColor.GRAY + name + ":" +  ChatColor.RESET + message);
        } else {
            sender.sendMessage(ChatColor.RED + "Use: /shout <message>");
        }

        return false;
    }
}
