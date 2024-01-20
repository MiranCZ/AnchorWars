package me.miran.anchorwars.commands;

import me.miran.anchorwars.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.awt.*;

public class ClearPPlacedBlocks implements CommandExecutor {

    Main main;

    public ClearPPlacedBlocks(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You don't have permissions to execute this command!");
            return false;
        }
        if (!main.map.isLoaded()) {
            sender.sendMessage(ChatColor.RED + "Command can be executed only in game!");
            return false;
        }
        int i = 0;
        for (Block bl : main.playerPlaced) {
            i = i + 1;
            World w = main.map.getWorld();
            w.getBlockAt(bl.getLocation()).setType(Material.AIR);
        }
        main.playerPlaced.clear();
        sender.sendMessage(ChatColor.GREEN + "All player placed blocks was successfully deleted!" + net.md_5.bungee.api.ChatColor.of(Color.orange) + " (" + i + ")");


        return false;
    }


}
